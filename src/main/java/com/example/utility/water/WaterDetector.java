/*
 * WaterDetector.java
 * Part of Jetpack Air Traffic Controller
 *
 * Utility for detecting water regions in map images and finding land points for jetpack navigation.
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.utility.water;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;

public class WaterDetector {
    private BufferedImage mapImage;
    private int width;
    private int height;

    public WaterDetector(String resourcePath) throws IOException {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IOException("Could not find resource");
        }
        try {
            mapImage = ImageIO.read(is);
        } catch (Exception e) {
            throw new IOException("Could not read image from resource: " + resourcePath);
        }
        if (mapImage == null) {
            throw new IOException("Could not read image from resource: " + resourcePath);
        }
        width = mapImage.getWidth();
        height = mapImage.getHeight();
    }

    public boolean isWater(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return true;
        }
        int rgb = mapImage.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (b > r + 20 && b > g + 20) ||
               (b > 150 && b > r && b > g) ||
               (r < 100 && g < 150 && b > 100 && b - r > 30);
    }

    public Point getRandomLandPoint(Random rand, int margin) {
        int maxAttempts = 1000;
        for (int i = 0; i < maxAttempts; i++) {
            int x = margin + rand.nextInt(width - 2 * margin);
            int y = margin + rand.nextInt(height - 2 * margin);
            if (!isWater(x, y)) {
                return new Point(x, y);
            }
        }
        return new Point(width / 2, height / 2);
    }

    public Point findClosestLandPoint(int x, int y) {
        if (!isWater(x, y)) {
            return new Point(x, y);
        }
        int maxRadius = Math.max(width, height);
        for (int radius = 10; radius < maxRadius; radius += 10) {
            int numPoints = radius * 4;
            for (int i = 0; i < numPoints; i++) {
                double angle = (2 * Math.PI * i) / numPoints;
                int testX = x + (int)(radius * Math.cos(angle));
                int testY = y + (int)(radius * Math.sin(angle));
                if (testX >= 0 && testX < width && testY >= 0 && testY < height) {
                    if (!isWater(testX, testY)) {
                        return new Point(testX, testY);
                    }
                }
            }
        }
        return new Point(width / 2, height / 2);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}