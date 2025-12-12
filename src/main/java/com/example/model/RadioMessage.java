/*
 * RadioMessage.java
 * Part of Jetpack Air Traffic Controller
 *
 * Represents a radio message sent between jetpack flights and control.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.model;

public class RadioMessage {
    private final String sender;
    private final String receiver;
    private final String content;
    private final MessageType type;

    public enum MessageType {
        INSTRUCTION, STATUS, ALERT, GENERAL
    }

    public RadioMessage(String sender, String receiver, String content, MessageType type) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("RadioMessage[type=%s, from=%s, to=%s, content=%s]", type, sender, receiver, content);
    }
}
