/**
 * RadioInstructionsPanel.java
 * by Haisam Elkewidy
 *
 * This class handles RadioInstructionsPanel functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - radioInstructionsArea (JTextArea)
 *
 * Methods:
 *   - RadioInstructionsPanel()
 *   - updateInstructions(text)
 *
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
    private JTextArea radioInstructionsArea;
    
    public RadioInstructionsPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 20, 60), 2),
            "Radio Instructions",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(220, 20, 60)
        ));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(300, 150));
        
        radioInstructionsArea = UIComponentFactory.createReadOnlyTextArea(6, 25, UIComponentFactory.COURIER_BOLD_11);
        radioInstructionsArea.setBackground(new Color(255, 240, 245));
        radioInstructionsArea.setForeground(new Color(139, 0, 0));
        radioInstructionsArea.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(radioInstructionsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Updates the radio instructions display
     */
    public void updateInstructions(String text) {
        if (radioInstructionsArea != null) {
            radioInstructionsArea.setText(text);
            radioInstructionsArea.setCaretPosition(0);
        }
    }
    
    public JTextArea getInstructionsArea() {
        return radioInstructionsArea;
    }
}
