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
    private DateTimeDisplayPanel dateTimePanel;
    private WeatherBroadcastPanel weatherPanel;
    private JetpackMovementPanel movementPanel;
    private RadioInstructionsPanel radioPanel;

    /** Constructs the CityControlPanel and initializes all sub-panels. */
    public CityControlPanel() {
        initializeUI();
    }

    /** Initializes the layout and adds all sub-panels to the control panel. */
    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        UIComponentFactory.setPreferredSize(this, 300, 600);
        setBackground(Color.WHITE);

        // Date/Time Panel
        dateTimePanel = new DateTimeDisplayPanel();
        add(dateTimePanel);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Weather Broadcast Panel
        weatherPanel = new WeatherBroadcastPanel();
        add(weatherPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Jetpack Movement Panel
        movementPanel = new JetpackMovementPanel();
        add(movementPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Radio Instructions Panel
        radioPanel = new RadioInstructionsPanel();
        add(radioPanel);
    }

    // Getters for each sub-panel
    public DateTimeDisplayPanel getDateTimePanel() {
        return dateTimePanel;
    }

    public WeatherBroadcastPanel getWeatherPanel() {
        return weatherPanel;
    }

    public JetpackMovementPanel getMovementPanel() {
        return movementPanel;
    }

    public RadioInstructionsPanel getRadioPanel() {
        return radioPanel;
    }
}