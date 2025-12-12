/**
 * WeatherBroadcastPanel.java
 * by Haisam Elkewidy
 *
 * This class handles WeatherBroadcastPanel functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - weatherBroadcastArea (JTextArea)
 *
 * Methods:
 *   - WeatherBroadcastPanel()
 *   - updateBroadcast(text)
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
 * WeatherBroadcastPanel.java
 * by Haisam Elkewidy
 * 
 * Displays real-time weather broadcast information for a city.
 */
public class WeatherBroadcastPanel extends JPanel {
    private JTextArea weatherBroadcastArea;
    
    public WeatherBroadcastPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Weather Broadcast",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(34, 139, 34)
        ));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(300, 150));
        
        weatherBroadcastArea = UIComponentFactory.createReadOnlyTextArea(6, 25, UIComponentFactory.COURIER_PLAIN_11);
        weatherBroadcastArea.setBackground(new Color(240, 255, 240));
        weatherBroadcastArea.setForeground(new Color(0, 100, 0));
        weatherBroadcastArea.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(weatherBroadcastArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Updates the weather broadcast display
     */
    public void updateBroadcast(String text) {
        if (weatherBroadcastArea != null) {
            weatherBroadcastArea.setText(text);
            weatherBroadcastArea.setCaretPosition(0);
        }
    }
    
    public JTextArea getBroadcastArea() {
        return weatherBroadcastArea;
    }
}
