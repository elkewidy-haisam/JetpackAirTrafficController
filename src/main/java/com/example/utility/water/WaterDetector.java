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
            throw new IOException("Could not find resource: " + resourcePath);
        }
        mapImage = ImageIO.read(is);
        width = mapImage.getWidth();
        height = mapImage.getHeight();
    }
    
    public boolean isWater(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return true; // Consider out of bounds as water
        }
        
        int rgb = mapImage.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        
        // Water detection: blue-ish colors
        // Water typically has higher blue value compared to red and green
        // and overall darker or saturated blue tones
        return (b > r + 20 && b > g + 20) || // More blue than red/green
               (b > 150 && b > r && b > g) || // High blue value
               (r < 100 && g < 150 && b > 100 && b - r > 30); // Dark bluish
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
        
        // Fallback: return center of map
        return new Point(width / 2, height / 2);
    }
    
    /**
     * Finds the closest land point from a given position
     * Used for emergency landings when jetpack is over water
     * 
     * @param x Current x position
     * @param y Current y position
     * @return Closest land point, or center of map if none found
     */
    public Point findClosestLandPoint(int x, int y) {
        // If already on land, return current position
        if (!isWater(x, y)) {
            return new Point(x, y);
        }
        
        // Spiral search outward from current position
        int maxRadius = Math.max(width, height);
        for (int radius = 10; radius < maxRadius; radius += 10) {
            // Check points in a circle around current position
            int numPoints = radius * 4; // More points for larger radius
            for (int i = 0; i < numPoints; i++) {
                double angle = (2 * Math.PI * i) / numPoints;
                int testX = x + (int)(radius * Math.cos(angle));
                int testY = y + (int)(radius * Math.sin(angle));
                
                // Check if point is within bounds
                if (testX >= 0 && testX < width && testY >= 0 && testY < height) {
                    if (!isWater(testX, testY)) {
                        return new Point(testX, testY);
                    }
                }
            }
        }
        
        // Fallback: return center of map
        return new Point(width / 2, height / 2);
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
}
