/**
 * Represents a structured radio transmission between ATC and jetpack pilots.
 * 
 * Purpose:
 * Models a single radio communication message with sender/receiver identification, content payload,
 * and message type classification. Provides a standardized data structure for radio communications
 * throughout the system, enabling logging, replay, and display in operator interfaces. Supports
 * categorization of messages for filtering and prioritization.
 * 
 * Key Responsibilities:
 * - Store sender and receiver identifiers (callsigns, controller IDs)
 * - Encapsulate message content as free-form text
 * - Classify message type (INSTRUCTION, STATUS, ALERT, GENERAL)
 * - Provide immutable message records for audit compliance
 * - Support formatted string output for logging and display
 * 
 * Interactions:
 * - Created by Radio subsystem during transmission operations
 * - Logged via RadioTransmissionLogger for compliance records
 * - Formatted by RadioMessageFormatter for display consistency
 * - Displayed in RadarTapeWindow for operator communication history
 * - Used in RadioCommandExecutor to parse and execute instructions
 * - Referenced in session exports for communication playback
 * 
 * Patterns & Constraints:
 * - Immutable value object ensures message integrity after creation
 * - MessageType enum provides fixed set of communication categories
 * - Lightweight structure suitable for high-frequency message generation
 * - No validation logic; consumers responsible for content interpretation
 * - Thread-safe by virtue of immutability
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class RadioMessage {
    // Identifier of message sender (callsign or controller ID)
    private final String sender;
    // Identifier of message receiver (callsign or controller ID)
    private final String receiver;
    // Text content of the radio message
    private final String content;
    // Classification type of this message
    private final MessageType type;

    /**
     * Enumeration of message types for categorization.
     * INSTRUCTION: Commands and directives (e.g., clearances, routing)
     * STATUS: Informational updates (e.g., position reports)
     * ALERT: Urgent notifications (e.g., weather warnings, emergencies)
     * GENERAL: General communications (e.g., acknowledgments)
     */
    public enum MessageType {
        INSTRUCTION, STATUS, ALERT, GENERAL
    }

    /**
     * Constructs a new RadioMessage with specified properties.
     * 
     * @param sender Identifier of message sender
     * @param receiver Identifier of message receiver
     * @param content Text content of message
     * @param type Classification type of message
     */
    public RadioMessage(String sender, String receiver, String content, MessageType type) {
        // Store sender identifier for message routing
        this.sender = sender;
        // Store receiver identifier for message routing
        this.receiver = receiver;
        // Store message text content
        this.content = content;
        // Store message classification type
        this.type = type;
    }

    /**
     * Returns the identifier of message sender.
     * 
     * @return Sender identifier string
     */
    public String getSender() {
        // Return stored sender identifier
        return sender;
    }

    /**
     * Returns the identifier of message receiver.
     * 
     * @return Receiver identifier string
     */
    public String getReceiver() {
        // Return stored receiver identifier
        return receiver;
    }

    /**
     * Returns the text content of the message.
     * 
     * @return Message content string
     */
    public String getContent() {
        // Return stored message content
        return content;
    }

    /**
     * Returns the classification type of the message.
     * 
     * @return MessageType enum value
     */
    public MessageType getType() {
        // Return stored message type classification
        return type;
    }

    /**
     * Returns formatted string representation of this radio message.
     * 
     * @return String with type, sender, receiver, and content
     */
    @Override
    public String toString() {
        // Format message details as readable string for logging/display
        return String.format("RadioMessage[type=%s, from=%s, to=%s, content=%s]", type, sender, receiver, content);
    }
}
