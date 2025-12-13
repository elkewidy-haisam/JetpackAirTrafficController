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
    
    // Text area for displaying current date and time information
    private JTextArea dateTimeDisplayArea;
    
    /**
     * Constructs a new DateTimeDisplayPanel.
     * Initializes UI components for date/time display.
     */
    public DateTimeDisplayPanel() {
        // Initialize and configure UI components
        initializeUI();
    }
    
    /**
     * Initializes and configures all UI components.
     * Sets up layout, borders, colors, and text area.
     */
    private void initializeUI() {
        // Set BorderLayout for simple CENTER positioning
        setLayout(new BorderLayout());
        // Create titled border with steel blue color (70,130,180)
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Current Date & Time",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(70, 130, 180)
        ));
        // Set white background for panel
        setBackground(Color.WHITE);
        // Constrain maximum size to fit date/time content
        setMaximumSize(new Dimension(300, 120));
        
        // Create read-only text area with 4 rows, 25 columns, bold Arial 13pt font
        dateTimeDisplayArea = UIComponentFactory.createReadOnlyTextArea(4, 25, UIComponentFactory.ARIAL_BOLD_13);
        // Set alice blue background (240,248,255)
        dateTimeDisplayArea.setBackground(new Color(240, 248, 255));
        // Set navy blue foreground text color (0,0,128)
        dateTimeDisplayArea.setForeground(new Color(0, 0, 128));
        // Add 10-pixel margin on all sides for spacing
        dateTimeDisplayArea.setMargin(new Insets(10, 10, 10, 10));
        
        // Add text area to center of panel
        add(dateTimeDisplayArea, BorderLayout.CENTER);
    }
    
    /**
     * Updates the displayed date/time text.
     * 
     * @param text New date/time string to display
     */
    public void updateDisplay(String text) {
        // Check if text area has been initialized
        if (dateTimeDisplayArea != null) {
            // Update text area content with new date/time string
            dateTimeDisplayArea.setText(text);
        }
    }
    
    /**
     * Returns the text area component for direct access.
     * 
     * @return JTextArea displaying date/time
     */
    public JTextArea getDisplayArea() {
        // Return reference to text area component
        return dateTimeDisplayArea;
    }
}
