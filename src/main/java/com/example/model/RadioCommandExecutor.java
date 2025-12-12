/**
 * RadioCommandExecutor.java
 * by Haisam Elkewidy
 *
 * Executes radio commands for jetpack flights.
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
