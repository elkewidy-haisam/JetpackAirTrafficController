/**
 * PerformanceMonitor.java
 * by Haisam Elkewidy
 *
 * This class handles PerformanceMonitor functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - frameCount (long)
 *   - currentFPS (double)
 *   - lastFPSUpdate (long)
 *   - visible (boolean)
 *
 * Methods:
 *   - PerformanceMonitor()
 *   - toggleVisibility()
 *   - tick()
 *   - render(g2d, x, y)
 *
 */

package com.example.utility.performance;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PerformanceMonitor {
    private long frameCount;
    private double currentFPS;
    private long lastFPSUpdate;
    private boolean visible;
    private static final long FPS_UPDATE_INTERVAL = 500; // Update FPS every 500ms

    public PerformanceMonitor() {
        this.frameCount = 0;
        this.currentFPS = 0;
        this.lastFPSUpdate = System.currentTimeMillis();
        this.visible = false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void toggleVisibility() {
        this.visible = !this.visible;
    }

    public void tick() {
        long currentTime = System.currentTimeMillis();
        frameCount++;
        if (currentTime - lastFPSUpdate >= FPS_UPDATE_INTERVAL) {
            long elapsed = currentTime - lastFPSUpdate;
            currentFPS = (frameCount * 1000.0) / elapsed;
            frameCount = 0;
            lastFPSUpdate = currentTime;
        }
    }

    public void render(Graphics2D g2d, int x, int y) {
        if (!visible) return;

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(x, y, 200, 80);

        g2d.setColor(new Color(100, 200, 100));
        g2d.drawRect(x, y, 200, 80);

        g2d.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2d.setColor(Color.WHITE);

        g2d.drawString("Performance Monitor", x + 5, y + 15);

        g2d.setColor(currentFPS < 15 ? Color.RED : currentFPS < 25 ? Color.YELLOW : Color.GREEN);
        g2d.drawString(String.format("FPS: %.1f", currentFPS), x + 5, y + 32);

        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;

        g2d.setColor(Color.WHITE);
        g2d.drawString(String.format("Memory: %d/%d MB", usedMemory, maxMemory), x + 5, y + 49);

        int barWidth = 180;
        int barHeight = 10;
        double memoryPercent = (double) usedMemory / maxMemory;

        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(x + 10, y + 55, barWidth, barHeight);

        Color barColor = memoryPercent < 0.7 ? Color.GREEN : memoryPercent < 0.9 ? Color.YELLOW : Color.RED;
        g2d.setColor(barColor);
        g2d.fillRect(x + 10, y + 55, (int)(barWidth * memoryPercent), barHeight);

        g2d.setColor(Color.WHITE);
        g2d.drawRect(x + 10, y + 55, barWidth, barHeight);
    }

    public double getFPS() {
        return currentFPS;
    }
}