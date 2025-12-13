/**
 * Represents an active jetpack flight with real-time position tracking and instruction handling.
 * 
 * Purpose:
 * Encapsulates a complete flight operation from origin to destination, managing position updates,
 * waypoint navigation, hazard avoidance, and emergency procedures. Coordinates multiple specialized
 * subsystems (movement controller, hazard monitor, emergency handler) to provide cohesive flight
 * management with logging and state synchronization.
 * 
 * Key Responsibilities:
 * - Track jetpack position, velocity, and flight status throughout journey
 * - Process ATC instructions (coordinate changes, altitude adjustments, emergency landings)
 * - Update position incrementally via FlightMovementController
 * - Monitor hazards continuously through FlightHazardMonitor
 * - Execute emergency procedures via FlightEmergencyHandler
 * - Log all movement events for replay and compliance review
 * - Manage waypoint-based navigation and detour routing
 * - Support flight halt/resume for temporary restrictions
 * 
 * Interactions:
 * - FlightMovementController: Calculates incremental position updates toward waypoints
 * - FlightHazardMonitor: Detects hazards (weather, accidents, police activity) requiring detours
 * - FlightEmergencyHandler: Orchestrates emergency landing and safety procedures
 * - MovementLogger: Records timestamped position and status changes
 * - FlightStateProvider: Supplies current position/altitude for external queries
 * - CityMapPanel: Receives position updates for animation rendering
 * - RadioInstructionManager: Sources instructions from ATC communications
 * 
 * Patterns & Constraints:
 * - Composition pattern: delegates specialized logic to controller/monitor/handler components
 * - Observer-like callbacks: onInstructionReceived, onInstructionCompleted for UI updates
 * - Stateful: maintains active/inactive, halted, emergency states
 * - Thread-safe position updates via synchronized access at higher layers
 * - Color-coded for visual distinction in UI (baseColor for normal, modified during emergencies)
 * 
 * @author Haisam Elkewidy
 */

package com.example.flight;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import com.example.jetpack.JetPack;
import com.example.parking.ParkingSpace;

/**
 * JetPackFlight manages animated jetpack flight with trails, destinations, and FlightPath logic.
 * It integrates movement, hazard monitoring, and emergency handling for each jetpack.
 */
public class JetPackFlight {
    /**
     * Returns the current direction angle (radians) of flight, for rendering orientation.
     */
    public double getDirectionAngle() {
        return movementController.getDirectionAngle();
    }
    private JetPack jetpack;
    private Color color;
    private Color baseColor; // Store original color
    private MovementLogger movementLogger;
    private FlightStateProvider flightStateProvider;
    // Extracted components
    private FlightMovementController movementController;
    private FlightHazardMonitor hazardMonitor;
    private FlightEmergencyHandler emergencyHandler;
    private static final JetPackFlightRenderer renderer = new JetPackFlightRenderer();
    // FlightPath integration - status
    private boolean isActive;
    private String currentStatus;
    private String pathID;

    public interface MovementLogger {
        void appendJetpackMovement(String message);
    }

    public interface RadioInstructionListener {
        void onInstructionReceived(JetPackFlight flight, String instruction);
        void onInstructionCompleted(JetPackFlight flight, String instruction);
    }

    public interface FlightStateProvider {
        JetPackFlightState getFlightState(JetPackFlight flight);
    }

    public JetPackFlight(JetPack jetpack, Point start, Point destination, Color color) {
        this.jetpack = jetpack;
        this.color = color;
        this.baseColor = color;

        // Initialize components
        double initialAltitude = 80 + Math.random() * 80; // Random altitude 80-160
        this.movementController = new FlightMovementController(start, destination, 2.0, initialAltitude);
        this.hazardMonitor = new FlightHazardMonitor();
        this.emergencyHandler = new FlightEmergencyHandler(jetpack.getCallsign());

        // Setup emergency handler callbacks
        this.emergencyHandler.setEmergencyLogger(message -> {
            if (movementLogger != null) {
                movementLogger.appendJetpackMovement(message);
            }
        });

        // Initialize FlightPath attributes
        this.isActive = true;
        this.currentStatus = "ACTIVE";
        this.pathID = jetpack.getCallsign() + "-PATH";
    }
    
    public void setMovementLogger(MovementLogger logger) {
        this.movementLogger = logger;
    }
    
    public void setFlightStateProvider(FlightStateProvider provider) {
        this.flightStateProvider = provider;
    }
    
    /**
     * Sets the map image for water detection during emergency landings
     */
    public void setMapImage(java.awt.image.BufferedImage mapImage) {
        emergencyHandler.setMapImage(mapImage);
    }
    
    public void setRadioInstructionListener(RadioInstructionListener listener) {
        emergencyHandler.setRadioInstructionListener(new FlightEmergencyHandler.RadioInstructionListener() {
            @Override
            public void onInstructionReceived(String instruction) {
                if (listener != null) {
                    listener.onInstructionReceived(JetPackFlight.this, instruction);
                }
            }
            
            @Override
            public void onInstructionCompleted(String instruction) {
                if (listener != null) {
                    listener.onInstructionCompleted(JetPackFlight.this, instruction);
                }
            }
        });
    }
    
    /**
     * Receives and executes a radio instruction to change coordinates
     */
    public void receiveCoordinateInstruction(int newX, int newY, String reason) {
        Point newDest = emergencyHandler.receiveCoordinateInstruction(newX, newY, reason);
        movementController.setNewDestination(newDest);
        this.currentStatus = "RADIO: " + reason;
    }
    
    /**
     * Receives and executes a radio instruction to change altitude
     */
    public void receiveAltitudeInstruction(double newAltitude, String reason) {
        emergencyHandler.receiveAltitudeInstruction(movementController.getAltitude(), newAltitude, reason);
        this.currentStatus = "RADIO: " + reason;
    }
    
    /**
     * Receives emergency landing instruction
     */
    public void receiveEmergencyLandingInstruction(List<ParkingSpace> parkingSpaces, String reason) {
        emergencyLanding(parkingSpaces, "RADIO ORDER: " + reason);
    }
    
    public void updatePosition() {
        // Don't move if emergency halt is active
        if (hazardMonitor.isEmergencyHalt() || !isActive) {
            return;
        }
        
        // Calculate effective speed based on hazards
        double effectiveSpeed = hazardMonitor.calculateEffectiveSpeed(movementController.getSpeed());
        
        // Update position through movement controller
        movementController.updatePosition(effectiveSpeed, hazardMonitor.isEmergencyHalt());
        
        // Check if radio destination reached
        Point radioDestination = emergencyHandler.getRadioDestination();
        if (radioDestination != null) {
            double distance = movementController.getDistanceToDestination();
            emergencyHandler.checkRadioDestinationReached(
                movementController.getDestination(), 
                distance, 
                movementController.getSpeed()
            );
            if (emergencyHandler.getRadioDestination() == null && emergencyHandler.getRadioAltitude() == null) {
                currentStatus = "ACTIVE";
            }
        }
        
        // Handle radio altitude instruction
        boolean altitudeReached = emergencyHandler.checkRadioAltitudeReached(movementController.getAltitude());
        if (altitudeReached && emergencyHandler.getRadioDestination() == null) {
            currentStatus = "ACTIVE";
        }
        
        // Update altitude
        movementController.updateAltitude(emergencyHandler.getRadioAltitude());
        
        // Update color based on speed
        updateColorBySpeed(effectiveSpeed);
    }
    
    private Point getActiveTarget() {
        return movementController.getActiveTarget();
    }
    
    public boolean hasReachedDestination() {
        return movementController.hasReachedDestination(hazardMonitor.isEmergencyHalt());
    }
    
    public void setNewDestination(Point newDest) {
        if (hazardMonitor.isEmergencyHalt()) {
            return;
        }
        
        movementController.setNewDestination(newDest);
        logMovementDirection();
    }
    
    // FlightPath methods
    
    public void addWaypoint(Point waypoint) {
        movementController.addWaypoint(waypoint);
    }
    
    public void setWaypoints(List<Point> waypoints) {
        movementController.setWaypoints(waypoints);
    }
    
    public void detour(List<Point> detourPoints, String hazardType) {
        if (detourPoints == null || detourPoints.isEmpty()) {
            return;
        }
        
        movementController.detour(detourPoints);
        this.currentStatus = "DETOUR";
        
        if (movementLogger != null) {
            movementLogger.appendJetpackMovement(jetpack.getCallsign() + 
                " DETOUR: Avoiding " + hazardType);
        }
    }
    
    public void halt(String reason) {
        hazardMonitor.setEmergencyHalt(true);
        this.isActive = false;
        this.currentStatus = "EMERGENCY HALT";
        this.color = Color.RED; // Change color to indicate emergency
        
        if (movementLogger != null) {
            movementLogger.appendJetpackMovement(jetpack.getCallsign() + 
                " ⛔ EMERGENCY HALT: " + reason);
        }
    }
    
    /**
     * Initiates emergency landing procedure
     * Finds nearest parking space and navigates there immediately
     */
    public void emergencyLanding(List<ParkingSpace> parkingSpaces, String reason) {
        Point emergencyDest = emergencyHandler.receiveEmergencyLandingInstruction(
            movementController.getX(), 
            movementController.getY(), 
            parkingSpaces, 
            reason
        );
        
        if (emergencyDest == null) {
            // No parking available, just halt in place
            halt("EMERGENCY LANDING - " + reason);
        } else {
            // Set parking as immediate destination
            movementController.setEmergencyDestination(emergencyDest, movementController.getSpeed() * 1.5);
            this.currentStatus = "EMERGENCY LANDING";
            this.color = Color.MAGENTA; // Magenta indicates emergency landing
        }
    }
    
    public void resumeNormalPath() {
        if (movementController.isDetourActive()) {
            movementController.resumeNormalPath();
            currentStatus = "ACTIVE";
            
            if (movementLogger != null) {
                movementLogger.appendJetpackMovement(jetpack.getCallsign() + 
                    " Resuming normal flight path");
            }
        }
    }
    
    public void clearEmergencyHalt() {
        if (hazardMonitor.isEmergencyHalt()) {
            hazardMonitor.clearEmergencyHalt();
            isActive = true;
            currentStatus = "ACTIVE";
            color = new Color(color.getRed(), color.getGreen(), color.getBlue()); // Restore original color
            
            if (movementLogger != null) {
                movementLogger.appendJetpackMovement(jetpack.getCallsign() + 
                    " ✓ Emergency cleared, resuming flight");
            }
        }
    }
    
    public List<String> getActiveHazards() {
        return hazardMonitor.getActiveHazards();
    }
    
    public boolean hasActiveHazards() {
        return hazardMonitor.hasActiveHazards();
    }
    
    // Hazard setters
    public void setInclementWeather(boolean active) {
        hazardMonitor.setInclementWeather(active);
        if (active) {
            currentStatus = "WEATHER WARNING";
        }
    }
    
    public void setBuildingCollapse(boolean active) {
        hazardMonitor.setBuildingCollapse(active);
        if (active) {
            currentStatus = "BUILDING HAZARD";
        }
    }
    
    public void setAirAccident(boolean active) {
        hazardMonitor.setAirAccident(active);
        if (active) {
            currentStatus = "ACCIDENT ZONE";
        }
    }
    
    /**
     * Triggers emergency rerouting due to collision
     */
    public void setEmergencyReroute(boolean active) {
        if (active) {
            List<Point> emergencyWaypoints = emergencyHandler.generateEmergencyDetour(
                movementController.getX(),
                movementController.getY(),
                movementController.getDestination()
            );
            
            detour(emergencyWaypoints, "COLLISION");
            hazardMonitor.setAirAccident(true);
        }
    }
    
    public void setPoliceActivity(boolean active) {
        hazardMonitor.setPoliceActivity(active);
        if (active) {
            currentStatus = "POLICE AREA";
        }
    }
    
    public String getCurrentStatus() {
        return currentStatus;
    }
    
    public boolean isEmergencyHalt() {
        return hazardMonitor.isEmergencyHalt();
    }
    
    private void logMovementDirection() {
        if (movementLogger != null) {
            String direction = movementController.getDirectionString();
            double distance = movementController.getDistanceToDestination();
            movementLogger.appendJetpackMovement(jetpack.getCallsign() + 
                " moving " + direction + " (" + String.format("%.0f", distance) + " units)");
        }
    }
    
    public void draw(Graphics2D g2d) {
        // Get current state from provider
        JetPackFlightState state = (flightStateProvider != null) ? flightStateProvider.getFlightState(this) : null;
        boolean isParked = (state != null && state.isParked());
        
        // Build render state
        JetPackFlightRenderer.FlightRenderState renderState = new JetPackFlightRenderer.FlightRenderState(
            movementController.getX(),
            movementController.getY(),
            movementController.getAltitude(),
            movementController.getDestination(),
            getActiveTarget(),
            color,
            jetpack.getCallsign(),
            isParked,
            hazardMonitor.isEmergencyHalt(),
            movementController.isDetourActive(),
            hasActiveHazards(),
            movementController.getTrail(),
            movementController.getWaypoints(),
            movementController.getCurrentWaypointIndex()
        );
        
        // Delegate to renderer
        renderer.renderFlight(g2d, renderState);
    }
    
    /**
     * Updates jetpack color based on current speed
     */
    private void updateColorBySpeed(double effectiveSpeed) {
        // Speed-based color gradient: Blue (slow) -> Green (normal) -> Yellow (fast) -> Red (very fast)
        if (effectiveSpeed < 1.0) {
            color = new Color(0, 100, 255); // Blue - slow
        } else if (effectiveSpeed < 2.0) {
            color = new Color(0, 200, 100); // Green - normal
        } else if (effectiveSpeed < 3.0) {
            color = new Color(255, 200, 0); // Yellow - fast
        } else {
            color = new Color(255, 50, 0); // Red - very fast
        }
    }
    
    // Getters
    public JetPack getJetpack() {
        return jetpack;
    }
    
    public double getX() {
        return movementController.getX();
    }
    
    public double getY() {
        return movementController.getY();
    }
    
    public Point getDestination() {
        return movementController.getDestination();
    }
    
    public Color getColor() {
        return color;
    }

    public double getAltitude() {
        return movementController.getAltitude();
    }
}
