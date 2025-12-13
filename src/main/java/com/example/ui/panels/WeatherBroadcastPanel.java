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
    /** Text area for displaying weather broadcast information */
    private JTextArea weatherBroadcastArea;
    
    /**
     * Constructs a new WeatherBroadcastPanel.
     * Initializes the UI with forest green themed border and text area.
     */
    public WeatherBroadcastPanel() {
        initializeUI();  // Initialize UI components
    }
    
    /**
     * Initializes the UI layout and components.
     * Creates styled text area with forest green border for weather information.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());  // Use BorderLayout for simple center placement
        // Create titled border with forest green color
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),  // Forest green 2px border
            "Weather Broadcast",  // Title text
            javax.swing.border.TitledBorder.LEFT,  // Title aligned left
            javax.swing.border.TitledBorder.TOP,  // Title at top
            new Font("Arial", Font.BOLD, 12),  // Title font
            new Color(34, 139, 34)  // Forest green title color matches border
        ));
        setBackground(Color.WHITE);  // White background for panel
        setMaximumSize(new Dimension(300, 150));  // Limit maximum size
        
        // Create read-only text area for displaying weather broadcast
        weatherBroadcastArea = UIComponentFactory.createReadOnlyTextArea(6, 25, UIComponentFactory.COURIER_PLAIN_11);
        weatherBroadcastArea.setBackground(new Color(240, 255, 240));  // Honeydew green background
        weatherBroadcastArea.setForeground(new Color(0, 100, 0));  // Dark green text
        weatherBroadcastArea.setMargin(new Insets(5, 5, 5, 5));  // 5px padding all sides
        
        // Add scroll pane for long weather broadcasts
        JScrollPane scrollPane = new JScrollPane(weatherBroadcastArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  // Show scrollbar when needed
        
        add(scrollPane, BorderLayout.CENTER);  // Add scroll pane to center
    }
    
    /**
     * Updates the weather broadcast display with new text.
     * Sets the text area content and scrolls to top.
     * 
     * @param text the formatted weather broadcast information to display
     */
    public void updateBroadcast(String text) {
        if (weatherBroadcastArea != null) {  // Check text area is initialized
            weatherBroadcastArea.setText(text);  // Update display text
            weatherBroadcastArea.setCaretPosition(0);  // Scroll to top
        }
    }
    
    /**
     * Returns the broadcast text area for direct manipulation.
     * 
     * @return the JTextArea displaying weather broadcast
     */
    public JTextArea getBroadcastArea() {
        return weatherBroadcastArea;  // Return text area reference
    }
}
