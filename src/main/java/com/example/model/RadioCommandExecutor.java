/**
 * Duplicate/alias of com.example.radio.RadioCommandExecutor for backwards compatibility.
 * 
 * Purpose:
 * Model package version of RadioCommandExecutor. Provides identical radio command execution via reflection.
 * Maintained for backwards compatibility with code that imports from model package instead of radio package.
 * May be deprecated and consolidated with radio package version in future refactoring.
 * 
 * @author Haisam Elkewidy
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
