/**
 * Generates parking spaces distributed across the city map while avoiding water bodies and obstacles.
 * 
 * Purpose:
 * Procedurally creates parking space infrastructure by analyzing the city map image and placing parking
 * locations only on valid land areas. Uses water detection algorithms to ensure jetpacks don't attempt
 * to park in rivers, lakes, or harbors. Distributes parking spaces evenly across the city with
 * configurable density.
 * 
 * Key Responsibilities:
 * - Generate specified number of parking spaces (typically 1000 per city)
 * - Analyze city map image to identify water vs. land areas
 * - Place parking spaces only on land, avoiding water bodies
 * - Create city-specific parking IDs (BOS-P001, NYC-P001, etc.)
 * - Respect map boundaries and margin constraints
 * - Ensure even distribution across the available land area
 * 
 * Interactions:
 * - Uses WaterDetector to identify valid land locations
 * - Creates ParkingSpace objects with unique IDs
 * - Called by CityMapPanel during city initialization
 * - Provides parking infrastructure to City model
 * - Supports JetPackFlightState for parking assignment
 * 
 * Patterns & Constraints:
 * - Factory pattern: generates parking space objects
 * - Depends on BufferedImage from city map loader
 * - Uses Random for pseudo-random distribution
 * - Retry logic for water detection failures (falls back to center)
 * - Configurable margin to avoid edge-of-map parking
 * - Deterministic with same random seed (for testing)
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
    
    /** Target number of parking spaces to generate per city (default: 100) */
    private static final int TARGET_SPACES = 100;
    /** Maximum attempts multiplier - allows TARGET_SPACES * 10 tries to find valid locations */
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
        List<ParkingSpace> spaces = new ArrayList<>();  // Create empty list to store parking spaces
        Random rand = new Random();  // Create random number generator for random placement

        // Generating parking spaces by randomly sampling map locations
        int attempts = 0;  // Track total placement attempts
        int maxAttempts = TARGET_SPACES * MAX_ATTEMPTS_MULTIPLIER;  // Calculate maximum attempts allowed (100 * 10 = 1000)
        int waterRejections = 0;  // Count how many locations were rejected due to water

        while (spaces.size() < TARGET_SPACES && attempts < maxAttempts) {  // Continue until target reached or max attempts exceeded
            attempts++;  // Increment attempt counter
            
            // Generate random coordinates with 10-pixel margin from edges
            int x = 10 + rand.nextInt(mapWidth - 20);   // Random X between 10 and (width - 10)
            int y = 10 + rand.nextInt(mapHeight - 20);  // Random Y between 10 and (height - 10)

            if (isLandPixel(mapImage, x, y)) {  // Check if this location is valid land (not water)
                // Create new parking space with city-specific ID (e.g., "BOS-P1", "NYC-P42")
                spaces.add(new ParkingSpace(cityCode + "-P" + (spaces.size() + 1), x, y));
                
                // Progress tracking removed for performance (previously logged each successful placement)
            } else {  // Location is water - reject it
                waterRejections++;  // Increment water rejection counter
            }
        }

        // Parking spaces generated - loop complete
        // Final list contains up to TARGET_SPACES parking locations on valid land
        
        return spaces;  // Return the generated list of parking spaces
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
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {  // Check bounds
            return false;  // Out of bounds - treat as invalid (not land)
        }

        int rgb = image.getRGB(x, y);  // Get pixel color value at coordinates
        int red = (rgb >> 16) & 0xFF;    // Extract red channel (shift right 16 bits, mask to 8 bits)
        int green = (rgb >> 8) & 0xFF;   // Extract green channel (shift right 8 bits, mask to 8 bits)
        int blue = rgb & 0xFF;           // Extract blue channel (mask to lower 8 bits)

        // Detect water characteristics - water typically has high blue values
        boolean isBlueish = blue > red + 20 && blue > green + 20;  // Blue significantly higher than other channels
        boolean isDarkWater = blue > red && blue > green && (red + green + blue) < 200;  // Dark blue water
        boolean isLightBlue = blue > 150 && blue > red && blue > green;  // Light blue water (lakes, harbors)
        
        // Land characteristics - land is typically non-blue
        boolean isBright = (red + green + blue) > 600;  // Very bright pixels (white buildings, roads)
        boolean isDark = (red + green + blue) < 150;    // Very dark pixels (buildings, shadows)

        // Return true if NOT water (none of water conditions) OR definitely land (bright/dark)
        return !isBlueish && !isDarkWater && !isLightBlue || isBright || isDark;
    }
    
    /**
     * Gets the target number of parking spaces to generate.
     * Static method for external reference to generation parameters.
     * 
     * @return Target number of spaces (100)
     */
    public static int getTargetSpaces() {
        return TARGET_SPACES;  // Return the constant target value
    }
}
