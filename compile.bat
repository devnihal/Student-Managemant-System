@echo off
echo Compiling Java files...
javac -cp "lib/mysql-connector-j-9.4.0.jar;src" -d build src/model/*.java src/dao/*.java src/gui/*.java src/util/*.java src/Main.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b 1
)
echo Compilation successful.
echo Running the application...
java -cp "lib/mysql-connector-j-9.4.0.jar;build" Main
pause
