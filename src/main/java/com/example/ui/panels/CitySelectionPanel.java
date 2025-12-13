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
    /** Dropdown combo box for selecting cities */
    private JComboBox<String> cityComboBox;
    /** Button to confirm city selection */
    private JButton selectButton;
    /** Label displaying instructions to user */
    private JLabel instructionLabel;
    /** Callback function invoked when city is selected */
    private Consumer<String> citySelectedCallback;

    /**
     * Constructs a new CitySelectionPanel with specified callback.
     * Initializes UI with city dropdown and selection button.
     * 
     * @param citySelectedCallback Callback function to invoke when a city is selected
     */
    public CitySelectionPanel(Consumer<String> citySelectedCallback) {
        this.citySelectedCallback = citySelectedCallback;  // Store callback for later invocation
        initializeUI();  // Initialize UI components
    }
    
    /**
     * Initializes the UI components.
     * Creates instruction label, city dropdown, and monitor button with styling.
     */
    private void initializeUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));  // Center layout with 15px horizontal, 20px vertical gaps
        UIComponentFactory.setPreferredSize(this, 600, 150);  // Set panel size
        setBackground(new Color(240, 248, 255));  // Alice blue background

        // Create instruction label with bold font
        instructionLabel = UIComponentFactory.createLabel("Select a city to monitor air traffic:", UIComponentFactory.ARIAL_BOLD_16);

        // Create city dropdown with available cities
        String[] cities = {"New York", "Boston", "Houston", "Dallas"};  // List of available cities
        cityComboBox = UIComponentFactory.createComboBox(cities, UIComponentFactory.ARIAL_PLAIN_14);  // Create dropdown
        UIComponentFactory.setPreferredSize(cityComboBox, 200, 30);  // Set dropdown size

        // Create and style monitor button
        selectButton = UIComponentFactory.createButton("Monitor City", 150, 35);  // Create button
        selectButton.setFont(UIComponentFactory.ARIAL_BOLD_14);  // Set button font
        selectButton.setBackground(new Color(70, 130, 180));  // Steel blue background
        selectButton.setForeground(Color.BLACK);  // Black text
        selectButton.setOpaque(true);  // Make background visible
        selectButton.setBorderPainted(true);  // Show button border

        // Add action listener to handle button clicks
        selectButton.addActionListener(e -> {
            String city = (String) cityComboBox.getSelectedItem();  // Get selected city from dropdown
            if (city != null && citySelectedCallback != null) {  // Check city and callback exist
                citySelectedCallback.accept(city);  // Invoke callback with selected city
            }
        });

        // Add components to panel in display order
        add(instructionLabel);  // Add instruction label
        add(cityComboBox);  // Add city dropdown
        add(selectButton);  // Add monitor button
    }
    
    /**
     * Gets the currently selected city from the dropdown.
     * Returns the city name as displayed in the combo box.
     * 
     * @return Selected city name, or null if none selected
     */
    public String getSelectedCity() {
        return (String) cityComboBox.getSelectedItem();  // Return selected item from dropdown
    }
}
