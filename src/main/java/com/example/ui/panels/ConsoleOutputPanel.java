/**
 * UI panel component for consoleoutput display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing consoleoutput-related visualization and user interaction.
 * Integrates with the main application frame to present consoleoutput data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render consoleoutput information with appropriate visual styling
 * - Handle user interactions related to consoleoutput operations
 * - Update display in response to system state changes
 * - Provide callbacks for parent frame coordination
 * 
 * Interactions:
 * - Embedded in AirTrafficControllerFrame or CityMapPanel
 * - Receives updates from manager classes and controllers
 * - Triggers actions via event listeners and callbacks
 * 
 * Patterns & Constraints:
 * - Extends JPanel for Swing integration
 * - Custom paintComponent for rendering where needed
 * - Event-driven updates for responsive UI
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.example.ui.utility.UIComponentFactory;

/**
 * ConsoleOutputPanel - Terminal-style output panel for status and log messages.
 */
public class ConsoleOutputPanel extends JScrollPane {
    // Text area for displaying terminal-style console messages
    private JTextArea consoleOutput;
    
    /**
     * Constructs a new ConsoleOutputPanel.
     * Initializes terminal-style console output display.
     */
    public ConsoleOutputPanel() {
        // Initialize and configure UI components
        initializeUI();
    }
    
    /**
     * Initializes and configures all UI components.
     * Sets up terminal-style display with black background and green text.
     */
    private void initializeUI() {
        // Create read-only text area with 8 rows, 80 columns, Courier Plain 11pt font
        consoleOutput = UIComponentFactory.createReadOnlyTextArea(8, 80, UIComponentFactory.COURIER_PLAIN_11);
        // Set black background for terminal appearance
        consoleOutput.setBackground(Color.BLACK);
        // Set green text color (0,255,0) for classic terminal look
        consoleOutput.setForeground(new Color(0, 255, 0)); // Green text
        // Add 5-pixel margin on all sides
        consoleOutput.setMargin(new Insets(5, 5, 5, 5));
        
        // Always show vertical scrollbar for consistent layout
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // Create titled border with dark gray color
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
            "Console Output",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            Color.DARK_GRAY
        ));
        
        // Set console text area as the scrollable viewport content
        setViewportView(consoleOutput);
    }
    
    /**
     * Appends a message to the console with newline.
     * Automatically scrolls to show newest messages.
     * 
     * @param message Message to append to console
     */
    public void appendMessage(String message) {
        // Check if console text area has been initialized
        if (consoleOutput != null) {
            // Append message with newline character
            consoleOutput.append(message + "\n");
            // Move caret to end of document to auto-scroll to newest message
            consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());
        }
    }
    
    /**
     * Clears all text from the console.
     */
    public void clear() {
        // Check if console text area has been initialized
        if (consoleOutput != null) {
            // Clear all text by setting empty string
            consoleOutput.setText("");
        }
    }
    
    /**
     * Returns the text area component for direct access.
     * 
     * @return JTextArea for console output
     */
    public JTextArea getConsoleArea() {
        // Return reference to console text area
        return consoleOutput;
    }
}
