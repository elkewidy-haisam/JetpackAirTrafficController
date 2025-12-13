/**
 * UI panel component for control display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing control-related visualization and user interaction.
 * Integrates with the main application frame to present control data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render control information with appropriate visual styling
 * - Handle user interactions related to control operations
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

import javax.swing.*;
import java.awt.*;
import com.example.ui.utility.UIComponentFactory;

/**
 * CityControlPanel is the right-side control panel for the main UI.
 * It contains sub-panels for date/time, weather, jetpack movement, and radio instructions.
 * Each sub-panel is accessible via a getter for integration with the main frame.
 */
public class CityControlPanel extends JPanel {
    // Date and time display sub-panel
    private DateTimeDisplayPanel dateTimePanel;
    // Weather broadcast information sub-panel
    private WeatherBroadcastPanel weatherPanel;
    // Jetpack movement controls sub-panel
    private JetpackMovementPanel movementPanel;
    // Radio instructions display sub-panel
    private RadioInstructionsPanel radioPanel;

    /** Constructs the CityControlPanel and initializes all sub-panels. */
    public CityControlPanel() {
        // Initialize and configure UI components with sub-panels
        initializeUI();
    }

    /** Initializes the layout and adds all sub-panels to the control panel. */
    private void initializeUI() {
        // Set vertical box layout for stacking panels
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Set preferred size to 300x600 pixels
        UIComponentFactory.setPreferredSize(this, 300, 600);
        // Set white background for panel
        setBackground(Color.WHITE);

        // Create and add Date/Time display panel
        dateTimePanel = new DateTimeDisplayPanel();
        // Add date/time panel to layout
        add(dateTimePanel);
        // Add 10-pixel vertical spacing
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Create and add Weather broadcast panel
        weatherPanel = new WeatherBroadcastPanel();
        // Add weather panel to layout
        add(weatherPanel);
        // Add 10-pixel vertical spacing
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Create and add Jetpack movement controls panel
        movementPanel = new JetpackMovementPanel();
        // Add movement panel to layout
        add(movementPanel);
        // Add 10-pixel vertical spacing
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Create and add Radio instructions panel
        radioPanel = new RadioInstructionsPanel();
        // Add radio panel to layout
        add(radioPanel);
    }

    // Accessor methods for each sub-panel
    
    /**
     * Returns the date/time display panel.
     * 
     * @return DateTimeDisplayPanel instance
     */
    public DateTimeDisplayPanel getDateTimePanel() {
        // Return reference to date/time panel
        return dateTimePanel;
    }

    /**
     * Returns the weather broadcast panel.
     * 
     * @return WeatherBroadcastPanel instance
     */
    public WeatherBroadcastPanel getWeatherPanel() {
        // Return reference to weather panel
        return weatherPanel;
    }

    /**
     * Returns the jetpack movement panel.
     * 
     * @return JetpackMovementPanel instance
     */
    public JetpackMovementPanel getMovementPanel() {
        // Return reference to movement panel
        return movementPanel;
    }

    /**
     * Returns the radio instructions panel.
     * 
     * @return RadioInstructionsPanel instance
     */
    public RadioInstructionsPanel getRadioPanel() {
        // Return reference to radio panel
        return radioPanel;
    }
}