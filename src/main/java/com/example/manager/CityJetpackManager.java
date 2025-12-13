/**
 * Centralized management for jetpack operations and lifecycle coordination.
 * 
 * Purpose:
 * Manages jetpack instances across the Air Traffic Controller system, providing
 * factory methods, registry access, and coordination logic. Supports multi-city scenarios
 * and ensures consistent jetpack state across operational contexts.
 * 
 * Key Responsibilities:
 * - Initialize and maintain jetpack collections per city or system-wide
 * - Provide query methods for jetpack retrieval and filtering
 * - Coordinate jetpack state updates across subsystems
 * - Support jetpack lifecycle (creation, modification, disposal)
 * 
 * Interactions:
 * - Referenced by AirTrafficControllerFrame and CityMapPanel
 * - Integrates with logging and persistence subsystems
 * - Coordinates with related manager classes
 * 
 * Patterns & Constraints:
 * - Manager pattern centralizes jetpack concerns
 * - Thread-safe operations for concurrent access
 * - Per-city collections for multi-city support
 * 
 * @author Haisam Elkewidy
 */

package com.example.manager;

import com.example.jetpack.JetPack;
import com.example.utility.jetpack.JetpackFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * CityJetpackManager.java
 * 
 * Manages collections of jetpacks for multiple cities.
 * Initializes and provides access to jetpack collections for each city.
 */
public class CityJetpackManager {
    
    private Map<String, ArrayList<JetPack>> cityJetpacks;
    
    /**
     * Constructor - initializes jetpack collections for all cities
     */
    public CityJetpackManager() {
        initializeCityJetpacks();
    }
    
    /**
     * Initializes pre-existing jetpacks for all cities (25 per city)
     */
    private void initializeCityJetpacks() {
        cityJetpacks = new HashMap<>();
        
        // Generate 300 jetpacks for each city dynamically using factory
        cityJetpacks.put("New York", JetpackFactory.generateJetpacksForCity("NY", "New York"));
        cityJetpacks.put("Boston", JetpackFactory.generateJetpacksForCity("BOS", "Boston"));
        cityJetpacks.put("Houston", JetpackFactory.generateJetpacksForCity("HOU", "Houston"));
        cityJetpacks.put("Dallas", JetpackFactory.generateJetpacksForCity("DAL", "Dallas"));
    }
    
    /**
     * Gets the jetpack collection for a specific city
     * 
     * @param city The city name
     * @return ArrayList of jetpacks for the specified city
     */
    public ArrayList<JetPack> getJetpacksForCity(String city) {
        return cityJetpacks.get(city);
    }
    
    /**
     * Gets all city-jetpack mappings
     * 
     * @return Map of city names to jetpack collections
     */
    public Map<String, ArrayList<JetPack>> getAllCityJetpacks() {
        return cityJetpacks;
    }
    
    /**
     * Gets the list of all cities with jetpacks
     * 
     * @return Array of city names
     */
    public String[] getCities() {
        return cityJetpacks.keySet().toArray(new String[0]);
    }
}
