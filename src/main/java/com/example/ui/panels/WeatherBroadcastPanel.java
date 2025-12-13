/**
 * UI panel component for weatherbroadcast display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing weatherbroadcast-related visualization and user interaction.
 * Integrates with the main application frame to present weatherbroadcast data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render weatherbroadcast information with appropriate visual styling
 * - Handle user interactions related to weatherbroadcast operations
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.example.ui.utility.UIComponentFactory;

/**
 * WeatherBroadcastPanel.java
 * by Haisam Elkewidy
 * 
 * Displays real-time weather broadcast information for a city.
 */
public class WeatherBroadcastPanel extends JPanel {
    // Text area for displaying real-time weather broadcast information
    private JTextArea weatherBroadcastArea;
    
    /**
     * Constructs a new WeatherBroadcastPanel.
     * Initializes weather broadcast display with forest green styling.
     */
    public WeatherBroadcastPanel() {
        // Initialize and configure UI components
        initializeUI();
    }
    
    /**
     * Initializes and configures all UI components.
     * Sets up display with forest green border and honeydew background.
     */
    private void initializeUI() {
        // Set BorderLayout for simple CENTER positioning
        setLayout(new BorderLayout());
        // Create titled border with forest green color (34,139,34)
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Weather Broadcast",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(34, 139, 34)
        ));
        // Set white background for panel
        setBackground(Color.WHITE);
        // Constrain maximum size to fit weather broadcast content
        setMaximumSize(new Dimension(300, 150));
        
        // Create read-only text area with 6 rows, 25 columns, Courier Plain 11pt font
        weatherBroadcastArea = UIComponentFactory.createReadOnlyTextArea(6, 25, UIComponentFactory.COURIER_PLAIN_11);
        // Set honeydew background (240,255,240)
        weatherBroadcastArea.setBackground(new Color(240, 255, 240));
        // Set dark green foreground text color (0,100,0)
        weatherBroadcastArea.setForeground(new Color(0, 100, 0));
        // Add 5-pixel margin on all sides
        weatherBroadcastArea.setMargin(new Insets(5, 5, 5, 5));
        
        // Create scroll pane wrapper for text area
        JScrollPane scrollPane = new JScrollPane(weatherBroadcastArea);
        // Show vertical scrollbar only when needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Add scroll pane to center of panel
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Updates the displayed weather broadcast text.
     * Resets caret to beginning for consistent display.
     * 
     * @param text New weather broadcast string to display
     */
    public void updateBroadcast(String text) {
        // Check if text area has been initialized
        if (weatherBroadcastArea != null) {
            // Update text area content with new broadcast
            weatherBroadcastArea.setText(text);
            // Reset caret to beginning of text
            weatherBroadcastArea.setCaretPosition(0);
        }
    }
    
    /**
     * Returns the text area component for direct access.
     * 
     * @return JTextArea displaying weather broadcast
     */
    public JTextArea getBroadcastArea() {
        // Return reference to broadcast text area
        return weatherBroadcastArea;
    }
}
