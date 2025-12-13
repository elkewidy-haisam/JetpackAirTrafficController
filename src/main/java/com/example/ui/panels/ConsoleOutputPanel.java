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
    /** Text area for terminal-style console output with green-on-black styling */
    private JTextArea consoleOutput;
    
    /**
     * Constructs a new ConsoleOutputPanel.
     * Initializes terminal-style output display with scrolling.
     */
    public ConsoleOutputPanel() {
        initializeUI();  // Initialize UI components
    }
    
    /**
     * Initializes the UI layout and components.
     * Creates terminal-style text area with green text on black background.
     */
    private void initializeUI() {
        // Create read-only text area with monospace font for console appearance
        consoleOutput = UIComponentFactory.createReadOnlyTextArea(8, 80, UIComponentFactory.COURIER_PLAIN_11);
        consoleOutput.setBackground(Color.BLACK);  // Black background like terminal
        consoleOutput.setForeground(new Color(0, 255, 0));  // Bright green text like terminal
        consoleOutput.setMargin(new Insets(5, 5, 5, 5));  // 5px padding all sides
        
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Always show scrollbar
        // Create titled border with dark gray color
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 2),  // Dark gray 2px border
            "Console Output",  // Title text
            javax.swing.border.TitledBorder.LEFT,  // Title aligned left
            javax.swing.border.TitledBorder.TOP,  // Title at top
            new Font("Arial", Font.BOLD, 12),  // Title font
            Color.DARK_GRAY  // Title color matches border
        ));
        
        setViewportView(consoleOutput);  // Set text area as scrollable viewport content
    }
    
    /**
     * Appends a message to the console with newline.
     * Auto-scrolls to show the new message at bottom.
     * 
     * @param message the message text to append
     */
    public void appendMessage(String message) {
        if (consoleOutput != null) {  // Check text area is initialized
            consoleOutput.append(message + "\n");  // Append message with newline
            consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());  // Scroll to bottom
        }
    }
    
    /**
     * Clears all console output.
     * Empties the text area to show blank console.
     */
    public void clear() {
        if (consoleOutput != null) {  // Check text area is initialized
            consoleOutput.setText("");  // Clear all text
        }
    }
    
    /**
     * Returns the console text area for direct manipulation.
     * 
     * @return the JTextArea displaying console output
     */
    public JTextArea getConsoleArea() {
        return consoleOutput;  // Return text area reference
    }
}
