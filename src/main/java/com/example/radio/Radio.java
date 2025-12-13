/**
 * Radio component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radio functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radio operations
 * - Maintain necessary state for radio functionality
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.jetpack.JetPack;

/**
 * Radio.java
 * by Haisam Elkewidy
 * 
 * The radio class is designed to offer instructions for takeoff and landing
 * from one point to another within the locale. The radio class does so by
 * transmitting messages to the jetpack pilot, which the pilot should receive.
 * 
 * Known methods:
 * - giveNewCoordinates() - communicates to a jetpack new coordinates to take to ensure safe flight
 * - giveNewAltitudes() - communicates to a jetpack new changes to their altitude depending on situation
 */
public class Radio {
    
    private String frequency;
    private List<RadioMessage> messageQueue;
    private Random random;
    
    // Extracted components
    private RadioCommandExecutor commandExecutor;
    private RadioMessageFormatter messageFormatter;
    private RadioTransmissionLogger transmissionLogger;
    
    // Standard pilot acknowledgment phrases
    private static final String[] ACKNOWLEDGMENTS = {
        "Roger",
        "Wilco",
        "Affirmative",
        "Copy that",
        "Understood"
    };
    
    /**
     * Default constructor
     */
    public Radio() {
        this.frequency = "121.5"; // Emergency frequency
        this.messageQueue = new ArrayList<>();
        this.random = new Random();
        this.commandExecutor = new RadioCommandExecutor();
        this.messageFormatter = new RadioMessageFormatter("ATC-CONTROL");
        this.transmissionLogger = new RadioTransmissionLogger();
    }
    
    /**
     * Parameterized constructor
     * 
     * @param frequency Radio frequency
     * @param controllerCallSign Call sign for the air traffic controller
     */
    public Radio(String frequency, String controllerCallSign) {
        this.frequency = frequency;
        this.messageQueue = new ArrayList<>();
        this.random = new Random();
        this.commandExecutor = new RadioCommandExecutor();
        this.messageFormatter = new RadioMessageFormatter(controllerCallSign);
        this.transmissionLogger = new RadioTransmissionLogger();
    }
    
    /**
     * Registers a flight for radio command execution
     */
    public void registerFlight(String callsign, Object flight) {
        commandExecutor.registerFlight(callsign, flight);
    }
    
    /**
     * Registers a flight state for radio command execution
     */
    public void registerFlightState(String callsign, Object flightState) {
        commandExecutor.registerFlightState(callsign, flightState);
    }
    
    /**
     * Unregisters a flight
     */
    public void unregisterFlight(String callsign) {
        commandExecutor.unregisterFlight(callsign);
    }
    
    /**
     * Communicates to a jetpack new coordinates to take to ensure safe flight
     * 
     * @param jetpack Target jetpack
     * @param newX New X coordinate
     * @param newY New Y coordinate
     * @param reason Reason for coordinate change
     */
    public void giveNewCoordinates(JetPack jetpack, int newX, int newY, String reason) {
        String message = messageFormatter.formatCoordinateInstruction(
            jetpack.getCallsign(), newX, newY, reason
        );
        
        transmissionLogger.transmitAndLog(message);
        commandExecutor.executeCoordinateInstruction(jetpack.getCallsign(), newX, newY, reason);
        
        // Radio transmission sent
    }
    
    /**
     * Communicates to a jetpack new coordinates to take to ensure safe flight
     * (Overloaded method without reason)
     * 
     * @param jetpack Target jetpack
     * @param newX New X coordinate
     * @param newY New Y coordinate
     */
    public void giveNewCoordinates(JetPack jetpack, int newX, int newY) {
        giveNewCoordinates(jetpack, newX, newY, "Route adjustment");
    }
    
    /**
     * Communicates to a jetpack new changes to their altitude depending on situation
     * 
     * @param jetpack Target jetpack
     * @param newAltitude New altitude in feet
     * @param reason Reason for altitude change
     */
    public void giveNewAltitude(JetPack jetpack, int newAltitude, String reason) {
        String message = messageFormatter.formatAltitudeInstruction(
            jetpack.getCallsign(), newAltitude, reason
        );
        
        transmissionLogger.transmitAndLog(message);
        commandExecutor.executeAltitudeInstruction(jetpack.getCallsign(), (double)newAltitude, reason);
        
        // Radio transmission sent
    }
    
    /**
     * Communicates to a jetpack new changes to their altitude depending on situation
     * (Overloaded method without reason)
     * 
     * @param jetpack Target jetpack
     * @param newAltitude New altitude in feet
     */
    public void giveNewAltitude(JetPack jetpack, int newAltitude) {
        giveNewAltitude(jetpack, newAltitude, "Altitude adjustment");
    }
    
    /**
     * Clears a jetpack for takeoff
     * 
     * @param jetpack Target jetpack
     * @param runway Takeoff location
     */
    public void clearForTakeoff(JetPack jetpack, String runway) {
        String message = messageFormatter.formatTakeoffClearance(jetpack.getCallsign(), runway);
        transmissionLogger.transmitAndLog(message);
        // Takeoff clearance sent
    }
    
    /**
     * Clears a jetpack for landing
     * 
     * @param jetpack Target jetpack
     * @param landingZone Landing zone identifier
     */
    public void clearForLanding(JetPack jetpack, String landingZone) {
        String message = messageFormatter.formatLandingClearance(jetpack.getCallsign(), landingZone);
        transmissionLogger.transmitAndLog(message);
        // Landing clearance sent
    }
    
    /**
     * Issues an emergency directive to a jetpack
     * 
     * @param jetpack Target jetpack
     * @param directive Emergency instruction
     */
    public void issueEmergencyDirective(JetPack jetpack, String directive) {
        String message = messageFormatter.formatEmergencyDirective(jetpack.getCallsign(), directive);
        transmissionLogger.transmitAndLog(message);
        
        // Execute emergency landing if directive contains "landing"
        if (directive.toLowerCase().contains("landing")) {
            commandExecutor.executeEmergencyLandingInstruction(jetpack.getCallsign(), directive);
        }
        
        // Emergency directive sent
    }
    
    /**
     * Broadcasts a general message to all jetpacks
     * 
     * @param message Message to broadcast
     */
    public void broadcastToAll(String message) {
        String broadcastMessage = messageFormatter.formatBroadcast(message);
        transmissionLogger.transmitAndLog(broadcastMessage);
        // Broadcast sent
    }
    
    /**
     * Provides weather information to a jetpack
     * 
     * @param jetpack Target jetpack
     * @param weatherInfo Weather information
     */
    public void provideWeatherInfo(JetPack jetpack, String weatherInfo) {
        String message = messageFormatter.formatWeatherInfo(jetpack.getCallsign(), weatherInfo);
        transmissionLogger.transmitAndLog(message);
        // Weather info sent
    }
    
    /**
     * Requests position report from a jetpack
     * 
     * @param jetpack Target jetpack
     */
    public void requestPositionReport(JetPack jetpack) {
        String message = messageFormatter.formatPositionRequest(jetpack.getCallsign());
        transmissionLogger.transmitAndLog(message);
        // Position request sent
    }
    
    /**
     * Reports an accident over the radio
     * 
     * @param accidentID Unique identifier for the accident
     * @param x X coordinate of accident location
     * @param y Y coordinate of accident location
     * @param type Type of accident
     * @param severity Severity level
     * @param description Description of the accident
     */
    public void reportAccident(String accidentID, int x, int y, String type, String severity, String description) {
        String message = messageFormatter.formatAccidentReport(accidentID, x, y, type, severity, description);
        transmissionLogger.transmitAndLog(message);
        // Accident reported
    }
    
    /**
     * Clears an accident report over the radio
     * 
     * @param accidentID ID of the accident that has been cleared
     * @param x X coordinate of accident location
     * @param y Y coordinate of accident location
     */
    public void clearAccidentReport(String accidentID, int x, int y) {
        String message = messageFormatter.formatAccidentCleared(accidentID, x, y);
        transmissionLogger.transmitAndLog(message);
        // Accident cleared
    }
    
    /**
     * Gets the transmission log
     */
    public List<String> getTransmissionLog() {
        return transmissionLogger.getTransmissionLog();
    }
    
    /**
     * Clears the transmission log
     */
    public void clearTransmissionLog() {
        transmissionLogger.clearTransmissionLog();
        // Transmission log cleared
    }
    
    /**
     * Prints the transmission log
     */
    public void printLog() {
        transmissionLogger.printLog();
    }
    
    // Getters and Setters
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
        // Frequency changed
    }
    
    public boolean isTransmitting() {
        return transmissionLogger.isTransmitting();
    }
    
    public String getControllerCallSign() {
        return messageFormatter.getControllerCallSign();
    }
    
    public void setControllerCallSign(String controllerCallSign) {
        messageFormatter.setControllerCallSign(controllerCallSign);
    }
    
    @Override
    public String toString() {
        return "Radio{" +
                "frequency='" + frequency + '\'' +
                ", isTransmitting=" + transmissionLogger.isTransmitting() +
                ", controllerCallSign='" + messageFormatter.getControllerCallSign() + '\'' +
                ", transmissionsLogged=" + transmissionLogger.getTransmissionCount() +
                '}';
    }
    
    /**
     * Generate a random pilot acknowledgment
     */
    private String generateAcknowledgment() {
        return ACKNOWLEDGMENTS[random.nextInt(ACKNOWLEDGMENTS.length)];
    }
    
    /**
     * Simulate pilot acknowledgment to a message
     */
    public String getPilotAcknowledgment(JetPack jetpack, String instruction) {
        String ack = generateAcknowledgment();
        String message = messageFormatter.formatPilotAcknowledgment(jetpack.getCallsign(), ack);
        transmissionLogger.logTransmission(message);
        return message;
    }
    
    /**
     * Send a two-way radio message
     */
    public RadioMessage sendMessage(String sender, String receiver, String message, RadioMessage.MessageType type) {
        RadioMessage radioMsg = new RadioMessage(sender, receiver, message, type);
        messageQueue.add(radioMsg);
        transmissionLogger.logTransmission(message);
        
        // Simulate pilot acknowledgment after a delay
        if (type == RadioMessage.MessageType.INSTRUCTION || type == RadioMessage.MessageType.EMERGENCY) {
            new Thread(() -> {
                try {
                    Thread.sleep(500 + random.nextInt(1000)); // 0.5-1.5 seconds
                    String ack = generateAcknowledgment();
                    String ackMessage = String.format("%s, %s, %s", receiver, ack, sender);
                    RadioMessage ackMsg = new RadioMessage(receiver, sender, ackMessage, RadioMessage.MessageType.ACKNOWLEDGMENT);
                    messageQueue.add(ackMsg);
                    radioMsg.setAcknowledged(true);
                    transmissionLogger.logTransmission(ackMessage);
                    // Pilot acknowledged
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        
        return radioMsg;
    }
    
    /**
     * Get all radio messages
     */
    public List<RadioMessage> getMessageQueue() {
        return new ArrayList<>(messageQueue);
    }
    
    /**
     * Get unacknowledged messages
     */
    public List<RadioMessage> getUnacknowledgedMessages() {
        List<RadioMessage> unacked = new ArrayList<>();
        for (RadioMessage msg : messageQueue) {
            if (!msg.isAcknowledged() && msg.getType() != RadioMessage.MessageType.ACKNOWLEDGMENT) {
                unacked.add(msg);
            }
        }
        return unacked;
    }
    
    /**
     * Clear message queue
     */
    public void clearMessageQueue() {
        messageQueue.clear();
    }
    
    /**
     * Switch to emergency frequency
     */
    public void switchToEmergencyFrequency() {
        setFrequency("121.5");
        // Switched to emergency frequency
    }
    
    /**
     * Generate realistic tower handoff message
     */
    public void towerHandoff(JetPack jetpack, String departureController, String arrivalController) {
        String message = messageFormatter.formatHandoffMessage(jetpack.getCallsign(), arrivalController);
        transmissionLogger.transmitAndLog(message);
        // Handoff initiated
        
        // Pilot acknowledges handoff
        String ack = messageFormatter.formatHandoffAcknowledgment(
            jetpack.getCallsign(), arrivalController, generateAcknowledgment()
        );
        transmissionLogger.logTransmission(ack);
        // Pilot acknowledged handoff
    }
}

