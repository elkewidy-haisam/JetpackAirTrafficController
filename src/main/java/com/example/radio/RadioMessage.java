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
    private String sender;
    private String receiver;
    private String message;
    private long timestamp;
    private MessageType type;
    private boolean acknowledged;
    
    public enum MessageType {
        INSTRUCTION,
        ACKNOWLEDGMENT,
        POSITION_REPORT,
        EMERGENCY,
        BROADCAST,
        WEATHER_INFO,
        ACCIDENT_REPORT
    }
    
    public RadioMessage(String sender, String receiver, String message, MessageType type) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
        this.acknowledged = false;
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getReceiver() {
        return receiver;
    }
    
    public String getMessage() {
        return message;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public MessageType getType() {
        return type;
    }
    
    public boolean isAcknowledged() {
        return acknowledged;
    }
    
    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
    
    @Override
    public String toString() {
        String time = java.time.Instant.ofEpochMilli(timestamp)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalTime()
            .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        String ackStatus = acknowledged ? "[ACK]" : "[PENDING]";
        return String.format("[%s] %s %s → %s: %s", time, ackStatus, sender, receiver, message);
    }
}
