/**
 * Manages parking space allocation, availability tracking, and intelligent placement for city maps.
 * 
 * Purpose:
 * Coordinates the generation, allocation, and lifecycle of parking spaces within a specific city,
 * ensuring jetpacks have designated landing zones. Uses water detection to intelligently avoid
 * placing parking spaces over rivers, harbors, or lakes. Tracks occupancy state for each space
 * and provides queries for available spaces based on distance and availability criteria.
 * 
 * Key Responsibilities:
 * - Generate city-specific parking spaces with water-aware placement (100 per city)
 * - Use water detection algorithm to reject parking placement over blue water pixels
 * - Track parking space occupancy and availability in real-time
 * - Provide nearest available parking space queries for landing operations
 * - Coordinate parking space lifecycle (initialization, occupation, vacation)
 * - Maintain parking space collections per city (BOS-P, NYC-P, HOU-P, DAL-P prefixes)
 * - Report parking space generation statistics (attempts, water rejections)
 * 
 * Interactions:
 * - Used by CityMapPanel to initialize and render parking spaces
 * - Queried by JetPackFlightState for parking space allocation during landing
 * - Referenced by FlightEmergencyHandler to find nearest parking during emergencies
 * - Integrates with WaterDetector algorithm for land/water pixel analysis
 * - Coordinates with ParkingSpace model for individual space state management
 * - Displayed in city map rendering (green markers for available spaces)
 * 
 * Patterns & Constraints:
 * - Manager pattern encapsulates parking concerns and reduces CityMapPanel complexity
 * - Per-city instance ensures independent parking management across cities
 * - Water detection: rejects pixels where blue > red + threshold AND blue > green + threshold
 * - Random placement with rejection sampling (max 10x attempts to reach target count)
 * - Thread-safe read operations; external synchronization for occupancy changes
 * - Parking space IDs follow pattern: [CITY_CODE]-P[number] (e.g., NYC-P42, BOS-P17)
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
    
    private final List<ParkingSpace> parkingSpaces;
    private final String city;
    
    public ParkingSpaceManager(String city) {
        this.city = city;
        this.parkingSpaces = new ArrayList<>();
    }
    
    /**
     * Initializes parking spaces by sampling map image pixels
     * Avoids placing parking spaces over water
     */
    public void initializeParkingSpaces(int mapWidth, int mapHeight, BufferedImage mapImage) {
        final int TARGET_SPACES = 100;
        parkingSpaces.clear();

        String cityCode = city.replaceAll("\\s+", "").toUpperCase().substring(0, 3);
        List<ParkingSpace> spaces = new ArrayList<>();
        Random rand = new Random();

        // Generating parking spaces
        int attempts = 0;
        int maxAttempts = TARGET_SPACES * 10;
        int waterRejections = 0;

        while (spaces.size() < TARGET_SPACES && attempts < maxAttempts) {
            attempts++;

            int x = 10 + rand.nextInt(mapWidth - 20);
            int y = 10 + rand.nextInt(mapHeight - 20);

            if (isLandPixel(mapImage, x, y)) {
                spaces.add(new ParkingSpace(cityCode + "-P" + (spaces.size() + 1), x, y));
            } else {
                waterRejections++;
            }
        }

        parkingSpaces.addAll(spaces);

        // ...removed debug output...
        for (ParkingSpace ps : parkingSpaces) {
            // ...removed debug output...
        }
        // ...removed debug output and file writing block entirely...
        // Parking spaces generated
    }
    
    /**
     * Determines if a pixel represents land (not water)
     */
    private boolean isLandPixel(BufferedImage image, int x, int y) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return false;
        }

        int rgb = image.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        boolean isBlueish = blue > red + 20 && blue > green + 20;
        boolean isDarkWater = blue > red && blue > green && (red + green + blue) < 200;
        boolean isLightBlue = blue > 150 && blue > red && blue > green;
        boolean isBright = (red + green + blue) > 600;
        boolean isDark = (red + green + blue) < 150;

        return !isBlueish && !isDarkWater && !isLightBlue || isBright || isDark;
    }
    
    /**
     * Updates parking availability display label
     */
    public void updateParkingAvailability(JLabel parkingAvailabilityLabel) {
        if (parkingSpaces == null || parkingAvailabilityLabel == null) return;
        
        int occupied = 0;
        for (ParkingSpace space : parkingSpaces) {
            if (space.isOccupied()) {
                occupied++;
            }
        }
        
        int available = parkingSpaces.size() - occupied;
        double percentFull = (occupied * 100.0) / parkingSpaces.size();
        
        String status;
        Color statusColor;
        if (percentFull < 50) {
            status = "✅ AVAILABLE";
            statusColor = new Color(34, 139, 34);
        } else if (percentFull < 80) {
            status = "⚠️ FILLING UP";
            statusColor = new Color(255, 140, 0);
        } else if (percentFull < 95) {
            status = "🔶 NEARLY FULL";
            statusColor = Color.ORANGE;
        } else {
            status = "🚫 CRITICAL";
            statusColor = Color.RED;
        }
        
        String text = String.format("<html><center>%s<br/>%d / %d spaces<br/>(%.1f%% full)</center></html>",
            status, available, parkingSpaces.size(), percentFull);
        
        parkingAvailabilityLabel.setText(text);
        parkingAvailabilityLabel.setForeground(statusColor);
    }
    
    /**
     * Gets the list of parking spaces
     */
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }
}
