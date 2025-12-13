/**
 * Node representation for A* pathfinding algorithm.
 * 
 * Purpose:
 * Represents a single node in the pathfinding grid, containing position,
 * cost information, and parent reference for path reconstruction.
 * 
 * Key Responsibilities:
 * - Store node position (x, y, altitude)
 * - Track g-cost (distance from start) and h-cost (heuristic to goal)
 * - Maintain parent reference for path backtracking
 * - Provide comparable interface for priority queue
 * 
 * @author Haisam Elkewidy
 */

package com.example.navigation;

public class PathfindingNode implements Comparable<PathfindingNode> {
    public final double x;
    public final double y;
    public final double altitude;
    
    public double gCost; // Distance from start
    public double hCost; // Heuristic distance to goal
    public PathfindingNode parent;
    
    public PathfindingNode(double x, double y, double altitude) {
        this.x = x;
        this.y = y;
        this.altitude = altitude;
        this.gCost = 0;
        this.hCost = 0;
        this.parent = null;
    }
    
    /**
     * Get total f-cost (g + h)
     */
    public double getFCost() {
        return gCost + hCost;
    }
    
    /**
     * Compare nodes by f-cost for priority queue
     */
    @Override
    public int compareTo(PathfindingNode other) {
        return Double.compare(this.getFCost(), other.getFCost());
    }
    
    /**
     * Check if two nodes are at the same position
     */
    public boolean isSamePosition(PathfindingNode other) {
        return Math.abs(this.x - other.x) < 1.0 &&
               Math.abs(this.y - other.y) < 1.0 &&
               Math.abs(this.altitude - other.altitude) < 5.0;
    }
    
    /**
     * Create a unique key for this node position
     */
    public String getKey() {
        return String.format("%.0f,%.0f,%.0f", x, y, altitude);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PathfindingNode)) {
            return false;
        }
        PathfindingNode other = (PathfindingNode) obj;
        return isSamePosition(other);
    }
    
    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
