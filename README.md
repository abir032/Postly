# Postly

Postly is a modern Android application that provides users with the latest news headlines with body using [NewsAPI](https://newsapi.org/). Built with Jetpack Compose and following modern Android development practices, it offers features like article browsing, searching, and favoriting capabilities.

## Features
- Browse latest news articles
- Search functionality
- Favorite articles management
- Offline support
- Clean and intuitive UI built with Jetpack Compose
- Theme change based on system (Dark/light)

## Setup & Build Instructions

1. **Prerequisites:**
   - Android Studio Hedgehog | 2023.1.1 or newer
   - Android SDK 24 or higher

2. **Clone the repository:**
   ```sh
   git clone https://github.com/abir032/Postly.git
   cd Postly
   ```

3. **API Key Setup:**
   - Get your API key from [newsapi.org](https://newsapi.org/)
   - Update `ApiConfig.kt` at path: `app/src/main/java/com/example/postly/Config/`
   - Add your API key:
     ```kotlin
     object ApiConfig {
         const val API_KEY = "your_api_key_here"
     }
     ```

4. **Build & Run:**
   - Open project in Android Studio
   - Sync project with Gradle files
   - Run the app using `Run 'app'`

## Technical Architecture

The application follows Clean Architecture principles with MVVM pattern, ensuring separation of concerns and maintainability.

### Architecture Components

```
┌─────────────────────────────────────────────────────────────────────┐
│                           Presentation Layer                         │
├─────────────────┬─────────────────────────┬─────────────────────────┤
│   Compose UI    │    ViewModels           │   Navigation            │
│  (Screens)      │ (PostViewModel,         │  (AppNavigations)       │
│                 │  LoginViewModel)        │                         │
└────────┬────────┴──────────┬──────────────┴─────────────────────────┘
         │                    │
         │                    │
┌────────▼────────┐  ┌───────▼────────────────────────────────────┐
│     DI Layer    │  │              Domain Layer                   │
│    (Hilt)       │  │        Repository Pattern                   │
│ AppModule       │  │    IFPostRepository, IFUserRepository       │
└────────┬────────┘  └───────────────────┬───────────────────────┬┘
         │                                │                       │
         │           ┌──────────────────┐ │ ┌───────────────────┐│
         └──────────►│   Data Layer    │◄┘ │  Remote Layer     ││
                    │ Room Database    │   │  API Service      ││
                    │ (DAO, Entities) │   │  (NewsAPI)        ││
                    └──────────────────┘   └───────────────────┘│
                     └───────────────────────────────────────────┘
```

### Technology Stack

1. **UI Layer**
   - Jetpack Compose for modern UI development
   - Material3 design components
   - Navigation Compose for screen navigation
   - Lottie for animations
   - SwipeRefresh for pull-to-refresh functionality

2. **Business Logic**
   - ViewModel + StateFlow for state management
   - Kotlin Coroutines for asynchronous operations
   - Hilt for dependency injection
   - Repository pattern for data operations

3. **Data Layer**
   - Room Database for local storage
   - Retrofit for network operations
   - Flow for reactive data streams
   - DTOs for data mapping

### Libraries Used
```gradle
// Core Libraries
implementation("androidx.core:core-ktx:${versions.core}")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:${versions.lifecycle}")

// UI Libraries
implementation("androidx.activity:activity-compose:${versions.activityCompose}")
implementation("androidx.compose.material3:material3")
implementation("com.airbnb.android:lottie-compose:4.0.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.9.3")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Database
implementation("androidx.room:room-runtime:2.7.2")
implementation("androidx.room:room-ktx:2.7.2")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.56.2")
```
- **Libraries:**
  - Retrofit (networking)
  - Hilt (dependency injection)
  - Room (local database)
  - SwipeRefresh 
  - lottie-compose
  - Kotlin Coroutines and flow
  - navigation-compose 

## Current Limitations

### Authentication & User Management
- Basic local authentication implementation
- Session management is not implemented
- User preferences are not persisted across app reinstalls

### Data Management
- No relationship between posts and users in the database
- Favorite posts are shared across all users

### API Limitations
- NewsAPI free tier restrictions apply
- Limited to 100 requests per day
- No real-time updates
- Some content might be truncated

## Future Improvements
1. **Authentication**
   - Implement proper authentication system
   - user session management

2. **Data Management**
   - Add user-specific favorite articles
   - Add article categories and filters

## Screenshots & Video
Video:  [Click here](https://drive.google.com/file/d/1ntYsWBc5WmNSsJNvAcnhBB_KajfPmfUu/view?usp=sharing)


