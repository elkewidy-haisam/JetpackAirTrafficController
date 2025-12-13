/**
 * Detects and prevents collisions between jetpacks and buildings/houses.
 * 
 * Purpose:
 * Analyzes jetpack positions and trajectories to identify collisions with city buildings and houses.
 * Prevents jetpacks from flying through buildings by checking their 3D hitboxes against building
 * geometry. Provides collision checking for both current positions and planned movement paths.
 * 
 * Key Responsibilities:
 * - Check if jetpack position collides with any building or house
 * - Validate proposed movement paths for building collisions
 * - Provide safe altitude recommendations to avoid buildings
 * - Calculate minimum safe distance from buildings
 * - Support pathfinding by identifying building-free routes
 * 
 * Interactions:
 * - Consumes Building3D and House3D data from CityModel3D
 * - Used by FlightMovementController to validate movements
 * - Integrates with pathfinding to create building-aware routes
 * - Coordinates with CollisionDetector for comprehensive safety
 * 
 * Patterns & Constraints:
 * - Stateless collision detection; operates on provided building collections
 * - Configurable jetpack collision radius (default 5 units)
 * - Efficient spatial queries using proximity filtering
 * - Thread-safe for concurrent collision checks
 * 
 * @author Haisam Elkewidy
 */

package com.example.detection;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Building3D;
import com.example.model.CityModel3D;
import com.example.model.House3D;

public class BuildingCollisionDetector {
    
    // Default jetpack collision radius (units)
    private static final double DEFAULT_JETPACK_RADIUS = 5.0;
    
    // Safety margin for collision avoidance (units)
    private static final double SAFETY_MARGIN = 2.0;
    
    private final CityModel3D cityModel;
    private final double jetpackRadius;
    
    /**
     * Constructor with city model
     * @param cityModel The 3D city model containing buildings and houses
     */
    public BuildingCollisionDetector(CityModel3D cityModel) {
        this(cityModel, DEFAULT_JETPACK_RADIUS);
    }
    
    /**
     * Constructor with custom jetpack radius
     * @param cityModel The 3D city model containing buildings and houses
     * @param jetpackRadius The collision radius for jetpacks
     */
    public BuildingCollisionDetector(CityModel3D cityModel, double jetpackRadius) {
        this.cityModel = cityModel;
        this.jetpackRadius = jetpackRadius;
    }
    
    /**
     * Check if a jetpack position collides with any building
     * @param x X coordinate
     * @param y Y coordinate (map coordinate)
     * @param altitude Altitude/height
     * @return true if collision detected, false otherwise
     */
    public boolean checkCollision(double x, double y, double altitude) {
        // Check buildings
        for (Building3D building : cityModel.getBuildings()) {
            if (building.collidesWithSphere(x, y, altitude, jetpackRadius)) {
                return true;
            }
        }
        
        // Check houses
        for (House3D house : cityModel.getHouses()) {
            if (house.collidesWithSphere(x, y, altitude, jetpackRadius)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if a movement from current position to target position would collide with buildings
     * @param x1 Starting X coordinate
     * @param y1 Starting Y coordinate
     * @param altitude1 Starting altitude
     * @param x2 Target X coordinate
     * @param y2 Target Y coordinate
     * @param altitude2 Target altitude
     * @return true if path is clear, false if collision detected
     */
    public boolean isPathClear(double x1, double y1, double altitude1, 
                                double x2, double y2, double altitude2) {
        // Calculate path distance
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = altitude2 - altitude1;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        
        // Adaptive sampling: one sample per jetpack radius distance, minimum 3, maximum 20
        int samples = Math.max(3, Math.min(20, (int)(distance / jetpackRadius)));
        
        for (int i = 0; i <= samples; i++) {
            double t = i / (double) samples;
            double x = x1 + t * dx;
            double y = y1 + t * dy;
            double altitude = altitude1 + t * dz;
            
            if (checkCollision(x, y, altitude)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get the minimum safe altitude to fly over buildings at a given position
     * @param x X coordinate
     * @param y Y coordinate
     * @return Minimum safe altitude, or 0 if no buildings nearby
     */
    public double getMinimumSafeAltitude(double x, double y) {
        double maxHeight = 0;
        
        // Check buildings within radius
        double searchRadius = 50.0; // Search within this radius
        for (Building3D building : cityModel.getBuildings()) {
            double distance = building.distanceTo(x, y);
            if (distance < searchRadius) {
                // If within footprint or very close, need to be above building
                if (building.containsPoint(x, y) || distance < jetpackRadius) {
                    maxHeight = Math.max(maxHeight, building.getHeight());
                }
            }
        }
        
        // Check houses within radius
        for (House3D house : cityModel.getHouses()) {
            double dx = Math.max(house.getX() - x, Math.max(0, x - (house.getX() + house.getWidth())));
            double dy = Math.max(house.getY() - y, Math.max(0, y - (house.getY() + house.getLength())));
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance < searchRadius) {
                boolean inFootprint = x >= house.getX() && x <= house.getX() + house.getWidth() &&
                                      y >= house.getY() && y <= house.getY() + house.getLength();
                if (inFootprint || distance < jetpackRadius) {
                    maxHeight = Math.max(maxHeight, house.getHeight());
                }
            }
        }
        
        // Add safety margin
        return maxHeight > 0 ? maxHeight + SAFETY_MARGIN : 0;
    }
    
    /**
     * Get all buildings within a certain radius of a position
     * @param x X coordinate
     * @param y Y coordinate
     * @param radius Search radius
     * @return List of nearby buildings
     */
    public List<Building3D> getNearbyBuildings(double x, double y, double radius) {
        List<Building3D> nearby = new ArrayList<>();
        for (Building3D building : cityModel.getBuildings()) {
            if (building.distanceTo(x, y) <= radius) {
                nearby.add(building);
            }
        }
        return nearby;
    }
    
    /**
     * Check if a position is safe (no collisions and not too close to buildings)
     * @param x X coordinate
     * @param y Y coordinate
     * @param altitude Altitude
     * @return true if position is safe, false otherwise
     */
    public boolean isSafePosition(double x, double y, double altitude) {
        // Check for direct collision
        if (checkCollision(x, y, altitude)) {
            return false;
        }
        
        // Check if altitude is sufficient for nearby buildings
        double minSafeAltitude = getMinimumSafeAltitude(x, y);
        if (altitude < minSafeAltitude) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Get the jetpack collision radius
     * @return Collision radius
     */
    public double getJetpackRadius() {
        return jetpackRadius;
    }
}
