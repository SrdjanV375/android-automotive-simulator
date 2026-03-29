.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
adb logcat | Select-String "Vehicle"
