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
    private JTextArea jetpackMovementArea;
    
    public JetpackMovementPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 140, 0), 2),
            "Jetpack Movement",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(255, 140, 0)
        ));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(300, 200));
        
        jetpackMovementArea = UIComponentFactory.createReadOnlyTextArea(10, 25, UIComponentFactory.COURIER_PLAIN_10);
        jetpackMovementArea.setBackground(new Color(255, 250, 240));
        jetpackMovementArea.setForeground(new Color(139, 69, 19));
        jetpackMovementArea.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(jetpackMovementArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Appends movement text to the display
     */
    public void appendMovement(String text) {
        if (jetpackMovementArea != null) {
            jetpackMovementArea.append(text + "\n");
            jetpackMovementArea.setCaretPosition(jetpackMovementArea.getDocument().getLength());
        }
    }
    
    /**
     * Clears the movement display
     */
    public void clearMovement() {
        if (jetpackMovementArea != null) {
            jetpackMovementArea.setText("");
        }
    }
    
    public JTextArea getMovementArea() {
        return jetpackMovementArea;
    }
}
