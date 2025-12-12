/**
 * CityMapPanelFactory.java
 * by Haisam Elkewidy
 *
 * This class handles CityMapPanelFactory functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - panel (JPanel)
 *   - textArea (JTextArea)
 *   - panel (JPanel)
 *   - label (JLabel)
 *
 * Methods:
 *   - createDateTimePanel(city, currentDayTime)
 *   - actionPerformed(e)
 *   - createParkingAvailabilityPanel(parkingSpaces)
 *   - createWeatherBroadcastPanel()
 *   - createJetpackMovementPanel(jetpackCount, city)
 *   - keyReleased(e)
 *   - createRadioInstructionsPanel(cityRadio, jetpacks)
 *
 */

package com.example.ui.citymap;

import com.example.logging.CityLogManager;
import com.example.jetpack.JetPack;
import com.example.parking.ParkingSpace;
import com.example.radio.Radio;
import com.example.ui.utility.UIComponentFactory;
import com.example.utility.timezone.TimezoneHelper;
import com.example.weather.DayTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * CityMapPanelFactory.java
 * 
 * Factory class for creating side panel components in the City Map display.
 * Extracts panel creation logic from CityMapPanel to improve maintainability.
 */
public class CityMapPanelFactory {
    
    /**
     * Creates the date/time display panel with automatic updates
     */
    public static JPanel createDateTimePanel(String city, DayTime currentDayTime) {
        JPanel panel = UIComponentFactory.createBorderLayoutPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Current Date & Time",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(70, 130, 180)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(300, 120));
        
        JTextArea dateTimeDisplayArea = UIComponentFactory.createReadOnlyTextArea(4, 25, UIComponentFactory.ARIAL_BOLD_13);
        dateTimeDisplayArea.setBackground(new Color(240, 248, 255));
        dateTimeDisplayArea.setForeground(new Color(0, 0, 128));
        dateTimeDisplayArea.setMargin(new Insets(10, 10, 10, 10));
        
        javax.swing.Timer dateTimeUpdateTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ZoneId timezone = TimezoneHelper.getTimezoneForCity(city);
                LocalDateTime now = LocalDateTime.now(timezone);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy\nHH:mm:ss");
                String timeStr = now.format(formatter);
                
                String timeOfDay = currentDayTime.getTimeOfDay();
                boolean isDay = timeOfDay.equals("DAY") || timeOfDay.equals("SUNRISE") || timeOfDay.equals("DAWN");
                String dayNight = isDay ? "☀️ Day" : "🌙 Night";
                
                String tzName = timezone.getId().replace("America/", "");
                dateTimeDisplayArea.setText(dayNight + " - " + tzName + "\n" + timeStr);
            }
        });
        dateTimeUpdateTimer.start();
        
        // Initial display
        ZoneId timezone = TimezoneHelper.getTimezoneForCity(city);
        LocalDateTime now = LocalDateTime.now(timezone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy\nHH:mm:ss");
        String timeStr = now.format(formatter);
        String timeOfDay = currentDayTime.getTimeOfDay();
        boolean isDay = timeOfDay.equals("DAY") || timeOfDay.equals("SUNRISE") || timeOfDay.equals("DAWN");
        String dayNight = isDay ? "☀️ Day" : "🌙 Night";
        String tzName = timezone.getId().replace("America/", "");
        dateTimeDisplayArea.setText(dayNight + " - " + tzName + "\n" + timeStr);
        
        panel.add(dateTimeDisplayArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the parking availability display panel
     */
    public static PanelWithLabel createParkingAvailabilityPanel(List<ParkingSpace> parkingSpaces) {
        JPanel panel = UIComponentFactory.createBorderLayoutPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Parking Availability",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(34, 139, 34)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(300, 80));
        
        JLabel parkingAvailabilityLabel = new JLabel();
        parkingAvailabilityLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        parkingAvailabilityLabel.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(parkingAvailabilityLabel, BorderLayout.CENTER);
        
        return new PanelWithLabel(panel, parkingAvailabilityLabel);
    }
    
    /**
     * Creates the weather broadcast panel
     */
    public static PanelWithTextArea createWeatherBroadcastPanel() {
        JPanel panel = UIComponentFactory.createBorderLayoutPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Weather Broadcast",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(70, 130, 180)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(300, 250));
        
        JTextArea weatherBroadcastArea = UIComponentFactory.createReadOnlyTextArea(12, 25, UIComponentFactory.MONOSPACED_PLAIN_11);
        weatherBroadcastArea.setBackground(new Color(240, 248, 255));
        weatherBroadcastArea.setForeground(new Color(25, 25, 112));
        weatherBroadcastArea.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollPane = UIComponentFactory.createScrollPane(weatherBroadcastArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return new PanelWithTextArea(panel, weatherBroadcastArea);
    }
    
    /**
     * Creates the jetpack movement log panel with filtering capability
     */
    public static PanelWithTextArea createJetpackMovementPanel(int jetpackCount, String city) {
        JPanel panel = UIComponentFactory.createBorderLayoutPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Jetpack Movement Log",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(34, 139, 34)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(300, 300));
        
        JTextArea jetpackMovementArea = UIComponentFactory.createReadOnlyTextArea(15, 25, UIComponentFactory.MONOSPACED_PLAIN_10);
        jetpackMovementArea.setBackground(new Color(240, 255, 240));
        jetpackMovementArea.setForeground(new Color(0, 100, 0));
        jetpackMovementArea.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollPane = UIComponentFactory.createScrollPaneAlwaysVisible(jetpackMovementArea);
        
        // Create filter panel at top
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(245, 255, 245));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Search field
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(new Color(245, 255, 245));
        JLabel searchLabel = new JLabel("Filter:");
        JTextField searchField = new JTextField();
        searchField.setToolTipText("Filter by callsign or message content");
        JButton clearButton = new JButton("Clear");
            clearButton.setBackground(com.example.ui.utility.UIComponentFactory.STANDARD_BUTTON_BLUE);
            clearButton.setForeground(java.awt.Color.WHITE);
            clearButton.setOpaque(true);
            clearButton.setBorderPainted(false);
        clearButton.setFont(new Font("Arial", Font.PLAIN, 10));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);
        filterPanel.add(searchPanel);
        
        // Store full log and filtered view
        final java.util.List<String> fullLog = new java.util.ArrayList<>();
        final Set<String> selectedCallsigns = new HashSet<>();
        
        // Filter function
        Runnable applyFilter = () -> {
            String searchText = searchField.getText().toLowerCase().trim();
            StringBuilder filtered = new StringBuilder();
            
            for (String logEntry : fullLog) {
                boolean matches = true;
                
                // Check search text
                if (!searchText.isEmpty() && !logEntry.toLowerCase().contains(searchText)) {
                    matches = false;
                }
                
                // Check callsign filter (if any selected)
                if (!selectedCallsigns.isEmpty() && matches) {
                    boolean callsignMatch = false;
                    for (String callsign : selectedCallsigns) {
                        if (logEntry.contains(callsign)) {
                            callsignMatch = true;
                            break;
                        }
                    }
                    if (!callsignMatch) {
                        matches = false;
                    }
                }
                
                if (matches) {
                    filtered.append(logEntry).append("\n");
                }
            }
            
            jetpackMovementArea.setText(filtered.toString());
            jetpackMovementArea.setCaretPosition(jetpackMovementArea.getDocument().getLength());
        };
        
        // Search field listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter.run();
            }
        });
        
        // Clear button
        clearButton.addActionListener(e -> {
            searchField.setText("");
            selectedCallsigns.clear();
            applyFilter.run();
        });
        
        // Store filter controls in text area for later access
        jetpackMovementArea.putClientProperty("fullLog", fullLog);
        jetpackMovementArea.putClientProperty("applyFilter", applyFilter);
        jetpackMovementArea.putClientProperty("selectedCallsigns", selectedCallsigns);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Initial messages
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String initMsg1 = "[" + timestamp + "] Monitoring " + jetpackCount + " jetpacks in " + city;
        String initMsg2 = "[" + timestamp + "] System initialized and ready";
        String initMsg3 = "[" + timestamp + "] ---";
        fullLog.add(initMsg1);
        fullLog.add(initMsg2);
        fullLog.add(initMsg3);
        jetpackMovementArea.append(initMsg1 + "\n");
        jetpackMovementArea.append(initMsg2 + "\n");
        jetpackMovementArea.append(initMsg3 + "\n");
        
        return new PanelWithTextArea(panel, jetpackMovementArea);
    }
    
    /**
     * Creates the radio instructions panel
     */
    public static PanelWithTextArea createRadioInstructionsPanel(Radio cityRadio, ArrayList<JetPack> jetpacks) {
        JPanel panel = UIComponentFactory.createBorderLayoutPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 20, 60), 2),
            "📻 ATC Radio Communications",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(220, 20, 60)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(300, 250));
        
        JTextArea radioInstructionsArea = UIComponentFactory.createReadOnlyTextArea(12, 25, UIComponentFactory.MONOSPACED_BOLD_10);
        radioInstructionsArea.setBackground(new Color(255, 250, 240));
        radioInstructionsArea.setForeground(new Color(139, 0, 0));
        radioInstructionsArea.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollPane = UIComponentFactory.createScrollPaneAlwaysVisible(radioInstructionsArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        radioInstructionsArea.append("=== ATC RADIO ACTIVE ===\n");
        radioInstructionsArea.append("Frequency: " + cityRadio.getFrequency() + " MHz\n");
        radioInstructionsArea.append("Callsign: " + cityRadio.getControllerCallSign() + "\n");
        radioInstructionsArea.append("Monitoring " + jetpacks.size() + " jet packs\n");
        radioInstructionsArea.append("========================\n\n");
        
        return new PanelWithTextArea(panel, radioInstructionsArea);
    }
    
    /**
     * Helper class to return both panel and its text area
     */
    public static class PanelWithTextArea {
        private final JPanel panel;
        private final JTextArea textArea;
        
        public PanelWithTextArea(JPanel panel, JTextArea textArea) {
            this.panel = panel;
            this.textArea = textArea;
        }
        
        public JPanel getPanel() {
            return panel;
        }
        
        public JTextArea getTextArea() {
            return textArea;
        }
    }
    
    /**
     * Helper class to return both panel and its label
     */
    public static class PanelWithLabel {
        private final JPanel panel;
        private final JLabel label;
        
        public PanelWithLabel(JPanel panel, JLabel label) {
            this.panel = panel;
            this.label = label;
        }
        
        public JPanel getPanel() {
            return panel;
        }
        
        public JLabel getLabel() {
            return label;
        }
    }
}
