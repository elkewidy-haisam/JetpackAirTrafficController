/**
 * Calculates and updates jetpack position incrementally toward waypoints and destinations.
 * 
 * Purpose:
 * Implements the core movement logic for jetpack flights, computing incremental position updates
 * based on speed, direction, and waypoint sequences. Supports normal navigation, detour routing,
 * altitude adjustments, and halt states. Maintains a position trail for visualization and records
 * waypoint progression for navigation tracking.
 * 
 * Key Responsibilities:
 * - Calculate incremental position changes toward current waypoint or destination
 * - Manage ordered waypoint list for sequential navigation
 * - Support detour activation with temporary waypoint overrides
 * - Track altitude changes with smooth transitions
 * - Maintain position trail (breadcrumb history) for rendering
 * - Detect waypoint and destination arrival based on proximity thresholds
 * - Handle halt state by freezing position updates
 * 
 * Interactions:
 * - Integrated into JetPackFlight for position update loops
 * - Consumes waypoint data from FlightPath for route guidance
 * - Provides current position to FlightStateProvider for external queries
 * - Supports detour instructions from FlightEmergencyHandler
 * - Referenced by JetPackFlightRenderer for trail visualization
 * - Used in collision detection for real-time position comparisons
 * 
 * Patterns & Constraints:
 * - Stateful: maintains current position, waypoint index, and trail history
 * - Uses vector mathematics for direction and distance calculations
 * - Waypoint arrival detected via Euclidean distance threshold
 * - Detour mode replaces normal waypoints; original route restored on resumption
 * - Thread-safe for position reads; synchronized writes required for updates
 * - Altitude transitions smoothed over multiple update cycles
 * 
 * @author Haisam Elkewidy
 */

package com.example.flight;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.example.detection.BuildingCollisionDetector;
import com.example.model.Building3D;
import com.example.model.CityModel3D;
import com.example.utility.geometry.GeometryUtils;

/**
 * FlightMovementController handles position updates, waypoint navigation, detours, and movement logic for flights/jetpacks.
 * It integrates collision avoidance, altitude management, and pathfinding with city/building models.
 */
public class FlightMovementController {
    // Current position
    private double x;
    private double y;
    // Navigation
    private Point destination;
    private double speed;
    private double altitude;
    // Trail of previous positions (for rendering path)
    private final List<Point> trail;
    private static final int TRAIL_LENGTH = 15;

    // Waypoint and detour management
    private List<Point> waypoints;
    private List<Point> detourWaypoints;
    private boolean isDetourActive;
    private int currentWaypointIndex;

    // Reference to city/building model for collision avoidance
    private CityModel3D cityModel;
    private BuildingCollisionDetector buildingCollisionDetector;

    /**
     * Constructs a FlightMovementController with initial position, destination, speed, and altitude.
     */
    public FlightMovementController(Point start, Point destination, double speed, double altitude) {
        this.x = start.x;
        this.y = start.y;
        this.destination = destination;
        this.speed = speed;
        this.altitude = altitude;
        this.trail = new ArrayList<>();
        this.waypoints = new ArrayList<>();
        this.detourWaypoints = new ArrayList<>();
        this.isDetourActive = false;
        this.currentWaypointIndex = 0;
    }

    /**
     * Updates position based on current target and speed.
     * Handles collision avoidance, detours, and trail updates.
     * @param effectiveSpeed the speed adjusted for hazards
     * @param isHalted whether emergency halt is active
     * @return true if position was updated, false if halted
     */
    public boolean updatePosition(double effectiveSpeed, boolean isHalted) {
        if (isHalted) {
            return false;
        }

        // Add current position to trail
        trail.add(0, new Point((int)x, (int)y));
        if (trail.size() > TRAIL_LENGTH) {
            trail.remove(trail.size() - 1);
        }

        // Determine target
        Point target = getActiveTarget();

        // Calculate direction to target
        double dx = target.x - x;
        double dy = target.y - y;
        double distance = GeometryUtils.calculateDistance(x, y, target.x, target.y);

        // Predict next position
        double nextX, nextY;
        if (distance > effectiveSpeed) {
            nextX = x + (dx / distance) * effectiveSpeed;
            nextY = y + (dy / distance) * effectiveSpeed;
        } else {
            nextX = target.x;
            nextY = target.y;
        }

        // Collision detection with buildings using BuildingCollisionDetector
        if (buildingCollisionDetector != null) {
            // Check if the path is clear
            if (!buildingCollisionDetector.isPathClear(x, y, altitude, nextX, nextY, altitude)) {
                // Collision detected! Try to navigate around the building
                
                // Get minimum safe altitude at next position
                double minSafeAlt = buildingCollisionDetector.getMinimumSafeAltitude(nextX, nextY);
                
                if (minSafeAlt > 0 && altitude < minSafeAlt) {
                    // Climb to safe altitude
                    altitude = Math.min(altitude + 5.0, minSafeAlt + 5.0);
                    return true; // Continue at current position while climbing
                }
                
                // Try to find a detour around the building
                Building3D nearestBuilding = findNearestBuilding(nextX, nextY);
                if (nearestBuilding != null) {
                    // Generate detour waypoints to go around the building
                    List<Point> detourPoints = generateBuildingDetour(nearestBuilding, target);
                    if (!detourPoints.isEmpty()) {
                        detour(detourPoints);
                        return true; // Skip movement this tick, will move to detour next tick
                    }
                }
                
                // If no detour found, try moving perpendicular to current direction
                double perpX = nextX + (dy / distance) * 10;
                double perpY = nextY - (dx / distance) * 10;
                if (!buildingCollisionDetector.checkCollision(perpX, perpY, altitude)) {
                    nextX = perpX;
                    nextY = perpY;
                } else {
                    // Can't move, stay in place
                    return false;
                }
            }
        }

        // Move if no collision
        if (distance > effectiveSpeed) {
            x = nextX;
            y = nextY;
        } else {
            // Reached current target
            if (isDetourActive && !detourWaypoints.isEmpty()) {
                detourWaypoints.remove(0);
                if (detourWaypoints.isEmpty()) {
                    resumeNormalPath();
                }
            } else if (!waypoints.isEmpty() && currentWaypointIndex < waypoints.size()) {
                currentWaypointIndex++;
            }
        }

        return true;
    }
    
    /**
     * Gets the active navigation target based on priority:
     * detour waypoints > regular waypoints > destination.
     */
    public Point getActiveTarget() {
        // Priority: detour waypoints > regular waypoints > destination
        if (isDetourActive && !detourWaypoints.isEmpty()) {
            return detourWaypoints.get(0);
        } else if (currentWaypointIndex < waypoints.size()) {
            return waypoints.get(currentWaypointIndex);
        } else {
            return destination;
        }
    }
    
    /**
     * Checks if flight has reached final destination (within one speed unit).
     */
    public boolean hasReachedDestination(boolean isHalted) {
        if (isHalted) {
            return false;
        }
        
        Point target = getActiveTarget();
        double dx = target.x - x;
        double dy = target.y - y;
        return Math.sqrt(dx * dx + dy * dy) < speed;
    }
    
    /**
     * Sets a new destination and resets waypoint navigation.
     */
    public void setNewDestination(Point newDest) {
        this.destination = newDest;
        this.currentWaypointIndex = 0;
    }
    
    /**
     * Adds a waypoint to the flight path.
     */
    public void addWaypoint(Point waypoint) {
        waypoints.add(waypoint);
    }
    
    /**
     * Sets the complete list of waypoints.
     */
    public void setWaypoints(List<Point> waypoints) {
        this.waypoints = new ArrayList<>(waypoints);
        this.currentWaypointIndex = 0;
    }
    
    /**
     * Initiates a detour with specified waypoints.
     */
    public void detour(List<Point> detourPoints) {
        if (detourPoints == null || detourPoints.isEmpty()) {
            return;
        }
        
        this.detourWaypoints = new ArrayList<>(detourPoints);
        this.isDetourActive = true;
    }
    
    /**
     * Resumes normal flight path after detour.
     */
    public void resumeNormalPath() {
        if (isDetourActive) {
            isDetourActive = false;
            detourWaypoints.clear();
        }
    }
    
    /**
     * Clears all waypoints and detours, heads directly to emergency/parking destination.
     */
    public void setEmergencyDestination(Point emergencyDest, double newSpeed) {
        waypoints.clear();
        detourWaypoints.clear();
        isDetourActive = false;
        this.destination = emergencyDest;
        this.speed = Math.min(newSpeed, 5.0);
    }
    
    /**
     * Updates altitude gradually toward target, or varies slightly if no target.
     */
    public void updateAltitude(Double targetAltitude) {
        if (targetAltitude != null) {
            double altDiff = targetAltitude - altitude;
            if (Math.abs(altDiff) > 1.0) {
                altitude += Math.signum(altDiff) * Math.min(Math.abs(altDiff), 3.0);
            }
        } else {
            // Vary altitude slightly (realistic flying)
            altitude += (Math.random() - 0.5) * 2;
        }
        altitude = Math.max(50, Math.min(200, altitude));
    }
    
    /**
     * Calculates direction angle (radians) from current position to destination.
     */
    public double getDirectionAngle() {
        double dx = destination.x - x;
        double dy = destination.y - y;
        return Math.atan2(dy, dx);
    }
    
    /**
     * Gets distance to destination (current position to destination).
     */
    public double getDistanceToDestination() {
        return GeometryUtils.calculateDistance(x, y, destination.x, destination.y);
    }
    
    /**
     * Gets compass direction string (e.g., "East", "Northwest").
     */
    public String getDirectionString() {
        double angle = getDirectionAngle();
        double degrees = Math.toDegrees(angle);
        
        if (degrees > -22.5 && degrees <= 22.5) {
            return "🚀 East";
        } else if (degrees > 22.5 && degrees <= 67.5) {
            return "🚀 Northeast";
        } else if (degrees > 67.5 && degrees <= 112.5) {
            return "🚀 North";
        } else if (degrees > 112.5 && degrees <= 157.5) {
            return "🚀 Northwest";
        } else if (degrees > 157.5 || degrees <= -157.5) {
            return "🚀 West";
        } else if (degrees > -157.5 && degrees <= -112.5) {
            return "🚀 Southwest";
        } else if (degrees > -112.5 && degrees <= -67.5) {
            return "🚀 South";
        } else {
            return "🚀 Southeast";
        }
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public Point getDestination() { return destination; }
    public double getSpeed() { return speed; }
    public double getAltitude() { return altitude; }
    public List<Point> getTrail() { return trail; }
    public List<Point> getWaypoints() { return waypoints; }
    public int getCurrentWaypointIndex() { return currentWaypointIndex; }
    public boolean isDetourActive() { return isDetourActive; }
    
    // Setters
    public void setSpeed(double speed) { this.speed = speed; }
    public void setAltitude(double altitude) { this.altitude = altitude; }
    
    /**
     * Sets the city model for building collision detection
     */
    public void setCityModel(CityModel3D cityModel) {
        this.cityModel = cityModel;
        if (cityModel != null) {
            this.buildingCollisionDetector = new BuildingCollisionDetector(cityModel);
        }
    }
    
    /**
     * Find the nearest building to a given position
     */
    private Building3D findNearestBuilding(double x, double y) {
        Building3D nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        if (cityModel != null) {
            for (Building3D building : cityModel.getBuildings()) {
                double distance = building.distanceTo(x, y);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = building;
                }
            }
        }
        
        return nearest;
    }
    
    /**
     * Generate detour waypoints to navigate around a building
     */
    private List<Point> generateBuildingDetour(Building3D building, Point finalTarget) {
        List<Point> detourPoints = new ArrayList<>();
        
        // Calculate building center and corners
        double bx = building.getX();
        double by = building.getY();
        double bw = building.getWidth();
        double bl = building.getLength();
        
        // Try going around each corner of the building
        Point[] corners = {
            new Point((int)(bx - 15), (int)(by - 15)),           // Bottom-left
            new Point((int)(bx + bw + 15), (int)(by - 15)),      // Bottom-right
            new Point((int)(bx + bw + 15), (int)(by + bl + 15)), // Top-right
            new Point((int)(bx - 15), (int)(by + bl + 15))       // Top-left
        };
        
        // Find the corner closest to the final target
        Point bestCorner = null;
        double minDistToTarget = Double.MAX_VALUE;
        
        for (Point corner : corners) {
            double distToTarget = GeometryUtils.calculateDistance(
                corner.x, corner.y, finalTarget.x, finalTarget.y);
            
            // Check if this corner is reachable (not blocked)
            if (buildingCollisionDetector != null) {
                double safeAlt = buildingCollisionDetector.getMinimumSafeAltitude(corner.x, corner.y);
                if (safeAlt > 0 && altitude < safeAlt) {
                    continue; // Skip this corner if we can't reach it safely
                }
            }
            
            if (distToTarget < minDistToTarget) {
                minDistToTarget = distToTarget;
                bestCorner = corner;
            }
        }
        
        if (bestCorner != null) {
            detourPoints.add(bestCorner);
        }
        
        return detourPoints;
    }
}
