# ComposeTaskApp (KMP)

A modern Kotlin Multiplatform (KMP) application targeting **Android** and **iOS**, built with Jetpack Compose (Android) and shared business logic. The project follows Clean Architecture principles and modern best practices.

## Tech Stack

### Multiplatform Core
- **Kotlin**: 2.3.20
- **Kotlin Multiplatform (KMP)**: Targetting Android & iOS
- **Kotlinx Coroutines**: 1.7.3
- **Kotlinx Serialization**: 1.11.0

### Dependency Injection
- **Koin**: 4.0.0 (Shared DI across Android & iOS)
- **Koin Android**: 4.0.0
- **Koin Compose**: 4.0.0

### Networking (Shared)
- **Ktor**: 3.4.3
- **Engines**: OkHttp (Android), Darwin (iOS)
- **Content Negotiation**: Kotlinx Serialization (JSON)
- **Logging**: Ktor Logging Plugin

### Local Database & Persistence (Shared)
- **Room KMP**: 2.7.0+ (Multiplatform database logic)
- **DataStore Preferences**: 1.1.1 (Multiplatform settings)
- **Okio**: 3.9.0 (File system access for iOS)

### Android UI
- **Jetpack Compose**: 2026.03.01 (BOM)
- **Material 3**: Latest from BOM
- **Navigation Compose**: 2.9.7
- **Lifecycle**: 2.10.0

### Project Configuration
- **Android Gradle Plugin (AGP)**: 9.1.0
- **KSP (Kotlin Symbol Processing)**: 2.3.2
- **Java Target**: 17
- **Min SDK**: 24
- **Target SDK**: 36

## Architecture

- **Clean Architecture** with a shared `domain` and `data` layer.
- **MVVM Pattern**: ViewModels in the Android app (moving towards shared ViewModels).
- **Dependency Injection**: Koin provides singletons and factories across platform boundaries.
- **Repository Pattern**: Abstracted data sources for remote (Ktor) and local (Room/DataStore) storage.
- **Flow/StateFlow**: Reactive data streams for state management.

## Project Structure

- **`:app`**: Android application module (Jetpack Compose).
- **`:shared`**: Kotlin Multiplatform module containing shared business logic, networking, and data persistence.
    - `commonMain`: Shared code across all platforms.
    - `androidMain`: Android-specific implementations (e.g., Ktor OkHttp engine).
    - `iosMain`: iOS-specific implementations (e.g., Ktor Darwin engine, DataStore path configuration).

## Features

- **Cross-Platform Logic**: 100% shared Networking, Database, and Domain logic.
- **User Authentication**: Login/Signup flow with secure token storage.
- **Task Management**: Core business logic shared between platforms.
- **Modern UI**: Material Design 3 and responsive layouts.
- **Type-Safe API**: Kotlinx Serialization for robust data parsing.
- **Centralized DI**: Single source of truth for dependencies via Koin.
