# MyVNDB

An unofficial Android client for [vndb.org](https://vndb.org), the Visual Novel Database. It uses the public [VNDB API (Kana)](https://api.vndb.org/kana).

Browse and search visual novels, then dig into the details: tags, releases, screenshots, staff, characters and voice actors.

## Technologies
- Kotlin
- Jetpack Compose (Material 3, Adaptive)
- Navigation 3
- Koin (Annotations, via the Koin compiler plugin)
- Ktor
- kotlinx.serialization
- Coroutines and Flow
- Coil
- Detekt

## Architecture
- Single `:app` module, organised by feature (discover, browse, detail, character, release, profile, login)
- Clean Architecture: each feature has `data`, `domain`, `presentation` and `di` packages
- MVI in the presentation layer: a ViewModel exposes one state and one-off effects, and handles intents
- Shared code lives in `common`

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
