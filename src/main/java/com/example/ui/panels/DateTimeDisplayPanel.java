/**
 * UI panel component for datetimedisplay display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing datetimedisplay-related visualization and user interaction.
 * Integrates with the main application frame to present datetimedisplay data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render datetimedisplay information with appropriate visual styling
 * - Handle user interactions related to datetimedisplay operations
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.example.ui.utility.UIComponentFactory;

/**
 * DateTimeDisplayPanel.java
 * by Haisam Elkewidy
 * 
 * Displays current date and time with day/night indicator for a specific city.
 */
public class DateTimeDisplayPanel extends JPanel {
    
    /** Text area for displaying date/time information with day/night indicator */
    private JTextArea dateTimeDisplayArea;
    
    /**
     * Constructs a new DateTimeDisplayPanel.
     * Initializes the UI components with styled border and text area.
     */
    public DateTimeDisplayPanel() {
        initializeUI();  // Initialize UI components
    }
    
    /**
     * Initializes the UI layout and components.
     * Sets up border, background, and creates the text display area.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());  // Use BorderLayout for simple center placement
        // Create titled border with steel blue color
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),  // Steel blue 2px border
            "Current Date & Time",  // Title text
            javax.swing.border.TitledBorder.LEFT,  // Title aligned left
            javax.swing.border.TitledBorder.TOP,  // Title at top
            new Font("Arial", Font.BOLD, 12),  // Title font
            new Color(70, 130, 180)  // Title color matches border
        ));
        setBackground(Color.WHITE);  // White background for panel
        setMaximumSize(new Dimension(300, 120));  // Limit maximum size
        
        // Create read-only text area for displaying date/time
        dateTimeDisplayArea = UIComponentFactory.createReadOnlyTextArea(4, 25, UIComponentFactory.ARIAL_BOLD_13);
        dateTimeDisplayArea.setBackground(new Color(240, 248, 255));  // Alice blue background
        dateTimeDisplayArea.setForeground(new Color(0, 0, 128));  // Navy blue text
        dateTimeDisplayArea.setMargin(new Insets(10, 10, 10, 10));  // 10px padding all sides
        
        add(dateTimeDisplayArea, BorderLayout.CENTER);  // Add text area to center
    }
    
    /**
     * Updates the date/time display with new text.
     * Sets the text area content to show current date, time, and day/night indicator.
     * 
     * @param text the formatted date/time string to display
     */
    public void updateDisplay(String text) {
        if (dateTimeDisplayArea != null) {  // Check text area is initialized
            dateTimeDisplayArea.setText(text);  // Update display text
        }
    }
    
    /**
     * Returns the text area component for direct manipulation.
     * Allows parent components to customize appearance or behavior.
     * 
     * @return the JTextArea displaying date/time information
     */
    public JTextArea getDisplayArea() {
        return dateTimeDisplayArea;  // Return text area reference
    }
}
