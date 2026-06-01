
https://github.com/user-attachments/assets/f60e8969-3cff-4810-a319-e94044772123

# WordMaster (Fishbowl) — Android

WordMaster is an Android implementation of the classic "Fishbowl" / party-word-guessing game. Players (in teams) contribute words, then take turns trying to get their teammates to guess as many words as possible within a timed round. This repository contains the Android app code, layouts, and database helper used by the app.

--

## Table of contents

- Overview
- App Flow (screens)
- Features
- Prerequisites
- Setup & Build
- Run on device / emulator
- Landscape / multi-orientation support (how it works and how to extend)
- Project structure
- Database / words model
- How to test
- Contribution & development notes
- Troubleshooting
- License

--

## Overview

The WordMaster Android app implements the core game flow:

1. Home screen (start / how to play)
2. Lobby / game settings (team names, number of players, words per player)
3. Word input screen — players enter their words
4. Ready-to-play screen — shows which team starts, round, and score
5. Gameplay screen — shows a single word card, 60s timer, Skip (recycle) and Tick (correct) buttons
6. End game screen — final score, winner, replay/back to home
7. How to Play screen (rules/instructions)

The app uses a simple local SQLite database (helper class `DatabaseHelper`) to store the pool of words and their used/unused state during a game.

--

## Features

- Enter words per player and persist them in-app
- Round-based play (3 rounds):
  - Round 1: Περιγραφή (Description)
  - Round 2: Παντομίμα (Pantomime)
  - Round 3: Μία Λέξη (One Word)
- Gameplay with a 60-second countdown timer
- Skip button: recycles the current word back into the pool
- Tick button: marks the word guessed and awards a point to the current team
- Word counter showing remaining / total words
- Score and round management across teams
- Designed UI that maps to the app flow (see app layouts in `app/src/main/res/layout`)

--

## Prerequisites

- Java JDK 11 or newer (installed and JAVA_HOME configured)
- Android SDK (platforms and build tools for the project's compileSdkVersion)
- Android Studio (recommended) or Gradle CLI
- Gradle wrapper is included in the repository; you can use `./gradlew` (Linux/macOS) or `gradlew.bat` (Windows)


## Setup & Build (Windows / PowerShell)

Open PowerShell and run these commands from the project root (`WordMaster-AndroidApp`):

```powershell
# change directory to project root
cd "C:\Users\User\Desktop\themata\android dev\WordMaster-AndroidApp"

# build the project (assemble debug)
.\gradlew assembleDebug --no-daemon

# or compile Java sources only
.\gradlew compileDebugJavaWithJavac --no-daemon
```

To open and work with the project interactively, open it in Android Studio: File → Open... and select the project directory.


## Run on device / emulator

- Use Android Studio: Run → Select a device → Run app
- Or use adb with the generated APK (debug build path):

```powershell
# assemble debug APK
.\gradlew assembleDebug --no-daemon

# install (example path, adjust for your module & Gradle output)
adb install -r app\build\outputs\apk\debug\app-debug.apk
```


## Landscape / multi-orientation support

Requirement: make every activity functional and fully viewable in landscape mode.

What the app currently does:
- Layouts are located in `app/src/main/res/layout` for portrait by default.

Recommended approach (what has been applied or should be applied during development):

1. Provide landscape-specific layouts: create matching files in `app/src/main/res/layout-land/` for activities that require different arrangements. Android will automatically load the `layout-land` variant when the device is in landscape.
2. Use responsive containers: prefer `ConstraintLayout` or nested `LinearLayout` with `ScrollView` for content that may overflow. This allows the UI to adapt in both portrait and landscape without fully separate layouts in some cases.
3. Set flexible text sizes and use `wrap_content`/`0dp` constraints with weights or constraints to avoid clipping.
4. Avoid forcing a single screen orientation in `AndroidManifest.xml` unless necessary. If an activity should always be landscape, set `android:screenOrientation="landscape"`. Otherwise let the system handle rotations.
5. Test on multiple screen sizes and densities (tablet landscape is common). In Android Studio, use the Layout Inspector and rotate the preview.

Practical checklist to fully support landscape for all activities:
- [ ] Inspect each layout in `app/src/main/res/layout` and decide if a `-land` variant is needed.
- [ ] Create `layout-land` variants and rearrange controls for horizontal space (e.g., timer and card side-by-side with controls).
- [ ] Wrap long vertically stacking content in a `ScrollView` or use two-column layout in landscape.
- [ ] Ensure any background drawables or images scale correctly (use 9-patch when appropriate).
- [ ] Verify `android:configChanges` is NOT misused. Prefer `onSaveInstanceState`/`ViewModel` to preserve state across rotations.

Tip: For the `Gameplay` screen, a good landscape layout is a horizontal split: left side large card + timer, right side Skip / Tick buttons stacked vertically with score and team name above.


## Project structure (high level)

- app/
  - src/main/java/...
    - activities/ (HomeActivity, TurnActivity, GameplayActivity, etc.)
    - data/ (DatabaseHelper.java)
  - src/main/res/layout/ (activity xml files)
  - src/main/res/values/ (strings, colors, dimens)
  - build.gradle.kts (module build file)


## Database & Data model

- The app uses a local SQLite DB through a `DatabaseHelper` helper class.
- Words are stored with flags indicating `used` or `unused` for the current game. Typical columns:
  - id (int primary key)
  - word (text)
  - contributor (optional player/team id)
  - is_used (0/1)

Useful helper methods typically available or recommended in `DatabaseHelper`:
- `addWord(word)`
- `getRandomUnusedWord()`
- `markWordAsUsed(wordId)`
- `resetAllWordsToUnused()`
- `getUnusedWordCount()` / `getTotalWordCount()`


## How the rounds and titles map

The app uses three rounds, each with a different title that affects how words are conveyed:
- Round 1 — "Περιγραφή" (Description)
- Round 2 — "Παντομίμα" (Pantomime)
- Round 3 — "Μία Λέξη" (One Word)

The `TurnActivity` displays the current round number and the appropriate game title, and shows a word counter x/y representing "words left to be won / total words".


## How to test

Manual testing checklist:
- Add words via the Word Input screen (ensure they are saved to the DB)
- Start a game and verify the Ready-to-Play screen shows current team and round
- In Gameplay:
  - Verify timer counts down from 60 seconds
  - Press "Tick" — the current word is marked used and team score increments
  - Press "Skip" — the current word is recycled (remains unused)
  - Confirm the word counter updates appropriately (remaining / total)
- Switch device orientation to landscape during gameplay and confirm UI adapts and state remains (timer and current word should not reset on rotation)
- End game → confirm final results and replay or back to home behavior

Automated testing:
- Add unit tests for `DatabaseHelper` methods (word counts, mark used/reset) using Robolectric or instrumentation tests as needed.


## Contribution & development notes

- Prefer using Android Studio for editing and running the app.
- Keep UI strings in `res/values/strings.xml` for localization.
- Use `ViewModel` and `LiveData` (or Jetpack libraries) to preserve UI state across rotations rather than locking orientation.
- When adding new layouts, include `layout-land` variants if the orientation changes require major rearrangement.
- Add new database migrations (if schema changes) to avoid data loss on upgrade.


## Troubleshooting

- Gradle build errors: run `./gradlew clean assembleDebug --stacktrace` and inspect the stacktrace.
- Missing SDK platforms: install the required Android SDK platform via the SDK Manager matching the project's `compileSdkVersion`.
- Layout clipping in landscape: create a `layout-land` variant and switch to a two-column or scrollable layout.


## TODO / Roadmap

- Add color sellections for each team.
- Add language support (English and Greek).
- Make the main Screen look better.
- Add unit and UI tests for game flow.


## License
-no license yet. 
