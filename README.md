# MyVNDB

An unofficial Android client for [vndb.org](https://vndb.org), the Visual Novel Database. It uses the public [VNDB API (Kana)](https://api.vndb.org/kana).

## Stack
- Kotlin 
- Jetpack Compose (Material 3, Adaptive)
- Clean Architecture + MVI
- Koin
- Ktor
- kotlinx.serialization
- DataStore
- Navigation 3
- Coil

## Requirements
- Android Studio (latest stable)
- JDK 11+
- Android 8.1 (API 27) or newer

## Build

```bash
./gradlew assembleDebug
```

```bash
./gradlew testDebugUnitTest
```

## Disclaimer

Not affiliated with vndb.org. All data is provided by VNDB and subject to its terms.

## License

Licensed under the [Apache License, Version 2.0](LICENSE).
