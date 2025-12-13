/**
 * UI panel component for radioinstructions display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing radioinstructions-related visualization and user interaction.
 * Integrates with the main application frame to present radioinstructions data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render radioinstructions information with appropriate visual styling
 * - Handle user interactions related to radioinstructions operations
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.example.ui.utility.UIComponentFactory;

/**
 * RadioInstructionsPanel.java
 * by Haisam Elkewidy
 * 
 * Displays radio communications and ATC instructions.
 */
public class RadioInstructionsPanel extends JPanel {
    /** Text area for displaying radio communications and ATC instructions */
    private JTextArea radioInstructionsArea;
    
    /**
     * Constructs a new RadioInstructionsPanel.
     * Initializes the UI with crimson-themed border and text area.
     */
    public RadioInstructionsPanel() {
        initializeUI();  // Initialize UI components
    }
    
    /**
     * Initializes the UI layout and components.
     * Creates styled text area with crimson border for radio communications.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());  // Use BorderLayout for simple center placement
        // Create titled border with crimson color for urgency
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 20, 60), 2),  // Crimson 2px border
            "Radio Instructions",  // Title text
            javax.swing.border.TitledBorder.LEFT,  // Title aligned left
            javax.swing.border.TitledBorder.TOP,  // Title at top
            new Font("Arial", Font.BOLD, 12),  // Title font
            new Color(220, 20, 60)  // Crimson title color matches border
        ));
        setBackground(Color.WHITE);  // White background for panel
        setMaximumSize(new Dimension(300, 150));  // Limit maximum size
        
        // Create read-only text area for displaying radio instructions
        radioInstructionsArea = UIComponentFactory.createReadOnlyTextArea(6, 25, UIComponentFactory.COURIER_BOLD_11);
        radioInstructionsArea.setBackground(new Color(255, 240, 245));  // Lavender blush background
        radioInstructionsArea.setForeground(new Color(139, 0, 0));  // Dark red text
        radioInstructionsArea.setMargin(new Insets(5, 5, 5, 5));  // 5px padding all sides
        
        // Add scroll pane for long instruction lists
        JScrollPane scrollPane = new JScrollPane(radioInstructionsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  // Show scrollbar when needed
        
        add(scrollPane, BorderLayout.CENTER);  // Add scroll pane to center
    }
    
    /**
     * Updates the radio instructions display with new text.
     * Sets the text area content and scrolls to top.
     * 
     * @param text the formatted radio instructions to display
     */
    public void updateInstructions(String text) {
        if (radioInstructionsArea != null) {  // Check text area is initialized
            radioInstructionsArea.setText(text);  // Update display text
            radioInstructionsArea.setCaretPosition(0);  // Scroll to top (newest first)
        }
    }
    
    /**
     * Returns the instructions text area for direct manipulation.
     * 
     * @return the JTextArea displaying radio instructions
     */
    public JTextArea getInstructionsArea() {
        return radioInstructionsArea;  // Return text area reference
    }
}
