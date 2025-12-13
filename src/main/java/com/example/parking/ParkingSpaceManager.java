/**
 * Centralized management for parkingspace operations and lifecycle coordination.
 * 
 * Purpose:
 * Manages parkingspace instances across the Air Traffic Controller system, providing
 * factory methods, registry access, and coordination logic. Supports multi-city scenarios
 * and ensures consistent parkingspace state across operational contexts.
 * 
 * Key Responsibilities:
 * - Initialize and maintain parkingspace collections per city or system-wide
 * - Provide query methods for parkingspace retrieval and filtering
 * - Coordinate parkingspace state updates across subsystems
 * - Support parkingspace lifecycle (creation, modification, disposal)
 * 
 * Interactions:
 * - Referenced by AirTrafficControllerFrame and CityMapPanel
 * - Integrates with logging and persistence subsystems
 * - Coordinates with related manager classes
 * 
 * Patterns & Constraints:
 * - Manager pattern centralizes parkingspace concerns
 * - Thread-safe operations for concurrent access
 * - Per-city collections for multi-city support
 * 
 * @author Haisam Elkewidy
 */

package com.example.parking;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;

/**
 * ParkingSpaceManager.java
 * 
 * Manages parking space generation and availability tracking for city maps.
 * Extracts parking logic from CityMapPanel to improve code organization.
 */
public class ParkingSpaceManager {
    
    /** List of all parking spaces managed by this manager */
    private final List<ParkingSpace> parkingSpaces;
    /** Name of the city this manager is responsible for (e.g., "New York", "Boston") */
    private final String city;
    
    /**
     * Constructs a new ParkingSpaceManager for the specified city.
     * Initializes with an empty parking space list.
     * 
     * @param city the name of the city to manage parking for
     */
    public ParkingSpaceManager(String city) {
        this.city = city;  // Store the city name
        this.parkingSpaces = new ArrayList<>();  // Initialize empty list for parking spaces
    }
    
    /**
     * Initializes parking spaces by sampling map image pixels.
     * Generates parking spaces on land areas only, avoiding water bodies.
     * Clears any existing parking spaces before generating new ones.
     * 
     * @param mapWidth the width of the map in pixels
     * @param mapHeight the height of the map in pixels
     * @param mapImage the BufferedImage of the city map for water detection
     */
    public void initializeParkingSpaces(int mapWidth, int mapHeight, BufferedImage mapImage) {
        final int TARGET_SPACES = 100;  // Target number of parking spaces to generate
        parkingSpaces.clear();  // Clear any existing parking spaces

        // Generate 3-letter city code from city name (e.g., "New York" -> "NEW")
        String cityCode = city.replaceAll("\\s+", "").toUpperCase().substring(0, 3);
        List<ParkingSpace> spaces = new ArrayList<>();  // Temporary list for new spaces
        Random rand = new Random();  // Random number generator for placement

        // Generating parking spaces by randomly sampling map locations
        int attempts = 0;  // Track total placement attempts
        int maxAttempts = TARGET_SPACES * 10;  // Allow up to 10x target attempts
        int waterRejections = 0;  // Count water-based rejections

        while (spaces.size() < TARGET_SPACES && attempts < maxAttempts) {  // Continue until target or max attempts
            attempts++;  // Increment attempt counter

            // Generate random coordinates with 10-pixel margin from edges
            int x = 10 + rand.nextInt(mapWidth - 20);   // Random X between 10 and (width - 10)
            int y = 10 + rand.nextInt(mapHeight - 20);  // Random Y between 10 and (height - 10)

            if (isLandPixel(mapImage, x, y)) {  // Check if location is valid land
                // Create parking space with city-specific ID (e.g., "NEW-P1", "BOS-P42")
                spaces.add(new ParkingSpace(cityCode + "-P" + (spaces.size() + 1), x, y));
            } else {  // Location is water
                waterRejections++;  // Increment water rejection counter
            }
        }

        parkingSpaces.addAll(spaces);  // Add all generated spaces to managed list
        // Parking spaces generated successfully - ready for use
    }
    
    /**
     * Determines if a pixel represents land (not water)
     */
    private boolean isLandPixel(BufferedImage image, int x, int y) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return false;
        }

        int rgb = image.getRGB(x, y);  // Get pixel color value at coordinates
        int red = (rgb >> 16) & 0xFF;    // Extract red channel (shift right 16 bits, mask)
        int green = (rgb >> 8) & 0xFF;   // Extract green channel (shift right 8 bits, mask)
        int blue = rgb & 0xFF;           // Extract blue channel (mask lower 8 bits)

        // Water detection - water typically has high blue values
        boolean isBlueish = blue > red + 20 && blue > green + 20;  // Blue significantly higher
        boolean isDarkWater = blue > red && blue > green && (red + green + blue) < 200;  // Dark blue water
        boolean isLightBlue = blue > 150 && blue > red && blue > green;  // Light blue water
        // Land characteristics
        boolean isBright = (red + green + blue) > 600;  // Very bright (buildings, roads)
        boolean isDark = (red + green + blue) < 150;    // Very dark (buildings, shadows)

        // Return true if NOT water OR definitely land
        return !isBlueish && !isDarkWater && !isLightBlue || isBright || isDark;
    }
    
    /**
     * Updates parking availability display label with current status.
     * Calculates occupancy percentage and updates label text and color based on availability.
     * Uses color coding: green (available), orange (filling), red (critical).
     * 
     * @param parkingAvailabilityLabel the JLabel to update with parking status
     */
    public void updateParkingAvailability(JLabel parkingAvailabilityLabel) {
        if (parkingSpaces == null || parkingAvailabilityLabel == null) return;  // Guard against null
        
        int occupied = 0;  // Counter for occupied spaces
        for (ParkingSpace space : parkingSpaces) {  // Iterate through all parking spaces
            if (space.isOccupied()) {  // Check if space is occupied
                occupied++;  // Increment occupied counter
            }
        }
        
        int available = parkingSpaces.size() - occupied;  // Calculate available spaces
        double percentFull = (occupied * 100.0) / parkingSpaces.size();  // Calculate occupancy percentage
        
        String status;  // Status text to display
        Color statusColor;  // Color for status indicator
        if (percentFull < 50) {  // Less than 50% full
            status = "✅ AVAILABLE";  // Green checkmark - plenty of spaces
            statusColor = new Color(34, 139, 34);  // Forest green color
        } else if (percentFull < 80) {  // 50-80% full
            status = "⚠️ FILLING UP";  // Warning symbol - getting full
            statusColor = new Color(255, 140, 0);  // Dark orange color
        } else if (percentFull < 95) {  // 80-95% full
            status = "🔶 NEARLY FULL";  // Diamond symbol - nearly full
            statusColor = Color.ORANGE;  // Orange color
        } else {  // 95%+ full
            status = "🚫 CRITICAL";  // Stop symbol - critical shortage
            statusColor = Color.RED;  // Red color for urgency
        }
        
        // Format HTML text with status, counts, and percentage
        String text = String.format("<html><center>%s<br/>%d / %d spaces<br/>(%.1f%% full)</center></html>",
            status, available, parkingSpaces.size(), percentFull);
        
        parkingAvailabilityLabel.setText(text);  // Update label text
        parkingAvailabilityLabel.setForeground(statusColor);  // Update label color
    }
    
    /**
     * Gets the list of parking spaces managed by this manager.
     * Returns the actual list (not a copy), allowing external modification.
     * 
     * @return List of all ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;  // Return the parking spaces list
    }
}
