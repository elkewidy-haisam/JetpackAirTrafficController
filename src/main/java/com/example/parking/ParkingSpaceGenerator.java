/**
 * ParkingSpaceGenerator.java
 * by Haisam Elkewidy
 *
 * ParkingSpaceGenerator.java
 */

package com.example.parking;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ParkingSpaceGenerator.java
 * 
 * Generates parking spaces for jetpacks by sampling map image pixels
 * and avoiding water areas.
 */
public class ParkingSpaceGenerator {
    
    private static final int TARGET_SPACES = 100;
    private static final int MAX_ATTEMPTS_MULTIPLIER = 10;
    
    /**
     * Generates parking spaces for a city map
     * 
     * @param mapWidth The width of the map
     * @param mapHeight The height of the map
     * @param mapImage The map image for water detection
     * @param cityCode The city code for parking space IDs
     * @return List of generated parking spaces
     */
    public List<ParkingSpace> generateParkingSpaces(int mapWidth, int mapHeight, 
                                                    BufferedImage mapImage, String cityCode) {
        List<ParkingSpace> spaces = new ArrayList<>();
        Random rand = new Random();

        // Generating parking spaces
        int attempts = 0;
        int maxAttempts = TARGET_SPACES * MAX_ATTEMPTS_MULTIPLIER;
        int waterRejections = 0;

        while (spaces.size() < TARGET_SPACES && attempts < maxAttempts) {
            attempts++;
            
            int x = 10 + rand.nextInt(mapWidth - 20);
            int y = 10 + rand.nextInt(mapHeight - 20);

            if (isLandPixel(mapImage, x, y)) {
                spaces.add(new ParkingSpace(cityCode + "-P" + (spaces.size() + 1), x, y));
                
                // Progress tracking removed for performance
            } else {
                waterRejections++;
            }
        }

        // Parking spaces generated
        
        return spaces;
    }
    
    /**
     * Determines if a pixel represents land (not water)
     * 
     * @param image The map image
     * @param x The x coordinate
     * @param y The y coordinate
     * @return true if the pixel is land, false if water
     */
    private boolean isLandPixel(BufferedImage image, int x, int y) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return false;
        }

        int rgb = image.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        // Detect water characteristics
        boolean isBlueish = blue > red + 20 && blue > green + 20;
        boolean isDarkWater = blue > red && blue > green && (red + green + blue) < 200;
        boolean isLightBlue = blue > 150 && blue > red && blue > green;
        
        // Land characteristics
        boolean isBright = (red + green + blue) > 600;
        boolean isDark = (red + green + blue) < 150;

        return !isBlueish && !isDarkWater && !isLightBlue || isBright || isDark;
    }
    
    /**
     * Gets the target number of parking spaces
     * 
     * @return Target number of spaces
     */
    public static int getTargetSpaces() {
        return TARGET_SPACES;
    }
}
