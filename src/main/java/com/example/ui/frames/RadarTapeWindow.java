/**
 * RadarTapeWindow component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radartapewindow functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radartapewindow operations
 * - Maintain necessary state for radartapewindow functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.example.ui.utility.UIComponentFactory;

/**
 * RadarTapeWindow - Displays communication logs with jetpacks in a radar-style window.
 * Provides message history, timestamping, and log file writing.
 */
public class RadarTapeWindow extends JFrame {
    private JTextArea radarTextArea;
    private LinkedList<String> messages;
    private static final int MAX_MESSAGES = 100;
    private DateTimeFormatter timeFormatter;
    private String city;
    private LogWriter logWriter;

    public interface LogWriter {
        void writeToRadarLog(String city, String message);
    }

    public RadarTapeWindow(String cityName, LogWriter logWriter) {
        this.city = cityName;
        this.logWriter = logWriter;
        setTitle("Radar Communications - " + cityName);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        messages = new LinkedList<>();
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        // Create text area with radar styling
        radarTextArea = UIComponentFactory.createReadOnlyTextArea(0, 0, UIComponentFactory.COURIER_PLAIN_12);
        radarTextArea.setBackground(Color.BLACK);
        radarTextArea.setForeground(new Color(0, 255, 0)); // Green text
        radarTextArea.setMargin(new Insets(10, 10, 10, 10));
        
        // Add scroll pane
        JScrollPane scrollPane = UIComponentFactory.createScrollPaneAlwaysVisible(radarTextArea);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.DARK_GRAY);
        
        JButton clearButton = new JButton("Clear Messages");
            clearButton.setBackground(com.example.ui.utility.UIComponentFactory.STANDARD_BUTTON_BLUE);
            clearButton.setForeground(java.awt.Color.WHITE);
            clearButton.setOpaque(true);
            clearButton.setBorderPainted(false);
        clearButton.addActionListener(e -> clearMessages());
        buttonPanel.add(clearButton);
        
        // Add components
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial message
        addMessage("Radar communication initialized for " + cityName);
    }
    
    public void addMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(timeFormatter);
        String formattedMessage = "[" + timestamp + "] " + message;
        
        messages.addFirst(formattedMessage);
        
        // Limit message count
        if (messages.size() > MAX_MESSAGES) {
            messages.removeLast();
        }
        
        // Write to radar log file for this city
        if (logWriter != null) {
            logWriter.writeToRadarLog(city, formattedMessage);
        }
        
        updateDisplay();
    }
    
    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (String msg : messages) {
            sb.append(msg).append("\n");
        }
        radarTextArea.setText(sb.toString());
        radarTextArea.setCaretPosition(0); // Scroll to top
    }
    
    private void clearMessages() {
        messages.clear();
        updateDisplay();
    }
}
