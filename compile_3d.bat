@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
echo Compiling 3D Tracking Implementation...
echo.
call setup-maven.bat > nul 2>&1
mvn clean compile
echo.
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation completed successfully!
    echo The 3D tracking window is ready to use.
) else (
    echo [ERROR] Compilation failed. Check errors above.
)
echo.
pause
