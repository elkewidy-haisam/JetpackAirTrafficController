/**
 * CityModel3D.java
 * by Haisam Elkewidy
 *
 * This class creates and manages realistic 3D city models with buildings and terrain.
 *
 * Variables:
 *   - cityName (String)
 *   - buildings (List<Building3D>)
 *   - roads (List<Road3D>)
 *   - bridges (List<Bridge3D>)
 *   - houses (List<House3D>)
 *   - cityMap (BufferedImage)
 *   - mapWidth (int)
 *   - mapHeight (int)
 *   - random (Random)
 *
 * Methods:
 *   - CityModel3D(cityName, cityMap)
 *
 */

package com.example.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * CityModel3D - Main 3D city model class for rendering and simulation.
 * Handles feature extraction, procedural building generation, and terrain queries.
 */
public class CityModel3D {
                public String getCityName() { return cityName; }
            public BufferedImage getMapImage() { return cityMap; }
        public int getMapWidth() { return mapWidth; }
        public int getMapHeight() { return mapHeight; }
    private final String cityName;
    private final List<Building3D> buildings;
    private final List<Road3D> roads;
    private final List<Bridge3D> bridges;
    private final List<House3D> houses;
    private final BufferedImage cityMap;
    private final int mapWidth;
    private final int mapHeight;
    private final Random random;

    public CityModel3D(String cityName, BufferedImage cityMap) {
        this.cityName = cityName;
        this.cityMap = cityMap;
        this.mapWidth = cityMap.getWidth();
        this.mapHeight = cityMap.getHeight();
        this.buildings = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.bridges = new ArrayList<>();
        this.houses = new ArrayList<>();
        this.random = new Random();
        extractMapFeatures();
        generateCityBuildings();
    }

    /**
     * Extract roads, bridges, and houses from the city map image using color/style conventions
     */
    private void extractMapFeatures() {
        // Example color conventions (adjust as needed to match your map style):
        // Roads: gray (RGB close to 128,128,128)
        // Bridges: orange/yellow (RGB high red+green, low blue)
        // Houses: brown (RGB high red, medium green, low blue)
        // Water: already handled
        for (int y = 0; y < mapHeight; y += 2) {
            for (int x = 0; x < mapWidth; x += 2) {
                int rgb = cityMap.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Detect road (gray)
                if (Math.abs(r - g) < 15 && Math.abs(r - b) < 15 && r > 80 && r < 200) {
                    roads.add(new Road3D(x, y, 20, 6, 0));
                }
                // Detect bridge (yellow/orange)
                else if (r > 180 && g > 120 && b < 100) {
                    bridges.add(new Bridge3D(x, y, 30, 8, 0));
                }
                // Detect house (brown)
                else if (r > 120 && g > 60 && g < 120 && b < 80) {
                    houses.add(new House3D(x, y, 10, 10, 18));
                }
            }
        }
    }
    
    /**
     * Generate buildings based on city characteristics
     */
    private void generateCityBuildings() {
        switch (cityName) {
            case "New York":
                generateNewYorkBuildings();
                break;
            case "Boston":
                generateBostonBuildings();
                break;
            case "Houston":
                generateHoustonBuildings();
                break;
            case "Dallas":
                generateDallasBuildings();
                break;
            default:
                generateGenericCityBuildings();
        }
    }

    public List<Road3D> getRoads() { return roads; }
    public List<Bridge3D> getBridges() { return bridges; }
    public List<House3D> getHouses() { return houses; }
    
    /**
     * Generate New York City buildings - dense skyscrapers, tall buildings
     */
    private void generateNewYorkBuildings() {
        // Manhattan: dense grid of tall skyscrapers
        addBuildingCluster(mapWidth * 0.45, mapHeight * 0.3, mapWidth * 0.25, mapHeight * 0.35, 180, 420, 60, 30, 60, "skyscraper");
        // Financial District: very tall buildings
        addBuildingCluster(mapWidth * 0.35, mapHeight * 0.65, mapWidth * 0.15, mapHeight * 0.2, 220, 480, 40, 25, 40, "skyscraper");
        // Midtown: mix of office and residential
        addBuildingCluster(mapWidth * 0.5, mapHeight * 0.45, mapWidth * 0.18, mapHeight * 0.18, 120, 250, 50, 30, 50, "office");
        // Upper Manhattan: medium residential
        addBuildingCluster(mapWidth * 0.4, mapHeight * 0.1, mapWidth * 0.2, mapHeight * 0.15, 80, 200, 50, 30, 60, "residential");
        // Brooklyn/Queens: lower buildings
        addBuildingCluster(mapWidth * 0.2, mapHeight * 0.2, mapWidth * 0.15, mapHeight * 0.6, 40, 120, 60, 35, 80, "office");

        // Landmarks
        buildings.add(new Building3D(mapWidth * 0.48, mapHeight * 0.38, 18, 18, 450, "skyscraper")); // Empire State Building
        buildings.add(new Building3D(mapWidth * 0.52, mapHeight * 0.36, 20, 20, 520, "skyscraper")); // One World Trade Center
        buildings.add(new Building3D(mapWidth * 0.41, mapHeight * 0.25, 60, 60, 10, "park")); // Central Park

        // Iconic bridges
        bridges.add(new Bridge3D(mapWidth * 0.55, mapHeight * 0.32, 60, 10, 30)); // Brooklyn Bridge
        bridges.add(new Bridge3D(mapWidth * 0.38, mapHeight * 0.68, 50, 8, 20)); // Manhattan Bridge
        bridges.add(new Bridge3D(mapWidth * 0.60, mapHeight * 0.28, 40, 8, 18)); // Williamsburg Bridge

        // Rivers
        for (int x = 0; x < mapWidth; x += 8) {
            for (int y = 0; y < mapHeight; y += 8) {
                if (x < mapWidth * 0.12 || x > mapWidth * 0.88) {
                    cityMap.setRGB(x, y, 0xFF2040E0); // Hudson/East River
                }
            }
        }
        // Add more parks
        buildings.add(new Building3D(mapWidth * 0.36, mapHeight * 0.18, 30, 30, 8, "park")); // Bryant Park
        buildings.add(new Building3D(mapWidth * 0.53, mapHeight * 0.42, 40, 40, 12, "park")); // Battery Park
    }

    /**
     * Generate Boston buildings - historic low-rise with some modern towers
     */
    private void generateBostonBuildings() {
        // Downtown: mix of historic and modern
        addBuildingCluster(mapWidth * 0.45, mapHeight * 0.4, mapWidth * 0.2, mapHeight * 0.25, 80, 250, 50, 30, 50, "office");
        // Back Bay: brownstone houses
        addBuildingCluster(mapWidth * 0.3, mapHeight * 0.35, mapWidth * 0.15, mapHeight * 0.3, 40, 100, 60, 35, 70, "residential");
        // Seaport: modern commercial
        addBuildingCluster(mapWidth * 0.5, mapHeight * 0.65, mapWidth * 0.25, mapHeight * 0.2, 60, 180, 45, 30, 40, "commercial");
        // Cambridge: low/medium residential
        addBuildingCluster(mapWidth * 0.6, mapHeight * 0.2, mapWidth * 0.2, mapHeight * 0.3, 30, 80, 70, 40, 80, "residential");

        // Landmarks
        buildings.add(new Building3D(mapWidth * 0.47, mapHeight * 0.42, 16, 16, 280, "skyscraper")); // Prudential Tower
        buildings.add(new Building3D(mapWidth * 0.32, mapHeight * 0.38, 22, 22, 60, "stadium")); // Fenway Park
        buildings.add(new Building3D(mapWidth * 0.44, mapHeight * 0.36, 18, 18, 80, "historic")); // Massachusetts State House
        buildings.add(new Building3D(mapWidth * 0.41, mapHeight * 0.41, 40, 40, 10, "park")); // Boston Common

        // Charles River
        for (int x = (int)(mapWidth * 0.2); x < mapWidth * 0.8; x += 8) {
            for (int y = (int)(mapHeight * 0.48); y < mapHeight * 0.52; y += 8) {
                cityMap.setRGB(x, y, 0xFF2080E0);
            }
        }

        // Historic bridges
        bridges.add(new Bridge3D(mapWidth * 0.5, mapHeight * 0.5, 40, 8, 10)); // Longfellow Bridge
        bridges.add(new Bridge3D(mapWidth * 0.42, mapHeight * 0.49, 30, 7, 8)); // Harvard Bridge
        bridges.add(new Bridge3D(mapWidth * 0.58, mapHeight * 0.51, 28, 6, 7)); // BU Bridge
        // Add more parks
        buildings.add(new Building3D(mapWidth * 0.38, mapHeight * 0.44, 24, 24, 8, "park")); // Public Garden
    }
    
    /**
     * Generate Dallas buildings - modern downtown with sprawling suburbs
     */
    private void generateDallasBuildings() {
        // Downtown: glass towers
        addBuildingCluster(mapWidth * 0.5, mapHeight * 0.5, mapWidth * 0.18, mapHeight * 0.18, 160, 350, 50, 30, 40, "skyscraper");
        // Uptown: office/commercial
        addBuildingCluster(mapWidth * 0.35, mapHeight * 0.35, mapWidth * 0.15, mapHeight * 0.15, 80, 200, 40, 25, 35, "office");
        // Suburbs: houses
        addBuildingCluster(mapWidth * 0.2, mapHeight * 0.7, mapWidth * 0.3, mapHeight * 0.2, 20, 60, 60, 35, 100, "residential");

        // Landmarks
        buildings.add(new Building3D(mapWidth * 0.53, mapHeight * 0.52, 18, 18, 210, "landmark")); // Reunion Tower
        buildings.add(new Building3D(mapWidth * 0.51, mapHeight * 0.48, 20, 20, 280, "skyscraper")); // Bank of America Plaza
        buildings.add(new Building3D(mapWidth * 0.49, mapHeight * 0.54, 22, 22, 40, "historic")); // Dealey Plaza
        buildings.add(new Building3D(mapWidth * 0.47, mapHeight * 0.50, 36, 36, 10, "park")); // Klyde Warren Park

        // Trinity River
        for (int x = (int)(mapWidth * 0.1); x < mapWidth * 0.9; x += 8) {
            for (int y = (int)(mapHeight * 0.6); y < mapHeight * 0.65; y += 8) {
                cityMap.setRGB(x, y, 0xFF2090E0);
            }
        }

        // Bridges
        bridges.add(new Bridge3D(mapWidth * 0.55, mapHeight * 0.62, 40, 8, 12)); // Margaret Hunt Hill Bridge
        bridges.add(new Bridge3D(mapWidth * 0.45, mapHeight * 0.63, 32, 7, 10)); // Continental Avenue Bridge

        // Wide roads
        for (int i = 0; i < 8; i++) {
            roads.add(new Road3D(mapWidth * (0.1 + i * 0.1), mapHeight * 0.5, 80, 10, 0));
        }
        // Add more parks
        buildings.add(new Building3D(mapWidth * 0.41, mapHeight * 0.56, 28, 28, 8, "park")); // Trinity River Park
    }
    
    /**
     * Generate Houston buildings - sprawling city with mixed heights
     */
    private void generateHoustonBuildings() {
        // Downtown: tall cluster
        addBuildingCluster(mapWidth * 0.5, mapHeight * 0.5, mapWidth * 0.15, mapHeight * 0.15, 150, 380, 40, 25, 35, "skyscraper");
        // Uptown: office/commercial
        addBuildingCluster(mapWidth * 0.35, mapHeight * 0.35, mapWidth * 0.15, mapHeight * 0.15, 80, 200, 40, 25, 35, "office");
        // Suburbs: houses
        addBuildingCluster(mapWidth * 0.2, mapHeight * 0.7, mapWidth * 0.3, mapHeight * 0.2, 20, 60, 60, 35, 100, "residential");

        // Landmarks
        buildings.add(new Building3D(mapWidth * 0.52, mapHeight * 0.52, 20, 20, 420, "skyscraper")); // JPMorgan Chase Tower
        buildings.add(new Building3D(mapWidth * 0.48, mapHeight * 0.54, 22, 22, 60, "stadium")); // Minute Maid Park
        buildings.add(new Building3D(mapWidth * 0.62, mapHeight * 0.38, 24, 24, 180, "office")); // Energy Corridor
        buildings.add(new Building3D(mapWidth * 0.44, mapHeight * 0.58, 36, 36, 12, "park")); // Buffalo Bayou Park

        // Bayous/water
        for (int x = (int)(mapWidth * 0.3); x < mapWidth * 0.7; x += 8) {
            for (int y = (int)(mapHeight * 0.55); y < mapHeight * 0.6; y += 8) {
                cityMap.setRGB(x, y, 0xFF2070E0);
            }
        }

        // Bridges
        bridges.add(new Bridge3D(mapWidth * 0.46, mapHeight * 0.57, 32, 7, 10)); // Main Street Bridge
        bridges.add(new Bridge3D(mapWidth * 0.54, mapHeight * 0.59, 28, 6, 8)); // Montrose Bridge

        // Sprawling roads
        for (int i = 0; i < 6; i++) {
            roads.add(new Road3D(mapWidth * (0.15 + i * 0.13), mapHeight * 0.5, 60, 8, 0));
        }
        // Add more parks
        buildings.add(new Building3D(mapWidth * 0.38, mapHeight * 0.62, 28, 28, 8, "park")); // Discovery Green
    }
    
    /**
     * Add a cluster of buildings in a specific area
     */
    private void addBuildingCluster(double centerX, double centerY, 
                                   double areaWidth, double areaHeight,
                                   double minHeight, double maxHeight,
                                   double buildingWidth, double buildingDepth,
                                   int count, String type) {
        for (int i = 0; i < count; i++) {
            // Random position within the cluster area
            double x = centerX + (random.nextDouble() - 0.5) * areaWidth;
            double y = centerY + (random.nextDouble() - 0.5) * areaHeight;
            
            // Check if on land (not water)
            if (!isWater(x, y)) {
                double height = minHeight + random.nextDouble() * (maxHeight - minHeight);
                double width = buildingWidth * (0.8 + random.nextDouble() * 0.4);
                double depth = buildingDepth * (0.8 + random.nextDouble() * 0.4);
                
                buildings.add(new Building3D(x, y, width, depth, height, type));
            }
        }
    }
    
    /**
     * Check if a position is over water based on map pixel color
     */
    public boolean isWater(double x, double y) {
        if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
            return false;
        }
        
        int px = (int)x;
        int py = (int)y;
        
        if (px < 0) px = 0;
        if (px >= mapWidth) px = mapWidth - 1;
        if (py < 0) py = 0;
        if (py >= mapHeight) py = mapHeight - 1;
        
        int rgb = cityMap.getRGB(px, py);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        
        // Water detection: blue dominant color
        // Water typically has: blue > red and blue > green
        return (blue > red + 15 && blue > green + 10) || 
               (blue > 120 && red < 100 && green < 130);
    }
    
    /**
     * Get terrain type at position (for rendering)
     */
    public String getTerrainType(double x, double y) {
        if (isWater(x, y)) {
            return "water";
        }
        
        // Check if in a building
        for (Building3D building : buildings) {
            if (building.containsPoint(x, y)) {
                return "building";
            }
        }
        
        return "land";
    }
    
    /**
     * Get buildings near a position (for rendering)
     */
    public List<Building3D> getBuildingsNear(double x, double y, double radius) {
        List<Building3D> nearBuildings = new ArrayList<>();
        for (Building3D building : buildings) {
            if (building.distanceTo(x, y) <= radius) {
                nearBuildings.add(building);
            }
        }
        return nearBuildings;
    }
    
    /**
     * Generate generic city buildings for unknown cities
     */
    private void generateGenericCityBuildings() {
        // Central business district
        addBuildingCluster(mapWidth * 0.5, mapHeight * 0.5, 
                          mapWidth * 0.2, mapHeight * 0.2, 
                          100, 300, 50, 30, 50, "office");
        
        // Surrounding areas
        addBuildingCluster(mapWidth * 0.3, mapHeight * 0.3, 
                          mapWidth * 0.4, mapHeight * 0.4, 
                          30, 100, 60, 35, 100, "residential");
    }
    
    // Getters
    public List<Building3D> getBuildings() {
        return buildings;
    }
    
}
