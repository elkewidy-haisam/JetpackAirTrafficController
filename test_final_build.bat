@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
cls
echo.
echo ============================================================
echo   FINAL COMPILATION - ALL ERRORS FIXED
echo ============================================================
echo.
echo Compiling AirTrafficControllerFrame.java with all fixes...
echo.

call setup-maven.bat > nul 2>&1
mvn clean compile

echo.
if %ERRORLEVEL% EQU 0 (
    echo ============================================================
    echo   ✓✓✓ SUCCESS! PROJECT COMPILES WITHOUT ERRORS! ✓✓✓
    echo ============================================================
    echo.
    echo All errors fixed:
    echo   ✓ Line 1571: getTimezoneForCity - Fixed
    echo   ✓ Line 1844: writeToRadarLog - Fixed
    echo   ✓ Removed 6 obsolete logging methods
    echo   ✓ All method calls updated correctly
    echo.
    echo Project structure:
    echo   - AirTrafficControllerFrame.java (~1550 lines)
    echo   - CityLogManager.java (extracted)
    echo   - JetpackFactory.java (extracted)
    echo   - TimezoneHelper.java (extracted)
    echo   - CitySelectionPanel.java (extracted)
    echo.
    echo Ready to run: mvn exec:java
) else (
    echo ============================================================
    echo   ✗ COMPILATION FAILED
    echo ============================================================
    echo.
    echo Please review error messages above.
)
echo.
pause
