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
    /** Panel displaying current date and time with day/night indicator */
    private DateTimeDisplayPanel dateTimePanel;
    /** Panel displaying current weather conditions */
    private WeatherBroadcastPanel weatherPanel;
    /** Panel displaying jetpack movement information */
    private JetpackMovementPanel movementPanel;
    /** Panel displaying radio instructions and communications */
    private RadioInstructionsPanel radioPanel;

    /**
     * Constructs the CityControlPanel and initializes all sub-panels.
     * Creates vertical layout with date/time, weather, movement, and radio panels.
     */
    public CityControlPanel() {
        initializeUI();  // Initialize all UI components
    }

    /**
     * Initializes the layout and adds all sub-panels to the control panel.
     * Sets up vertical BoxLayout with 10px spacing between panels.
     */
    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  // Stack panels vertically
        UIComponentFactory.setPreferredSize(this, 300, 600);  // Set preferred size
        setBackground(Color.WHITE);  // White background

        // Date/Time Panel - shows current date, time, and day/night indicator
        dateTimePanel = new DateTimeDisplayPanel();  // Create date/time panel
        add(dateTimePanel);  // Add to control panel
        add(Box.createRigidArea(new Dimension(0, 10)));  // Add 10px vertical spacing

        // Weather Broadcast Panel - shows weather conditions
        weatherPanel = new WeatherBroadcastPanel();  // Create weather panel
        add(weatherPanel);  // Add to control panel
        add(Box.createRigidArea(new Dimension(0, 10)));  // Add 10px vertical spacing

        // Jetpack Movement Panel - shows jetpack activity
        movementPanel = new JetpackMovementPanel();  // Create movement panel
        add(movementPanel);  // Add to control panel
        add(Box.createRigidArea(new Dimension(0, 10)));  // Add 10px vertical spacing

        // Radio Instructions Panel - shows radio communications
        radioPanel = new RadioInstructionsPanel();  // Create radio panel
        add(radioPanel);  // Add to control panel (no spacing after last panel)
    }

    // Getters for each sub-panel to allow parent components to access and update them

    /**
     * Returns the date/time display panel.
     * @return DateTimeDisplayPanel instance
     */
    public DateTimeDisplayPanel getDateTimePanel() {
        return dateTimePanel;  // Return date/time panel reference
    }

    /**
     * Returns the weather broadcast panel.
     * @return WeatherBroadcastPanel instance
     */
    public WeatherBroadcastPanel getWeatherPanel() {
        return weatherPanel;  // Return weather panel reference
    }

    /**
     * Returns the jetpack movement panel.
     * @return JetpackMovementPanel instance
     */
    public JetpackMovementPanel getMovementPanel() {
        return movementPanel;  // Return movement panel reference
    }

    /**
     * Returns the radio instructions panel.
     * @return RadioInstructionsPanel instance
     */
    public RadioInstructionsPanel getRadioPanel() {
        return radioPanel;  // Return radio panel reference
    }
}