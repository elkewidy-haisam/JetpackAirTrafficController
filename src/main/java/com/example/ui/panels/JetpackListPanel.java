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
    private JTextArea jetpackListArea;
    private ArrayList<JetPack> jetpacks;
    
    public JetpackListPanel(ArrayList<JetPack> jetpacks) {
        this.jetpacks = jetpacks;
        initializeUI();
        populateList();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        UIComponentFactory.setPreferredSize(this, 800, 200);
        setBorder(BorderFactory.createTitledBorder("Active Jetpacks"));
        
        jetpackListArea = UIComponentFactory.createReadOnlyTextArea(0, 0, UIComponentFactory.MONOSPACED_PLAIN_12);
        
        JScrollPane scrollPane = new JScrollPane(jetpackListArea);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Populates the list with jetpack information
     */
    private void populateList() {
        if (jetpacks == null || jetpacks.isEmpty()) {
            jetpackListArea.setText("No jetpacks available.");
            return;
        }
        
        StringBuilder listText = new StringBuilder();
        listText.append(String.format("%-15s %-12s %-20s %-8s %-15s %-15s\n",
            "Serial #", "Callsign", "Owner", "Year", "Model", "Manufacturer"));
        listText.append("-".repeat(95)).append("\n");
        
        for (JetPack jp : jetpacks) {
            listText.append(String.format("%-15s %-12s %-20s %-8s %-15s %-15s\n",
                jp.getSerialNumber(), jp.getCallsign(), jp.getOwnerName(),
                jp.getYear(), jp.getModel(), jp.getManufacturer()));
        }
        
        jetpackListArea.setText(listText.toString());
    }
    
    /**
     * Updates the list with new jetpack data
     */
    public void updateList(ArrayList<JetPack> newJetpacks) {
        this.jetpacks = newJetpacks;
        populateList();
    }
}
