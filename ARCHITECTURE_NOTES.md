# Architecture Notes

## Two Parallel Implementations

This project contains two separate air traffic control implementations:

### 1. GUI Implementation (Active/Primary)
**Entry Point:** `App.java` → `AirTrafficControllerFrame.java`

**Flight Management:** Uses `JetPackFlight` inner class with integrated FlightPath logic
- Visual representation with Swing
- Real-time animation and rendering
- Interactive map with jetpack tracking
- Integrated hazard management and waypoint navigation
- Weather integration with visual feedback

**Usage:** This is the primary application interface used when running the program.

### 2. Non-GUI Backend Implementation (Architectural/Optional)
**Entry Point:** `AirTrafficController.java`

**Flight Management:** Uses standalone `FlightPath.java` class
- Programmatic API for air traffic management
- No GUI dependencies
- Suitable for command-line tools, batch processing, or services
- Complete flight path management with hazards and detours

**Usage:** This backend can be used independently for:
- Automated testing without GUI overhead
- Server-side processing
- Command-line utilities
- API/service implementations
- Batch simulations

## Why Two Implementations?

The `FlightPath.java` class and `AirTrafficController.java` were the original backend design. When the GUI was enhanced, the FlightPath logic was integrated directly into `JetPackFlight` to provide:
- Tighter coupling between visualization and flight logic
- Better performance (no separate object hierarchy)
- Simplified GUI code

However, the original backend remains useful for non-GUI scenarios and as architectural documentation.

## Which Should You Use?

**For Visual Applications:** Use `AirTrafficControllerFrame` and `JetPackFlight`
- Has all FlightPath features integrated
- Provides real-time visual feedback
- Interactive and user-friendly

**For Backend/Programmatic Use:** Use `AirTrafficController` and `FlightPath`
- No GUI overhead
- Clean API for programmatic control
- Better for testing and automation

## Future Considerations

If you need to unify the two implementations:
1. Consider extracting FlightPath logic to a shared service layer
2. Have both GUI and non-GUI implementations use the same flight management
3. Or, remove AirTrafficController/FlightPath if non-GUI scenarios aren't needed

For now, both implementations coexist without conflict.
