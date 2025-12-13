/**
 * Represents a bidirectional radio communication message with sender, receiver, and acknowledgment tracking.
 * 
 * Purpose:
 * Models a single radio transmission in the air traffic control system, encapsulating the message content,
 * participants (sender/receiver), type classification (INSTRUCTION, EMERGENCY, etc.), and acknowledgment
 * status. Used for logging, queuing, and tracking radio communications between ATC and jetpacks.
 * 
 * Key Responsibilities:
 * - Store message content and transmission metadata
 * - Identify sender and receiver (ATC, jetpack callsigns)
 * - Classify messages by type (INSTRUCTION, EMERGENCY, BROADCAST, etc.)
 * - Track acknowledgment status for two-way communication
 * - Provide timestamp for chronological message ordering
 * 
 * Interactions:
 * - Created by Radio when transmitting messages
 * - Queued in Radio's message queue for processing
 * - Logged by RadioTransmissionLogger for record keeping
 * - Displayed in RadarTapeWindow and RadioInstructionsPanel
 * - Referenced by CityLogManager for persistent logging
 * 
 * Patterns & Constraints:
 * - Immutable core fields (sender, receiver, message, type, timestamp)
 * - Single mutable field (acknowledged) for lifecycle tracking
 * - MessageType enum defines all valid communication types
 * - Timestamp automatically set at construction time
 * - Value object with comprehensive data for auditing
 * 
 * @author Haisam Elkewidy
 */

package com.example.radio;

/**
 * RadioMessage represents a two-way radio communication message
 */
public class RadioMessage {
    /** Callsign or identifier of the message sender (e.g., "ATC-CONTROL", "ALPHA-01") */
    private String sender;
    
    /** Callsign or identifier of the message receiver (e.g., jetpack callsign, "ALL") */
    private String receiver;
    
    /** The actual message content/text being transmitted */
    private String message;
    
    /** Unix timestamp (milliseconds) when this message was created */
    private long timestamp;
    
    /** Classification of the message (INSTRUCTION, EMERGENCY, BROADCAST, etc.) */
    private MessageType type;
    
    /** Whether this message has been acknowledged by the receiver */
    private boolean acknowledged;
    
    /**
     * Enumeration of valid radio message types.
     * Defines categories for different kinds of radio communications.
     */
    public enum MessageType {
        /** ATC instruction to a jetpack (coordinate/altitude changes) */
        INSTRUCTION,
        /** Acknowledgment response from jetpack to ATC */
        ACKNOWLEDGMENT,
        /** Position report from jetpack to ATC */
        POSITION_REPORT,
        /** Emergency communication requiring immediate attention */
        EMERGENCY,
        /** General broadcast to all aircraft */
        BROADCAST,
        /** Weather information update */
        WEATHER_INFO,
        /** Accident/incident report */
        ACCIDENT_REPORT
    }
    
    /**
     * Constructs a new radio message with specified parameters.
     * Automatically sets timestamp to current system time and marks as unacknowledged.
     * 
     * @param sender the callsign of the message sender
     * @param receiver the callsign of the intended receiver
     * @param message the message content/text
     * @param type the classification type of this message
     */
    public RadioMessage(String sender, String receiver, String message, MessageType type) {
        this.sender = sender;                      // Store sender callsign
        this.receiver = receiver;                  // Store receiver callsign
        this.message = message;                    // Store message content
        this.type = type;                          // Store message type
        this.timestamp = System.currentTimeMillis(); // Record current time
        this.acknowledged = false;                 // Initially not acknowledged
    }
    
    /**
     * Returns the sender's callsign.
     * @return the sender identifier
     */
    public String getSender() {
        return sender;  // Return stored sender
    }
    
    /**
     * Returns the receiver's callsign.
     * @return the receiver identifier
     */
    public String getReceiver() {
        return receiver;  // Return stored receiver
    }
    
    /**
     * Returns the message content.
     * @return the message text
     */
    public String getMessage() {
        return message;  // Return stored message
    }
    
    /**
     * Returns the timestamp when message was created.
     * @return Unix timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;  // Return stored timestamp
    }
    
    /**
     * Returns the message type classification.
     * @return the MessageType enum value
     */
    public MessageType getType() {
        return type;  // Return stored type
    }
    
    /**
     * Checks if message has been acknowledged.
     * @return true if acknowledged, false otherwise
     */
    public boolean isAcknowledged() {
        return acknowledged;  // Return acknowledgment status
    }
    
    /**
     * Sets the acknowledgment status of this message.
     * @param acknowledged true to mark as acknowledged, false otherwise
     */
    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;  // Update acknowledgment flag
    }
    
    /**
     * Returns a formatted string representation of this radio message.
     * Includes timestamp, acknowledgment status, sender, receiver, and message content.
     * Format: [HH:mm:ss] [ACK/PENDING] sender → receiver: message
     * 
     * @return formatted message string for display/logging
     */
    @Override
    public String toString() {
        // Convert timestamp to readable time format (HH:mm:ss)
        String time = java.time.Instant.ofEpochMilli(timestamp)
            .atZone(java.time.ZoneId.systemDefault())  // Use system timezone
            .toLocalTime()                              // Extract time component
            .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")); // Format as HH:mm:ss
        
        // Determine acknowledgment status string
        String ackStatus = acknowledged ? "[ACK]" : "[PENDING]";  // Show ACK or PENDING
        
        // Format and return complete message string
        return String.format("[%s] %s %s → %s: %s", time, ackStatus, sender, receiver, message);
    }
}
