/**
 * Central radio communication system for broadcasting instructions, emergencies, and weather to jetpacks.
 * 
 * Purpose:
 * Serves as the primary communication hub between air traffic control and all jetpacks in the airspace.
 * Transmits coordinate changes, altitude instructions, emergency directives, weather broadcasts, and
 * accident alerts. Uses reflection to execute commands on registered flight objects without tight coupling.
 * 
 * Key Responsibilities:
 * - Broadcast messages to all jetpacks or specific callsigns
 * - Issue coordinate and altitude change instructions
 * - Transmit emergency landing directives
 * - Broadcast weather updates and accident reports
 * - Queue and manage radio messages with acknowledgment tracking
 * - Log all radio transmissions for auditing and replay
 * 
 * Interactions:
 * - Uses RadioCommandExecutor to invoke commands on flight objects via reflection
 * - Formats messages through RadioMessageFormatter for consistent presentation
 * - Logs transmissions via RadioTransmissionLogger for record keeping
 * - Creates RadioMessage objects for each communication
 * - Broadcasts to JetPackFlight objects registered with the command executor
 * - Integrates with AccidentReporter for accident notifications
 * - Used by Weather system to broadcast condition updates
 * 
 * Patterns & Constraints:
 * - Delegation pattern: delegates to specialized components (executor, formatter, logger)
 * - Command pattern: encapsulates instructions as executable commands
 * - Observer pattern: broadcasts to multiple registered listeners (jetpacks)
 * - Uses reflection via ReflectionHelper for loose coupling
 * - Emergency frequency 121.5 MHz as default
 * - Thread-safety not guaranteed - external synchronization required
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
    
    /** frequency */
    private String frequency;
    /** messageQueue */
    private List<RadioMessage> messageQueue;
    /** random */
    private Random random;
    
    // Extracted components
    private RadioCommandExecutor commandExecutor;
    /** messageFormatter */
    private RadioMessageFormatter messageFormatter;
    /** transmissionLogger */
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
        this.frequency = "121.5"; // Set to emergency frequency 121.5 MHz
        this.messageQueue = new ArrayList<>();  // Initialize empty message queue
        this.random = new Random();  // Create random number generator for acknowledgments
        this.commandExecutor = new RadioCommandExecutor();  // Create command executor for flight commands
        this.messageFormatter = new RadioMessageFormatter("ATC-CONTROL");  // Create formatter with default ATC callsign
        this.transmissionLogger = new RadioTransmissionLogger();  // Create logger for transmission history
    }
    
    /**
     * Parameterized constructor
     * 
     * @param frequency Radio frequency
     * @param controllerCallSign Call sign for the air traffic controller
     */
    public Radio(String frequency, String controllerCallSign) {
        this.frequency = frequency;  // Set specified radio frequency
        this.messageQueue = new ArrayList<>();  // Initialize empty message queue
        this.random = new Random();  // Create random number generator for acknowledgments
        this.commandExecutor = new RadioCommandExecutor();  // Create command executor for flight commands
        this.messageFormatter = new RadioMessageFormatter(controllerCallSign);  // Create formatter with specified callsign
        this.transmissionLogger = new RadioTransmissionLogger();  // Create logger for transmission history
    }
    
    /**
     * Registers a flight for radio command execution
     */
    public void registerFlight(String callsign, Object flight) {
        commandExecutor.registerFlight(callsign, flight);  // Delegate registration to command executor
    }
    
    /**
     * Registers a flight state for radio command execution
     */
    public void registerFlightState(String callsign, Object flightState) {
        commandExecutor.registerFlightState(callsign, flightState);  // Delegate flight state registration to executor
    }
    
    /**
     * Unregisters a flight
     */
    public void unregisterFlight(String callsign) {
        commandExecutor.unregisterFlight(callsign);  // Delegate unregistration to command executor
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
        String message = messageFormatter.formatCoordinateInstruction(  // Format coordinate instruction message
            jetpack.getCallsign(), newX, newY, reason  // Pass callsign, coordinates, and reason
        );
        
        transmissionLogger.transmitAndLog(message);  // Log the transmission for record keeping
        commandExecutor.executeCoordinateInstruction(jetpack.getCallsign(), newX, newY, reason);  // Execute command on flight object
        
        // Radio transmission sent - jetpack should receive and acknowledge
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
        giveNewCoordinates(jetpack, newX, newY, "Route adjustment");  // Call overloaded method with default reason
    }
    
    /**
     * Communicates to a jetpack new changes to their altitude depending on situation
     * 
     * @param jetpack Target jetpack
     * @param newAltitude New altitude in feet
     * @param reason Reason for altitude change
     */
    public void giveNewAltitude(JetPack jetpack, int newAltitude, String reason) {
        String message = messageFormatter.formatAltitudeInstruction(  // Format altitude instruction message
            jetpack.getCallsign(), newAltitude, reason  // Pass callsign, altitude, and reason
        );
        
        transmissionLogger.transmitAndLog(message);  // Log the transmission for record keeping
        commandExecutor.executeAltitudeInstruction(jetpack.getCallsign(), (double)newAltitude, reason);  // Execute command on flight object (convert to double)
        
        // Radio transmission sent - jetpack should receive and adjust altitude
    }
    
    /**
     * Communicates to a jetpack new changes to their altitude depending on situation
     * (Overloaded method without reason)
     * 
     * @param jetpack Target jetpack
     * @param newAltitude New altitude in feet
     */
    public void giveNewAltitude(JetPack jetpack, int newAltitude) {
        giveNewAltitude(jetpack, newAltitude, "Altitude adjustment");  // Call overloaded method with default reason
    }
    
    /**
     * Clears a jetpack for takeoff
     * 
     * @param jetpack Target jetpack
     * @param runway Takeoff location
     */
    public void clearForTakeoff(JetPack jetpack, String runway) {
        String message = messageFormatter.formatTakeoffClearance(jetpack.getCallsign(), runway);  // Format takeoff clearance message
        transmissionLogger.transmitAndLog(message);  // Log the takeoff clearance
        // Takeoff clearance sent - jetpack may proceed with departure
    }
    
    /**
     * Clears a jetpack for landing
     * 
     * @param jetpack Target jetpack
     * @param landingZone Landing zone identifier
     */
    public void clearForLanding(JetPack jetpack, String landingZone) {
        String message = messageFormatter.formatLandingClearance(jetpack.getCallsign(), landingZone);  // Format landing clearance message
        transmissionLogger.transmitAndLog(message);  // Log the landing clearance
        // Landing clearance sent - jetpack may proceed to land
    }
    
    /**
     * Issues an emergency directive to a jetpack
     * 
     * @param jetpack Target jetpack
     * @param directive Emergency instruction
     */
    public void issueEmergencyDirective(JetPack jetpack, String directive) {
        String message = messageFormatter.formatEmergencyDirective(jetpack.getCallsign(), directive);  // Format emergency directive message
        transmissionLogger.transmitAndLog(message);  // Log the emergency directive
        
        // Execute emergency landing if directive contains "landing"
        if (directive.toLowerCase().contains("landing")) {  // Check if emergency directive involves landing
            commandExecutor.executeEmergencyLandingInstruction(jetpack.getCallsign(), directive);  // Execute emergency landing command on flight object
        }
        
        // Emergency directive sent - immediate pilot action required
    }
    
    /**
     * Broadcasts a general message to all jetpacks
     * 
     * @param message Message to broadcast
     */
    public void broadcastToAll(String message) {
        String broadcastMessage = messageFormatter.formatBroadcast(message);  // Format as broadcast message
        transmissionLogger.transmitAndLog(broadcastMessage);  // Log the broadcast transmission
        // Broadcast sent to all jetpacks on frequency
    }
    
    /**
     * Provides weather information to a jetpack
     * 
     * @param jetpack Target jetpack
     * @param weatherInfo Weather information
     */
    public void provideWeatherInfo(JetPack jetpack, String weatherInfo) {
        String message = messageFormatter.formatWeatherInfo(jetpack.getCallsign(), weatherInfo);  // Format weather information message
        transmissionLogger.transmitAndLog(message);  // Log the weather transmission
        // Weather info sent to jetpack for pilot awareness
    }
    
    /**
     * Requests position report from a jetpack
     * 
     * @param jetpack Target jetpack
     */
    public void requestPositionReport(JetPack jetpack) {
        String message = messageFormatter.formatPositionRequest(jetpack.getCallsign());  // Format position report request
        transmissionLogger.transmitAndLog(message);  // Log the position request
        // Position request sent - pilot should respond with current position
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
        String message = messageFormatter.formatAccidentReport(accidentID, x, y, type, severity, description);  // Format accident report message with all details
        transmissionLogger.transmitAndLog(message);  // Log the accident report transmission
        // Accident reported to all aircraft - pilots should avoid area
    }
    
    /**
     * Clears an accident report over the radio
     * 
     * @param accidentID ID of the accident that has been cleared
     * @param x X coordinate of accident location
     * @param y Y coordinate of accident location
     */
    public void clearAccidentReport(String accidentID, int x, int y) {
        String message = messageFormatter.formatAccidentCleared(accidentID, x, y);  // Format accident cleared message
        transmissionLogger.transmitAndLog(message);  // Log the accident cleared transmission
        // Accident cleared - area is now safe for flight operations
    }
    
    /**
     * Gets the transmission log
     */
    public List<String> getTransmissionLog() {
        return transmissionLogger.getTransmissionLog();  // Retrieve complete transmission history from logger
    }
    
    /**
     * Clears the transmission log
     */
    public void clearTransmissionLog() {
        transmissionLogger.clearTransmissionLog();  // Clear all logged transmissions
        // Transmission log cleared - history reset
    }
    
    /**
     * Prints the transmission log
     */
    public void printLog() {
        transmissionLogger.printLog();  // Print transmission log to console
    }
    
    // Getters and Setters
    
    public String getFrequency() {
        return frequency;  // Return current radio frequency
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;  // Update radio frequency
        // Frequency changed - will affect all future transmissions
    }
    
    public boolean isTransmitting() {
        return transmissionLogger.isTransmitting();  // Check if radio is currently transmitting
    }
    
    public String getControllerCallSign() {
        return messageFormatter.getControllerCallSign();  // Get ATC callsign from formatter
    }
    
    public void setControllerCallSign(String controllerCallSign) {
        messageFormatter.setControllerCallSign(controllerCallSign);  // Update ATC callsign in formatter
    }
    
    @Override
    public String toString() {
        return "Radio{" +  // Build string representation
                "frequency='" + frequency + '\'' +  // Include frequency
                ", isTransmitting=" + transmissionLogger.isTransmitting() +  // Include transmission status
                ", controllerCallSign='" + messageFormatter.getControllerCallSign() + '\'' +  // Include callsign
                ", transmissionsLogged=" + transmissionLogger.getTransmissionCount() +  // Include log count
                '}';  // Close string
    }
    
    /**
     * Generate a random pilot acknowledgment
     */
    private String generateAcknowledgment() {
        return ACKNOWLEDGMENTS[random.nextInt(ACKNOWLEDGMENTS.length)];  // Select random acknowledgment from array
    }
    
    /**
     * Simulate pilot acknowledgment to a message
     */
    public String getPilotAcknowledgment(JetPack jetpack, String instruction) {
        String ack = generateAcknowledgment();  // Generate random acknowledgment phrase
        String message = messageFormatter.formatPilotAcknowledgment(jetpack.getCallsign(), ack);  // Format pilot's acknowledgment message
        transmissionLogger.logTransmission(message);  // Log the pilot's response
        return message;  // Return formatted acknowledgment
    }
    
    /**
     * Send a two-way radio message
     */
    public RadioMessage sendMessage(String sender, String receiver, String message, RadioMessage.MessageType type) {
        RadioMessage radioMsg = new RadioMessage(sender, receiver, message, type);  // Create radio message object
        messageQueue.add(radioMsg);  // Add message to queue
        transmissionLogger.logTransmission(message);  // Log the message transmission
        
        // Simulate pilot acknowledgment after a delay
        if (type == RadioMessage.MessageType.INSTRUCTION || type == RadioMessage.MessageType.EMERGENCY) {  // Check if message requires acknowledgment
            new Thread(() -> {  // Start async thread for delayed acknowledgment
                try {
                    Thread.sleep(500 + random.nextInt(1000)); // Random delay 0.5-1.5 seconds (simulates pilot thinking time)
                    String ack = generateAcknowledgment();  // Generate acknowledgment phrase
                    String ackMessage = String.format("%s, %s, %s", receiver, ack, sender);  // Format acknowledgment message
                    RadioMessage ackMsg = new RadioMessage(receiver, sender, ackMessage, RadioMessage.MessageType.ACKNOWLEDGMENT);  // Create acknowledgment message
                    messageQueue.add(ackMsg);  // Add acknowledgment to queue
                    radioMsg.setAcknowledged(true);  // Mark original message as acknowledged
                    transmissionLogger.logTransmission(ackMessage);  // Log acknowledgment
                    // Pilot acknowledged - message received and understood
                } catch (InterruptedException e) {  // Handle thread interruption
                    Thread.currentThread().interrupt();  // Restore interrupt status
                }
            }).start();  // Start the acknowledgment thread
        }
        
        return radioMsg;  // Return the sent message
    }
    
    /**
     * Get all radio messages
     */
    public List<RadioMessage> getMessageQueue() {
        return new ArrayList<>(messageQueue);  // Return copy of message queue
    }
    
    /**
     * Get unacknowledged messages
     */
    public List<RadioMessage> getUnacknowledgedMessages() {
        List<RadioMessage> unacked = new ArrayList<>();  // Create list for unacknowledged messages
        for (RadioMessage msg : messageQueue) {  // Iterate through all messages
            if (!msg.isAcknowledged() && msg.getType() != RadioMessage.MessageType.ACKNOWLEDGMENT) {  // Check if message is unacknowledged and not itself an acknowledgment
                unacked.add(msg);  // Add to unacknowledged list
            }
        }
        return unacked;  // Return list of unacknowledged messages
    }
    
    /**
     * Clear message queue
     */
    public void clearMessageQueue() {
        messageQueue.clear();  // Remove all messages from queue
    }
    
    /**
     * Switch to emergency frequency
     */
    public void switchToEmergencyFrequency() {
        setFrequency("121.5");  // Switch to international emergency frequency 121.5 MHz
        // Switched to emergency frequency - all aircraft monitor this channel
    }
    
    /**
     * Generate realistic tower handoff message
     */
    public void towerHandoff(JetPack jetpack, String departureController, String arrivalController) {
        String message = messageFormatter.formatHandoffMessage(jetpack.getCallsign(), arrivalController);  // Format handoff instruction message
        transmissionLogger.transmitAndLog(message);  // Transmit and log handoff message
        // Handoff initiated - jetpack transferring to new controller
        
        // Pilot acknowledges handoff
        String ack = messageFormatter.formatHandoffAcknowledgment(  // Format pilot's handoff acknowledgment
            jetpack.getCallsign(), arrivalController, generateAcknowledgment()  // Include callsign, new controller, and acknowledgment phrase
        );
        transmissionLogger.logTransmission(ack);  // Log pilot's acknowledgment
        // Pilot acknowledged handoff - now in contact with arrival controller
    }
}

