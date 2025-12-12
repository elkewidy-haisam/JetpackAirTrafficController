/**
 * RadioInstructionManager.java
 * by Haisam Elkewidy
 *
 * This class handles RadioInstructionManager functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - cityRadio (Radio)
 *   - currentWeather (Weather)
 *   - city (String)
 *   - random (Random)
 *   - logManager (CityLogManager)
 *   - radioInstructionsArea (JTextArea)
 *
 * Methods:
 *   - RadioInstructionManager(cityRadio, city, logManager)
 *
 */

package com.example.manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.swing.JTextArea;

import com.example.accident.AccidentReporter;
import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.logging.CityLogManager;
import com.example.radio.Radio;
import com.example.weather.Weather;

/**
 * RadioInstructionManager.java
 * 
 * Manages radio instructions and communications for air traffic control.
 * Issues various types of instructions based on weather conditions,
 * traffic situations, and random events.
 */
public class RadioInstructionManager {
    
    private Radio cityRadio;
    private Weather currentWeather;
    private String city;
    private Random random;
    private CityLogManager logManager;
    private JTextArea radioInstructionsArea;
    
    /**
     * Constructor
     * 
     * @param cityRadio The radio for the city
     * @param city The city name
     * @param logManager The log manager
     */
    public RadioInstructionManager(Radio cityRadio, String city, CityLogManager logManager) {
        this.cityRadio = cityRadio;
        this.city = city;
        this.logManager = logManager;
        this.random = new Random();
    }
    
    /**
     * Sets the current weather
     * 
     * @param weather The current weather object
     */
    public void setWeather(Weather weather) {
        this.currentWeather = weather;
    }
    
    /**
     * Sets the radio instructions text area
     * 
     * @param textArea The text area for displaying instructions
     */
    public void setRadioInstructionsArea(JTextArea textArea) {
        this.radioInstructionsArea = textArea;
    }
    
    /**
     * Issues a random radio instruction to a random flight
     * 
     * @param jetpackFlights List of active flights
     * @param flightStates Map of flight states
     * @param accidentReporter The accident reporter for handling accidents
     */
    public void issueRandomRadioInstruction(java.util.List<JetPackFlight> jetpackFlights,
                                           java.util.Map<JetPackFlight, JetPackFlightState> flightStates,
                                           AccidentReporter accidentReporter) {
        if (jetpackFlights.isEmpty() || radioInstructionsArea == null) return;
        
        JetPackFlight flight = jetpackFlights.get(random.nextInt(jetpackFlights.size()));
        JetPackFlightState state = flightStates.get(flight);
        
        if (state != null && state.isParked()) return;
        
        int instructionType = random.nextInt(100);
        
        // 2% chance of accident report
        if (instructionType < 2 && accidentReporter != null) {
            accidentReporter.reportRandomAccident();
            return;
        }
        
        String instruction;
        String action;
        
        if (currentWeather != null && !currentWeather.isSafeToFly()) {
            instruction = issueWeatherCriticalInstruction(flight, instructionType);
            action = getWeatherCriticalAction(instructionType);
        } else if (currentWeather != null && currentWeather.getCurrentSeverity() >= 3) {
            instruction = issueWeatherAdvisoryInstruction(flight, instructionType);
            action = getWeatherAdvisoryAction(instructionType);
        } else {
            instruction = issueRoutineInstruction(flight, instructionType);
            action = getRoutineAction(instructionType);
        }
        
        logInstruction(flight, instruction, action);
    }
    
    private String issueWeatherCriticalInstruction(JetPackFlight flight, int type) {
        if (type < 40) {
            cityRadio.issueEmergencyDirective(flight.getJetpack(), 
                "Weather conditions critical - " + currentWeather.getCurrentWeather() + 
                ". Emergency landing authorized");
            return "EMERGENCY LANDING REQUIRED";
        } else if (type < 70) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getX() + random.nextInt(200) - 100, 
                (int)flight.getY() + random.nextInt(200) - 100, 
                "Weather avoidance - " + currentWeather.getCurrentWeather());
            return "DETOUR AROUND WEATHER";
        } else {
            cityRadio.giveNewAltitude(flight.getJetpack(), 
                3000 + random.nextInt(2000), 
                "Weather avoidance");
            return "ALTITUDE CHANGE";
        }
    }
    
    private String getWeatherCriticalAction(int type) {
        if (type < 40) return "Land immediately at nearest parking";
        else if (type < 70) return "Alter course to avoid storm";
        else return "Climb to avoid weather";
    }
    
    private String issueWeatherAdvisoryInstruction(JetPackFlight flight, int type) {
        if (type < 30) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getDestination().x, (int)flight.getDestination().y, 
                "Weather deteriorating");
            return "RETURN TO BASE";
        } else if (type < 60) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getX() + random.nextInt(150) - 75, 
                (int)flight.getY() + random.nextInt(150) - 75, 
                "Precautionary route change");
            return "COURSE ADJUSTMENT";
        } else {
            cityRadio.giveNewAltitude(flight.getJetpack(), 
                2000 + random.nextInt(1500), 
                "Weather advisory");
            return "ALTITUDE ADVISORY";
        }
    }
    
    private String getWeatherAdvisoryAction(int type) {
        if (type < 30) return "Return to origin point";
        else if (type < 60) return "Detour for safety";
        else return "Adjust altitude";
    }
    
    private String issueRoutineInstruction(JetPackFlight flight, int type) {
        if (type < 20) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getX() + random.nextInt(100) - 50, 
                (int)flight.getY() + random.nextInt(100) - 50, 
                "Traffic separation");
            return "COLLISION AVOIDANCE";
        } else if (type < 40) {
            cityRadio.giveNewAltitude(flight.getJetpack(), 
                1500 + random.nextInt(2500), 
                "Routine altitude change");
            return "ALTITUDE CLEARANCE";
        } else if (type < 60) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getDestination().x, (int)flight.getDestination().y, 
                "Direct routing");
            return "ROUTE OPTIMIZATION";
        } else if (type < 80) {
            cityRadio.requestPositionReport(flight.getJetpack());
            return "POSITION REPORT REQUEST";
        } else {
            if (currentWeather != null) {
                cityRadio.provideWeatherInfo(flight.getJetpack(), 
                    currentWeather.getCurrentWeather() + " - Visibility " + 
                    currentWeather.getVisibility() + " miles");
            }
            return "WEATHER UPDATE";
        }
    }
    
    private String getRoutineAction(int type) {
        if (type < 20) return "Traffic alert - change course";
        else if (type < 40) return "Cleared to new altitude";
        else if (type < 60) return "Direct route cleared";
        else if (type < 80) return "Report position";
        else return "Current conditions advisory";
    }
    
    private void logInstruction(JetPackFlight flight, String instruction, String action) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String radioMsg = "[" + timestamp + "] " + cityRadio.getControllerCallSign() + 
                          " → " + flight.getJetpack().getCallsign() + "\n" +
                          "📡 " + instruction + "\n" +
                          "➤ " + action + "\n" +
                          "---\n";
        
        if (radioInstructionsArea != null) {
            radioInstructionsArea.append(radioMsg);
            radioInstructionsArea.setCaretPosition(radioInstructionsArea.getDocument().getLength());
        }
        
        if (logManager != null) {
            logManager.writeToRadarLog(city, radioMsg);
        }
    }
}
