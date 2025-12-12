/**
 * JetpackFactory.java
 * by Haisam Elkewidy
 *
 * Factory for creating JetPack instances.
 */

package com.example.utility;

import com.example.jetpack.JetPack;

public class JetpackFactory {
    public static JetPack createJetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer) {
        return new JetPack(id, serialNumber, callsign, ownerName, year, model, manufacturer, new java.awt.Point(0, 0), 0.0, 0.0);
    }
}
