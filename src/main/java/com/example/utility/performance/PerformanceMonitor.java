/**
 * Duplicate/alias of com.example.utility.PerformanceMonitor for performance profiling.
 * 
 * Purpose:
 * Subpackage version with identical timing functionality. May include extended performance metrics or
 * be consolidated with parent package version in future refactoring.
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility.performance;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PerformanceMonitor {
    /** Count of frames rendered since last FPS calculation */
    private long frameCount;
    /** Current calculated frames per second */
    private double currentFPS;
    /** Timestamp of last FPS calculation update */
    private long lastFPSUpdate;
    /** Whether the performance overlay should be rendered */
    private boolean visible;
    /** Interval in milliseconds between FPS updates (500ms = 2 updates/second) */
    private static final long FPS_UPDATE_INTERVAL = 500;

    /**
     * Constructs a new PerformanceMonitor with default settings.
     * Initializes FPS tracking and sets overlay as hidden.
     */
    public PerformanceMonitor() {
        this.frameCount = 0;                            // Start with no frames counted
        this.currentFPS = 0;                            // Initial FPS is 0
        this.lastFPSUpdate = System.currentTimeMillis();  // Record start time
        this.visible = false;                           // Start with overlay hidden
    }

    /**
     * Sets the visibility of the performance overlay.
     * @param visible true to show overlay, false to hide
     */
    public void setVisible(boolean visible) {
        this.visible = visible;  // Update visibility flag
    }

    /**
     * Returns whether the performance overlay is visible.
     * @return true if visible, false if hidden
     */
    public boolean isVisible() {
        return visible;  // Return visibility state
    }

    /**
     * Toggles the visibility of the performance overlay.
     * If visible, makes hidden; if hidden, makes visible.
     */
    public void toggleVisibility() {
        this.visible = !this.visible;  // Flip visibility flag
    }

    /**
     * Updates FPS calculation - call once per frame.
     * Increments frame counter and recalculates FPS if update interval has elapsed.
     */
    public void tick() {
        long currentTime = System.currentTimeMillis();  // Get current timestamp
        frameCount++;  // Increment frame counter
        if (currentTime - lastFPSUpdate >= FPS_UPDATE_INTERVAL) {  // Check if update interval passed
            long elapsed = currentTime - lastFPSUpdate;  // Calculate time elapsed since last update
            currentFPS = (frameCount * 1000.0) / elapsed;  // Calculate FPS: (frames * 1000ms) / elapsed ms
            frameCount = 0;                      // Reset frame counter for next interval
            lastFPSUpdate = currentTime;        // Update last update timestamp
        }
    }

    /**
     * Renders the performance overlay at specified position.
     * Draws FPS, memory usage, and memory bar chart if visible.
     * 
     * @param g2d Graphics2D context to render on
     * @param x x-coordinate of overlay top-left corner
     * @param y y-coordinate of overlay top-left corner
     */
    public void render(Graphics2D g2d, int x, int y) {
        if (!visible) return;  // Skip rendering if overlay is hidden

        // Draw semi-transparent black background
        g2d.setColor(new Color(0, 0, 0, 180));  // Black with 180/255 alpha
        g2d.fillRect(x, y, 200, 80);  // Draw background rectangle

        // Draw green border
        g2d.setColor(new Color(100, 200, 100));  // Light green color
        g2d.drawRect(x, y, 200, 80);  // Draw border rectangle

        // Set font for text
        g2d.setFont(new Font("Monospaced", Font.BOLD, 12));  // Monospaced bold 12pt
        g2d.setColor(Color.WHITE);  // White text color

        // Draw title
        g2d.drawString("Performance Monitor", x + 5, y + 15);  // Title text at top

        // Draw FPS with color coding based on performance
        g2d.setColor(currentFPS < 15 ? Color.RED : currentFPS < 25 ? Color.YELLOW : Color.GREEN);
        g2d.drawString(String.format("FPS: %.1f", currentFPS), x + 5, y + 32);  // FPS with 1 decimal

        // Get memory usage from Runtime
        Runtime runtime = Runtime.getRuntime();  // Get Runtime instance
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;  // Convert bytes to MB
        long maxMemory = runtime.maxMemory() / 1024 / 1024;  // Max memory in MB

        // Draw memory text
        g2d.setColor(Color.WHITE);  // White text
        g2d.drawString(String.format("Memory: %d/%d MB", usedMemory, maxMemory), x + 5, y + 49);

        // Draw memory usage bar
        int barWidth = 180;  // Width of memory bar
        int barHeight = 10;  // Height of memory bar
        double memoryPercent = (double) usedMemory / maxMemory;  // Calculate usage percentage

        // Draw bar background (dark gray)
        g2d.setColor(new Color(50, 50, 50));  // Dark gray
        g2d.fillRect(x + 10, y + 55, barWidth, barHeight);  // Background bar

        // Draw filled portion based on memory usage (color coded)
        Color barColor = memoryPercent < 0.7 ? Color.GREEN : memoryPercent < 0.9 ? Color.YELLOW : Color.RED;
        g2d.setColor(barColor);  // Set color based on memory usage level
        g2d.fillRect(x + 10, y + 55, (int)(barWidth * memoryPercent), barHeight);  // Draw filled portion

        // Draw bar border
        g2d.setColor(Color.WHITE);  // White border
        g2d.drawRect(x + 10, y + 55, barWidth, barHeight);  // Draw border around bar
    }

    /**
     * Returns the current FPS value.
     * @return current frames per second
     */
    public double getFPS() {
        return currentFPS;  // Return current FPS value
    }
}