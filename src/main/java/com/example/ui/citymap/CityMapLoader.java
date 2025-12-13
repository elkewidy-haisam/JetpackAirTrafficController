/**
 * Loads city map images from resources for display and analysis.
 * 
 * Purpose:
 * Handles loading of city map image files (PNG/JPG) from classpath resources. Provides loaded BufferedImage
 * objects to rendering and analysis components including CityMapPanel, WaterDetector, and CityModel3D.
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.citymap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

/**
 * CityMapLoader - Handles loading of city map images from various sources
 */
public class CityMapLoader {
    
    /**
     * Result class containing loaded map data
     */
    public static class MapLoadResult {
        private final ImageIcon mapIcon;
        private final BufferedImage mapImage;
        private final int mapWidth;
        private final int mapHeight;
        
        public MapLoadResult(ImageIcon mapIcon, BufferedImage mapImage, int mapWidth, int mapHeight) {
            this.mapIcon = mapIcon;
            this.mapImage = mapImage;
            this.mapWidth = mapWidth;
            this.mapHeight = mapHeight;
        }
        
        public ImageIcon getMapIcon() {
            return mapIcon;
        }
        
        public BufferedImage getMapImage() {
            return mapImage;
        }
        
        public int getMapWidth() {
            return mapWidth;
        }
        
        public int getMapHeight() {
            return mapHeight;
        }
    }
    
    /**
     * Loads the map image for the specified city
     * 
     * @param city The city name
     * @param panel The panel requesting the map (for context in error messages)
     * @return MapLoadResult containing the loaded map data
     * @throws RuntimeException if map cannot be loaded
     */
    public static MapLoadResult loadCityMap(String city, Object panel) {
        String mapFileName;
        switch (city) {
            case "Boston":
                mapFileName = "Boston-Road-Map-1265x977.jpg";
                break;
            case "Dallas":
                mapFileName = "Dallas-Road-Map.jpg";
                break;
            case "Houston":
                mapFileName = "Houston-Road-Map.jpg";
                break;
            case "New York":
            default:
                mapFileName = "New-York-City-Road-Map.jpg";
                break;
        }
        
        final ImageIcon mapIcon;
        
        // Try to load from classpath first
        java.net.URL imageUrl = panel.getClass().getClassLoader().getResource(mapFileName);
        if (imageUrl != null) {
            // Loading from classpath
            mapIcon = new ImageIcon(imageUrl);
        } else {
            // Try various file system paths
            String[] possiblePaths = {
                "src/resources/" + mapFileName,
                "src\\resources\\" + mapFileName,
                "../resources/" + mapFileName,
                "..\\resources\\" + mapFileName,
                "resources/" + mapFileName,
                "resources\\" + mapFileName
            };
            
            ImageIcon tempIcon = null;
            for (String path : possiblePaths) {
                java.io.File imageFile = new java.io.File(path);
                if (imageFile.exists()) {
                    // Loading from file
                    tempIcon = new ImageIcon(imageFile.getAbsolutePath());
                    break;
                }
            }
            mapIcon = tempIcon;
        }
        
        // Validate that map was loaded successfully
        if (mapIcon == null || mapIcon.getIconWidth() <= 0) {
            System.err.println("Could not find or load map image: " + mapFileName);
            System.err.println("Current working directory: " + System.getProperty("user.dir"));
            throw new RuntimeException("Could not find map image: " + mapFileName);
        }
        
        int mapWidth = mapIcon.getIconWidth();
        int mapHeight = mapIcon.getIconHeight();
        
        // Create BufferedImage from ImageIcon
        BufferedImage mapImage = new BufferedImage(
            mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mapImage.createGraphics();
        mapIcon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        
        // Map loaded
        
        return new MapLoadResult(mapIcon, mapImage, mapWidth, mapHeight);
    }
    
    /**
     * Loads the map image for the specified city (simplified version)
     * 
     * @param cityName The city name
     * @return BufferedImage of the city map
     * @throws RuntimeException if map cannot be loaded
     */
    public static BufferedImage loadCityMap(String cityName) {
        String mapFileName;
        switch (cityName) {
            case "Boston":
                mapFileName = "Boston-Road-Map-1265x977.jpg";
                break;
            case "Dallas":
                mapFileName = "Dallas-Road-Map.jpg";
                break;
            case "Houston":
                mapFileName = "Houston-Road-Map.jpg";
                break;
            case "New York":
            default:
                mapFileName = "New-York-City-Road-Map.jpg";
                break;
        }
        
        try {
            // Try to load from classpath first
            java.net.URL imageUrl = CityMapLoader.class.getClassLoader().getResource(mapFileName);
            if (imageUrl != null) {
                return javax.imageio.ImageIO.read(imageUrl);
            }
            
            // Try various file system paths
            String[] possiblePaths = {
                "src/resources/" + mapFileName,
                "src\\resources\\" + mapFileName,
                "../resources/" + mapFileName,
                "..\\resources\\" + mapFileName,
                "resources/" + mapFileName,
                "resources\\" + mapFileName,
                "src/main/resources/" + mapFileName,
                "src\\main\\resources\\" + mapFileName
            };
            
            for (String path : possiblePaths) {
                java.io.File imageFile = new java.io.File(path);
                if (imageFile.exists()) {
                    return javax.imageio.ImageIO.read(imageFile);
                }
            }
            
            throw new RuntimeException("Could not find map image: " + mapFileName);
        } catch (Exception e) {
            System.err.println("Error loading city map: " + e.getMessage());
            throw new RuntimeException("Failed to load city map for " + cityName, e);
        }
    }
}
