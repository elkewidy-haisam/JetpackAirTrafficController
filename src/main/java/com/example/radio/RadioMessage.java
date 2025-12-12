/**
 * RadioMessage.java
 * by Haisam Elkewidy
 *
 * This class handles RadioMessage functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - sender (String)
 *   - receiver (String)
 *   - message (String)
 *   - timestamp (long)
 *   - type (MessageType)
 *   - acknowledged (boolean)
 *
 * Methods:
 *   - RadioMessage(sender, receiver, message, type)
 *   - toString()
 *
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
