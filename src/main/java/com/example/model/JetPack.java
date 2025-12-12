/*
 * JetPack.java
 * Part of Jetpack Air Traffic Controller
 *
 * Represents a jetpack device used in city flights.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.model;

public class JetPack {
    private final String callsign;
    private final String pilotName;
    private boolean available;

    public JetPack(String callsign, String pilotName) {
        this.callsign = callsign;
        this.pilotName = pilotName;
        this.available = true;
    }

    public String getCallsign() {
        return callsign;
    }

    public String getPilotName() {
        return pilotName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("JetPack[callsign=%s, pilot=%s, available=%s]", callsign, pilotName, available);
    }
}
