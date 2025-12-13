/**
 * RadioMessage component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radiomessage functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radiomessage operations
 * - Maintain necessary state for radiomessage functionality
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
 * RadioMessage represents a two-way radio communication message
 */
public class RadioMessage {
    // Originating station or aircraft callsign (e.g., "ATC-001", "NY-JP42")
    private String sender;
    
    // Destination callsign or "ALL" for broadcasts
    private String receiver;
    
    // Message content/payload (e.g., instructions, reports, acknowledgments)
    private String message;
    
    // Unix timestamp (milliseconds) when message was created
    private long timestamp;
    
    // Classification of message purpose and priority
    private MessageType type;
    
    // Flag indicating if recipient has acknowledged receipt
    private boolean acknowledged;
    
    /**
     * Enumeration of radio message types for categorization and routing.
     * Defines the purpose and priority of radio communications.
     */
    public enum MessageType {
        INSTRUCTION,        // ATC commands to aircraft (e.g., "LAND", "HOLD")
        ACKNOWLEDGMENT,     // Pilot responses confirming receipt (e.g., "Roger", "Wilco")
        POSITION_REPORT,    // Aircraft reporting position, altitude, heading
        EMERGENCY,          // Urgent safety messages requiring immediate action
        BROADCAST,          // General information to all aircraft
        WEATHER_INFO,       // Weather updates and advisories
        ACCIDENT_REPORT     // Accident notifications and avoidance instructions
    }
    
    /**
     * Constructs a new RadioMessage with specified details.
     * Automatically captures current timestamp and sets acknowledged to false.
     * 
     * @param sender Originating station or callsign
     * @param receiver Destination callsign or "ALL" for broadcasts
     * @param message Message content/payload
     * @param type Message classification from MessageType enum
     */
    public RadioMessage(String sender, String receiver, String message, MessageType type) {
        // Store message routing information
        this.sender = sender;
        this.receiver = receiver;
        
        // Store message payload
        this.message = message;
        
        // Store message classification
        this.type = type;
        
        // Capture creation timestamp for chronological ordering
        this.timestamp = System.currentTimeMillis();
        
        // New messages start unacknowledged
        this.acknowledged = false;
    }
    
    /** Returns the sender callsign or station identifier */
    public String getSender() {
        return sender;
    }
    
    /** Returns the receiver callsign or "ALL" for broadcasts */
    public String getReceiver() {
        return receiver;
    }
    
    /** Returns the message content/payload */
    public String getMessage() {
        return message;
    }
    
    /** Returns the Unix timestamp (milliseconds) when message was created */
    public long getTimestamp() {
        return timestamp;
    }
    
    /** Returns the message type classification */
    public MessageType getType() {
        return type;
    }
    
    /** Returns true if message has been acknowledged by recipient */
    public boolean isAcknowledged() {
        return acknowledged;
    }
    
    /**
     * Updates the acknowledgment status of this message.
     * Typically set to true when pilot confirms receipt.
     * 
     * @param acknowledged true if message has been acknowledged
     */
    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
    
    /**
     * Returns a formatted string representation of this radio message.
     * Includes timestamp, acknowledgment status, sender, receiver, and message content.
     * Format: [HH:mm:ss] [ACK/PENDING] sender → receiver: message
     * 
     * @return Human-readable radio message transcript entry
     */
    @Override
    public String toString() {
        // Convert Unix timestamp to local time formatted as HH:mm:ss
        String time = java.time.Instant.ofEpochMilli(timestamp)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalTime()
            .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        // Format acknowledgment status as [ACK] or [PENDING]
        String ackStatus = acknowledged ? "[ACK]" : "[PENDING]";
        
        // Combine all elements into formatted transcript line
        return String.format("[%s] %s %s → %s: %s", time, ackStatus, sender, receiver, message);
    }
}
