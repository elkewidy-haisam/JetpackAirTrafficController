@echo off
echo Compiling with JOGL support...
cd /d "%~dp0"
call mvn clean compile
if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo BUILD SUCCESSFUL - JOGL INTEGRATED
    echo ========================================
    echo.
) else (
    echo.
    echo ========================================
    echo BUILD FAILED - Check errors above
    echo ========================================
    echo.
)
pause
