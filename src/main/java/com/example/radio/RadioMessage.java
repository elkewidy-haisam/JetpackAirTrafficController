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
