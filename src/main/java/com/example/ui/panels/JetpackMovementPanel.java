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
    /** Text area for displaying jetpack movement activity log */
    private JTextArea jetpackMovementArea;
    
    /**
     * Constructs a new JetpackMovementPanel.
     * Initializes the UI with dark orange themed border and scrolling text area.
     */
    public JetpackMovementPanel() {
        initializeUI();  // Initialize UI components
    }
    
    /**
     * Initializes the UI layout and components.
     * Creates styled text area with dark orange border for movement tracking.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());  // Use BorderLayout for simple center placement
        // Create titled border with dark orange color
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 140, 0), 2),  // Dark orange 2px border
            "Jetpack Movement",  // Title text
            javax.swing.border.TitledBorder.LEFT,  // Title aligned left
            javax.swing.border.TitledBorder.TOP,  // Title at top
            new Font("Arial", Font.BOLD, 12),  // Title font
            new Color(255, 140, 0)  // Dark orange title color matches border
        ));
        setBackground(Color.WHITE);  // White background for panel
        setMaximumSize(new Dimension(300, 200));  // Limit maximum size
        
        // Create read-only text area for displaying movement log
        jetpackMovementArea = UIComponentFactory.createReadOnlyTextArea(10, 25, UIComponentFactory.COURIER_PLAIN_10);
        jetpackMovementArea.setBackground(new Color(255, 250, 240));  // Floral white background
        jetpackMovementArea.setForeground(new Color(139, 69, 19));  // Saddle brown text
        jetpackMovementArea.setMargin(new Insets(5, 5, 5, 5));  // 5px padding all sides
        
        // Add scroll pane for long movement log
        JScrollPane scrollPane = new JScrollPane(jetpackMovementArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Always show scrollbar
        
        add(scrollPane, BorderLayout.CENTER);  // Add scroll pane to center
    }
    
    /**
     * Appends movement text to the display with newline.
     * Auto-scrolls to show the new movement entry at bottom.
     * 
     * @param text the movement message to append to the log
     */
    public void appendMovement(String text) {
        if (jetpackMovementArea != null) {  // Check text area is initialized
            jetpackMovementArea.append(text + "\n");  // Append message with newline
            jetpackMovementArea.setCaretPosition(jetpackMovementArea.getDocument().getLength());  // Scroll to bottom
        }
    }
    
    /**
     * Clears all movement display entries.
     * Empties the text area to show blank log.
     */
    public void clearMovement() {
        if (jetpackMovementArea != null) {  // Check text area is initialized
            jetpackMovementArea.setText("");  // Clear all text
        }
    }
    
    /**
     * Returns the movement text area for direct manipulation.
     * 
     * @return the JTextArea displaying jetpack movement log
     */
    public JTextArea getMovementArea() {
        return jetpackMovementArea;  // Return text area reference
    }
}
