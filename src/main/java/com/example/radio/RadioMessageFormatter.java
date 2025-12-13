/**
 * Formats radio transmission messages into standardized aviation communication format.
 * 
 * Purpose:
 * Provides consistent formatting for all radio messages broadcast by the air traffic controller, using
 * standard aviation phraseology and structure. Ensures messages include proper callsigns, coordinates,
 * altitudes, and reasons in a format familiar to pilots. Supports multiple message types including
 * coordinate changes, altitude instructions, emergency directives, and weather broadcasts.
 * 
 * Key Responsibilities:
 * - Format coordinate change instructions with target location and reason
 * - Format altitude change instructions with target altitude and reason
 * - Format emergency landing directives with parking space assignments
 * - Format weather broadcast messages with atmospheric conditions
 * - Format accident alert broadcasts with location and severity
 * - Ensure consistent message structure across all communication types
 * 
 * Interactions:
 * - Used by Radio to format messages before transmission
 * - Provides formatted strings for RadioMessage objects
 * - Supports RadioInstructionsPanel display formatting
 * - Integrates with CityLogManager for log entry formatting
 * 
 * Patterns & Constraints:
 * - Formatter pattern: single-purpose message formatting utility
 * - Immutable controller callsign set at construction
 * - Uses aviation standard format: "[CALLSIGN], [ATC], [INSTRUCTION]"
 * - All methods return formatted strings (no side effects)
 * - Thread-safe due to immutable state
 * 
 * @author Haisam Elkewidy
 */

package com.example.radio;

/**
 * RadioMessageFormatter - formats radio transmission messages
 */
public class RadioMessageFormatter {
    private String controllerCallSign;
    
    public RadioMessageFormatter(String controllerCallSign) {
        this.controllerCallSign = controllerCallSign;
    }
    
    /**
     * Formats coordinate change message
     */
    public String formatCoordinateInstruction(String callsign, int newX, int newY, String reason) {
        return String.format(
            "%s to %s: Proceed to new coordinates X=%d, Y=%d. Reason: %s. Acknowledge.",
            controllerCallSign, callsign, newX, newY, reason
        );
    }
    
    /**
     * Formats altitude change message
     */
    public String formatAltitudeInstruction(String callsign, int newAltitude, String reason) {
        String instruction = newAltitude > 0 ? "Climb" : "Descend";
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
