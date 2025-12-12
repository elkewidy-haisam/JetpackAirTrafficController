/**
 * RadioCommandExecutor.java
 * by Haisam Elkewidy
 *
 * This class handles RadioCommandExecutor functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - RadioCommandExecutor()
 *   - executeCommand(command, radio)
 *
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
