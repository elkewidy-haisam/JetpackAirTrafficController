@echo off
echo Verifying code changes...
echo.

echo Checking BostonMapViewer...
findstr /C:"targetSpaces = 1000" src\main\java\com\example\BostonMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Boston: 1000 parking spaces) else (echo   ✗ Boston: parking spaces not found)

findstr /C:"300" src\main\java\com\example\BostonMapViewer.java | findstr /C:"jetpacks" >nul
if %errorlevel%==0 (echo   ✓ Boston: 300 jetpacks) else (echo   ✗ Boston: jetpacks not found)

findstr /C:"Color.GREEN" src\main\java\com\example\BostonMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Boston: Green parking spaces) else (echo   ✗ Boston: color not found)

findstr /C:"WaterDetector" src\main\java\com\example\BostonMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Boston: Water detection enabled) else (echo   ✗ Boston: water detection not found)

echo.
echo Checking ManhattanMapViewer...
findstr /C:"targetSpaces = 1000" src\main\java\com\example\ManhattanMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Manhattan: 1000 parking spaces) else (echo   ✗ Manhattan: parking spaces not found)

findstr /C:"300" src\main\java\com\example\ManhattanMapViewer.java | findstr /C:"jetpacks" >nul
if %errorlevel%==0 (echo   ✓ Manhattan: 300 jetpacks) else (echo   ✗ Manhattan: jetpacks not found)

findstr /C:"Color.GREEN" src\main\java\com\example\ManhattanMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Manhattan: Green parking spaces) else (echo   ✗ Manhattan: color not found)

findstr /C:"WaterDetector" src\main\java\com\example\ManhattanMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Manhattan: Water detection enabled) else (echo   ✗ Manhattan: water detection not found)

echo.
echo Checking DallasMapViewer...
findstr /C:"targetSpaces = 1000" src\main\java\com\example\DallasMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Dallas: 1000 parking spaces) else (echo   ✗ Dallas: parking spaces not found)

findstr /C:"300" src\main\java\com\example\DallasMapViewer.java | findstr /C:"jetpacks" >nul
if %errorlevel%==0 (echo   ✓ Dallas: 300 jetpacks) else (echo   ✗ Dallas: jetpacks not found)

findstr /C:"Color.GREEN" src\main\java\com\example\DallasMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Dallas: Green parking spaces) else (echo   ✗ Dallas: color not found)

findstr /C:"WaterDetector" src\main\java\com\example\DallasMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Dallas: Water detection enabled) else (echo   ✗ Dallas: water detection not found)

echo.
echo Checking HoustonMapViewer...
findstr /C:"targetSpaces = 1000" src\main\java\com\example\HoustonMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Houston: 1000 parking spaces) else (echo   ✗ Houston: parking spaces not found)

findstr /C:"300" src\main\java\com\example\HoustonMapViewer.java | findstr /C:"jetpacks" >nul
if %errorlevel%==0 (echo   ✓ Houston: 300 jetpacks) else (echo   ✗ Houston: jetpacks not found)

findstr /C:"Color.GREEN" src\main\java\com\example\HoustonMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Houston: Green parking spaces) else (echo   ✗ Houston: color not found)

findstr /C:"WaterDetector" src\main\java\com\example\HoustonMapViewer.java >nul
if %errorlevel%==0 (echo   ✓ Houston: Water detection enabled) else (echo   ✗ Houston: water detection not found)

echo.
echo Verification complete!
pause
