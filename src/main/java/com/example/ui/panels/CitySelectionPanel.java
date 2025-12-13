/**
 * UI panel component for selection display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing selection-related visualization and user interaction.
 * Integrates with the main application frame to present selection data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render selection information with appropriate visual styling
 * - Handle user interactions related to selection operations
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

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.ui.utility.UIComponentFactory;

/**
 * CitySelectionPanel - UI panel for city selection in the Air Traffic Controller app.
 */
public class CitySelectionPanel extends JPanel {
    private JComboBox<String> cityComboBox;
    private JButton selectButton;
    private JLabel instructionLabel;
    private Consumer<String> citySelectedCallback;

    /**
     * Constructor
     * 
     * @param citySelectedCallback Callback function to invoke when a city is selected
     */
    public CitySelectionPanel(Consumer<String> citySelectedCallback) {
        this.citySelectedCallback = citySelectedCallback;
        initializeUI();
    }
    
    /**
     * Initializes the UI components
     */
    private void initializeUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        UIComponentFactory.setPreferredSize(this, 600, 150);
        setBackground(new Color(240, 248, 255));

        instructionLabel = UIComponentFactory.createLabel("Select a city to monitor air traffic:", UIComponentFactory.ARIAL_BOLD_16);

        String[] cities = {"New York", "Boston", "Houston", "Dallas"};
        cityComboBox = UIComponentFactory.createComboBox(cities, UIComponentFactory.ARIAL_PLAIN_14);
        UIComponentFactory.setPreferredSize(cityComboBox, 200, 30);

        selectButton = UIComponentFactory.createButton("Monitor City", 150, 35);
        selectButton.setFont(UIComponentFactory.ARIAL_BOLD_14);
        selectButton.setBackground(new Color(70, 130, 180));
        selectButton.setForeground(Color.BLACK);
        selectButton.setOpaque(true);
        selectButton.setBorderPainted(true);

        selectButton.addActionListener(e -> {
            String city = (String) cityComboBox.getSelectedItem();
            if (city != null && citySelectedCallback != null) {
                citySelectedCallback.accept(city);
            }
        });

        add(instructionLabel);
        add(cityComboBox);
        add(selectButton);
    }
    
    /**
     * Gets the currently selected city
     * 
     * @return Selected city name
     */
    public String getSelectedCity() {
        return (String) cityComboBox.getSelectedItem();
    }
}
