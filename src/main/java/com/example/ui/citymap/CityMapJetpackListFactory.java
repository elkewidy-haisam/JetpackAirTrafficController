/**
 * CityMapJetpackListFactory component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides citymapjetpacklistfactory functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core citymapjetpacklistfactory operations
 * - Maintain necessary state for citymapjetpacklistfactory functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.citymap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.example.flight.JetPackFlight;
import com.example.jetpack.JetPack;
import com.example.ui.utility.UIComponentFactory;


/**
 * CityMapJetpackListFactory
 * Creates the jetpack list panel at the bottom of the city map, with search, filter, and tracking controls.
 */
public class CityMapJetpackListFactory {

    /**
     * Callback interface for jetpack tracking.
     */
    public interface JetpackTrackingCallback {
        void trackJetpack(JetPack jetpack, JetPackFlight flight);
    }

    /**
     * Creates a panel displaying all jetpacks in a formatted table with tracking buttons and filter checkboxes.
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
        // Main panel setup
        JPanel jetpackListPanel = UIComponentFactory.createBorderLayoutPanel();
        UIComponentFactory.setPreferredSize(jetpackListPanel, 800, 250);
        jetpackListPanel.setBorder(BorderFactory.createTitledBorder("Active Jetpacks - Click 'Track' to follow individual jetpack"));

        // Table panel holds the search bar and the scrollable list
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);

        // Top panel: info label and search bar
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Info label
        JLabel infoLabel = new JLabel("Filter by callsign or scroll to see all " + jetpacks.size() + " jetpacks. Click 'Track' to follow a jetpack.");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        infoLabel.setForeground(new Color(30, 60, 120));
        infoLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        topPanel.add(infoLabel);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(new Color(240, 248, 255));
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(180, 28));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(Box.createVerticalStrut(6));
        topPanel.add(searchPanel);

        tablePanel.add(topPanel, BorderLayout.NORTH);

        // Entries panel: holds the list of jetpack entries
        JPanel entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        entriesPanel.setBackground(Color.WHITE);

        // Store filter checkboxes for bulk operations
        ArrayList<JCheckBox> filterCheckboxes = new ArrayList<>();
        ArrayList<JetPackFlight> flightsForCheckboxes = new ArrayList<>();

        // Helper to (re)populate entriesPanel based on search
        Runnable updateEntries = () -> {
            entriesPanel.removeAll();
            String filter = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < jetpacks.size(); i++) {
                JetPack jp = jetpacks.get(i);
                JetPackFlight flight = (jetpackFlights != null && i < jetpackFlights.size()) ? jetpackFlights.get(i) : null;
                if (!filter.isEmpty() && !jp.getCallsign().toLowerCase().contains(filter)) continue;

                JPanel entryPanel = new JPanel();
                entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.X_AXIS));
                entryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
                entryPanel.setBackground(i % 2 == 0 ? new Color(250, 252, 255) : new Color(235, 240, 250));
                entryPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 230)),
                        BorderFactory.createEmptyBorder(3, 8, 3, 8)));

                // Filter checkbox (leftmost)
                JCheckBox filterCheckbox = new JCheckBox("Show", true);
                filterCheckbox.setFont(new Font("Arial", Font.BOLD, 11));
                filterCheckbox.setBackground(new Color(70, 130, 180));
                filterCheckbox.setForeground(Color.WHITE);
                filterCheckbox.setOpaque(true);
                filterCheckbox.setToolTipText("Show/hide this jetpack on the map and 3D view");
                if (flight != null) {
                    filterCheckbox.setSelected(true);
                    filterCheckbox.addItemListener(e -> {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            java.awt.Component ancestor = javax.swing.SwingUtilities.getAncestorOfClass(CityMapPanel.class, entryPanel);
                            if (ancestor instanceof CityMapPanel) {
                                CityMapPanel cityMapPanel = (CityMapPanel) ancestor;
                                if (filterCheckbox.isSelected()) {
                                    cityMapPanel.showJetpack(flight);
                                } else {
                                    cityMapPanel.hideJetpack(flight);
                                }
                            }
                            for (com.example.ui.panels.JOGL3DPanel panel : com.example.ui.panels.JOGL3DPanel.getOpenPanels()) {
                                if (filterCheckbox.isSelected()) {
                                    panel.showJetpack(flight);
                                } else {
                                    panel.hideJetpack(flight);
                                }
                            }
                        });
                    });
                    filterCheckboxes.add(filterCheckbox);
                    flightsForCheckboxes.add(flight);
                }
                entryPanel.add(filterCheckbox);
                entryPanel.add(Box.createHorizontalStrut(10));

                // Info label
                // Generate a unique aviation/bird-themed name for each jetpack
                String[] birdNames = {
                    "Falcon", "Eagle", "Hawk", "Osprey", "Swift", "Albatross", "Condor", "Kestrel", "Merlin", "Harrier",
                    "Heron", "Raven", "Sparrow", "Vireo", "Peregrine", "Goshawk", "Avocet", "Tern", "Cormorant", "Buzzard",
                    "Crane", "Lark", "Oriole", "Jay", "Wren", "Finch", "Bittern", "Snipe", "Stork", "Ibis"
                };
                String birdName = birdNames[i % birdNames.length];
                String info = String.format("%-12s  |  %-15s  |  %-20s  |  %s %s",
                        jp.getCallsign(), jp.getSerialNumber(), jp.getOwnerName(),
                        jp.getModel(), birdName);
                JLabel infoLabelEntry = new JLabel(info);
                infoLabelEntry.setFont(new Font("Monospaced", Font.PLAIN, 12));
                infoLabelEntry.setForeground(new Color(30, 30, 60));
                infoLabelEntry.setToolTipText("Callsign: " + jp.getCallsign() + ", Owner: " + jp.getOwnerName());
                entryPanel.add(infoLabelEntry);
                entryPanel.add(Box.createHorizontalGlue());

                // Track button
                if (flight != null && trackingCallback != null) {
                    JButton trackButton = new JButton("🔍 Track");
                    trackButton.setBackground(new Color(100, 149, 237));
                    trackButton.setForeground(Color.WHITE);
                    trackButton.setOpaque(true);
                    trackButton.setBorderPainted(false);
                    trackButton.setFont(new Font("Arial", Font.BOLD, 11));
                    trackButton.setFocusPainted(false);
                    trackButton.setPreferredSize(new Dimension(80, 28));
                    trackButton.setToolTipText("Open 3D tracking window for this jetpack");
                    trackButton.addActionListener(e -> trackingCallback.trackJetpack(jp, flight));
                    entryPanel.add(Box.createHorizontalStrut(10));
                    entryPanel.add(trackButton);
                }

                entriesPanel.add(entryPanel);
                entriesPanel.add(Box.createVerticalStrut(2));
            }
            entriesPanel.revalidate();
            entriesPanel.repaint();
        };

        // Initial population
        updateEntries.run();
        // Add search listener
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateEntries.run(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateEntries.run(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateEntries.run(); }
        });

        // All entry creation is handled by updateEntries for search/filter support.

        JScrollPane scrollPane = new JScrollPane(entriesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        jetpackListPanel.add(tablePanel, BorderLayout.CENTER);

        // Button panel: Remove All, Show All, and Back
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Remove All button
        JButton removeAllButton = new JButton("Remove All");
        removeAllButton.setBackground(new Color(220, 53, 69)); // Bootstrap danger red
        removeAllButton.setForeground(Color.WHITE);
        removeAllButton.setFont(new Font("Arial", Font.BOLD, 13));
        removeAllButton.setFocusPainted(false);
        removeAllButton.setOpaque(true);
        removeAllButton.setBorderPainted(false);
        removeAllButton.setPreferredSize(new Dimension(120, 30));
        removeAllButton.setToolTipText("Hide all jetpacks from the map and 3D view");
        removeAllButton.addActionListener(e -> {
            for (int i = 0; i < filterCheckboxes.size(); i++) {
                JCheckBox cb = filterCheckboxes.get(i);
                JetPackFlight flight = flightsForCheckboxes.get(i);
                cb.setSelected(false);
                java.awt.Component ancestor = javax.swing.SwingUtilities.getAncestorOfClass(CityMapPanel.class, cb);
                if (ancestor instanceof CityMapPanel) {
                    ((CityMapPanel) ancestor).hideJetpack(flight);
                }
                // Sync with all open JOGL3DPanel instances
                for (com.example.ui.panels.JOGL3DPanel panel : com.example.ui.panels.JOGL3DPanel.getOpenPanels()) {
                    panel.hideJetpack(flight);
                }
            }
        });

        // Show All button
        JButton showAllButton = new JButton("Show All");
        showAllButton.setBackground(new Color(40, 167, 69)); // Bootstrap success green
        showAllButton.setForeground(Color.WHITE);
        showAllButton.setFont(new Font("Arial", Font.BOLD, 13));
        showAllButton.setFocusPainted(false);
        showAllButton.setOpaque(true);
        showAllButton.setBorderPainted(false);
        showAllButton.setPreferredSize(new Dimension(120, 30));
        showAllButton.setToolTipText("Show all jetpacks on the map and 3D view");
        showAllButton.addActionListener(e -> {
            for (int i = 0; i < filterCheckboxes.size(); i++) {
                JCheckBox cb = filterCheckboxes.get(i);
                JetPackFlight flight = flightsForCheckboxes.get(i);
                cb.setSelected(true);
                java.awt.Component ancestor = javax.swing.SwingUtilities.getAncestorOfClass(CityMapPanel.class, cb);
                if (ancestor instanceof CityMapPanel) {
                    ((CityMapPanel) ancestor).showJetpack(flight);
                }
                // Sync with all open JOGL3DPanel instances
                for (com.example.ui.panels.JOGL3DPanel panel : com.example.ui.panels.JOGL3DPanel.getOpenPanels()) {
                    panel.showJetpack(flight);
                }
            }
        });

        buttonPanel.add(removeAllButton);
        buttonPanel.add(Box.createHorizontalStrut(14));
        buttonPanel.add(showAllButton);

        // Back button
        JButton backButton = new JButton("Select Different City");
        backButton.setBackground(UIComponentFactory.STANDARD_BUTTON_BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            if (showCitySelectionCallback != null) {
                showCitySelectionCallback.run();
            }
        });
        buttonPanel.add(backButton);

        jetpackListPanel.add(buttonPanel, BorderLayout.SOUTH);

        return jetpackListPanel;
    }

    /**
     * Backward compatibility method - creates panel without tracking functionality.
     */
    public static JPanel createJetpackListPanel(ArrayList<JetPack> jetpacks,
                                                Runnable showCitySelectionCallback,
                                                Runnable stopAnimationCallback) {
        return createJetpackListPanel(jetpacks, null, showCitySelectionCallback, stopAnimationCallback, null);
    }
}
