/*
 * AirTrafficControllerFrame.java
 * Main application frame for the Air Traffic Controller system.
 * Handles city selection, map display, session management, and user actions.
 *
 * Last updated: December 10, 2025
 * Author: Jetpack Air Traffic Controller Team
 */
package com.example.ui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.example.jetpack.JetPack;
import com.example.logging.CityLogManager;
import com.example.manager.CityDisplayUpdater;
import com.example.manager.CityJetpackManager;
import com.example.manager.CityTimerManager;
import com.example.ui.citymap.CityMapPanel;
import com.example.ui.panels.CitySelectionPanel;
import com.example.ui.panels.ConsoleOutputPanel;
import com.example.util.ConfigManager;
import com.example.utility.map.SessionManager;
import com.example.weather.DayTime;
import com.example.weather.Weather;

/**
 * AirTrafficControllerFrame - Main application window for the Air Traffic Controller system.
 * Handles city selection, map display, session management, and user actions.
 */
public class AirTrafficControllerFrame extends JFrame implements ActionListener {

    private String currentCity;
    private final JPanel mainPanel;
    private final CitySelectionPanel citySelectionPanel;
    private CityMapPanel cityMapPanel;
    private Weather currentWeather;
    private DayTime currentDayTime;
    private RadarTapeWindow radarTapeWindow;
    
    // Manager classes
    private final CityLogManager logManager;
    private final CityJetpackManager jetpackManager;
    private final CityTimerManager timerManager;
    private final CityDisplayUpdater displayUpdater;
    private final ConsoleOutputPanel consolePanel;
    
    // Data persistence
    private final ConfigManager configManager;
    private final SessionManager sessionManager;


   
    /**
     * Displays the map for the selected city
     */
    private void displayCityMap(String city) {
        currentCity = city;
        ArrayList<JetPack> jetpacks = jetpackManager.getJetpacksForCity(city);
        mainPanel.removeAll();
        
        // Initialize weather and day/time for this city
        currentWeather = new Weather();
        currentDayTime = new DayTime();
        
        // Create CityMapPanel with external class
        cityMapPanel = new CityMapPanel(city, jetpacks, currentWeather, currentDayTime, logManager, radarTapeWindow);
        
        // Set callbacks for communication with parent frame
        cityMapPanel.setShowCitySelectionCallback(() -> showCitySelection());
        cityMapPanel.setOpenRadarTapeCallback(() -> openRadarTape());
        cityMapPanel.setRadarTapeWindow(radarTapeWindow);
        
        mainPanel.add(cityMapPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
        setTitle("Air Traffic Controller - " + city);
    }

    /**
     * Shows the city selection panel
     */
    private void showCitySelection() {
        System.out.println("showCitySelection() called"); // Debug
        
        // Stop animation and clean up before switching panels
        if (cityMapPanel != null) {
            try {
                System.out.println("Stopping animations..."); // Debug
                cityMapPanel.stopAnimation();
                // Give timers a moment to stop
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println("Error stopping animation: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Switch panels on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Switching to city selection panel..."); // Debug
                mainPanel.removeAll();
                mainPanel.add(citySelectionPanel, BorderLayout.CENTER);
                mainPanel.add(consolePanel, BorderLayout.SOUTH);
                mainPanel.revalidate();
                mainPanel.repaint();
                setTitle("Air Traffic Controller - City Selection");
                
                // Log the action
                consolePanel.appendMessage("Returned to city selection");
                System.out.println("City selection panel displayed"); // Debug
                
                // Nullify the reference to help with cleanup
                cityMapPanel = null;
            } catch (Exception e) {
                System.err.println("Error switching panels: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    

    

    

    

    

    
    /**
     * Opens the radar tape window for communication logs
     */
    private void openRadarTape() {
        if (radarTapeWindow == null || !radarTapeWindow.isVisible()) {
            radarTapeWindow = new RadarTapeWindow(currentCity, (city, message) -> {
                logManager.writeToRadarLog(city, message);
            });
            radarTapeWindow.setVisible(true);
            
            // Update the city map panel with the new radar window reference
            if (cityMapPanel != null) {
                cityMapPanel.setRadarTapeWindow(radarTapeWindow);
            }
        } else {
            radarTapeWindow.toFront();
        }
    }
    


    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle action events here
    }

    /**
     * Constructor - initializes the Air Traffic Controller application
     */
    public AirTrafficControllerFrame() {
        // Initialize manager classes
        logManager = new CityLogManager();
        jetpackManager = new CityJetpackManager();
        timerManager = new CityTimerManager();
        displayUpdater = new CityDisplayUpdater();
        configManager = new ConfigManager();
        sessionManager = new SessionManager();

        // Set up the main frame
        setTitle("Air Traffic Controller - City Selection");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create menu bar
        createMenuBar();

        // Create main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Create and add city selection panel
        citySelectionPanel = new CitySelectionPanel(city -> {
            displayCityMap(city);
        });
        mainPanel.add(citySelectionPanel, BorderLayout.CENTER);
        
        // Create console output panel
        consolePanel = new ConsoleOutputPanel();
        mainPanel.add(consolePanel, BorderLayout.SOUTH);
        
        consolePanel.appendMessage("Air Traffic Controller System Initialized");
        consolePanel.appendMessage("Select a city to begin monitoring...");

        add(mainPanel);
        setVisible(true);
    }
    
    /**
     * Create the menu bar with File menu
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        
        // Save Session
        JMenuItem saveSessionItem = new JMenuItem("Save Session");
        saveSessionItem.setMnemonic('S');
        saveSessionItem.addActionListener(e -> saveSession());
        fileMenu.add(saveSessionItem);
        
        // Export Radar Log
        JMenuItem exportRadarItem = new JMenuItem("Export Radar Log");
        exportRadarItem.setMnemonic('R');
        exportRadarItem.addActionListener(e -> exportRadarLog());
        fileMenu.add(exportRadarItem);
        
        // Export Accident Report
        JMenuItem exportAccidentItem = new JMenuItem("Export Accident Report");
        exportAccidentItem.setMnemonic('A');
        exportAccidentItem.addActionListener(e -> exportAccidentReport());
        fileMenu.add(exportAccidentItem);
        
        fileMenu.addSeparator();
        
        // Open Sessions Folder
        JMenuItem openFolderItem = new JMenuItem("Open Sessions Folder");
        openFolderItem.setMnemonic('O');
        openFolderItem.addActionListener(e -> openSessionsFolder());
        fileMenu.add(openFolderItem);
        
        fileMenu.addSeparator();
        
        // Exit
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('X');
        exitItem.addActionListener(e -> {
            configManager.saveConfig();
            System.exit(0);
        });
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    
    /**
     * Save current session to file
     */
    private void saveSession() {
        if (cityMapPanel == null || currentCity == null) {
            JOptionPane.showMessageDialog(this, 
                "No active city session to save.", 
                "Save Session", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String path = sessionManager.saveSession(
            cityMapPanel.getCity(), 
            cityMapPanel.getJetpackFlights(), 
            currentWeather
        );
        
        if (path != null) {
            JOptionPane.showMessageDialog(this,
                "Session saved successfully:\n" + path,
                "Save Session",
                JOptionPane.INFORMATION_MESSAGE);
            consolePanel.appendMessage("Session saved: " + new File(path).getName());
        }
    }
    
    /**
     * Export radar log to text file
     */
    private void exportRadarLog() {
        java.util.List<String> radarLog = logManager.getRadarLog();
        if (radarLog.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Radar log is empty.",
                "Export Radar Log",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String path = sessionManager.exportRadarLog(radarLog);
        if (path != null) {
            JOptionPane.showMessageDialog(this,
                "Radar log exported successfully:\n" + path,
                "Export Radar Log",
                JOptionPane.INFORMATION_MESSAGE);
            consolePanel.appendMessage("Radar log exported: " + new File(path).getName());
        }
    }
    
    /**
     * Export accident report to text file
     */
    private void exportAccidentReport() {
        // Get accident data from city map panel
        if (cityMapPanel == null) {
            JOptionPane.showMessageDialog(this,
                "No active city session.",
                "Export Accident Report",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        java.util.List<String> accidents = cityMapPanel.getAccidentRecords();
        if (accidents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No accidents recorded.",
                "Export Accident Report",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String path = sessionManager.exportAccidentReport(accidents);
        if (path != null) {
            JOptionPane.showMessageDialog(this,
                "Accident report exported successfully:\n" + path,
                "Export Accident Report",
                JOptionPane.INFORMATION_MESSAGE);
            consolePanel.appendMessage("Accident report exported: " + new File(path).getName());
        }
    }
    
    /**
     * Open the sessions folder in file explorer
     */
    private void openSessionsFolder() {
        try {
            File sessionsDir = new File(sessionManager.getSessionsDirectory());
            if (!sessionsDir.exists()) {
                sessionsDir.mkdirs();
            }
            Desktop desktop = Desktop.getDesktop();
            desktop.open(sessionsDir);
            consolePanel.appendMessage("Opened sessions folder: " + sessionsDir.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error opening sessions folder:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    

}
