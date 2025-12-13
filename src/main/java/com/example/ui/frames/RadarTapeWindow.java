/**
 * Displays scrolling radar communication tape showing radio transmissions and ATC instructions.
 * 
 * Purpose:
 * Provides a dedicated window for viewing radio communications between ATC and jetpacks, similar to
 * real-world radar tape displays used in air traffic control. Shows chronological message history with
 * automatic scrolling and color-coded message types.
 * 
 * Key Responsibilities:
 * - Display radio communication messages
 * - Auto-scroll to show latest transmissions
 * - Color-code different message types
 * - Maintain message history
 * - Integrate with Radio system
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
    /** Text area displaying radar communications with green-on-black styling */
    private JTextArea radarTextArea;
    /** Linked list of messages for efficient add/remove at both ends */
    private LinkedList<String> messages;
    /** Maximum number of messages to retain in history before pruning oldest */
    private static final int MAX_MESSAGES = 100;
    /** Formatter for message timestamps in HH:mm:ss format */
    private DateTimeFormatter timeFormatter;
    /** Name of the city this radar window is monitoring */
    private String city;
    /** Interface for writing messages to persistent log files */
    private LogWriter logWriter;

    /**
     * Callback interface for writing radar communications to log files.
     */
    public interface LogWriter {
        /**
         * Writes a radar message to the city-specific log file.
         * @param city the city name for log file identification
         * @param message the formatted message to write
         */
        void writeToRadarLog(String city, String message);
    }

    /**
     * Constructs a new RadarTapeWindow for specified city.
     * Creates window with radar-style green-on-black display and message history.
     * 
     * @param cityName name of city for window title and logging
     * @param logWriter callback for writing messages to log files (can be null)
     */
    public RadarTapeWindow(String cityName, LogWriter logWriter) {
        this.city = cityName;  // Store city name for logging
        this.logWriter = logWriter;  // Store log writer callback
        setTitle("Radar Communications - " + cityName);  // Set window title with city name
        setSize(500, 600);  // Set window dimensions
        setLocationRelativeTo(null);  // Center window on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only this window on X
        
        messages = new LinkedList<>();  // Initialize message history list
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");  // Create time formatter
        
        // Create text area with radar styling (green text on black background)
        radarTextArea = UIComponentFactory.createReadOnlyTextArea(0, 0, UIComponentFactory.COURIER_PLAIN_12);
        radarTextArea.setBackground(Color.BLACK);  // Black background like radar screen
        radarTextArea.setForeground(new Color(0, 255, 0));  // Bright green text like radar
        radarTextArea.setMargin(new Insets(10, 10, 10, 10));  // Add padding around text
        
        // Add scroll pane for long message lists
        JScrollPane scrollPane = UIComponentFactory.createScrollPaneAlwaysVisible(radarTextArea);
        
        // Create button panel at bottom
        JPanel buttonPanel = new JPanel();  // Create panel for buttons
        buttonPanel.setBackground(Color.DARK_GRAY);  // Dark background to match theme
        
        // Create clear button with styling
        JButton clearButton = new JButton("Clear Messages");  // Create button
        clearButton.setBackground(com.example.ui.utility.UIComponentFactory.STANDARD_BUTTON_BLUE);  // Blue background
        clearButton.setForeground(java.awt.Color.WHITE);  // White text
        clearButton.setOpaque(true);  // Make background visible
        clearButton.setBorderPainted(false);  // Remove button border
        clearButton.addActionListener(e -> clearMessages());  // Wire up clear action
        buttonPanel.add(clearButton);  // Add button to panel
        
        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);  // Text area in center (fills space)
        add(buttonPanel, BorderLayout.SOUTH);  // Buttons at bottom
        
        // Initial message to show system is ready
        addMessage("Radar communication initialized for " + cityName);
    }
    
    /**
     * Adds a new message to the radar tape display.
     * Timestamps the message, adds to history, logs to file, and updates display.
     * Maintains MAX_MESSAGES limit by removing oldest when exceeded.
     * 
     * @param message the message text to add (without timestamp)
     */
    public void addMessage(String message) {
        LocalDateTime now = LocalDateTime.now();  // Get current time
        String timestamp = now.format(timeFormatter);  // Format time as HH:mm:ss
        String formattedMessage = "[" + timestamp + "] " + message;  // Add timestamp prefix
        
        messages.addFirst(formattedMessage);  // Add to front of list (newest first)
        
        // Limit message count to prevent memory issues
        if (messages.size() > MAX_MESSAGES) {  // Check if exceeded limit
            messages.removeLast();  // Remove oldest message from end
        }
        
        // Write to radar log file for this city
        if (logWriter != null) {  // Check if log writer is available
            logWriter.writeToRadarLog(city, formattedMessage);  // Write to persistent log
        }
        
        updateDisplay();  // Refresh the text area display
    }
    
    /**
     * Updates the text area display with current message history.
     * Rebuilds entire text from message list and scrolls to top.
     */
    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();  // Create builder for efficient string construction
        for (String msg : messages) {  // Iterate through all messages
            sb.append(msg).append("\n");  // Append each message with newline
        }
        radarTextArea.setText(sb.toString());  // Set text area content
        radarTextArea.setCaretPosition(0);  // Scroll to top (newest messages)
    }
    
    /**
     * Clears all messages from the radar tape display.
     * Empties message history and updates display to show blank screen.
     */
    private void clearMessages() {
        messages.clear();  // Remove all messages from list
        updateDisplay();  // Refresh display to show empty
    }
}
