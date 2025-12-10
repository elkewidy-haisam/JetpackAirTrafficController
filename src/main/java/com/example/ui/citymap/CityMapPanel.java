package com.example.ui.citymap;


import com.example.ui.frames.RadarTapeWindow;
import com.example.ui.frames.JetpackTrackingWindow;
import com.example.jetpack.JetPack;
import com.example.grid.Grid;
import com.example.grid.GridRenderer;
import com.example.parking.ParkingSpaceManager;
import com.example.parking.ParkingSpace;
import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.radio.Radio;
import com.example.weather.Weather;
import com.example.weather.DayTime;
import com.example.utility.performance.PerformanceMonitor;
import com.example.logging.CityLogManager;
import com.example.ui.utility.UIComponentFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

/**
 * CityMapPanel - Main panel for displaying city map with animated jetpacks
 */
public class CityMapPanel extends JPanel {
    private String city;
    private final ArrayList<JetPack> jetpacks;
    private JPanel mapPanel;
    private final List<JetPackFlight> jetpackFlights;
    private Map<JetPackFlight, JetPackFlightState> flightStates;
    private RadarTapeWindow radarTape;
    private javax.swing.Timer dateTimeTimer;
    private javax.swing.Timer parkingTimer;
    private JLabel weatherLabel;
    private JLabel dateTimeLabel;
    private List<ParkingSpace> parkingSpaces;
    private JTextArea weatherBroadcastArea;
    private JTextArea jetpackMovementArea;
    private JTextArea radioInstructionsArea;
    private Radio cityRadio;
    private final Random random = new Random();
    
    // Performance and visualization tools
    private PerformanceMonitor performanceMonitor;
    private GridRenderer gridRenderer;
    private JLabel parkingAvailabilityLabel;
    private ParkingSpaceManager parkingManager;
    
    // Rendering, update, and radio handlers
    private CityMapRenderer renderer;
    private CityMapUpdater updater;
    private CityMapRadioInstructionHandler radioHandler;
    private CityMapAnimationController animationController;
    private CityMapWeatherManager weatherManager;
    private JPanel jetpackListPanel;
    
    // Zoom functionality
    private double zoomScale = 1.0;
    private static final double MIN_ZOOM = 0.5;
    private static final double MAX_ZOOM = 2.0;
    private static final double ZOOM_STEP = 0.1;
    
    // Callbacks for communication with parent frame
    private final Weather currentWeather;
    private final DayTime currentDayTime;
    private CityLogManager logManager;
    private RadarTapeWindow radarTapeWindow;
    private Runnable showCitySelectionCallback;
    private Runnable openRadarTapeCallback;

    public CityMapPanel(String city, ArrayList<JetPack> jetpacks, Weather weather, DayTime dayTime, 
                        CityLogManager logManager, RadarTapeWindow radarWindow) {
        this.city = city;
        this.jetpacks = jetpacks;
        this.currentWeather = weather;
        this.currentDayTime = dayTime;
        this.logManager = logManager;
        this.radarTapeWindow = radarWindow;
        this.jetpackFlights = new ArrayList<>();
        this.flightStates = new HashMap<>();
        this.parkingSpaces = new ArrayList<>();
        this.parkingManager = new ParkingSpaceManager(city);
        this.cityRadio = new Radio("122.8", "ATC-" + city.toUpperCase().substring(0, 3));
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        initializeComponents();
        startDateTimeTimer();
        startRadioInstructions();
    }
    
    public void setShowCitySelectionCallback(Runnable callback) {
        this.showCitySelectionCallback = callback;
        
        // Recreate the jetpack list panel with the proper callback
        if (jetpackListPanel != null) {
            remove(jetpackListPanel);
        }
        jetpackListPanel = CityMapJetpackListFactory.createJetpackListPanel(
            jetpacks,
            jetpackFlights,
            showCitySelectionCallback,
            this::stopAnimation,
            (jetpack, flight) -> openJetpackTrackingWindow(jetpack, flight)
        );
        add(jetpackListPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
    
    public void setOpenRadarTapeCallback(Runnable callback) {
        this.openRadarTapeCallback = callback;
    }
    
    public void setRadarTapeWindow(RadarTapeWindow window) {
        this.radarTape = window;
        this.radarTapeWindow = window;
        
        // Update all flight states with the new radar window
        if (flightStates != null) {
            for (JetPackFlightState state : flightStates.values()) {
                state.setRadarTapeWindow(window);
            }
        }
    }
    
    private void initializeComponents() {
        // Header Panel with weather and datetime
        JPanel headerPanel = UIComponentFactory.createBorderLayoutPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        UIComponentFactory.setPreferredSize(headerPanel, 800, 80);
        
        // Left side - city and info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(70, 130, 180));
        
        JLabel cityLabel = new JLabel("Air Traffic Control - " + city);
        cityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        cityLabel.setForeground(Color.WHITE);
        leftPanel.add(cityLabel);
        
        weatherLabel = new JLabel();
        updateWeatherDisplay();
        weatherLabel.setFont(new Font("Arial", Font.BOLD, 14));
        weatherLabel.setForeground(Color.YELLOW);
        leftPanel.add(weatherLabel);
        
        dateTimeLabel = new JLabel();
        updateDateTimeDisplay();
        dateTimeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dateTimeLabel.setForeground(Color.WHITE);
        leftPanel.add(dateTimeLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        // Right side - radar button
        JButton radarButton = new JButton("📻 Open Radar Tape");
        radarButton.setFont(new Font("Arial", Font.BOLD, 12));
        radarButton.addActionListener(e -> {
            if (openRadarTapeCallback != null) {
                openRadarTapeCallback.run();
            }
        });
        JPanel radarButtonPanel = new JPanel();
        radarButtonPanel.setBackground(new Color(70, 130, 180));
        radarButtonPanel.add(radarButton);
        headerPanel.add(radarButtonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Initialize performance monitor and grid renderer
        Grid cityGrid = new Grid(2000, 2000, "Cartesian", city, "Local");
        gridRenderer = new GridRenderer(cityGrid);
        performanceMonitor = new PerformanceMonitor();
        
        // Create main content panel with map and info panels
        JPanel contentPanel = UIComponentFactory.createBorderLayoutPanel(10, 0);
        contentPanel.setBackground(Color.WHITE);

        // Load road map
        mapPanel = UIComponentFactory.createBorderLayoutPanel();
        UIComponentFactory.setPreferredSize(mapPanel, 800, 600);
        try {
            // Load map using helper class
            CityMapLoader.MapLoadResult mapResult = CityMapLoader.loadCityMap(city, this);
            ImageIcon mapIcon = mapResult.getMapIcon();
            java.awt.image.BufferedImage mapImage = mapResult.getMapImage();
            int mapWidth = mapResult.getMapWidth();
            int mapHeight = mapResult.getMapHeight();
            
            parkingManager.initializeParkingSpaces(mapWidth, mapHeight, mapImage);
            parkingSpaces = parkingManager.getParkingSpaces();
            
            // Initialize renderer with map components
            renderer = new CityMapRenderer(city, mapIcon, gridRenderer, performanceMonitor);
            
            // Parking spaces initialized
            
            // Initialize flights using helper class
            CityMapFlightInitializer.initializeFlights(
                jetpacks, jetpackFlights, flightStates, parkingSpaces, cityRadio,
                mapWidth, mapHeight, mapImage,
                message -> appendJetpackMovement(message),
                f -> flightStates.get(f)
            );
            
            // Set radar tape window for all states
            for (JetPackFlightState state : flightStates.values()) {
                state.setRadarTapeWindow(radarTapeWindow);
            }
            
            JPanel mapWithJetpacks = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Apply zoom transformation
                    g2d.scale(zoomScale, zoomScale);
                    
                    renderer.paintMapComponent(g, this, jetpackFlights, parkingSpaces);
                }
                
                @Override
                public Dimension getPreferredSize() {
                    Dimension original = renderer.getPreferredSize();
                    return new Dimension(
                        (int)(original.width * zoomScale),
                        (int)(original.height * zoomScale)
                    );
                }
            };
            
            // Add keyboard shortcuts
            mapWithJetpacks.setFocusable(true);
            mapWithJetpacks.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_G) {
                        // Toggle grid with 'G' key
                        if (gridRenderer != null) {
                            gridRenderer.toggleVisibility();
                            mapWithJetpacks.repaint();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_P) {
                        // Toggle performance monitor with 'P' key
                        if (performanceMonitor != null) {
                            performanceMonitor.toggleVisibility();
                            mapWithJetpacks.repaint();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
                        // Zoom in with '+' or '=' key
                        zoomIn(mapWithJetpacks);
                    } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                        // Zoom out with '-' key
                        zoomOut(mapWithJetpacks);
                    } else if (e.getKeyCode() == KeyEvent.VK_0) {
                        // Reset zoom with '0' key
                        resetZoom(mapWithJetpacks);
                    }
                }
            });
            
            // Add mouse wheel zoom
            mapWithJetpacks.addMouseWheelListener(e -> {
                if (e.getWheelRotation() < 0) {
                    // Scroll up - zoom in
                    zoomIn(mapWithJetpacks);
                } else {
                    // Scroll down - zoom out
                    zoomOut(mapWithJetpacks);
                }
            });
            
            mapWithJetpacks.requestFocusInWindow();
            
            JScrollPane mapScrollPane = new JScrollPane(mapWithJetpacks);
            mapPanel.add(mapScrollPane, BorderLayout.CENTER);
            
            // Initialize weather and animation controllers
            weatherManager = new CityMapWeatherManager(city, currentWeather, jetpackFlights,
                flightStates, parkingSpaces, cityRadio, radarTapeWindow, this, null);
            weatherManager.setWeatherLabel(weatherLabel);
            
            animationController = new CityMapAnimationController(jetpackFlights, flightStates,
                currentWeather, performanceMonitor, radarTapeWindow, weatherManager);
            animationController.startAnimation(mapWithJetpacks, mapWidth, mapHeight);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading map: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        contentPanel.add(mapPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        UIComponentFactory.setPreferredSize(rightPanel, 300, 600);
        rightPanel.setBackground(Color.WHITE);
        
        // Date/Time Panel
        JPanel dateTimePanel = CityMapPanelFactory.createDateTimePanel(city, currentDayTime);
        rightPanel.add(dateTimePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Parking Availability Panel
        CityMapPanelFactory.PanelWithLabel parkingResult = CityMapPanelFactory.createParkingAvailabilityPanel(parkingSpaces);
        parkingAvailabilityLabel = parkingResult.getLabel();
        rightPanel.add(parkingResult.getPanel());
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Weather Broadcast Panel
        CityMapPanelFactory.PanelWithTextArea weatherResult = CityMapPanelFactory.createWeatherBroadcastPanel();
        weatherBroadcastArea = weatherResult.getTextArea();
        rightPanel.add(weatherResult.getPanel());
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Jetpack Movement Panel
        CityMapPanelFactory.PanelWithTextArea movementResult = CityMapPanelFactory.createJetpackMovementPanel(jetpackFlights.size(), city);
        jetpackMovementArea = movementResult.getTextArea();
        rightPanel.add(movementResult.getPanel());
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Radio Instructions Panel
        CityMapPanelFactory.PanelWithTextArea radioResult = CityMapPanelFactory.createRadioInstructionsPanel(cityRadio, jetpacks);
        radioInstructionsArea = radioResult.getTextArea();
        rightPanel.add(radioResult.getPanel());
        
        // Initialize updater and radio handler
        updater = new CityMapUpdater(city, logManager, weatherBroadcastArea, jetpackMovementArea, radioInstructionsArea);
        radioHandler = new CityMapRadioInstructionHandler(city, cityRadio, currentWeather, logManager, updater);
        
        // Now that updater is initialized, update the weather broadcast
        updateWeatherBroadcast();
        
        // Recreate weather manager with updater now that it's initialized
        weatherManager = new CityMapWeatherManager(city, currentWeather, jetpackFlights,
            flightStates, parkingSpaces, cityRadio, radarTapeWindow, this, updater);
        weatherManager.setWeatherLabel(weatherLabel);
        weatherManager.startWeatherTimer(() -> {
            updateWeatherDisplay();
            updateWeatherBroadcast();
        });
        
        // Start parking availability updates
        parkingTimer = new javax.swing.Timer(2000, e -> parkingManager.updateParkingAvailability(parkingAvailabilityLabel));
        parkingTimer.start();
        
        contentPanel.add(rightPanel, BorderLayout.EAST);
        add(contentPanel, BorderLayout.CENTER);

        // Create jetpack list panel using factory (will be recreated when callback is set)
        jetpackListPanel = CityMapJetpackListFactory.createJetpackListPanel(
            jetpacks, 
            showCitySelectionCallback,
            this::stopAnimation
        );
        add(jetpackListPanel, BorderLayout.SOUTH);
    }
    
    public void updateWeatherBroadcast() {
        if (updater != null) {
            updater.updateWeatherBroadcast(currentWeather);
        }
    }
    
    public void appendJetpackMovement(String message) {
        if (updater != null) {
            updater.appendJetpackMovement(message);
        }
    }
    
    private void startRadioInstructions() {
        javax.swing.Timer radioTimer = new javax.swing.Timer(8000 + random.nextInt(7000), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioHandler != null) {
                    radioHandler.issueRandomRadioInstruction(jetpackFlights, flightStates);
                }
            }
        });
        radioTimer.start();
    }
    
    // Zoom control methods
    private void zoomIn(JPanel panel) {
        if (zoomScale < MAX_ZOOM) {
            zoomScale += ZOOM_STEP;
            panel.revalidate();
            panel.repaint();
        }
    }
    
    private void zoomOut(JPanel panel) {
        if (zoomScale > MIN_ZOOM) {
            zoomScale -= ZOOM_STEP;
            panel.revalidate();
            panel.repaint();
        }
    }
    
    private void resetZoom(JPanel panel) {
        zoomScale = 1.0;
        panel.revalidate();
        panel.repaint();
    }
    
    private void startDateTimeTimer() {
        dateTimeTimer = new javax.swing.Timer(60000, e -> {
            currentDayTime.updateToCurrentTime();
            updateDateTimeDisplay();
            // Shading updates handled by animation timer
        });
        dateTimeTimer.start();
    }
    
    private void updateWeatherDisplay() {
        if (weatherManager != null) {
            weatherManager.updateWeatherDisplay();
        }
    }
    
    private void updateDateTimeDisplay() {
        if (dateTimeLabel != null && currentDayTime != null) {
            String timeOfDay = currentDayTime.getTimeOfDay();
            // Check if it's a daytime period (DAY, SUNRISE, DAWN) vs night periods
            boolean isDay = timeOfDay.equals("DAY") || timeOfDay.equals("SUNRISE") || timeOfDay.equals("DAWN");
            String dayNight = isDay ? "☀️ Day" : "🌙 Night";
            dateTimeLabel.setText("Time: " + dayNight);
        }
    }
    
    public void stopAnimation() {
        if (animationController != null) {
            animationController.stopAnimation();
        }
        if (weatherManager != null) {
            weatherManager.stopWeatherTimer();
        }
        if (dateTimeTimer != null) {
            dateTimeTimer.stop();
        }
        if (parkingTimer != null) {
            parkingTimer.stop();
        }
    }
    
    /**
     * Opens a tracking window for an individual jetpack with full context
     */
    private void openJetpackTrackingWindow(JetPack jetpack, JetPackFlight flight) {
        JetpackTrackingWindow trackingWindow = new JetpackTrackingWindow(
            jetpack, 
            flight, 
            city,
            jetpackFlights,           // All flights for nearby jetpack rendering
            flightStates,              // All states for parking/emergency detection
            animationController        // For accessing accidents
        );
        trackingWindow.setVisible(true);
    }
    
    // Getter methods for data persistence
    public com.example.city.City getCity() {
        // Create a City object from current state
        com.example.city.City cityObj = new com.example.city.City(city, 1200, 800);
        cityObj.setParkingSpaces(parkingSpaces);
        return cityObj;
    }
    
    public List<JetPackFlight> getJetpackFlights() {
        return new ArrayList<>(jetpackFlights);
    }
    
    public List<String> getAccidentRecords() {
        // Collect accident records from collision detector
        // For now, return empty list - will be populated by CollisionDetector
        return new ArrayList<>();
    }
}

