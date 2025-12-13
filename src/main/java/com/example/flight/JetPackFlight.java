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
        this.jetpack = jetpack;  // Store jetpack reference for callsign, identification, and properties
        this.color = color;  // Set current display color for rendering
        this.baseColor = color;  // Preserve original color to restore after temporary color changes (emergencies, hazards)

        // Initialize components - delegate movement, hazard, and emergency logic to specialized controllers
        double initialAltitude = 80 + Math.random() * 80;  // Random altitude 80-160 for realistic separation and variety
        this.movementController = new FlightMovementController(start, destination, 2.0, initialAltitude);  // Create controller with initial position, target, speed 2.0, and altitude
        this.hazardMonitor = new FlightHazardMonitor();  // Create hazard monitor to track and respond to flight hazards
        this.emergencyHandler = new FlightEmergencyHandler(jetpack.getCallsign());  // Create emergency handler with jetpack identifier for logging

        // Setup emergency handler callbacks - wire emergency messages to movement logger if available
        this.emergencyHandler.setEmergencyLogger(message -> {  // Lambda callback for emergency log messages
            if (movementLogger != null) {  // Check if movement logger has been set
                movementLogger.appendJetpackMovement(message);  // Forward emergency message to movement log
            }
        });

        // Initialize FlightPath attributes - set up active flight tracking state
        this.isActive = true;  // Flight starts in active state (not halted or completed)
        this.currentStatus = "ACTIVE";  // Set initial status text for UI display
        this.pathID = jetpack.getCallsign() + "-PATH";  // Generate unique path identifier from callsign for tracking
    }
    
    public void setMovementLogger(MovementLogger logger) {
        this.movementLogger = logger;  // Set logger for recording all flight movement and status changes
    }
    
    public void setFlightStateProvider(FlightStateProvider provider) {
        this.flightStateProvider = provider;  // Set provider that supplies parking/timer state for this flight
    }
    
    /**
     * Sets the map image for water detection during emergency landings
     */
    public void setMapImage(java.awt.image.BufferedImage mapImage) {
        emergencyHandler.setMapImage(mapImage);  // Pass map image to emergency handler for water pixel detection during landing site selection
    }
    
    public void setRadioInstructionListener(RadioInstructionListener listener) {
        emergencyHandler.setRadioInstructionListener(new FlightEmergencyHandler.RadioInstructionListener() {  // Create adapter to bridge emergency handler callbacks to flight-level listener
            @Override
            public void onInstructionReceived(String instruction) {
                if (listener != null) {  // Check if listener has been provided
                    listener.onInstructionReceived(JetPackFlight.this, instruction);  // Notify listener that this flight received an instruction, passing flight reference
                }
            }
            
            @Override
            public void onInstructionCompleted(String instruction) {
                if (listener != null) {  // Check if listener has been provided
                    listener.onInstructionCompleted(JetPackFlight.this, instruction);  // Notify listener that this flight completed an instruction, passing flight reference
                }
            }
        });
    }
    
    /**
     * Receives and executes a radio instruction to change coordinates
     */
    public void receiveCoordinateInstruction(int newX, int newY, String reason) {
        Point newDest = emergencyHandler.receiveCoordinateInstruction(newX, newY, reason);  // Process coordinate instruction through emergency handler (logs and creates new Point)
        movementController.setNewDestination(newDest);  // Update movement controller to fly to new coordinates
        this.currentStatus = "RADIO: " + reason;  // Update status text to reflect radio instruction and reason
    }
    
    /**
     * Receives and executes a radio instruction to change altitude
     */
    public void receiveAltitudeInstruction(double newAltitude, String reason) {
        emergencyHandler.receiveAltitudeInstruction(movementController.getAltitude(), newAltitude, reason);  // Process altitude instruction through emergency handler (logs current and target altitude)
        this.currentStatus = "RADIO: " + reason;  // Update status text to reflect radio instruction and reason
    }
    
    /**
     * Receives emergency landing instruction
     */
    public void receiveEmergencyLandingInstruction(List<ParkingSpace> parkingSpaces, String reason) {
        emergencyLanding(parkingSpaces, "RADIO ORDER: " + reason);  // Delegate to emergency landing method with radio order prefix for logging
    }
    
    public void updatePosition() {
        // Don't move if emergency halt is active or flight is inactive
        if (hazardMonitor.isEmergencyHalt() || !isActive) {  // Check both emergency halt flag and active status
            return;  // Skip position update to keep jetpack stationary during halt
        }
        
        // Calculate effective speed based on hazards - may reduce speed for safety
        double effectiveSpeed = hazardMonitor.calculateEffectiveSpeed(movementController.getSpeed());  // Get speed adjusted for hazard flags (weather, accidents, etc.)
        
        // Update position through movement controller - incremental movement toward destination
        movementController.updatePosition(effectiveSpeed, hazardMonitor.isEmergencyHalt());  // Move jetpack using calculated speed, passing halt status
        
        // Check if radio destination reached - handle completion of coordinate instructions
        Point radioDestination = emergencyHandler.getRadioDestination();  // Get active radio coordinate instruction if any
        if (radioDestination != null) {  // If there's an active radio coordinate instruction
            double distance = movementController.getDistanceToDestination();  // Calculate remaining distance to target
            emergencyHandler.checkRadioDestinationReached(  // Check if we've arrived at radio-instructed coordinates
                movementController.getDestination(),  // Current destination point
                distance,  // Distance remaining
                movementController.getSpeed()  // Current speed for threshold calculation
            );
            if (emergencyHandler.getRadioDestination() == null && emergencyHandler.getRadioAltitude() == null) {  // If both coordinate and altitude instructions completed
                currentStatus = "ACTIVE";  // Reset status to normal active flight
            }
        }
        
        // Handle radio altitude instruction - check if target altitude reached
        boolean altitudeReached = emergencyHandler.checkRadioAltitudeReached(movementController.getAltitude());  // Check if current altitude matches radio-instructed altitude
        if (altitudeReached && emergencyHandler.getRadioDestination() == null) {  // If altitude reached and no coordinate instruction pending
            currentStatus = "ACTIVE";  // Reset status to normal active flight
        }
        
        // Update altitude - gradually adjust toward target altitude
        movementController.updateAltitude(emergencyHandler.getRadioAltitude());  // Pass target altitude (null if none) for gradual adjustment
        
        // Update color based on speed - visual feedback for speed changes
        updateColorBySpeed(effectiveSpeed);  // Adjust jetpack display color based on current effective speed
    }
    
    private Point getActiveTarget() {
        return movementController.getActiveTarget();  // Retrieve current active waypoint or final destination from movement controller
    }
    
    public boolean hasReachedDestination() {
        return movementController.hasReachedDestination(hazardMonitor.isEmergencyHalt());  // Check if jetpack has reached its destination, considering halt status
    }
    
    public void setNewDestination(Point newDest) {
        if (hazardMonitor.isEmergencyHalt()) {  // Check if emergency halt is active
            return;  // Ignore destination change during emergency halt (safety lock)
        }
        
        movementController.setNewDestination(newDest);  // Update destination in movement controller
        logMovementDirection();  // Log new heading direction for movement history
    }
    
    // FlightPath methods - waypoint and detour management
    
    public void addWaypoint(Point waypoint) {
        movementController.addWaypoint(waypoint);  // Add single waypoint to flight path queue for sequential navigation
    }
    
    public void setWaypoints(List<Point> waypoints) {
        movementController.setWaypoints(waypoints);  // Replace entire waypoint queue with new list of waypoints
    }
    
    public void detour(List<Point> detourPoints, String hazardType) {
        if (detourPoints == null || detourPoints.isEmpty()) {  // Validate detour points provided
            return;  // Ignore empty detour request (no alternate route available)
        }
        
        movementController.detour(detourPoints);  // Insert detour waypoints at front of queue to navigate around hazard
        this.currentStatus = "DETOUR";  // Update status to indicate active detour
        
        if (movementLogger != null) {  // Check if movement logger is available
            movementLogger.appendJetpackMovement(jetpack.getCallsign() +  // Log detour event with callsign
                " DETOUR: Avoiding " + hazardType);  // Include hazard type being avoided
        }
    }
    
    public void halt(String reason) {
        hazardMonitor.setEmergencyHalt(true);  // Set emergency halt flag in hazard monitor to freeze movement
        this.isActive = false;  // Mark flight as inactive to prevent position updates
        this.currentStatus = "EMERGENCY HALT";  // Update status text for UI display
        this.color = Color.RED;  // Change color to red to visually indicate emergency halt state
        
        if (movementLogger != null) {  // Check if movement logger is available
            movementLogger.appendJetpackMovement(jetpack.getCallsign() +  // Log halt event with callsign
                " ⛔ EMERGENCY HALT: " + reason);  // Include stop sign emoji and reason for halt
        }
    }
    
    /**
     * Initiates emergency landing procedure
     * Finds nearest parking space and navigates there immediately
     */
    public void emergencyLanding(List<ParkingSpace> parkingSpaces, String reason) {
        Point emergencyDest = emergencyHandler.receiveEmergencyLandingInstruction(  // Find nearest available parking space for emergency landing
            movementController.getX(),  // Current X position for distance calculations
            movementController.getY(),  // Current Y position for distance calculations
            parkingSpaces,  // List of all parking spaces to search
            reason  // Reason for emergency landing (logged by handler)
        );
        
        if (emergencyDest == null) {  // Check if emergency handler found a parking space
            // No parking available, just halt in place - safety fallback
            halt("EMERGENCY LANDING - " + reason);  // Execute emergency halt with reason
        } else {
            // Set parking as immediate destination - clear waypoints and head straight there
            movementController.setEmergencyDestination(emergencyDest, movementController.getSpeed() * 1.5);  // Set emergency destination with 50% speed boost for urgency
            this.currentStatus = "EMERGENCY LANDING";  // Update status to indicate emergency landing in progress
            this.color = Color.MAGENTA;  // Change color to magenta to visually distinguish emergency landing from other states
        }
    }
    
    public void resumeNormalPath() {
        if (movementController.isDetourActive()) {  // Check if jetpack is currently following a detour route
            movementController.resumeNormalPath();  // Clear detour waypoints and return to original flight path
            currentStatus = "ACTIVE";  // Reset status to normal active flight
            
            if (movementLogger != null) {  // Check if movement logger is available
                movementLogger.appendJetpackMovement(jetpack.getCallsign() +  // Log path resumption with callsign
                    " Resuming normal flight path");  // Indicate return to original route
            }
        }
    }
    
    public void clearEmergencyHalt() {
        if (hazardMonitor.isEmergencyHalt()) {  // Check if emergency halt flag is set
            hazardMonitor.clearEmergencyHalt();  // Clear emergency halt flag to allow movement
            isActive = true;  // Reactivate flight to enable position updates
            currentStatus = "ACTIVE";  // Reset status to normal active flight
            color = new Color(color.getRed(), color.getGreen(), color.getBlue());  // Restore original color (creates new Color instance to ensure fresh state)
            
            if (movementLogger != null) {  // Check if movement logger is available
                movementLogger.appendJetpackMovement(jetpack.getCallsign() +  // Log emergency clearance with callsign
                    " ✓ Emergency cleared, resuming flight");  // Include checkmark to indicate successful clearance
            }
        }
    }
    
    public List<String> getActiveHazards() {
        return hazardMonitor.getActiveHazards();  // Retrieve list of currently active hazard types (weather, accidents, police, etc.)
    }
    
    public boolean hasActiveHazards() {
        return hazardMonitor.hasActiveHazards();  // Check if any hazard flags are currently set
    }
    
    // Hazard setters - update hazard flags and status text
    public void setInclementWeather(boolean active) {
        hazardMonitor.setInclementWeather(active);  // Set or clear inclement weather hazard flag
        if (active) {  // If weather hazard is being activated
            currentStatus = "WEATHER WARNING";  // Update status to indicate weather-related speed reduction
        }
    }
    
    public void setBuildingCollapse(boolean active) {
        hazardMonitor.setBuildingCollapse(active);  // Set or clear building collapse hazard flag
        if (active) {  // If building hazard is being activated
            currentStatus = "BUILDING HAZARD";  // Update status to indicate building-related avoidance
        }
    }
    
    public void setAirAccident(boolean active) {
        hazardMonitor.setAirAccident(active);  // Set or clear air accident hazard flag
        if (active) {  // If accident hazard is being activated
            currentStatus = "ACCIDENT ZONE";  // Update status to indicate accident zone avoidance
        }
    }
    
    /**
     * Triggers emergency rerouting due to collision
     */
    public void setEmergencyReroute(boolean active) {
        if (active) {  // If emergency reroute is being activated
            List<Point> emergencyWaypoints = emergencyHandler.generateEmergencyDetour(  // Generate alternate route around collision
                movementController.getX(),  // Current X position as detour start
                movementController.getY(),  // Current Y position as detour start
                movementController.getDestination()  // Final destination to return to after detour
            );
            
            detour(emergencyWaypoints, "COLLISION");  // Execute detour with generated waypoints, logging collision as reason
            hazardMonitor.setAirAccident(true);  // Set air accident hazard flag to reduce speed during detour
        }
    }
    
    public void setPoliceActivity(boolean active) {
        hazardMonitor.setPoliceActivity(active);  // Set or clear police activity hazard flag
        if (active) {  // If police activity hazard is being activated
            currentStatus = "POLICE AREA";  // Update status to indicate police presence requiring caution
        }
    }
    
    public String getCurrentStatus() {
        return currentStatus;  // Return current flight status text for UI display
    }
    
    public boolean isEmergencyHalt() {
        return hazardMonitor.isEmergencyHalt();  // Check if emergency halt flag is active
    }
    
    private void logMovementDirection() {
        if (movementLogger != null) {  // Check if movement logger is available
            String direction = movementController.getDirectionString();  // Get compass direction string (e.g., "N", "NE", "E", etc.)
            double distance = movementController.getDistanceToDestination();  // Calculate remaining distance to destination
            movementLogger.appendJetpackMovement(jetpack.getCallsign() +  // Log movement with callsign
                " moving " + direction + " (" + String.format("%.0f", distance) + " units)");  // Include direction and formatted distance
        }
    }
    
    public void draw(Graphics2D g2d) {
        // Get current state from provider - check parking and timer status
        JetPackFlightState state = (flightStateProvider != null) ? flightStateProvider.getFlightState(this) : null;  // Query flight state provider if available
        boolean isParked = (state != null && state.isParked());  // Determine if jetpack is currently parked based on state
        
        // Build render state - aggregate all visual properties into single object
        JetPackFlightRenderer.FlightRenderState renderState = new JetPackFlightRenderer.FlightRenderState(  // Create render state with all drawing parameters
            movementController.getX(),  // Current X position for jetpack icon
            movementController.getY(),  // Current Y position for jetpack icon
            movementController.getAltitude(),  // Current altitude for shadow/size scaling
            movementController.getDestination(),  // Final destination for line drawing
            getActiveTarget(),  // Current active waypoint or destination
            color,  // Current display color (may be modified for hazards/emergencies)
            jetpack.getCallsign(),  // Callsign text for label
            isParked,  // Parking status affects rendering (static vs animated)
            hazardMonitor.isEmergencyHalt(),  // Emergency halt affects color/icon
            movementController.isDetourActive(),  // Detour affects waypoint rendering
            hasActiveHazards(),  // Hazards affect color/warnings
            movementController.getTrail(),  // Position history for trail rendering
            movementController.getWaypoints(),  // Waypoint list for path visualization
            movementController.getCurrentWaypointIndex()  // Current waypoint index for progress indication
        );
        
        // Delegate to renderer - use static renderer for all drawing operations
        renderer.renderFlight(g2d, renderState);  // Pass graphics context and render state to renderer for visual output
    }
    
    /**
     * Updates jetpack color based on current speed
     */
    private void updateColorBySpeed(double effectiveSpeed) {
        // Speed-based color gradient: Blue (slow) -> Green (normal) -> Yellow (fast) -> Red (very fast) - provides visual feedback for speed changes
        if (effectiveSpeed < 1.0) {  // Check if speed is below normal (hazard-reduced)
            color = new Color(0, 100, 255);  // Blue - indicates slow/cautious movement
        } else if (effectiveSpeed < 2.0) {  // Check if speed is at normal cruise (2.0 is base speed)
            color = new Color(0, 200, 100);  // Green - indicates normal flight speed
        } else if (effectiveSpeed < 3.0) {  // Check if speed is above normal but not extreme
            color = new Color(255, 200, 0);  // Yellow - indicates fast movement (emergency landing boost)
        } else {  // Speed is 3.0 or higher (extreme)
            color = new Color(255, 50, 0);  // Red - indicates very fast/urgent movement
        }
    }
    
    // Getters - provide read access to internal state via delegation
    public JetPack getJetpack() {
        return jetpack;  // Return jetpack reference for callsign and properties access
    }
    
    public double getX() {
        return movementController.getX();  // Delegate X coordinate retrieval to movement controller
    }
    
    public double getY() {
        return movementController.getY();  // Delegate Y coordinate retrieval to movement controller
    }
    
    public Point getDestination() {
        return movementController.getDestination();  // Delegate destination retrieval to movement controller
    }
    
    public Color getColor() {
        return color;  // Return current display color (may be modified from base color)
    }

    public double getAltitude() {
        return movementController.getAltitude();  // Delegate altitude retrieval to movement controller
    }
}
