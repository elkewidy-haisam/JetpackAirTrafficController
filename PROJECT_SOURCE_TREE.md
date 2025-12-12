# Project Source Tree

This document provides a tree view of the main source code structure under `src/main/java` for the JetpackAirTrafficController project.

```
src/
└── main/
    └── java/
        └── com/
            └── example/
                accident/
                    Accident.java
                    AccidentAlert.java
                    AccidentReporter.java
                city/
                    City.java
                controller/
                    AirTrafficController.java
                detection/
                    CollisionDetector.java
                    Radar.java
                flight/
                    FlightEmergencyHandler.java
                    FlightHazardMonitor.java
                    FlightMovementController.java
                    FlightPath.java
                    JetPackFlight.java
                    JetPackFlightRenderer.java
                    JetPackFlightState.java
                grid/
                    Grid.java
                    GridRenderer.java
                jetpack/
                    JetPack.java
                logging/
                    CityLogManager.java
                manager/
                    AccidentReporter.java
                    CityDisplayUpdater.java
                    CityJetpackManager.java
                    CityTimerManager.java
                    FlightHazardManager.java
                    RadioInstructionManager.java
                map/
                    RealisticCityMap.java
                model/
                    (many domain classes: AccidentAlert, AirTrafficController, Bridge3D, Building3D, City, CityModel3D, Grid, House3D, JetPack, JetPackFlight, ParkingSpace, Radio, RadioCommandExecutor, RadioMessage, RadioMessageFormatter, RadioTransmissionLogger, RealisticCityMap, Road3D, Weather)
                navigation/
                    NavigationCalculator.java
                parking/
                    ParkingSpace.java
                    ParkingSpaceGenerator.java
                    ParkingSpaceManager.java
                radio/
                    Radio.java
                    RadioCommandExecutor.java
                    RadioMessage.java
                    RadioMessageFormatter.java
                    RadioTransmissionLogger.java
                renderer/
                    JOGLRenderer3D.java
                ui/
                    citymap/
                    frames/
                    panels/
                    utility/
                util/
                    ConfigManager.java
                    DebugConfig.java
                    SessionManager.java
                utility/
                    geometry/
                    GeometryUtils.java
                    GridRenderer.java
                    jetpack/
                    JetpackFactory.java
                    map/
                    MapLoader.java
                    ParkingSpaceGenerator.java
                    performance/
                    PerformanceMonitor.java
                    reflection/
                    ReflectionHelper.java
                    timezone/
                    TimezoneHelper.java
                    water/
                    WaterDetector.java
                weather/
                    DayTime.java
                    Weather.java
                App.java
                MapTest.java
                setup-maven.bat
                sources.txt
                temp_files.txt
```

This chart covers all main packages, classes, and utility folders in your Java source directory.
