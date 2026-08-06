# ComposeTaskApp (KMP)

A modern Kotlin Multiplatform (KMP) application targeting **Android** and **iOS**, built with Jetpack Compose (Android) and shared business logic. The project follows Clean Architecture principles and modern best practices.

## Tech Stack

### Multiplatform Core
- **Kotlin**: 2.3.20
- **Kotlin Multiplatform (KMP)**: Targetting Android & iOS
- **Kotlinx Coroutines**: 1.10.2
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
- **Automated Refresh**: Custom `TokenRefreshPlugin` for transparent access token renewal.

### Local Database & Persistence (Shared)
- **Room KMP**: 2.8.4 (Multiplatform database logic with SQLite)
- **DataStore Preferences**: 1.1.1 (Multiplatform settings)
- **Okio**: 3.9.0 (File system access for iOS)

### Android UI
- **Jetpack Compose**: 2026.03.01 (BOM)
- **Material 3**: Latest from BOM
- **Navigation Compose**: 2.9.7
- **Lifecycle**: 2.10.0

### Project Configuration
- **Android Gradle Plugin (AGP)**: 9.3.1
- **KSP (Kotlin Symbol Processing)**: 2.3.2
- **Java Target**: 11
- **Min SDK**: 24
- **Target SDK**: 36

## Architecture

The project adheres to **Clean Architecture** with a clear separation of concerns:
- **Domain Layer**: Contains platform-agnostic business logic, use cases, and repository interfaces.
- **Data Layer**: Implements repository interfaces, handling data retrieval from Ktor (Remote) and Room/DataStore (Local).
- **UI Layer (Android)**: Built with Jetpack Compose using the MVVM pattern. ViewModels are currently platform-specific but consume shared Use Cases.

## Features

- **Cross-Platform Logic**: 100% shared Networking, Database, and Domain logic.
- **Automated Token Refresh**: Transparently handles access token expiration using a Ktor plugin and refresh token rotation.
- **Offline Support**: Vendor data is cached locally using Room, allowing for offline viewing and reduced network usage.
- **Session Validation**: Proactive session checking at app startup to ensure valid user state.
- **Modern UI**: Fully responsive Material Design 3 layouts with adaptive components.
- **Safe Migrations**: Manual Room database migrations (e.g., v1 to v2) to ensure user data persistence during schema updates.

## Project Structure

- **`:app`**: Android application module (Jetpack Compose).
- **`:shared`**: Kotlin Multiplatform module containing shared business logic, networking, and data persistence.
    - `commonMain`: Shared code across all platforms.
    - `androidMain`: Android-specific implementations (e.g., OkHttp engine, Room builder).
    - `iosMain`: iOS-specific implementations (e.g., Darwin engine, DataStore path configuration).

## Getting Started

### Prerequisites
- Android Studio Ladybug or newer.
- Xcode (for iOS builds).
- JDK 17.

### Build and Run
1. Clone the repository.
2. Sync the project with Gradle files.
3. Run the `:app` module on an Android emulator or device.
4. For iOS, open the `iosApp` folder in Xcode or run via the shared framework integration.

## Database Migrations
The project uses manual migrations for Room to prevent data loss.
- **Migration 1 to 2**: Migrated `VendorEntity` primary key from `INTEGER` to `TEXT` and added `averageRating` and `totalReviews` fields.
