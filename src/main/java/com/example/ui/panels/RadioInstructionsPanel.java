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
    // Text area for displaying radio communications and ATC instructions
    private JTextArea radioInstructionsArea;
    
    /**
     * Constructs a new RadioInstructionsPanel.
     * Initializes radio instructions display with crimson styling.
     */
    public RadioInstructionsPanel() {
        // Initialize and configure UI components
        initializeUI();
    }
    
    /**
     * Initializes and configures all UI components.
     * Sets up display with crimson border and pale rose background.
     */
    private void initializeUI() {
        // Set BorderLayout for simple CENTER positioning
        setLayout(new BorderLayout());
        // Create titled border with crimson color (220,20,60)
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 20, 60), 2),
            "Radio Instructions",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(220, 20, 60)
        ));
        // Set white background for panel
        setBackground(Color.WHITE);
        // Constrain maximum size to fit radio instructions content
        setMaximumSize(new Dimension(300, 150));
        
        // Create read-only text area with 6 rows, 25 columns, Courier Bold 11pt font
        radioInstructionsArea = UIComponentFactory.createReadOnlyTextArea(6, 25, UIComponentFactory.COURIER_BOLD_11);
        // Set lavender blush background (255,240,245)
        radioInstructionsArea.setBackground(new Color(255, 240, 245));
        // Set dark red foreground text color (139,0,0)
        radioInstructionsArea.setForeground(new Color(139, 0, 0));
        // Add 5-pixel margin on all sides
        radioInstructionsArea.setMargin(new Insets(5, 5, 5, 5));
        
        // Create scroll pane wrapper for text area
        JScrollPane scrollPane = new JScrollPane(radioInstructionsArea);
        // Show vertical scrollbar only when needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Add scroll pane to center of panel
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Updates the displayed radio instructions text.
     * Resets caret to beginning for consistent display.
     * 
     * @param text New radio instructions string to display
     */
    public void updateInstructions(String text) {
        // Check if text area has been initialized
        if (radioInstructionsArea != null) {
            // Update text area content with new instructions
            radioInstructionsArea.setText(text);
            // Reset caret to beginning of text
            radioInstructionsArea.setCaretPosition(0);
        }
    }
    
    /**
     * Returns the text area component for direct access.
     * 
     * @return JTextArea displaying radio instructions
     */
    public JTextArea getInstructionsArea() {
        // Return reference to instructions text area
        return radioInstructionsArea;
    }
}
