/**
 * GridRenderer.java
 * by Haisam Elkewidy
 *
 * This class handles GridRenderer functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - GridRenderer()
 *
 */

package com.example.utility;

import java.awt.Graphics;
import javax.swing.JPanel;

public class GridRenderer extends JPanel {
    public GridRenderer() {
        super();
        // Initialization logic if needed
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Custom rendering logic for city grid
    }
}
