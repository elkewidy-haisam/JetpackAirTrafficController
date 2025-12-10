package com.example.ui.panels;

import javax.swing.*;
import java.awt.*;

import com.example.ui.utility.UIComponentFactory;

/**
 * CityControlPanel.java
 * by Haisam Elkewidy
 * 
 * Right-side control panel containing date/time, weather, movement, and radio displays.
 */
public class CityControlPanel extends JPanel {
    private DateTimeDisplayPanel dateTimePanel;
    private WeatherBroadcastPanel weatherPanel;
    private JetpackMovementPanel movementPanel;
    private RadioInstructionsPanel radioPanel;
    
    public CityControlPanel() {
        initializeUI();
    }
    
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
