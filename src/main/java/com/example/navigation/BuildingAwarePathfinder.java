/**
 * A* pathfinding algorithm that accounts for buildings and 3D obstacles.
 * 
 * Purpose:
 * Calculates optimal flight paths that navigate around buildings in the city.
 * Uses A* algorithm with 3D grid-based search to find collision-free routes
 * from start to destination, considering building heights and safe altitudes.
 * 
 * Key Responsibilities:
 * - Generate waypoint paths that avoid buildings
 * - Use A* algorithm for optimal pathfinding
 * - Consider 3D space (x, y, altitude) for flight paths
 * - Provide simplified waypoint lists for flight navigation
 * - Support both horizontal and vertical movement
 * 
 * Interactions:
 * - Uses CityModel3D and BuildingCollisionDetector for obstacle detection
 * - Generates waypoints for FlightMovementController
 * - Called during flight initialization to pre-calculate paths
 * 
 * Patterns & Constraints:
 * - Grid-based pathfinding with configurable step size
 * - Heuristic uses 3D Euclidean distance
 * - Optimized with closed set to avoid revisiting nodes
 * - Simplifies path by removing redundant intermediate waypoints
 * 
 * @author Haisam Elkewidy
 */

package com.example.navigation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.example.detection.BuildingCollisionDetector;
import com.example.model.CityModel3D;

public class BuildingAwarePathfinder {
    
    private final CityModel3D cityModel;
    private final BuildingCollisionDetector collisionDetector;
    
    // Pathfinding parameters
    private static final double STEP_SIZE = 20.0; // Grid step size for pathfinding
    private static final double ALTITUDE_STEP = 30.0; // Vertical movement step
    private static final int MAX_ITERATIONS = 5000; // Prevent infinite loops
    
    public BuildingAwarePathfinder(CityModel3D cityModel) {
        this.cityModel = cityModel;
        this.collisionDetector = new BuildingCollisionDetector(cityModel);
    }
    
    /**
     * Find a path from start to destination that avoids buildings
     * @param startX Starting X coordinate
     * @param startY Starting Y coordinate
     * @param destX Destination X coordinate
     * @param destY Destination Y coordinate
     * @return List of waypoints (as Points) representing the path, or empty list if no path found
     */
    public List<Point> findPath(double startX, double startY, double destX, double destY) {
        // Determine safe starting and ending altitudes
        double startAlt = Math.max(100, collisionDetector.getMinimumSafeAltitude(startX, startY) + 10);
        double destAlt = Math.max(100, collisionDetector.getMinimumSafeAltitude(destX, destY) + 10);
        
        // Use A* to find path in 3D space
        List<PathfindingNode> nodePath = findPathAStar(startX, startY, startAlt, destX, destY, destAlt);
        
        if (nodePath.isEmpty()) {
            // No path found, return straight line (collision detection will handle at runtime)
            return new ArrayList<>();
        }
        
        // Simplify path by removing redundant waypoints
        List<PathfindingNode> simplifiedPath = simplifyPath(nodePath);
        
        // Convert to Point list (2D coordinates for compatibility)
        List<Point> waypoints = new ArrayList<>();
        for (PathfindingNode node : simplifiedPath) {
            waypoints.add(new Point((int)node.x, (int)node.y));
        }
        
        return waypoints;
    }
    
    /**
     * A* pathfinding algorithm in 3D space
     */
    private List<PathfindingNode> findPathAStar(double startX, double startY, double startAlt,
                                                 double destX, double destY, double destAlt) {
        PathfindingNode startNode = new PathfindingNode(startX, startY, startAlt);
        PathfindingNode goalNode = new PathfindingNode(destX, destY, destAlt);
        
        PriorityQueue<PathfindingNode> openSet = new PriorityQueue<>();
        Set<String> closedSet = new HashSet<>();
        Map<String, PathfindingNode> openSetMap = new HashMap<>();
        
        startNode.gCost = 0;
        startNode.hCost = calculateHeuristic(startNode, goalNode);
        openSet.add(startNode);
        openSetMap.put(startNode.getKey(), startNode);
        
        int iterations = 0;
        
        while (!openSet.isEmpty() && iterations < MAX_ITERATIONS) {
            iterations++;
            
            PathfindingNode current = openSet.poll();
            String currentKey = current.getKey();
            openSetMap.remove(currentKey);
            
            // Check if we reached the goal
            if (current.isSamePosition(goalNode)) {
                return reconstructPath(current);
            }
            
            closedSet.add(currentKey);
            
            // Explore neighbors
            List<PathfindingNode> neighbors = getNeighbors(current);
            for (PathfindingNode neighbor : neighbors) {
                String neighborKey = neighbor.getKey();
                
                if (closedSet.contains(neighborKey)) {
                    continue;
                }
                
                // Check if neighbor collides with buildings
                if (collisionDetector.checkCollision(neighbor.x, neighbor.y, neighbor.altitude)) {
                    continue;
                }
                
                double tentativeGCost = current.gCost + calculateDistance(current, neighbor);
                
                PathfindingNode existingNeighbor = openSetMap.get(neighborKey);
                if (existingNeighbor == null || tentativeGCost < existingNeighbor.gCost) {
                    neighbor.gCost = tentativeGCost;
                    neighbor.hCost = calculateHeuristic(neighbor, goalNode);
                    neighbor.parent = current;
                    
                    if (existingNeighbor != null) {
                        openSet.remove(existingNeighbor);
                    }
                    
                    openSet.add(neighbor);
                    openSetMap.put(neighborKey, neighbor);
                }
            }
        }
        
        // No path found
        return new ArrayList<>();
    }
    
    /**
     * Get neighboring nodes in 3D space
     */
    private List<PathfindingNode> getNeighbors(PathfindingNode node) {
        List<PathfindingNode> neighbors = new ArrayList<>();
        
        // 8 horizontal directions + vertical movement
        double[][] directions = {
            {STEP_SIZE, 0, 0},           // East
            {-STEP_SIZE, 0, 0},          // West
            {0, STEP_SIZE, 0},           // North
            {0, -STEP_SIZE, 0},          // South
            {STEP_SIZE, STEP_SIZE, 0},   // Northeast
            {STEP_SIZE, -STEP_SIZE, 0},  // Southeast
            {-STEP_SIZE, STEP_SIZE, 0},  // Northwest
            {-STEP_SIZE, -STEP_SIZE, 0}, // Southwest
            {0, 0, ALTITUDE_STEP},       // Up
            {0, 0, -ALTITUDE_STEP}       // Down
        };
        
        for (double[] dir : directions) {
            double newX = node.x + dir[0];
            double newY = node.y + dir[1];
            double newAlt = node.altitude + dir[2];
            
            // Keep altitude within reasonable bounds
            if (newAlt < 50 || newAlt > 300) {
                continue;
            }
            
            // Keep within map bounds
            if (newX < 0 || newX >= cityModel.getMapWidth() ||
                newY < 0 || newY >= cityModel.getMapHeight()) {
                continue;
            }
            
            neighbors.add(new PathfindingNode(newX, newY, newAlt));
        }
        
        return neighbors;
    }
    
    /**
     * Calculate heuristic (3D Euclidean distance)
     */
    private double calculateHeuristic(PathfindingNode from, PathfindingNode to) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = (to.altitude - from.altitude) * 0.5; // Weight altitude less
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * Calculate actual distance between two nodes
     */
    private double calculateDistance(PathfindingNode from, PathfindingNode to) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = (to.altitude - from.altitude) * 0.5; // Weight altitude less
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * Reconstruct path from goal node to start
     */
    private List<PathfindingNode> reconstructPath(PathfindingNode goalNode) {
        List<PathfindingNode> path = new ArrayList<>();
        PathfindingNode current = goalNode;
        
        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }
        
        return path;
    }
    
    /**
     * Simplify path by removing redundant waypoints
     * Uses line-of-sight checks to skip intermediate waypoints
     */
    private List<PathfindingNode> simplifyPath(List<PathfindingNode> path) {
        if (path.size() <= 2) {
            return path;
        }
        
        List<PathfindingNode> simplified = new ArrayList<>();
        simplified.add(path.get(0)); // Always include start
        
        int current = 0;
        while (current < path.size() - 1) {
            int farthest = current + 1;
            
            // Find the farthest node we can reach directly
            for (int i = current + 2; i < path.size(); i++) {
                PathfindingNode from = path.get(current);
                PathfindingNode to = path.get(i);
                
                if (collisionDetector.isPathClear(from.x, from.y, from.altitude,
                                                   to.x, to.y, to.altitude)) {
                    farthest = i;
                } else {
                    break;
                }
            }
            
            if (farthest != path.size() - 1) {
                simplified.add(path.get(farthest));
            }
            current = farthest;
        }
        
        simplified.add(path.get(path.size() - 1)); // Always include goal
        
        return simplified;
    }
}
