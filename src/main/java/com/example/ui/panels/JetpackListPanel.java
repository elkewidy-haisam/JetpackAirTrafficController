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
