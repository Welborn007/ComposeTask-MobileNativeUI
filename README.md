# ComposeTaskApp

A modern Android application built with Jetpack Compose, following clean architecture principles and best practices.

## Tech Stack

### Core Android
- **Android Gradle Plugin (AGP)**: 9.1.0
- **Kotlin**: 2.3.20
- **Java Target**: 17
- **Min SDK**: 24
- **Target SDK**: 36
- **Compile SDK**: 36

### UI & Jetpack Compose
- **Jetpack Compose**: 2026.03.01 (BOM)
- **Material 3**: Latest from BOM
- **Compose UI Tooling**: Latest from BOM
- **Activity Compose**: 1.13.0
- **Material Icons Extended**: Latest from BOM

### Navigation
- **Navigation Compose**: 2.9.7

### Dependency Injection
- **Hilt Android**: 2.59.2
- **Hilt Navigation Compose**: 1.3.0

### State Management & Lifecycle
- **ViewModel**: Built-in with Compose
- **Lifecycle**: 2.10.0
- **Core KTX**: 1.18.0

### Local Database
- **Room**: 2.8.4
  - Room Runtime
  - Room KTX
  - Room Compiler (KSP)

### Data Persistence
- **DataStore Preferences**: 1.2.1

### Networking
- **Retrofit**: 3.0.0
- **Retrofit Gson Converter**: 3.0.0
- **OkHttp**: 5.3.2
- **OkHttp Logging Interceptor**: 5.3.2

### Code Generation
- **KSP (Kotlin Symbol Processing)**: 2.3.2
- **Kotlin Compose Plugin**: 2.3.20

### Testing
- **JUnit**: 4.13.2

## Architecture

- **Clean Architecture** with MVVM pattern
- **Jetpack Compose** for declarative UI
- **Hilt** for dependency injection
- **Room** for local data persistence
- **Retrofit** for remote API calls
- **Flow/StateFlow** for reactive data streams

## Build Configuration

- **Build Tool Version**: 36.0.0
- **AGP 9.0+ Features**:
  - Built-in Kotlin support (no separate kotlin-android plugin needed)
  - KSP for annotation processing (migrated from KAPT)
  - Modern source set management via `android.sourceSets`

## Features

- User authentication (Login/Splash screen)
- Task management
- Modern Material Design 3 UI
- Responsive Compose layouts
- Secure local data storage with Room
- Network requests with Retrofit
- Dependency injection with Hilt

