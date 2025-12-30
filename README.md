# RunForYourLife - Android Obstacle Game

An engaging endless runner game developed for Android using Kotlin.
The player controls a dog navigating through a matrix of obstacles, collecting bones for points while avoiding the Grim Reaper.
The game features multiple control modes, sound effects, and a location-based leaderboard integrated with Google Maps.

## Features

### Gameplay Modes
* Buttons Mode: Classic control using Left/Right buttons to navigate lanes.
* Sensors Mode: Immersive control using the device's Accelerometer (Tilt control) to move the dog.

### Core Mechanics
* Dynamic Grid System: Logic-based matrix managing obstacles and bonuses.
* Lives System: Player starts with 3 lives. Crashing into an obstacle reduces health.
* Speed & Difficulty: Game speed increases over time.
* Sound Effects: Custom sounds for crashes and item collection.
* Haptic Feedback: Vibrations on collisions.

### Location & Data Persistence
* Top 10 Leaderboard: High scores are saved locally using SharedPreferences (Gson).
* GPS Integration: Utilizes FusedLocationProviderClient to fetch the real-world location where the high score was achieved.
* Google Maps Integration: View the exact location of each high score on an interactive map within the app.

## Tech Stack

* Language: Kotlin
* Architecture: MVC (Model-View-Controller)
* UI Components:
    * RecyclerView (for High Score list)
    * Fragments (for Map and List separation)
    * Material Design components (Cards, Buttons)
* Sensors: SensorManager (Accelerometer)
* Location Services: Play Services Location
* Maps: Google Maps SDK for Android
* Storage: SharedPreferences + Gson

## Getting Started

1. Clone the repository:
   git clone https://github.com/yahav97/RunForYourLife.git

2. Open in Android Studio.

3. Google Maps API Key:
   * Obtain an API Key from the Google Cloud Console.
   * Add it to your AndroidManifest.xml:
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="YOUR_API_KEY_HERE" />

4. Run the app on an emulator or a physical device.
