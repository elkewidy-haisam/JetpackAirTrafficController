/*
 * RadioCommandExecutor.java
 * Part of Jetpack Air Traffic Controller
 *
 * Executes radio commands for jetpack flights.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.model;

public class RadioCommandExecutor {
    public RadioCommandExecutor() {
        // Default constructor
    }

    public void executeCommand(String command, Radio radio) {
        if (radio != null && radio.isEnabled()) {
            radio.transmit("Executing command: " + command);
        }
    }
}
