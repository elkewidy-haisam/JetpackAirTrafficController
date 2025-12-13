/**
 * UI panel component for factory display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing factory-related visualization and user interaction.
 * Integrates with the main application frame to present factory data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render factory information with appropriate visual styling
 * - Handle user interactions related to factory operations
 * - Update display in response to system state changes
 * - Provide callbacks for parent frame coordination
 * 
 * Interactions:
 * - Embedded in AirTrafficControllerFrame or CityMapPanel
 * - Receives updates from manager classes and controllers
 * - Triggers actions via event listeners and callbacks
 * 
 * Patterns & Constraints:
 * - Extends JPanel for Swing integration
 * - Custom paintComponent for rendering where needed
 * - Event-driven updates for responsive UI
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.panels;

/**
 * Factory class for creating CityMapPanel instances.
 * Centralizes panel creation with consistent configuration.
 */
public class CityMapPanelFactory {
    
    /**
     * Creates a new CityMapPanel with default settings.
     * Returns a fully initialized panel ready for city visualization.
     *
     * @return New CityMapPanel instance
     */
    public static CityMapPanel createCityMapPanel() {
        // Create and return new city map panel with default configuration
        return new CityMapPanel();
    }

    // TODO: Add more factory methods for other related panels if needed
    // Examples: createCityMapPanelWithConfig(), createMinimapPanel()
}
