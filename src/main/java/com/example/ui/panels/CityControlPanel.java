/*
 * CityControlPanel.java
 * Part of Jetpack Air Traffic Controller
 *
 * Right-side control panel containing date/time, weather, movement, and radio displays.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
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