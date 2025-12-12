/**
 * ConsoleOutputPanel.java
 * by Haisam Elkewidy
 *
 * ConsoleOutputPanel.java
 */

package com.example.ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.example.ui.utility.UIComponentFactory;

/**
 * ConsoleOutputPanel - Terminal-style output panel for status and log messages.
 */
public class ConsoleOutputPanel extends JScrollPane {
    private JTextArea consoleOutput;
    
    public ConsoleOutputPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        consoleOutput = UIComponentFactory.createReadOnlyTextArea(8, 80, UIComponentFactory.COURIER_PLAIN_11);
        consoleOutput.setBackground(Color.BLACK);
        consoleOutput.setForeground(new Color(0, 255, 0)); // Green text
        consoleOutput.setMargin(new Insets(5, 5, 5, 5));
        
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
            "Console Output",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            Color.DARK_GRAY
        ));
        
        setViewportView(consoleOutput);
    }
    
    /**
     * Appends a message to the console
     */
    public void appendMessage(String message) {
        if (consoleOutput != null) {
            consoleOutput.append(message + "\n");
            consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());
        }
    }
    
    /**
     * Clears the console
     */
    public void clear() {
        if (consoleOutput != null) {
            consoleOutput.setText("");
        }
    }
    
    public JTextArea getConsoleArea() {
        return consoleOutput;
    }
}
