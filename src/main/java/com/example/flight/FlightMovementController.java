package com.example.flight;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.example.utility.geometry.GeometryUtils;

/**
 * FlightMovementController - handles position updates, waypoint navigation, and movement logic
 */
public class FlightMovementController {
    private double x;
    private double y;
    private Point destination;
    private Point origin;
    private double speed;
    private double altitude;
    private List<Point> trail;
    private static final int TRAIL_LENGTH = 15;
    
    // Waypoint management
    private List<Point> waypoints;
    private List<Point> detourWaypoints;
    private boolean isDetourActive;
    private int currentWaypointIndex;
    
    public FlightMovementController(Point start, Point destination, double speed, double altitude) {
        this.x = start.x;
        this.y = start.y;
        this.origin = new Point(start);
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
     * Updates position based on current target and speed
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
        
        if (distance > effectiveSpeed) {
            x += (dx / distance) * effectiveSpeed;
            y += (dy / distance) * effectiveSpeed;
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
     * Gets the active navigation target based on priority
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
     * Checks if flight has reached final destination
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
     * Sets a new destination and resets waypoint navigation
     */
    public void setNewDestination(Point newDest) {
        this.destination = newDest;
        this.currentWaypointIndex = 0;
    }
    
    /**
     * Adds a waypoint to the flight path
     */
    public void addWaypoint(Point waypoint) {
        waypoints.add(waypoint);
    }
    
    /**
     * Sets the complete list of waypoints
     */
    public void setWaypoints(List<Point> waypoints) {
        this.waypoints = new ArrayList<>(waypoints);
        this.currentWaypointIndex = 0;
    }
    
    /**
     * Initiates a detour with specified waypoints
     */
    public void detour(List<Point> detourPoints) {
        if (detourPoints == null || detourPoints.isEmpty()) {
            return;
        }
        
        this.detourWaypoints = new ArrayList<>(detourPoints);
        this.isDetourActive = true;
    }
    
    /**
     * Resumes normal flight path after detour
     */
    public void resumeNormalPath() {
        if (isDetourActive) {
            isDetourActive = false;
            detourWaypoints.clear();
        }
    }
    
    /**
     * Clears all waypoints and detours, heads directly to parking
     */
    public void setEmergencyDestination(Point emergencyDest, double newSpeed) {
        waypoints.clear();
        detourWaypoints.clear();
        isDetourActive = false;
        this.destination = emergencyDest;
        this.speed = Math.min(newSpeed, 5.0);
    }
    
    /**
     * Updates altitude gradually toward target
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
     * Calculates direction angle from current position to destination
     */
    public double getDirectionAngle() {
        double dx = destination.x - x;
        double dy = destination.y - y;
        return Math.atan2(dy, dx);
    }
    
    /**
     * Gets distance to destination
     */
    public double getDistanceToDestination() {
        return GeometryUtils.calculateDistance(x, y, destination.x, destination.y);
    }
    
    /**
     * Gets compass direction string
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
}
