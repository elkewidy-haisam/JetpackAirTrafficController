/*
 * JetpackFactory.java
 * Part of Jetpack Air Traffic Controller
 *
 * Factory for creating JetPack instances.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.utility;

import com.example.model.JetPack;

public class JetpackFactory {
    public static JetPack createJetPack(String callsign, String pilotName) {
        return new JetPack(callsign, pilotName);
    }
}
