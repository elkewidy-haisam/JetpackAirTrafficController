/**
 * CityMapPanelFactory.java
 * by Haisam Elkewidy
 *
 * This class handles CityMapPanelFactory functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - createCityMapPanel()
 *
 */

package com.example.ui.panels;


public class CityMapPanelFactory {
    public static CityMapPanel createCityMapPanel() {
        return new CityMapPanel();
    }

    // Add more factory methods for other related panels if needed
}
