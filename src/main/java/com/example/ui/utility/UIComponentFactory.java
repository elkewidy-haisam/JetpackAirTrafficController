/**
 * UIComponentFactory component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides uicomponentfactory functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core uicomponentfactory operations
 * - Maintain necessary state for uicomponentfactory functionality
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

package com.example.ui.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * UIComponentFactory - Centralized factory for styled UI components.
 */
public class UIComponentFactory {
        // Standard button color
        public static final java.awt.Color STANDARD_BUTTON_BLUE = new java.awt.Color(30, 90, 180);
    
    // Standard fonts
    public static final Font MONOSPACED_PLAIN_10 = new Font("Monospaced", Font.PLAIN, 10);
    public static final Font MONOSPACED_PLAIN_11 = new Font("Monospaced", Font.PLAIN, 11);
    public static final Font MONOSPACED_PLAIN_12 = new Font("Monospaced", Font.PLAIN, 12);
    public static final Font MONOSPACED_BOLD_10 = new Font("Monospaced", Font.BOLD, 10);
    public static final Font MONOSPACED_BOLD_11 = new Font("Monospaced", Font.BOLD, 11);
    public static final Font MONOSPACED_BOLD_14 = new Font("Monospaced", Font.BOLD, 14);
    
    public static final Font COURIER_PLAIN_10 = new Font("Courier New", Font.PLAIN, 10);
    public static final Font COURIER_PLAIN_11 = new Font("Courier New", Font.PLAIN, 11);
    public static final Font COURIER_PLAIN_12 = new Font("Courier New", Font.PLAIN, 12);
    public static final Font COURIER_BOLD_11 = new Font("Courier New", Font.BOLD, 11);
    
    public static final Font ARIAL_PLAIN_12 = new Font("Arial", Font.PLAIN, 12);
    public static final Font ARIAL_PLAIN_14 = new Font("Arial", Font.PLAIN, 14);
    public static final Font ARIAL_BOLD_12 = new Font("Arial", Font.BOLD, 12);
    public static final Font ARIAL_BOLD_13 = new Font("Arial", Font.BOLD, 13);
    public static final Font ARIAL_BOLD_14 = new Font("Arial", Font.BOLD, 14);
    public static final Font ARIAL_BOLD_16 = new Font("Arial", Font.BOLD, 16);
    public static final Font ARIAL_BOLD_20 = new Font("Arial", Font.BOLD, 20);
    
    /**
     * Creates a non-editable, wrapped JTextArea with specified dimensions
     * 
     * @param rows Number of rows
     * @param columns Number of columns
     * @param font Font to use
     * @return Configured JTextArea
     */
    public static JTextArea createReadOnlyTextArea(int rows, int columns, Font font) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(font);
        return textArea;
    }
    
    /**
     * Creates a non-editable JTextArea with specified dimensions and default font
     * 
     * @param rows Number of rows
     * @param columns Number of columns
     * @return Configured JTextArea
     */
    public static JTextArea createReadOnlyTextArea(int rows, int columns) {
        return createReadOnlyTextArea(rows, columns, COURIER_PLAIN_11);
    }
    
    /**
     * Creates a JScrollPane with specified viewport view
     * 
     * @param view Component to wrap in scroll pane
     * @return JScrollPane containing the view
     */
    public static JScrollPane createScrollPane(Component view) {
        return new JScrollPane(view);
    }
    
    /**
     * Creates a JScrollPane with both scroll bars always visible
     * 
     * @param view Component to wrap in scroll pane
     * @return JScrollPane with always-visible scroll bars
     */
    public static JScrollPane createScrollPaneAlwaysVisible(Component view) {
        return new JScrollPane(view, 
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }
    
    /**
     * Creates a JPanel with BorderLayout
     * 
     * @return JPanel with BorderLayout
     */
    public static JPanel createBorderLayoutPanel() {
        return new JPanel(new BorderLayout());
    }
    
    /**
     * Creates a JPanel with BorderLayout and gaps
     * 
     * @param hgap Horizontal gap
     * @param vgap Vertical gap
     * @return JPanel with BorderLayout
     */
    public static JPanel createBorderLayoutPanel(int hgap, int vgap) {
        return new JPanel(new BorderLayout(hgap, vgap));
    }
    
    /**
     * Creates a JPanel with FlowLayout
     * 
     * @return JPanel with FlowLayout
     */
    public static JPanel createFlowLayoutPanel() {
        return new JPanel(new FlowLayout());
    }
    
    /**
     * Creates a JPanel with FlowLayout and alignment
     * 
     * @param align Alignment (FlowLayout.LEFT, CENTER, RIGHT, etc.)
     * @return JPanel with FlowLayout
     */
    public static JPanel createFlowLayoutPanel(int align) {
        return new JPanel(new FlowLayout(align));
    }
    
    /**
     * Sets preferred size on a component and returns it for chaining
     * 
     * @param component Component to set size on
     * @param width Preferred width
     * @param height Preferred height
     * @return The component (for chaining)
     */
    public static <T extends JComponent> T setPreferredSize(T component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        return component;
    }
    
    /**
     * Creates a JButton with specified text and font
     * 
     * @param text Button text
     * @param font Font to use
     * @return Configured JButton
     */
    public static JButton createButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(STANDARD_BUTTON_BLUE);
        button.setForeground(java.awt.Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }
    
    /**
     * Creates a JButton with specified text and preferred size
     * 
     * @param text Button text
     * @param width Preferred width
     * @param height Preferred height
     * @return Configured JButton
     */
    public static JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(STANDARD_BUTTON_BLUE);
        button.setForeground(java.awt.Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }
    
    /**
     * Creates a JLabel with specified text and font
     * 
     * @param text Label text
     * @param font Font to use
     * @return Configured JLabel
     */
    public static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }
    
    /**
     * Creates a centered JLabel with specified text and font
     * 
     * @param text Label text
     * @param font Font to use
     * @return Configured JLabel with centered alignment
     */
    public static JLabel createCenteredLabel(String text, Font font) {
        JLabel label = createLabel(text, font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    /**
     * Creates a JComboBox with specified items and font
     * 
     * @param items Items for the combo box
     * @param font Font to use
     * @return Configured JComboBox
     */
    public static <T> JComboBox<T> createComboBox(T[] items, Font font) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(font);
        return comboBox;
    }
}
