# Swing Component Architecture

## Component Hierarchy

```
AirTrafficControllerFrame (JFrame)
│
├── CitySelectionPanel (extracted - CitySelectionPanel.java)
│   ├── JLabel (instructionLabel)
│   ├── JComboBox<String> (cityComboBox)
│   └── JButton (selectButton)
│
├── ConsoleOutputPanel (extracted - ConsoleOutputPanel.java)
│   └── JTextArea (consoleOutput) in JScrollPane
│
└── CityMapPanel (inner class - complex, stays in main frame)
    │
    ├── Map Display (CENTER)
    │   └── MapDisplayPanel (extracted - MapDisplayPanel.java)
    │       ├── ImageIcon (city map)
    │       ├── JetPackFlight animations (draws jetpacks)
    │       ├── Parking spaces rendering
    │       └── Time-based shading overlay
    │
    ├── Control Panel (EAST - right side)
    │   └── CityControlPanel (extracted - CityControlPanel.java)
    │       ├── DateTimeDisplayPanel (extracted)
    │       │   └── JTextArea (date/time display)
    │       │
    │       ├── WeatherBroadcastPanel (extracted)
    │       │   └── JTextArea (weather info)
    │       │
    │       ├── JetpackMovementPanel (extracted)
    │       │   └── JTextArea (movement logs)
    │       │
    │       └── RadioInstructionsPanel (extracted)
    │           └── JTextArea (radio comms)
    │
    └── Jetpack List (SOUTH - bottom)
        └── JetpackListPanel (extracted - JetpackListPanel.java)
            └── JTextArea (jetpack table)
```

## Component Relationships

### Standalone Components (No Dependencies)
- **DateTimeDisplayPanel** - Pure UI component
- **WeatherBroadcastPanel** - Pure UI component
- **JetpackMovementPanel** - Pure UI component
- **RadioInstructionsPanel** - Pure UI component
- **ConsoleOutputPanel** - Pure UI component

### Data-Dependent Components
- **JetpackListPanel** - Requires `ArrayList<JetPack>`
- **MapDisplayPanel** - Requires `ImageIcon`, `List<JetPackFlight>`, `List<ParkingSpace>`

### Composite Components
- **CityControlPanel** - Aggregates DateTimeDisplayPanel, WeatherBroadcastPanel, JetpackMovementPanel, RadioInstructionsPanel

### Complex Inner Classes (Remain in AirTrafficControllerFrame)
- **CityMapPanel** - Orchestrates map display, controls, and list
- **JetPackFlight** - Animation logic for individual jetpacks
- **JetPackFlightState** - Parking behavior management
- **RadarTapeWindow** - Separate window for radar communications

## Data Flow

```
AirTrafficControllerFrame
        ↓
    [Creates]
        ↓
  CityMapPanel
        ↓
    [Creates]
        ↓
┌───────────────────────────────┐
│                               │
│  MapDisplayPanel              │
│  ├── Receives: mapIcon        │
│  ├── Receives: jetpackFlights │
│  ├── Receives: parkingSpaces  │
│  └── Renders: map + overlays  │
│                               │
│  CityControlPanel             │
│  ├── DateTimeDisplayPanel     │
│  │   └── Updates via timer    │
│  ├── WeatherBroadcastPanel    │
│  │   └── Updates via weather  │
│  ├── JetpackMovementPanel     │
│  │   └── Updates via flights  │
│  └── RadioInstructionsPanel   │
│      └── Updates via radio    │
│                               │
│  JetpackListPanel             │
│  └── Populated with jetpacks  │
│                               │
└───────────────────────────────┘
```

## Update Patterns

### Timer-Based Updates
```
Weather Timer (30s) → Weather object → WeatherBroadcastPanel.updateBroadcast()
DateTime Timer (1s) → DateTime calculation → DateTimeDisplayPanel.updateDisplay()
Radio Timer (5s) → Radio object → RadioInstructionsPanel.updateInstructions()
Animation Timer (50ms) → JetPackFlights → MapDisplayPanel.repaint()
```

### Event-Based Updates
```
Jetpack Movement → JetpackMovementPanel.appendMovement()
Parking Events → JetpackMovementPanel.appendMovement()
Console Messages → ConsoleOutputPanel.appendMessage()
City Selection → Create new CityMapPanel with new components
```

## Component Communication

### Parent → Child (Direct)
```java
// AirTrafficControllerFrame → Components
controlPanel.getDateTimePanel().updateDisplay(text);
controlPanel.getWeatherPanel().updateBroadcast(text);
```

### Child → Parent (Callback)
```java
// CitySelectionPanel → AirTrafficControllerFrame
CitySelectionPanel panel = new CitySelectionPanel(city -> {
    selectedCity = city;
    displayCityMap(city);
});
```

### Component → Component (Through Parent)
```java
// Weather changes → Update multiple components
currentWeather.changeWeatherRandomly();
updateWeatherDisplay(); // Updates WeatherBroadcastPanel
// Also affects MapDisplayPanel shading
```

## Design Patterns Used

1. **Composite Pattern**
   - CityControlPanel aggregates multiple display panels

2. **Observer Pattern** (implicit)
   - Timers observe time changes
   - Panels observe data updates

3. **Strategy Pattern**
   - MapDisplayPanel uses functional interfaces for rendering strategies
   - `ShadingRenderer` - customizable shading
   - `ParkingSpaceRenderer` - customizable parking display

4. **Factory Pattern**
   - JetpackFactory for creating jetpacks (existing)

5. **Singleton-like Pattern**
   - CityLogManager manages all logging (existing)

## Extension Points

### Adding New Display Panels
1. Create new panel class extending JPanel
2. Add to CityControlPanel
3. Update layout and spacing
4. Connect data source

### Customizing Rendering
```java
mapPanel.setShadingRenderer((g2d, w, h) -> {
    // Custom shading algorithm
});

mapPanel.setParkingRenderer(g2d -> {
    // Custom parking visualization
});
```

### Adding New Components
1. Follow existing component pattern
2. Implement proper encapsulation
3. Provide public API methods
4. Document in SWING_COMPONENT_EXTRACTION.md

## Testing Strategy

### Unit Testing Components
```java
@Test
public void testDateTimeDisplay() {
    DateTimeDisplayPanel panel = new DateTimeDisplayPanel();
    panel.updateDisplay("Test Text");
    assertEquals("Test Text", panel.getDisplayArea().getText());
}
```

### Integration Testing
```java
@Test
public void testCityControlPanel() {
    CityControlPanel panel = new CityControlPanel();
    assertNotNull(panel.getDateTimePanel());
    assertNotNull(panel.getWeatherPanel());
    // etc.
}
```

## Performance Considerations

- **MapDisplayPanel**: Repaints at 20 FPS (50ms intervals)
- **Timers**: DateTime (1s), Weather (30s), Radio (5s)
- **Memory**: Each component ~1-2KB, negligible overhead
- **Rendering**: Double-buffered by Swing automatically

## Summary

- **8 extracted components** for better organization
- **Clear hierarchy** with well-defined relationships
- **Flexible architecture** supporting easy modifications
- **Proper encapsulation** with public APIs
- **Maintainable design** following SOLID principles
