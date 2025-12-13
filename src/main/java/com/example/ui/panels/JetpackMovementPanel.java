/**
 * UI panel component for jetpackmovement display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing jetpackmovement-related visualization and user interaction.
 * Integrates with the main application frame to present jetpackmovement data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render jetpackmovement information with appropriate visual styling
 * - Handle user interactions related to jetpackmovement operations
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
 * JetpackMovementPanel.java
 * by Haisam Elkewidy
 * 
 * Displays real-time jetpack movement information.
 */
public class JetpackMovementPanel extends JPanel {
    // Text area for displaying real-time jetpack movement information
    private JTextArea jetpackMovementArea;
    
    /**
     * Constructs a new JetpackMovementPanel.
     * Initializes movement tracking display with orange styling.
     */
    public JetpackMovementPanel() {
        // Initialize and configure UI components
        initializeUI();
    }
    
    /**
     * Initializes and configures all UI components.
     * Sets up display with dark orange border and cornsilk background.
     */
    private void initializeUI() {
        // Set BorderLayout for simple CENTER positioning
        setLayout(new BorderLayout());
        // Create titled border with dark orange color (255,140,0)
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 140, 0), 2),
            "Jetpack Movement",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(255, 140, 0)
        ));
        // Set white background for panel
        setBackground(Color.WHITE);
        // Constrain maximum size to fit movement content
        setMaximumSize(new Dimension(300, 200));
        
        // Create read-only text area with 10 rows, 25 columns, Courier Plain 10pt font
        jetpackMovementArea = UIComponentFactory.createReadOnlyTextArea(10, 25, UIComponentFactory.COURIER_PLAIN_10);
        // Set cornsilk background (255,250,240)
        jetpackMovementArea.setBackground(new Color(255, 250, 240));
        // Set saddle brown foreground text color (139,69,19)
        jetpackMovementArea.setForeground(new Color(139, 69, 19));
        // Add 5-pixel margin on all sides
        jetpackMovementArea.setMargin(new Insets(5, 5, 5, 5));
        
        // Create scroll pane wrapper for text area
        JScrollPane scrollPane = new JScrollPane(jetpackMovementArea);
        // Always show vertical scrollbar for consistent layout
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Add scroll pane to center of panel
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Appends movement text to the display with newline.
     * Automatically scrolls to show newest movement updates.
     * 
     * @param text Movement text to append
     */
    public void appendMovement(String text) {
        // Check if text area has been initialized
        if (jetpackMovementArea != null) {
            // Append movement text with newline character
            jetpackMovementArea.append(text + "\n");
            // Move caret to end of document to auto-scroll to newest movement
            jetpackMovementArea.setCaretPosition(jetpackMovementArea.getDocument().getLength());
        }
    }
    
    /**
     * Clears all text from the movement display.
     */
    public void clearMovement() {
        // Check if text area has been initialized
        if (jetpackMovementArea != null) {
            // Clear all text by setting empty string
            jetpackMovementArea.setText("");
        }
    }
    
    /**
     * Returns the text area component for direct access.
     * 
     * @return JTextArea displaying jetpack movement
     */
    public JTextArea getMovementArea() {
        // Return reference to movement text area
        return jetpackMovementArea;
    }
}
