/**
 * UI panel component for jetpacklist display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing jetpacklist-related visualization and user interaction.
 * Integrates with the main application frame to present jetpacklist data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render jetpacklist information with appropriate visual styling
 * - Handle user interactions related to jetpacklist operations
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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.example.jetpack.JetPack;
import com.example.ui.utility.UIComponentFactory;

/**
 * JetpackListPanel.java
 * by Haisam Elkewidy
 * 
 * Displays a list of all active jetpacks for a city.
 */
public class JetpackListPanel extends JPanel {
    // Text area for displaying tabular jetpack list
    private JTextArea jetpackListArea;
    // Collection of jetpacks to display
    private ArrayList<JetPack> jetpacks;
    
    /**
     * Constructs a new JetpackListPanel with jetpack data.
     * 
     * @param jetpacks ArrayList of JetPack objects to display
     */
    public JetpackListPanel(ArrayList<JetPack> jetpacks) {
        // Store jetpack collection for display
        this.jetpacks = jetpacks;
        // Initialize and configure UI components
        initializeUI();
        // Populate list with jetpack data
        populateList();
    }
    
    /**
     * Initializes and configures all UI components.
     * Sets up tabular display with monospaced font.
     */
    private void initializeUI() {
        // Set BorderLayout for simple CENTER positioning
        setLayout(new BorderLayout());
        // Set preferred size to 800x200 pixels
        UIComponentFactory.setPreferredSize(this, 800, 200);
        // Create titled border for panel
        setBorder(BorderFactory.createTitledBorder("Active Jetpacks"));
        
        // Create read-only text area with monospaced font for tabular alignment
        jetpackListArea = UIComponentFactory.createReadOnlyTextArea(0, 0, UIComponentFactory.MONOSPACED_PLAIN_12);
        
        // Create scroll pane wrapper for text area
        JScrollPane scrollPane = new JScrollPane(jetpackListArea);
        // Add scroll pane to center of panel
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Populates the list with jetpack information in tabular format.
     */
    private void populateList() {
        // Check if jetpack list is empty or null
        if (jetpacks == null || jetpacks.isEmpty()) {
            // Display message when no jetpacks available
            jetpackListArea.setText("No jetpacks available.");
            return;
        }
        
        // Create string builder for efficient text construction
        StringBuilder listText = new StringBuilder();
        // Append header row with column titles (15, 12, 20, 8, 15, 15 char widths)
        listText.append(String.format("%-15s %-12s %-20s %-8s %-15s %-15s\n",
            "Serial #", "Callsign", "Owner", "Year", "Model", "Manufacturer"));
        // Append separator line (95 dashes)
        listText.append("-".repeat(95)).append("\n");
        
        // Iterate through all jetpacks
        for (JetPack jp : jetpacks) {
            // Append formatted row for each jetpack with aligned columns
            listText.append(String.format("%-15s %-12s %-20s %-8s %-15s %-15s\n",
                jp.getSerialNumber(), jp.getCallsign(), jp.getOwnerName(),
                jp.getYear(), jp.getModel(), jp.getManufacturer()));
        }
        
        // Set constructed text to text area
        jetpackListArea.setText(listText.toString());
    }
    
    /**
     * Updates the list with new jetpack data and refreshes display.
     * 
     * @param newJetpacks New ArrayList of JetPack objects
     */
    public void updateList(ArrayList<JetPack> newJetpacks) {
        // Replace jetpack collection with new data
        this.jetpacks = newJetpacks;
        // Repopulate list display with new data
        populateList();
    }
}
