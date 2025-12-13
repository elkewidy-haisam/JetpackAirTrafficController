/**
 * Records and maintains history of radio transmissions for compliance, playback, and auditing.
 * 
 * Purpose:
 * Provides an in-memory log of all radio communications for regulatory compliance, incident
 * investigation, and operator training. Maintains ordered list of transmission records with
 * timestamps and content for complete communication history. Supports queries for transmission
 * retrieval and analysis. Serves as a lightweight audit trail for radio operations.
 * 
 * Key Responsibilities:
 * - Log all radio transmissions chronologically in memory
 * - Maintain ordered list of transmission records
 * - Provide transmission history queries for compliance reporting
 * - Support playback and review of communication sequences
 * - Enable incident investigation with complete communication context
 * - Facilitate operator training with recorded communication examples
 * - Serve as audit trail for regulatory compliance
 * 
 * Interactions:
 * - Used by Radio subsystem to log outgoing transmissions
 * - Referenced by RadarTapeWindow for displaying communication history
 * - Consulted during incident investigations for timeline reconstruction
 * - Supports CityLogManager for persistent file-based logging
 * - Integrated with RadioMessageFormatter for consistent log formatting
 * - Used in training scenarios for communication protocol examples
 * - May be exported for external compliance systems
 * 
 * Patterns & Constraints:
 * - Simple in-memory list-based logging (no persistence in this class)
 * - Unbounded growth; consider size limits for long-running systems
 * - Chronological ordering via ArrayList insertion order
 * - Thread-safe reads via ArrayList; synchronization needed for concurrent writes
 * - Lightweight structure suitable for moderate transmission volumes
 * - Transmission format managed by external formatter (RadioMessageFormatter)
 * - Coordinates with file-based logging for persistence
 * - Returns direct list reference; callers should not modify
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class RadioTransmissionLogger {
    private final List<String> transmissions;

    public RadioTransmissionLogger() {
        this.transmissions = new ArrayList<>();
    }

    public void logTransmission(String transmission) {
        transmissions.add(transmission);
    }

    public List<String> getTransmissions() {
        return transmissions;
    }
}
