/**
 * FlightEmergencyHandler.java
 * by Haisam Elkewidy
 *
 * This class handles FlightEmergencyHandler functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - radioDestination (Point)
 *   - radioAltitude (Double)
 *   - followingRadioInstruction (boolean)
 *   - callsign (String)
 *   - mapImage (BufferedImage)
 *   - logger (EmergencyLogger)
 *   - radioListener (RadioInstructionListener)
 *
 * Methods:
 *   - FlightEmergencyHandler(callsign)
 *   - FlightEmergencyHandler()
 *   - receiveCoordinateInstruction(newX, newY, reason)
 *   - receiveAltitudeInstruction(currentAltitude, newAltitude, reason)
 *   - receiveEmergencyLandingInstruction(currentX, currentY, parkingSpaces, reason)
 *   - findEmergencyLandingSpot(currentX, currentY, parkingSpaces, reason)
 *   - generateEmergencyDetour(currentX, currentY, destination)
 *   - checkRadioDestinationReached(currentDest, distance, speed)
 *   - checkRadioAltitudeReached(currentAltitude)
 *   - clearInstructions()
 *
 */

package com.example.flight;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.example.parking.ParkingSpace;
import com.example.utility.geometry.GeometryUtils;

/**
 * FlightEmergencyHandler handles radio instructions and emergency procedures for jetpack flights.
 * It manages emergency logging, radio instruction listeners, and water detection for landings.
 */
public class FlightEmergencyHandler {
    private Point radioDestination;
    private Double radioAltitude;
    private boolean followingRadioInstruction;
    private String callsign;
    private BufferedImage mapImage;

    public interface EmergencyLogger {
        void logEmergency(String message);
    }

    public interface RadioInstructionListener {
        void onInstructionReceived(String instruction);
        void onInstructionCompleted(String instruction);
    }

    private EmergencyLogger logger;
    private RadioInstructionListener radioListener;

    public FlightEmergencyHandler() {
        this.callsign = "";
    }

    public FlightEmergencyHandler(String callsign) {
        this.callsign = callsign;
    }

    public void setEmergencyLogger(EmergencyLogger logger) {
        this.logger = logger;
    }

    public void setRadioInstructionListener(RadioInstructionListener listener) {
        this.radioListener = listener;
    }

    /**
     * Sets the map image for water detection during emergency landings
     */
    public void setMapImage(BufferedImage mapImage) {
        this.mapImage = mapImage;
    }

    /**
     * Receives and executes a radio instruction to change coordinates
     */
    public Point receiveCoordinateInstruction(int newX, int newY, String reason) {
        this.radioDestination = new Point(newX, newY);
        this.followingRadioInstruction = true;
        if (logger != null) {
            logger.logEmergency(callsign + " acknowledges radio instruction: " +
                "Proceeding to (" + newX + "," + newY + ") - " + reason);
        }
        if (radioListener != null) {
            radioListener.onInstructionReceived("COORDINATE_CHANGE: " + reason);
        }
        return new Point(newX, newY);
    }

    /**
     * Receives and executes a radio instruction to change altitude
     */
    public double receiveAltitudeInstruction(double currentAltitude, double newAltitude, String reason) {
        this.radioAltitude = newAltitude;
        double altDiff = newAltitude - currentAltitude;
        double adjustedAltitude = currentAltitude + Math.signum(altDiff) * Math.min(Math.abs(altDiff), 5.0);
        this.followingRadioInstruction = true;
        if (radioListener != null) {
            radioListener.onInstructionReceived("ALTITUDE_CHANGE: " + reason);
        }
        return adjustedAltitude;
    }

    /**
     * Receives emergency landing instruction
     */
    public Point receiveEmergencyLandingInstruction(double currentX, double currentY,
                                                   List<ParkingSpace> parkingSpaces, String reason) {
        Point emergencyDest = findEmergencyLandingSpot(currentX, currentY, parkingSpaces, reason);
        if (radioListener != null) {
            radioListener.onInstructionReceived("EMERGENCY_LANDING: " + reason);
        }
        return emergencyDest;
    }

    /**
     * Finds nearest available parking space for emergency landing
     */
    public Point findEmergencyLandingSpot(double currentX, double currentY,
                                         List<ParkingSpace> parkingSpaces, String reason) {
        if (parkingSpaces == null || parkingSpaces.isEmpty()) {
            if (logger != null) {
                logger.logEmergency(callsign + " ⛔ EMERGENCY HALT: " + reason);
            }
            return null;
        }
        Point targetPosition = new Point((int)currentX, (int)currentY);
        if (mapImage != null && isOverWater((int)currentX, (int)currentY)) {
            targetPosition = findClosestLand((int)currentX, (int)currentY);
            if (logger != null) {
                logger.logEmergency(callsign +
                    " ⚠️ OVER WATER - Redirecting to land at (" + targetPosition.x +
                    "," + targetPosition.y + ") before landing");
            }
        }
        ParkingSpace nearestParking = null;
        double minDistance = Double.MAX_VALUE;
        for (ParkingSpace ps : parkingSpaces) {
            if (!ps.isOccupied()) {
                double distance = GeometryUtils.calculateDistance(targetPosition.x, targetPosition.y, ps.getX(), ps.getY());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestParking = ps;
                }
            }
        }
        if (nearestParking != null) {
            if (logger != null) {
                logger.logEmergency(callsign +
                    " 🚨 EMERGENCY LANDING: " + reason +
                    " - Heading to parking at (" + (int)nearestParking.getX() +
                    "," + (int)nearestParking.getY() + ")");
            }
            return GeometryUtils.createPoint(nearestParking.getX(), nearestParking.getY());
        } else {
            if (logger != null) {
                logger.logEmergency(callsign + " ⛔ EMERGENCY LANDING - NO PARKING AVAILABLE - " + reason);
            }
            return null;
        }
    }

    /**
     * Checks if a position is over water
     */
    private boolean isOverWater(int x, int y) {
        if (mapImage == null) {
            return false;
        }
        if (x < 0 || x >= mapImage.getWidth() || y < 0 || y >= mapImage.getHeight()) {
            return true;
        }
        int rgb = mapImage.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (b > r + 20 && b > g + 20) || (r < 100 && g < 150 && b > 100 && b - r > 30);
    }

    /**
     * Finds the closest land point using spiral search
     */
    private Point findClosestLand(int x, int y) {
        if (mapImage == null) {
            return new Point(x, y);
        }
        int maxRadius = Math.max(mapImage.getWidth(), mapImage.getHeight());
        for (int radius = 10; radius < maxRadius; radius += 10) {
            int numPoints = radius * 4;
            for (int i = 0; i < numPoints; i++) {
                double angle = (2 * Math.PI * i) / numPoints;
                int testX = x + (int)(radius * Math.cos(angle));
                int testY = y + (int)(radius * Math.sin(angle));
                if (testX >= 0 && testX < mapImage.getWidth() &&
                    testY >= 0 && testY < mapImage.getHeight() && !isOverWater(testX, testY)) {
                    return new Point(testX, testY);
                }
            }
        }
        return new Point(mapImage.getWidth() / 2, mapImage.getHeight() / 2);
    }

    /**
     * Generates emergency detour waypoints away from collision
     */
    public List<Point> generateEmergencyDetour(double currentX, double currentY, Point destination) {
        List<Point> emergencyWaypoints = new ArrayList<>();
        int offsetX = (int)(Math.random() * 200 - 100);
        int offsetY = (int)(Math.random() * 200 - 100);
        emergencyWaypoints.add(new Point((int)currentX + offsetX, (int)currentY + offsetY));
        emergencyWaypoints.add(new Point(destination.x, destination.y));
        return emergencyWaypoints;
    }

    /**
     * Checks if radio destination has been reached
     */
    public boolean checkRadioDestinationReached(Point currentDest, double distance, double speed) {
        if (radioDestination != null && currentDest.equals(radioDestination) && distance < speed * 2) {
            if (radioListener != null && followingRadioInstruction) {
                radioListener.onInstructionCompleted("Destination reached");
            }
            radioDestination = null;
            if (radioAltitude == null) {
                followingRadioInstruction = false;
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if radio altitude has been reached
     */
    public boolean checkRadioAltitudeReached(double currentAltitude) {
        if (radioAltitude != null) {
            double altDiff = radioAltitude - currentAltitude;
            if (Math.abs(altDiff) <= 1.0) {
                if (radioListener != null && followingRadioInstruction) {
                    radioListener.onInstructionCompleted("Altitude " + radioAltitude.intValue() + " reached");
                }
                radioAltitude = null;
                if (radioDestination == null) {
                    followingRadioInstruction = false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all radio instructions and emergency state
     */
    public void clearInstructions() {
        radioDestination = null;
        radioAltitude = null;
        followingRadioInstruction = false;
    }

    // Getters
    public Point getRadioDestination() { return radioDestination; }
    public Double getRadioAltitude() { return radioAltitude; }
    public boolean isFollowingRadioInstruction() { return followingRadioInstruction; }

}
