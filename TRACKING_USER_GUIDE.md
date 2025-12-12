# How to Use Individual Jetpack Tracking

## Quick Start

1. **Launch the Application**
   ```
   mvn clean compile
   mvn exec:java -Dexec.mainClass="com.example.App"
   ```

2. **Select a City**
   - Choose from: New York, Boston, Houston, or Dallas
   - The main view will load showing all jetpacks flying

3. **Track an Individual Jetpack**
   - Scroll through the jetpack list at the bottom of the screen
   - Find the jetpack you want to track
   - Click the "🔍 Track" button next to that jetpack

4. **View the Tracking Window**
   - A new window opens showing just that one jetpack
   - The jetpack appears as a large glowing dot
   - Position and status information is displayed in the top-left corner
   - The destination is shown as a red cross marker

5. **Track Multiple Jetpacks**
   - You can open multiple tracking windows
   - Each window independently tracks its assigned jetpack
   - All tracking happens in real-time

6. **Close Tracking Windows**
   - Simply close the tracking window when done
   - This doesn't affect the main simulation
   - You can reopen tracking for the same jetpack anytime

## Visual Guide

### Main View
```
┌─────────────────────────────────────────────┐
│  Air Traffic Control - [City Name]         │
│  Weather: Sunny  Time: Day                  │
├─────────────────────────────────────────────┤
│                                             │
│         [City Map with all jetpacks]        │
│              • • • • •                      │
│            • • • • • • •                    │
│         • • • • • • • • • •                 │
│                                             │
├─────────────────────────────────────────────┤
│ Jetpack List:                               │
│ ALPHA-1  | JP-001 | John Doe    | [Track]  │
│ BRAVO-2  | JP-002 | Jane Smith  | [Track]  │
│ CHARLIE-3| JP-003 | Bob Johnson | [Track]  │
│ ...                                         │
└─────────────────────────────────────────────┘
```

### Tracking Window
```
┌─────────────────────────────────────────────┐
│ Jetpack Tracking - Boston                   │
│ Callsign: ALPHA-1                           │
│ Serial: JP-001 | Owner: John Doe            │
│ Model: JX-2000 | Manufacturer: JetCorp      │
├─────────────────────────────────────────────┤
│ ┌──────────────┐                            │
│ │Position: XY  │     [City Map]             │
│ │Status: ACTIVE│                            │
│ │Dest: (X, Y)  │         ◉ ← Jetpack        │
│ └──────────────┘                            │
│                                             │
│                            ╳ ← Destination  │
│                                             │
├─────────────────────────────────────────────┤
│ Real-time tracking of selected jetpack      │
└─────────────────────────────────────────────┘
```

## Information Displayed

### Main View Jetpack List
- **Callsign**: Radio identification (e.g., ALPHA-1, BRAVO-2)
- **Serial Number**: Unique jetpack identifier (e.g., JP-001)
- **Owner**: Name of jetpack owner
- **Model**: Jetpack model name
- **Manufacturer**: Company that made the jetpack
- **Track Button**: Click to open tracking window

### Tracking Window Header
- **City Name**: Which city this jetpack is flying in
- **Callsign**: Radio call sign
- **Serial Number**: Unique identifier
- **Owner Name**: Pilot/owner name
- **Model**: Jetpack model
- **Manufacturer**: Maker
- **Year**: Manufacturing year

### Tracking Window Overlay
- **Position**: Current (X, Y) coordinates
- **Status**: Flight status (ACTIVE, DETOUR, EMERGENCY, etc.)
- **Destination**: Target (X, Y) coordinates

## Tips

1. **Finding Specific Jetpacks**
   - Use the scroll bar in the jetpack list
   - Look for unique callsigns
   - Track multiple jetpacks to compare their paths

2. **Window Management**
   - Position tracking windows side-by-side
   - Keep main view visible to see overall traffic
   - Close tracking windows you're not using

3. **Understanding Status**
   - **ACTIVE**: Normal flight
   - **DETOUR**: Avoiding hazard
   - **EMERGENCY LANDING**: Making emergency descent
   - **RADIO: [reason]**: Following ATC instructions
   - **WEATHER WARNING**: Flying in bad weather

4. **Performance**
   - More tracking windows = more CPU usage
   - Close windows you don't need
   - Typically can track 5-10 jetpacks smoothly

## Keyboard Shortcuts (Main View Only)

- **G**: Toggle grid overlay
- **P**: Toggle performance monitor
- **+/=**: Zoom in
- **-**: Zoom out
- **0**: Reset zoom

Note: Keyboard shortcuts don't work in tracking windows (tracking windows are view-only).

## Troubleshooting

**Q: Track button doesn't work**
- Make sure the simulation has started (city loaded)
- Try clicking again - may be timing issue

**Q: Tracking window shows blank map**
- Map may be loading - wait a few seconds
- Check that map files exist in resources folder

**Q: Jetpack position not updating**
- Make sure main simulation is running
- Check that jetpack hasn't landed/parked

**Q: Can't see jetpack in tracking window**
- Jetpack may be in different area of map
- Window shows full city map - jetpack appears as bright dot

**Q: Multiple windows slow down computer**
- Close unused tracking windows
- Each window updates 20 times per second
- Recommended max: 5-10 simultaneous tracking windows

## Examples

### Track a Specific Owner's Jetpack
1. Scroll through list to find owner name
2. Click Track button for that entry
3. Window opens showing their flight

### Monitor Multiple Jetpacks
1. Click Track for jetpack A → Window 1 opens
2. Click Track for jetpack B → Window 2 opens
3. Click Track for jetpack C → Window 3 opens
4. Arrange windows to see all three simultaneously

### Follow an Emergency
1. Watch main view for emergency situations (red/magenta dots)
2. Note the callsign
3. Find that callsign in the list
4. Click Track to monitor emergency closely

## Technical Notes

- Each tracking window is independent
- Tracking uses same flight data as main view
- Updates happen 20 times per second (50ms interval)
- Closing tracking window doesn't affect jetpack
- Can track same jetpack in multiple windows (if needed)
