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
    /** Text area for displaying formatted jetpack list in tabular format */
    private JTextArea jetpackListArea;
    /** List of jetpack objects to display */
    private ArrayList<JetPack> jetpacks;
    
    /**
     * Constructs a new JetpackListPanel with specified jetpack list.
     * Initializes UI and populates the list display.
     * 
     * @param jetpacks ArrayList of JetPack objects to display
     */
    public JetpackListPanel(ArrayList<JetPack> jetpacks) {
        this.jetpacks = jetpacks;  // Store jetpack list reference
        initializeUI();  // Initialize UI components
        populateList();  // Populate list with jetpack data
    }
    
    /**
     * Initializes the UI layout and components.
     * Creates scrollable text area for jetpack list display.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());  // Use BorderLayout for simple center placement
        UIComponentFactory.setPreferredSize(this, 800, 200);  // Set preferred size for wide list
        setBorder(BorderFactory.createTitledBorder("Active Jetpacks"));  // Add simple titled border
        
        // Create read-only text area with monospaced font for tabular alignment
        jetpackListArea = UIComponentFactory.createReadOnlyTextArea(0, 0, UIComponentFactory.MONOSPACED_PLAIN_12);
        
        // Add scroll pane for long jetpack lists
        JScrollPane scrollPane = new JScrollPane(jetpackListArea);
        add(scrollPane, BorderLayout.CENTER);  // Add scroll pane to center
    }
    
    /**
     * Populates the list with jetpack information in tabular format.
     * Creates formatted table with headers and columns for each jetpack property.
     */
    private void populateList() {
        if (jetpacks == null || jetpacks.isEmpty()) {  // Check if jetpack list is empty
            jetpackListArea.setText("No jetpacks available.");  // Show message for empty list
            return;  // Exit early
        }
        
        StringBuilder listText = new StringBuilder();  // Build formatted list text
        // Append table header with column titles
        listText.append(String.format("%-15s %-12s %-20s %-8s %-15s %-15s\n",
            "Serial #", "Callsign", "Owner", "Year", "Model", "Manufacturer"));
        listText.append("-".repeat(95)).append("\n");  // Add separator line (95 dashes)
        
        // Iterate through all jetpacks and format each as table row
        for (JetPack jp : jetpacks) {
            listText.append(String.format("%-15s %-12s %-20s %-8s %-15s %-15s\n",
                jp.getSerialNumber(),   // Serial number in column 1
                jp.getCallsign(),       // Callsign in column 2
                jp.getOwnerName(),      // Owner name in column 3
                jp.getYear(),           // Manufacturing year in column 4
                jp.getModel(),          // Model name in column 5
                jp.getManufacturer())); // Manufacturer in column 6
        }
        
        jetpackListArea.setText(listText.toString());  // Set formatted text to display area
    }
    
    /**
     * Updates the list with new jetpack data.
     * Replaces current jetpack list and repopulates the display.
     * 
     * @param newJetpacks ArrayList of updated JetPack objects
     */
    public void updateList(ArrayList<JetPack> newJetpacks) {
        this.jetpacks = newJetpacks;  // Replace jetpack list with new data
        populateList();  // Repopulate display with new data
    }
}
