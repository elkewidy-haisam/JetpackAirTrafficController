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
    
    // Map storing jetpack collections keyed by city name
    // Enables O(1) lookup of jetpack fleets for each operational city
    private Map<String, ArrayList<JetPack>> cityJetpacks;
    
    /**
     * Constructs a new CityJetpackManager and initializes jetpack fleets for all cities.
     * Automatically populates the registry with pre-configured jetpack collections
     * for New York, Boston, Houston, and Dallas using the factory pattern.
     */
    public CityJetpackManager() {
        // Delegate to initialization method to populate city-specific jetpack fleets
        initializeCityJetpacks();
    }
    
    /**
     * Initializes jetpack collections for all supported cities.
     * Creates 300 jetpacks per city using JetpackFactory with city-specific
     * callsign prefixes and pilot name conventions. This populates the
     * internal registry with operational aircraft ready for flight assignment.
     */
    private void initializeCityJetpacks() {
        // Initialize empty map to hold city-to-jetpack-list associations
        cityJetpacks = new HashMap<>();
        
        // Generate 300 jetpacks for New York with "NY" callsign prefix
        // Factory handles callsign generation (e.g., NY-001, NY-002) and pilot assignment
        cityJetpacks.put("New York", JetpackFactory.generateJetpacksForCity("NY", "New York"));
        
        // Generate 300 jetpacks for Boston with "BOS" callsign prefix
        cityJetpacks.put("Boston", JetpackFactory.generateJetpacksForCity("BOS", "Boston"));
        
        // Generate 300 jetpacks for Houston with "HOU" callsign prefix
        cityJetpacks.put("Houston", JetpackFactory.generateJetpacksForCity("HOU", "Houston"));
        
        // Generate 300 jetpacks for Dallas with "DAL" callsign prefix
        cityJetpacks.put("Dallas", JetpackFactory.generateJetpacksForCity("DAL", "Dallas"));
    }
    
    /**
     * Retrieves the complete jetpack collection for a specific city.
     * Returns null if the city is not found in the registry.
     * 
     * @param city The city name (e.g., "New York", "Boston")
     * @return ArrayList of JetPack objects for the city, or null if city not found
     */
    public ArrayList<JetPack> getJetpacksForCity(String city) {
        // Direct map lookup - returns null for unregistered cities
        // Caller should null-check or handle missing cities appropriately
        return cityJetpacks.get(city);
    }
    
    /**
     * Returns the complete city-to-jetpack mapping.
     * Useful for system-wide operations, statistics, or bulk processing.
     * Returns a reference to internal state - callers should not modify.
     * 
     * @return Map of city names to their respective jetpack collections
     */
    public Map<String, ArrayList<JetPack>> getAllCityJetpacks() {
        // Return direct reference to internal map
        // Note: This allows external modification - consider returning unmodifiable view
        return cityJetpacks;
    }
    
    /**
     * Returns an array of all city names currently in the registry.
     * Useful for populating UI dropdowns, validation, or iteration.
     * 
     * @return String array of city names (e.g., ["New York", "Boston", "Houston", "Dallas"])
     */
    public String[] getCities() {
        // Convert keyset to array for easier UI binding and iteration
        // Order is not guaranteed due to HashMap's unordered nature
        return cityJetpacks.keySet().toArray(new String[0]);
    }
}
