# City-Specific Log Files Implementation

## Summary of Changes

The Air Traffic Controller application now creates **separate log files for each city** instead of using single global log files. This allows for better organization and tracking of city-specific activities.

## Log Files Created

For each city (New York, Boston, Houston, Dallas), three log files are created:

### New York City
- `newyork_jetpack_movement_log.txt` - Tracks all jetpack movements in NYC
- `newyork_radar_communications_log.txt` - Records radar/radio communications in NYC
- `newyork_weather_broadcast_log.txt` - Logs weather broadcasts for NYC

### Boston
- `boston_jetpack_movement_log.txt` - Tracks all jetpack movements in Boston
- `boston_radar_communications_log.txt` - Records radar/radio communications in Boston
- `boston_weather_broadcast_log.txt` - Logs weather broadcasts for Boston

### Houston
- `houston_jetpack_movement_log.txt` - Tracks all jetpack movements in Houston
- `houston_radar_communications_log.txt` - Records radar/radio communications in Houston
- `houston_weather_broadcast_log.txt` - Logs weather broadcasts for Houston

### Dallas
- `dallas_jetpack_movement_log.txt` - Tracks all jetpack movements in Dallas
- `dallas_radar_communications_log.txt` - Records radar/radio communications in Dallas
- `dallas_weather_broadcast_log.txt` - Logs weather broadcasts for Dallas

## Implementation Details

### Key Changes in AirTrafficControllerFrame.java

1. **Replaced static log file constants with Maps**:
   ```java
   private Map<String, String> cityJetpackLogFiles;
   private Map<String, String> cityRadarLogFiles;
   private Map<String, String> cityWeatherLogFiles;
   ```

2. **Added initializeLogFileMaps() method**:
   - Creates log file name mappings for each city
   - Called during constructor initialization

3. **Modified clearLogFiles() method**:
   - Now loops through all cities
   - Clears and initializes log files for each city
   - Adds city name header to each log file

4. **Updated log writing methods**:
   - `writeToJetpackLog(String city, String message)`
   - `writeToRadarLog(String city, String message)`
   - `writeToWeatherLog(String city, String message)`
   - All methods now require a city parameter to write to the correct log file

5. **Updated all log method calls**:
   - CityMapPanel methods now pass the city parameter
   - RadarTapeWindow stores city and passes it to log methods
   - All log writes are city-specific

## Behavior

### On Application Start
- All 12 log files (3 per city × 4 cities) are cleared and initialized
- Each file receives a header with city name and start timestamp
- Clean slate for each new application run

### During Operation
- When a city is selected, only that city's log files are written to
- Jetpack movements, radio communications, and weather updates are logged separately per city
- Log files can be reviewed independently for each city

### File Locations
All log files are created in the project root directory:
```
e10btermproject/
├── newyork_jetpack_movement_log.txt
├── newyork_radar_communications_log.txt
├── newyork_weather_broadcast_log.txt
├── boston_jetpack_movement_log.txt
├── boston_radar_communications_log.txt
├── boston_weather_broadcast_log.txt
├── houston_jetpack_movement_log.txt
├── houston_radar_communications_log.txt
├── houston_weather_broadcast_log.txt
├── dallas_jetpack_movement_log.txt
├── dallas_radar_communications_log.txt
└── dallas_weather_broadcast_log.txt
```

## Benefits

1. **Better Organization**: Separate logs for each city make it easier to track city-specific activities
2. **Cleaner Logs**: No mixing of different city data in the same file
3. **Easier Analysis**: Can review logs for a specific city without filtering through other cities
4. **Scalability**: Easy to add new cities - just add to the cities array
5. **Fresh Start**: Each application run starts with clean log files

## Testing

To compile and test the changes:
```batch
test_city_logs.bat
```

Or use Maven directly:
```batch
mvn clean compile
```

Then run the application and select different cities. Check that log files are created and populated correctly for each city.
