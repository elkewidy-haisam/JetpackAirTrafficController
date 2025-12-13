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
    
    private JTextArea dateTimeDisplayArea;
    
    public DateTimeDisplayPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Current Date & Time",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(70, 130, 180)
        ));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(300, 120));
        
        dateTimeDisplayArea = UIComponentFactory.createReadOnlyTextArea(4, 25, UIComponentFactory.ARIAL_BOLD_13);
        dateTimeDisplayArea.setBackground(new Color(240, 248, 255));
        dateTimeDisplayArea.setForeground(new Color(0, 0, 128));
        dateTimeDisplayArea.setMargin(new Insets(10, 10, 10, 10));
        
        add(dateTimeDisplayArea, BorderLayout.CENTER);
    }
    
    /**
     * Updates the date/time display
     */
    public void updateDisplay(String text) {
        if (dateTimeDisplayArea != null) {
            dateTimeDisplayArea.setText(text);
        }
    }
    
    public JTextArea getDisplayArea() {
        return dateTimeDisplayArea;
    }
}
