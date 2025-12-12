# Jetpack Air Traffic Controller: Package/Class/Member Summary

This document lists every package, every class within that package, and their instance methods/variables, with a brief description of what each does. This is a high-level reference for understanding the codebase structure and intent.

---

## com.example.weather
- **DayTime**
  - Variables: `currentTime` (tracks time), `timeOfDay` (morning/afternoon/evening)
  - Methods: `setTime()`, `updateToCurrentTime()`, `getTimeOfDayDescription()`, etc.
  - Purpose: Manages and describes the current time of day for weather and UI.
- **Weather**
  - Variables: `currentWeather`, `currentSeverity`, `temperature`, `windSpeed`, `visibility`, `weatherID`, `lastUpdated`
  - Methods: `changeWeather()`, `sendWeatherBroadcast()`, `isSafeToFly()`, etc.
  - Purpose: Represents and manages weather conditions, broadcasts, and safety checks.

## com.example.ui.citymap
- **CityMapPanel**
  - Variables: UI components (`mapPanel`, `weatherLabel`, `dateTimeLabel`, etc.), domain models (`parkingManager`, `cityRadio`, `performanceMonitor`)
  - Methods: UI event handlers, rendering, callbacks, state updates
  - Purpose: Main UI panel for city map, manages jetpack display, weather, parking, and user interaction.
- **CityMapWeatherManager**
  - Variables: `city`, `currentWeather`, `cityRadio`, `radarTapeWindow`, `parentComponent`, `updater`, `weatherTimer`, `weatherLabel`
  - Methods: `startWeatherTimer()`, `updateWeatherDisplay()`, `groundAllFlights()`
  - Purpose: Manages weather display and related flight operations in the city map.

## com.example.accident
- **AccidentAlert**
  - Variables: `alertId`, inner class `Accident` (with accident details)
  - Methods: `reportAccident()`, `alertJetpacksOfAccident()`, `removeAlert()`
  - Purpose: Handles accident alerts, reporting, and notification to jetpacks.
- **AccidentReporter**
  - Variables: `cityRadio`, `city`, `random`, `logManager`, `radioInstructionsArea`, `movementLogger`
  - Methods: `reportRandomAccident()`, `setMovementLogger()`, `setRadioInstructionsArea()`
  - Purpose: Reports accidents, logs movements, and updates UI.

## com.example.flight
- **FlightPath**
  - Variables: `pathID`, `origin`, `destination`, hazard flags
  - Methods: `setFlightPath()`, `detour()`, `halt()`, `resumeNormalPath()`, `hasActiveHazards()`
  - Purpose: Represents and manages flight paths, detours, and hazard states.

## com.example.utility
- **PerformanceMonitor**
  - Variables: `startTime`, `endTime`, `frameCount`, `currentFPS`, `visible`
  - Methods: `start()`, `stop()`, `getElapsedTime()`, `render()`, `getFPS()`
  - Purpose: Monitors and displays performance metrics (FPS, timing).
- **JetpackFactory**
  - Methods: `createJetPack()`
  - Purpose: Factory for creating jetpack instances.
- **GeometryUtils**
  - Methods: `distance()`, `calculateDistance()`, `calculateAngle()`, `isWithinRange()`
  - Purpose: Utility for geometric calculations.

---

This summary covers the main structure and intent of your codebase. For a full exhaustive listing, request a detailed Markdown export.
