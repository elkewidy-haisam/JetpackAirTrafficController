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
    // Dropdown combo box for selecting a city
    private JComboBox<String> cityComboBox;
    // Button to confirm city selection
    private JButton selectButton;
    // Label with instructions for user
    private JLabel instructionLabel;
    // Callback function invoked when city is selected
    private Consumer<String> citySelectedCallback;

    /**
     * Constructs a new CitySelectionPanel with callback.
     * 
     * @param citySelectedCallback Callback function to invoke when a city is selected
     */
    public CitySelectionPanel(Consumer<String> citySelectedCallback) {
        // Store callback for later invocation
        this.citySelectedCallback = citySelectedCallback;
        // Initialize and configure UI components
        initializeUI();
    }
    
    /**
     * Initializes and configures all UI components.
     * Sets up city selector with available cities and action handler.
     */
    private void initializeUI() {
        // Set FlowLayout centered with 15px horizontal and 20px vertical gaps
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        // Set preferred size to 600x150 pixels
        UIComponentFactory.setPreferredSize(this, 600, 150);
        // Set alice blue background (240,248,255)
        setBackground(new Color(240, 248, 255));

        // Create instruction label with bold Arial 16pt font
        instructionLabel = UIComponentFactory.createLabel("Select a city to monitor air traffic:", UIComponentFactory.ARIAL_BOLD_16);

        // Define array of available cities for selection
        String[] cities = {"New York", "Boston", "Houston", "Dallas"};
        // Create combo box with city options and Arial Plain 14pt font
        cityComboBox = UIComponentFactory.createComboBox(cities, UIComponentFactory.ARIAL_PLAIN_14);
        // Set combo box size to 200x30 pixels
        UIComponentFactory.setPreferredSize(cityComboBox, 200, 30);

        // Create button with "Monitor City" label, 150x35 pixels
        selectButton = UIComponentFactory.createButton("Monitor City", 150, 35);
        // Set button font to bold Arial 14pt
        selectButton.setFont(UIComponentFactory.ARIAL_BOLD_14);
        // Set steel blue background (70,130,180)
        selectButton.setBackground(new Color(70, 130, 180));
        // Set black text color for button
        selectButton.setForeground(Color.BLACK);
        // Make background color visible (not transparent)
        selectButton.setOpaque(true);
        // Show button border
        selectButton.setBorderPainted(true);

        // Add action listener to handle button click
        selectButton.addActionListener(e -> {
            // Get selected city from combo box
            String city = (String) cityComboBox.getSelectedItem();
            // Check if city is selected and callback is registered
            if (city != null && citySelectedCallback != null) {
                // Invoke callback with selected city
                citySelectedCallback.accept(city);
            }
        });

        // Add instruction label to panel
        add(instructionLabel);
        // Add city combo box to panel
        add(cityComboBox);
        // Add selection button to panel
        add(selectButton);
    }
    
    /**
     * Gets the currently selected city from combo box.
     * 
     * @return Selected city name string
     */
    public String getSelectedCity() {
        // Return currently selected item from combo box cast to String
        return (String) cityComboBox.getSelectedItem();
    }
}
