/**
 * JetPackFlight.java
 * by Haisam Elkewidy
 *
 * Represents a jetpack flight in the city simulation.
 */

package com.example.model;

public class JetPackFlight {
    private final String jetpackId;
    private double x;
    private double y;
    private boolean active;

    public JetPackFlight(String jetpackId, double x, double y) {
        this.jetpackId = jetpackId;
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public String getJetpackId() {
        return jetpackId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("JetPackFlight[id=%s, x=%.2f, y=%.2f, active=%s]", jetpackId, x, y, active);
    }
}
