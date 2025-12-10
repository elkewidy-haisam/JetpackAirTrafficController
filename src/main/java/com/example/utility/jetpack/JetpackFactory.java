package com.example.utility.jetpack;

import java.util.ArrayList;

import com.example.jetpack.JetPack;

/**
 * JetpackFactory.java
 * by Haisam Elkewidy
 * 
 * Factory class for generating jetpacks for different cities.
 * Provides city-specific callsigns, models, and manufacturers.
 */
public class JetpackFactory {
    
    /**
     * Generates 300 jetpacks dynamically for a city
     */
    public static ArrayList<JetPack> generateJetpacksForCity(String prefix, String cityName) {
        ArrayList<JetPack> jetpacks = new ArrayList<>();
        
        // Base arrays to cycle through
        String[] baseCallsigns = getCallsignsForCity(cityName);
        String[] baseOwners = {"John", "Jane", "Michael", "Sarah", "David", "Emily", "Chris", "Amanda", "Robert", "Lisa"};
        String[] baseModels = getModelsForCity(cityName);
        String[] baseManufacturers = getManufacturersForCity(cityName);
        String[] years = {"2022", "2023", "2024"};
        
        // Generate 100 jetpacks
        for (int i = 0; i < 100; i++) {
            int jetpackNumber = i + 1;
            String formattedNumber = (i <= 9) ? String.format("%02d", jetpackNumber) : String.valueOf(jetpackNumber); // Pad with leading zero for i=0 to 10
            String callsign = baseCallsigns[i % baseCallsigns.length] + "-" + formattedNumber;
            String owner = baseOwners[i % baseOwners.length] + " #" + (i / baseOwners.length + 1);
            String model = baseModels[i % baseModels.length];
            String manufacturer = baseManufacturers[i % baseManufacturers.length];
            String year = years[i % years.length];
            
            jetpacks.add(new JetPack(
                prefix + "-" + String.format("%03d", i + 1),
                callsign,
                owner,
                year,
                model,
                manufacturer
            ));
        }
        
        return jetpacks;
    }
    
    /**
     * Gets city-specific callsigns
     */
    private static String[] getCallsignsForCity(String cityName) {
        switch (cityName) {
            case "New York":
                return new String[]{"ALPHA", "BRAVO", "CHARLIE", "DELTA", "ECHO", "FOXTROT", "GOLF", "HOTEL", "INDIA", "JULIET"};
            case "Boston":
                return new String[]{"PATRIOT", "CELTICS", "REDSOX", "BRUINS", "BEACON", "HARBOR", "FENWAY", "LIBERTY", "REVERE", "QUINCY"};
            case "Houston":
                return new String[]{"ROCKET", "ASTRO", "TEXAN", "OILER", "SPACE", "NASA", "GALAXY", "COMET", "SATURN", "JUPITER"};
            case "Dallas":
                return new String[]{"COWBOY", "MAVS", "STARS", "RANGER", "MUSTANG", "BRONCO", "LONGHORN", "ARMADILLO", "PRAIRIE", "HORIZON"};
            default:
                return new String[]{"ALPHA", "BRAVO", "CHARLIE"};
        }
    }
    
    /**
     * Gets city-specific jetpack models
     */
    private static String[] getModelsForCity(String cityName) {
        switch (cityName) {
            case "New York":
                return new String[]{"SkyRider X1", "MetroFlyer Pro", "CityJet Elite", "UrbanJet Max", "SkyGlider Pro"};
            case "Boston":
                return new String[]{"FreedomFlyer", "BayJet Pro", "HarborHopper", "IceRider X1", "SeaBreeze Elite"};
            case "Houston":
                return new String[]{"SpaceJet X1", "LoneStar Pro", "HeatSeeker 3000", "RocketRider Elite", "GalaxyFlyer Max"};
            case "Dallas":
                return new String[]{"PrairieFlyer", "StarJet Pro", "RangeRider 3000", "CowboyJet Elite", "MaverickFlyer Max"};
            default:
                return new String[]{"GenericJet Pro"};
        }
    }
    
    /**
     * Gets city-specific manufacturers
     */
    private static String[] getManufacturersForCity(String cityName) {
        switch (cityName) {
            case "New York":
                return new String[]{"AeroTech", "UrbanJet", "MetroAir"};
            case "Boston":
                return new String[]{"LibertyCorp", "CoastalAir", "SeaBreeze", "NorthTech"};
            case "Houston":
                return new String[]{"TexasSky", "GulfAero", "SunTech"};
            case "Dallas":
                return new String[]{"SouthernAir", "DFWAero", "WestTech"};
            default:
                return new String[]{"GenericAir"};
        }
    }
}
