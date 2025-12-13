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
    
    /** Map storing jetpack collections keyed by city name */
    private Map<String, ArrayList<JetPack>> cityJetpacks;
    
    /**
     * Constructor - initializes jetpack collections for all cities.
     * Calls initialization method to populate jetpack fleets.
     */
    public CityJetpackManager() {
        initializeCityJetpacks();  // Initialize jetpacks for all supported cities
    }
    
    /**
     * Initializes pre-existing jetpacks for all cities (300 per city).
     * Uses JetpackFactory to generate city-specific jetpack fleets with proper callsigns.
     */
    private void initializeCityJetpacks() {
        cityJetpacks = new HashMap<>();  // Create empty map for city-to-jetpacks mapping
        
        // Generate 300 jetpacks for each city dynamically using factory
        cityJetpacks.put("New York", JetpackFactory.generateJetpacksForCity("NY", "New York"));  // NY fleet with NY- prefix
        cityJetpacks.put("Boston", JetpackFactory.generateJetpacksForCity("BOS", "Boston"));     // Boston fleet with BOS- prefix
        cityJetpacks.put("Houston", JetpackFactory.generateJetpacksForCity("HOU", "Houston"));   // Houston fleet with HOU- prefix
        cityJetpacks.put("Dallas", JetpackFactory.generateJetpacksForCity("DAL", "Dallas"));     // Dallas fleet with DAL- prefix
    }
    
    /**
     * Gets the jetpack collection for a specific city.
     * Returns null if city not found.
     * 
     * @param city The city name (e.g., "New York", "Boston")
     * @return ArrayList of jetpacks for the specified city, or null if city not found
     */
    public ArrayList<JetPack> getJetpacksForCity(String city) {
        return cityJetpacks.get(city);  // Lookup and return city's jetpack list
    }
    
    /**
     * Gets all city-jetpack mappings.
     * Returns the actual map (not a copy), allowing external modification.
     * 
     * @return Map of city names to jetpack collections
     */
    public Map<String, ArrayList<JetPack>> getAllCityJetpacks() {
        return cityJetpacks;  // Return the complete map
    }
    
    /**
     * Gets the list of all cities with jetpacks.
     * Returns array of city names from the map keys.
     * 
     * @return Array of city names
     */
    public String[] getCities() {
        return cityJetpacks.keySet().toArray(new String[0]);  // Convert keys to array
    }
}
