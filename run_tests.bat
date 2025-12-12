@echo off
REM Run the comprehensive test suite

echo.
echo ==========================================
echo  JetPack Traffic Control System
echo  Comprehensive Test Suite
echo ==========================================
echo.

REM Check if JUnit library exists
if not exist "lib\junit-4.13.2.jar" (
    echo ERROR: JUnit library not found!
    echo Please run: download_test_libs.bat
    pause
    exit /b 1
)

REM Step 1: List all main source files recursively
(for /r src\main\java %%f in (*.java) do @echo %%f) > sources_main.txt

echo Step 1: Compiling main source files...
javac -cp ".;lib\*" @sources_main.txt
if errorlevel 1 (
    echo ERROR: Main source compilation failed!
    pause
    exit /b 1
)
echo [OK] Main source compiled

echo.
echo Step 2: Creating test class list...
dir /s /b src\test\java\*.java > sources_test.txt
echo [OK] Test sources listed

echo.
echo Step 3: Creating test-classes directory...
mkdir target\test-classes 2>nul
echo [OK] Directory ready

echo.
echo Step 4: Compiling test classes...
javac -cp ".;lib\*;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar;target\classes" -d target\test-classes @sources_test.txt
if errorlevel 1 (
    echo ERROR: Test compilation failed!
    pause
    exit /b 1
)
echo [OK] Tests compiled

echo.
echo Step 5: Running tests...
echo ==========================================
java -cp ".;lib\*;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar;target\classes;target\test-classes" org.junit.runner.JUnitCore com.example.AllTests

echo.
echo ==========================================
echo Test execution complete!
echo ==========================================
pause
