package com.example.utility.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;

/**
 * MapLoader.java
 * 
 * Handles loading of city map images from classpath or file system.
 * Provides fallback mechanisms for locating map resources.
 */
public class MapLoader {
    
    /**
     * Result class containing loaded map information
     */
    public static class MapLoadResult {
        public final ImageIcon mapIcon;
        public final BufferedImage mapImage;
        public final int width;
        public final int height;
        
        public MapLoadResult(ImageIcon icon, BufferedImage image, int width, int height) {
            this.mapIcon = icon;
            this.mapImage = image;
            this.width = width;
            this.height = height;
        }
    }
    
    /**
     * Loads a map for the specified city
     * 
     * @param city The city name
     * @return MapLoadResult containing the loaded map data
     * @throws RuntimeException if map cannot be loaded
     */
    public MapLoadResult loadMap(String city) {
        String mapFileName = getMapFileName(city);
        ImageIcon mapIcon = loadMapIcon(mapFileName);
        
        if (mapIcon == null || mapIcon.getIconWidth() <= 0) {
            System.err.println("Could not find or load map image: " + mapFileName);
            System.err.println("Current working directory: " + System.getProperty("user.dir"));
            throw new RuntimeException("Could not find map image: " + mapFileName);
        }
        
        int mapWidth = mapIcon.getIconWidth();
        int mapHeight = mapIcon.getIconHeight();
        
        BufferedImage mapImage = createBufferedImage(mapIcon, mapWidth, mapHeight);
        
        // Map loaded successfully
        
        return new MapLoadResult(mapIcon, mapImage, mapWidth, mapHeight);
    }
    
    /**
     * Gets the map file name for a city
     * 
     * @param city The city name
     * @return The map file name
     */
    private String getMapFileName(String city) {
        switch (city) {
            case "Boston":
                return "Boston-Road-Map-1265x977.jpg";
            case "Dallas":
                return "Dallas-Road-Map.jpg";
            case "Houston":
                return "Houston-Road-Map.jpg";
            case "New York":
            default:
                return "New-York-City-Road-Map.jpg";
        }
    }
    
    /**
     * Loads a map icon from classpath or file system
     * 
     * @param mapFileName The map file name
     * @return The loaded ImageIcon or null if not found
     */
    private ImageIcon loadMapIcon(String mapFileName) {
        // Try loading from classpath first
        java.net.URL imageUrl = getClass().getClassLoader().getResource(mapFileName);
        if (imageUrl != null) {
            // Loading map from classpath
            return new ImageIcon(imageUrl);
        }
        
        // Try various file paths
        String[] possiblePaths = {
            "src/resources/" + mapFileName,
            "src\\resources\\" + mapFileName,
            "../resources/" + mapFileName,
            "..\\resources\\" + mapFileName,
            "resources/" + mapFileName,
            "resources\\" + mapFileName
        };
        
        for (String path : possiblePaths) {
            File imageFile = new File(path);
            if (imageFile.exists()) {
                // Loading map from file
                return new ImageIcon(imageFile.getAbsolutePath());
            }
        }
        
        return null;
    }
    
    /**
     * Creates a BufferedImage from an ImageIcon
     * 
     * @param mapIcon The ImageIcon
     * @param width The image width
     * @param height The image height
     * @return The BufferedImage
     */
    private BufferedImage createBufferedImage(ImageIcon mapIcon, int width, int height) {
        BufferedImage mapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mapImage.createGraphics();
        mapIcon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        return mapImage;
    }
}
