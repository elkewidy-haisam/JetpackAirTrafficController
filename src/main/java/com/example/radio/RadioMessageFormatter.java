/**
 * RadioMessageFormatter component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radiomessageformatter functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radiomessageformatter operations
 * - Maintain necessary state for radiomessageformatter functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.radio;

/**
 * RadioMessageFormatter - formats radio transmission messages
 */
public class RadioMessageFormatter {
    // Controller's radio callsign used as message originator
    private String controllerCallSign;
    
    /**
     * Constructs a RadioMessageFormatter with specified controller callsign.
     * All formatted messages will originate from this identifier.
     * 
     * @param controllerCallSign ATC station identifier (e.g., "ATC-NY-001")
     */
    public RadioMessageFormatter(String controllerCallSign) {
        // Store controller callsign for inclusion in all formatted messages
        this.controllerCallSign = controllerCallSign;
    }
    
    /**
     * Formats coordinate change instruction message.
     * Creates standardized ATC phraseology for position changes.
     * 
     * @param callsign Target aircraft identifier
     * @param newX New X coordinate
     * @param newY New Y coordinate
     * @param reason Explanation for position change
     * @return Formatted radio message string
     */
    public String formatCoordinateInstruction(String callsign, int newX, int newY, String reason) {
        // Format using standard ATC phraseology: FROM to TO: INSTRUCTION. Acknowledge.
        return String.format(
            "%s to %s: Proceed to new coordinates X=%d, Y=%d. Reason: %s. Acknowledge.",
            controllerCallSign, callsign, newX, newY, reason
        );
    }
    
    /**
     * Formats altitude change instruction message.
     * Automatically determines "Climb" or "Descend" based on altitude sign.
     * 
     * @param callsign Target aircraft identifier
     * @param newAltitude Target altitude (positive for climb, negative for descent)
     * @param reason Explanation for altitude change
     * @return Formatted radio message string
     */
    public String formatAltitudeInstruction(String callsign, int newAltitude, String reason) {
        // Determine instruction verb based on altitude sign
        String instruction = newAltitude > 0 ? "Climb" : "Descend";
        
        // Format with absolute altitude value and appropriate verb
        return String.format(
            "%s to %s: %s to altitude %d feet. Reason: %s. Acknowledge.",
            controllerCallSign, callsign, instruction, Math.abs(newAltitude), reason
        );
    }
    
    /**
     * Formats takeoff clearance message
     */
    public String formatTakeoffClearance(String callsign, String runway) {
        return String.format(
            "%s to %s: You are cleared for takeoff from %s. Wind calm. Safe flight.",
            controllerCallSign, callsign, runway
        );
    }
    
    /**
     * Formats landing clearance message
     */
    public String formatLandingClearance(String callsign, String landingZone) {
        return String.format(
            "%s to %s: You are cleared to land at %s. Winds light and variable. Welcome back.",
            controllerCallSign, callsign, landingZone
        );
    }
    
    /**
     * Formats emergency directive message
     */
    public String formatEmergencyDirective(String callsign, String directive) {
        return String.format(
            "*** EMERGENCY *** %s to %s: %s. Respond immediately!",
            controllerCallSign, callsign, directive
        );
    }
    
    /**
     * Formats broadcast message
     */
    public String formatBroadcast(String message) {
        return String.format(
            "%s to ALL AIRCRAFT: %s",
            controllerCallSign, message
        );
    }
    
    /**
     * Formats weather information message
     */
    public String formatWeatherInfo(String callsign, String weatherInfo) {
        return String.format(
            "%s to %s: Current weather conditions: %s",
            controllerCallSign, callsign, weatherInfo
        );
    }
    
    /**
     * Formats position report request
     */
    public String formatPositionRequest(String callsign) {
        return String.format(
            "%s to %s: Please report your current position and altitude.",
            controllerCallSign, callsign
        );
    }
    
    /**
     * Formats accident report
     */
    public String formatAccidentReport(String accidentID, int x, int y, String type, String severity, String description) {
        return String.format(
            "*** ACCIDENT REPORT *** %s: ID=%s | Type=%s | Severity=%s | Location=(%d,%d) | %s | ALL AIRCRAFT AVOID AREA",
            controllerCallSign, accidentID, type, severity, x, y, description
        );
    }
    
    /**
     * Formats accident cleared message
     */
    public String formatAccidentCleared(String accidentID, int x, int y) {
        return String.format(
            "*** ACCIDENT CLEARED *** %s: Accident ID=%s at location (%d,%d) has been cleared. Area is now safe for transit.",
            controllerCallSign, accidentID, x, y
        );
    }
    
    /**
     * Formats handoff message
     */
    public String formatHandoffMessage(String callsign, String arrivalController) {
        return String.format(
            "%s to %s: Contact %s on frequency 119.1. Safe flight.",
            controllerCallSign, callsign, arrivalController
        );
    }
    
    /**
     * Formats pilot acknowledgment message
     */
    public String formatPilotAcknowledgment(String callsign, String acknowledgment) {
        return String.format("%s, %s, %s", callsign, acknowledgment, controllerCallSign);
    }
    
    /**
     * Formats handoff acknowledgment
     */
    public String formatHandoffAcknowledgment(String callsign, String arrivalController, String acknowledgment) {
        return String.format("%s, switching to %s, %s", callsign, arrivalController, acknowledgment);
    }
    
    public String getControllerCallSign() {
        return controllerCallSign;
    }
    
    public void setControllerCallSign(String controllerCallSign) {
        this.controllerCallSign = controllerCallSign;
    }
}
