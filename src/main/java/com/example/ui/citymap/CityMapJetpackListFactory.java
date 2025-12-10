package com.example.ui.citymap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import com.example.flight.JetPackFlight;
import com.example.jetpack.JetPack;
import com.example.ui.utility.UIComponentFactory;

/**
 * CityMapJetpackListFactory - Creates the jetpack list panel at the bottom of the city map
 */
public class CityMapJetpackListFactory {
    
    /**
     * Callback interface for jetpack tracking
     */
    public interface JetpackTrackingCallback {
        void trackJetpack(JetPack jetpack, JetPackFlight flight);
    }
    
    /**
     * Creates a panel displaying all jetpacks in a formatted table with tracking buttons
     * 
     * @param jetpacks The list of jetpacks to display
     * @param jetpackFlights The list of jetpack flights (must match order of jetpacks)
     * @param showCitySelectionCallback Callback for the back button
     * @param stopAnimationCallback Callback to stop animations before switching cities
     * @param trackingCallback Callback for tracking individual jetpacks
     * @return The complete jetpack list panel with back button
     */
    public static JPanel createJetpackListPanel(ArrayList<JetPack> jetpacks,
                                                List<JetPackFlight> jetpackFlights,
                                                Runnable showCitySelectionCallback,
                                                Runnable stopAnimationCallback,
                                                JetpackTrackingCallback trackingCallback) {
        JPanel jetpackListPanel = UIComponentFactory.createBorderLayoutPanel();
        UIComponentFactory.setPreferredSize(jetpackListPanel, 800, 250);
        jetpackListPanel.setBorder(BorderFactory.createTitledBorder("Active Jetpacks - Click 'Track' to follow individual jetpack"));

        // Create table model
        String[] columnNames = {"Callsign", "Serial #", "Owner", "Model", "Track"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        // Create table panel with buttons
        JPanel tablePanel = new JPanel(new BorderLayout());
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel infoLabel = new JLabel("Scroll to see all " + jetpacks.size() + " jetpacks. Click 'Track' button to follow a jetpack in a separate window.");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoPanel.add(infoLabel);
        tablePanel.add(infoPanel, BorderLayout.NORTH);
        
        // Create panel for jetpack entries
        JPanel entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        entriesPanel.setBackground(Color.WHITE);
        
        // Add jetpack entries with track buttons
        for (int i = 0; i < jetpacks.size(); i++) {
            JetPack jp = jetpacks.get(i);
            JetPackFlight flight = (jetpackFlights != null && i < jetpackFlights.size()) ? jetpackFlights.get(i) : null;
            
            JPanel entryPanel = new JPanel(new BorderLayout(5, 0));
            entryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            entryPanel.setBackground(i % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
            entryPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            
            // Info label
            String info = String.format("%-12s  |  %-15s  |  %-20s  |  %s %s",
                jp.getCallsign(), jp.getSerialNumber(), jp.getOwnerName(),
                jp.getModel(), jp.getManufacturer());
            JLabel infoLabelEntry = new JLabel(info);
            infoLabelEntry.setFont(new Font("Monospaced", Font.PLAIN, 11));
            entryPanel.add(infoLabelEntry, BorderLayout.CENTER);
            
            // Track button
            if (flight != null && trackingCallback != null) {
                JButton trackButton = new JButton("🔍 Track");
                trackButton.setFont(new Font("Arial", Font.BOLD, 10));
                trackButton.setBackground(new Color(100, 149, 237));
                trackButton.setForeground(Color.WHITE);
                trackButton.setFocusPainted(false);
                trackButton.setPreferredSize(new Dimension(80, 25));
                trackButton.addActionListener(e -> trackingCallback.trackJetpack(jp, flight));
                entryPanel.add(trackButton, BorderLayout.EAST);
            }
            
            entriesPanel.add(entryPanel);
        }
        
        JScrollPane scrollPane = new JScrollPane(entriesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        jetpackListPanel.add(tablePanel, BorderLayout.CENTER);

        // Add back button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JButton backButton = new JButton("Select Different City");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            // First show city selection, then stop animation
            if (showCitySelectionCallback != null) {
                showCitySelectionCallback.run();
            }
        });
        buttonPanel.add(backButton);
        jetpackListPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return jetpackListPanel;
    }
    
    /**
     * Backward compatibility method - creates panel without tracking functionality
     */
    public static JPanel createJetpackListPanel(ArrayList<JetPack> jetpacks, 
                                                Runnable showCitySelectionCallback,
                                                Runnable stopAnimationCallback) {
        return createJetpackListPanel(jetpacks, null, showCitySelectionCallback, stopAnimationCallback, null);
    }
}
