@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
echo Testing 3D Tracking Window Compilation...
call setup-maven.bat > nul 2>&1
mvn clean compile -q
if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] 3D Tracking Window compiled successfully!
    echo.
) else (
    echo.
    echo [ERROR] Compilation failed. Check errors above.
    echo.
)
pause
