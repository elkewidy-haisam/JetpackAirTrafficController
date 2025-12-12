/**
 * SessionManager.java
 * by Haisam Elkewidy
 *
 * This class handles SessionManager functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - SessionManager()
 *   - saveSession(city, flights, weather)
 *   - exportRadarLog(radarLog)
 *   - exportAccidentReport(accidents)
 *
 */

package com.example.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.city.City;
import com.example.flight.JetPackFlight;
import com.example.parking.ParkingSpace;
import com.example.weather.Weather;

public class SessionManager {
    private static final String SESSIONS_DIR = System.getProperty("user.home") + File.separator + ".jetpack" + File.separator + "sessions";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public SessionManager() {
        ensureSessionsDirectory();
    }

    /**
     * Ensure the sessions directory exists
     */
    private void ensureSessionsDirectory() {
        File dir = new File(SESSIONS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Save current city state to a session file
     */
    public String saveSession(City city, List<JetPackFlight> flights, Weather weather) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String filename = String.format("%s_%s.session", city.getName().replaceAll(" ", "_"), timestamp);
        File sessionFile = new File(SESSIONS_DIR, filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(sessionFile))) {
            // Session header
            writer.println("# JetPack Traffic Control Session");
            writer.println("# Saved: " + LocalDateTime.now());
            writer.println();

            // City information
            writer.println("[CITY]");
            writer.println("width=" + city.getWidth());
            writer.println("height=" + city.getHeight());
            writer.println();

            // Weather information
            writer.println("type=" + weather.getCurrentWeather());
            writer.println("severity=" + weather.getCurrentSeverity());
            writer.println("visibility=1.0");
            writer.println();

            // Parking spaces
            writer.println("[PARKING]");
            List<ParkingSpace> parkingSpaces = city.getParkingSpaces();
            writer.println("total=" + parkingSpaces.size());
            int occupied = 0;
            for (ParkingSpace space : parkingSpaces) {
                if (space.isOccupied()) {
                    occupied++;
                }
            }
            writer.println("occupied=" + occupied);
            writer.println("available=" + (parkingSpaces.size() - occupied));
            writer.println();

            // Flight information
            writer.println("[FLIGHTS]");
            writer.println("count=" + flights.size());
            for (JetPackFlight flight : flights) {
                writer.println(String.format("flight=%s,%.2f,%.2f,ACTIVE",
                    flight.getJetpack().getCallsign(),
                    flight.getX(),
                    flight.getY()));
            }

            System.out.println("Session saved: " + sessionFile.getAbsolutePath());
            return sessionFile.getAbsolutePath();

        } catch (IOException e) {
            System.err.println("Error saving session: " + e.getMessage());
            return null;
        }
    }

    /**
     * Export radar log to text file
     */
    public String exportRadarLog(List<String> radarLog) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String filename = String.format("radar_log_%s.txt", timestamp);
        File logFile = new File(SESSIONS_DIR, filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile))) {
            writer.println("=".repeat(80));
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - RADAR LOG");
            writer.println("Exported: " + LocalDateTime.now());
            writer.println("=".repeat(80));
            writer.println();

            for (String entry : radarLog) {
                writer.println(entry);
            }

            System.out.println("Radar log exported: " + logFile.getAbsolutePath());
            return logFile.getAbsolutePath();

        } catch (IOException e) {
            System.err.println("Error exporting radar log: " + e.getMessage());
            return null;
        }
    }

    /**
     * Export accident report
     */
    public String exportAccidentReport(List<String> accidents) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String filename = String.format("accident_report_%s.txt", timestamp);
        File reportFile = new File(SESSIONS_DIR, filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            writer.println("=".repeat(80));
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - ACCIDENT REPORT");
            writer.println("Generated: " + LocalDateTime.now());
            writer.println("=".repeat(80));
            writer.println();
            writer.println("Total Accidents: " + accidents.size());
            writer.println();

            for (int i = 0; i < accidents.size(); i++) {
                writer.println(String.format("Accident #%d:", i + 1));
                writer.println(accidents.get(i));
                writer.println("-".repeat(80));
            }

            System.out.println("Accident report exported: " + reportFile.getAbsolutePath());
            return reportFile.getAbsolutePath();

        } catch (IOException e) {
            System.err.println("Error exporting accident report: " + e.getMessage());
            return null;
        }
    }

    /**
     * List all saved sessions
     */
    public File[] listSessions() {
        File dir = new File(SESSIONS_DIR);
        return dir.listFiles((d, name) -> name.endsWith(".session"));
    }

    /**
     * Get sessions directory path
     */
    public String getSessionsDirectory() {
        return SESSIONS_DIR;
    }
}