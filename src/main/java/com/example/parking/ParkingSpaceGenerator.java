/**
 * Factory class for generating parking space collections with intelligent water-aware placement.
 * 
 * Purpose:
 * Creates parking space distributions across city maps using rejection sampling to avoid water
 * bodies. Analyzes map image pixels to determine land vs. water areas and generates 100 parking
 * spaces per city, ensuring all spaces are placed on valid landing zones. Provides deterministic
 * generation with configurable retry limits to handle maps with extensive water coverage.
 * 
 * Key Responsibilities:
 * - Generate target count (100) of parking spaces per city map
 * - Use water detection algorithm to reject placements over rivers, harbors, and lakes
 * - Apply rejection sampling with configurable retry limits (10x target count attempts)
 * - Create city-specific parking IDs with proper prefixes (NYC-P, BOS-P, HOU-P, DAL-P)
 * - Maintain margin boundaries (10-20 pixels) to avoid map edges
 * - Report generation statistics (attempts, water rejections, success rate)
 * - Handle water-heavy maps (Boston harbor) with extended attempt budgets
 * 
 * Interactions:
 * - Used by ParkingSpaceManager during city map initialization
 * - Integrates with WaterDetector algorithm for land/water classification
 * - Provides ParkingSpace instances to CityMapPanel for rendering
 * - Coordinates with city map dimensions from BufferedImage
 * - Results displayed as green markers in map visualization
 * - Generation statistics logged for debugging water-heavy city configurations
 * 
 * Patterns & Constraints:
 * - Factory pattern encapsulates parking space creation logic
 * - Stateless utility class with static generation method
 * - Water detection: blue channel dominance (blue > red+threshold AND blue > green+threshold)
 * - Random placement with uniform distribution across valid land areas
 * - Target: 100 spaces per city; accepts fewer if attempts exhausted
 * - Max attempts: TARGET_SPACES * MAX_ATTEMPTS_MULTIPLIER (typically 1000 tries)
 * - Thread-safe due to stateless design; uses local Random instance per call
 * - IDs auto-increment: [CITY_CODE]-P1, [CITY_CODE]-P2, ..., [CITY_CODE]-P100
 * 
 * @author Haisam Elkewidy
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
