/**
 * WaterDetector.java
 * by Haisam Elkewidy
 *
 * This class detects water regions in map images and finds safe land points for jetpack navigation.
 * Uses RGB pixel analysis with empirically-derived thresholds to identify water bodies like rivers,
 * harbors, and lakes, ensuring jetpacks don't park or land in water.
 *
 * Variables:
 *   - mapImage (BufferedImage): The city map image used for water detection
 *   - width (int): Width of the map image in pixels
 *   - height (int): Height of the map image in pixels
 *
 * Methods:
 *   - WaterDetector(resourcePath): Constructor that loads the map image
 *   - isWater(x, y): Checks if a coordinate is water based on RGB analysis
 *   - getRandomLandPoint(rand, margin): Finds a random valid land point
 *   - findClosestLandPoint(x, y): Finds nearest land using spiral search
 *   - getWidth(): Returns map width
 *   - getHeight(): Returns map height
 *
 * The water detection algorithm analyzes blue dominance in RGB values to identify water pixels.
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
            throw new IOException("Could not find resource");
        }
        if (mapImage == null) {
            throw new IOException("Could not find resource");
        }
        width = mapImage.getWidth();
        height = mapImage.getHeight();
    }

    public boolean isWater(int x, int y) {
        // Treat out-of-bounds as water for safety
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return true;
        }
        
        // Extract RGB components from pixel
        int rgb = mapImage.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        
        // Water detection using empirically-derived RGB thresholds from city map analysis
        // These values were tuned to match typical water colors in urban map imagery:
        // - Rivers/harbors tend to be blue-dominant (b > r+20, b > g+20)
        // - Deep water has high absolute blue values (b > 150)
        // - Dark water (shadows) has low red, moderate green, higher blue (r < 100, g < 150, b > 100)
        return (b > r + 20 && b > g + 20) ||        // Standard water: blue dominates by significant margin
               (b > 150 && b > r && b > g) ||        // Deep water: high blue value with blue dominance
               (r < 100 && g < 150 && b > 100 && b - r > 30);  // Dark water: constrained RGB ranges with blue bias
    }

    public Point getRandomLandPoint(Random rand, int margin) {
        int maxAttempts = 1000;
        
        // Try random points until we find land
        for (int i = 0; i < maxAttempts; i++) {
            int x = margin + rand.nextInt(width - 2 * margin);
            int y = margin + rand.nextInt(height - 2 * margin);
            if (!isWater(x, y)) {
                return new Point(x, y);
            }
        }
        
        // Fallback to map center if no land found after max attempts
        return new Point(width / 2, height / 2);
    }

    public Point findClosestLandPoint(int x, int y) {
        // If already on land, return current position
        if (!isWater(x, y)) {
            return new Point(x, y);
        }
        
        // Spiral search outward from current position to find nearest land
        int maxRadius = Math.max(width, height);
        for (int radius = 10; radius < maxRadius; radius += 10) {
            // Check points in a circle at this radius
            int numPoints = radius * 4;  // More points for larger circles
            for (int i = 0; i < numPoints; i++) {
                double angle = (2 * Math.PI * i) / numPoints;
                int testX = x + (int)(radius * Math.cos(angle));
                int testY = y + (int)(radius * Math.sin(angle));
                
                // Check if point is within map bounds
                if (testX >= 0 && testX < width && testY >= 0 && testY < height) {
                    if (!isWater(testX, testY)) {
                        return new Point(testX, testY);  // Found closest land
                    }
                }
            }
        }
        
        // Fallback to map center if no land found (unlikely)
        return new Point(width / 2, height / 2);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}