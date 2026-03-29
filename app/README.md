# Android Automotive Infotainment Simulator

A complete offline Android infotainment system simulation written in
Java + JNI (C), designed to demonstrate embedded, system-level, and
automotive software development skills.

## Features
- Offline route simulation (Belgrade → Novi Sad)
- Realistic vehicle movement along polyline
- Smooth speed simulation via JNI (C)
- Smooth battery discharge via JNI (C)
- OSMDroid map rendering (OpenStreetMap)
- Native C module compiled using CMake

> Note: Speed and battery values are **simulated**, not taken from real sensors.  
> This project is intended as a demo for infotainment concepts.

## Tech
Java, Android SDK, Android NDK, C, JNI, CMake, OSMDroid

## Build & Run
1. Open project in Android Studio
2. Run on device
3. Observe moving vehicle, live speed & battery updates
### Quick Run (Windows PowerShell)

For convenience, a `run.ps1` script is included.  
It will:
- Build the project (`assembleDebug`)
- Install the APK on a connected device (`installDebug`)
- Stream logcat output filtered for "Vehicle"

To use:
```powershell
.\run.ps1

