# Design Document — Virasat Heritage App

## Overview

This document contains the technical design for the Virasat Android heritage app. All core features have been implemented.

### Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.0.0 |
| UI | Jetpack Compose (BOM 2024.09.00) |
| Target API | 26–35 |
| Architecture | Single-Activity MVVM |
| Remote DB | Firebase Firestore |
| Auth | Firebase Authentication (email + Google) |
| Push | Firebase Cloud Messaging (FCM) |
| Storage | Firebase Storage |
| Analytics | Firebase Analytics |
| Local DB | Room 2.6.1 (VirasatDatabase v4) |
| AI | Gemini 2.0 Flash REST API (GeminiHeritageService) |
| Maps | Google Maps SDK + Maps Compose 6.1.1 |
| QR Scanning | ML Kit BarcodeScanning 17.2.0 |
| Camera | CameraX 1.3.3 |
| Biometrics | Android BiometricPrompt (biometric:1.1.0) |
| Images | Coil 2.6.0 (disk + memory cache) |
| PBT Library | Kotest Property (`io.kotest:kotest-property-jvm:5.9.1`) |

---

## Architecture

### Pattern: Single-Activity MVVM

The app uses a single `MainActivity` hosting a Jetpack Compose `NavHost`. All screens are composable functions navigated via `NavController`. ViewModels are scoped to the NavBackStackEntry and obtained via `viewModel()`.

### Dependency Injection

Manual DI via `RepositoryProvider` object. No Hilt or Dagger. ViewModels receive the `Application` context and call `RepositoryProvider.getRepository(application)` to obtain the active repository implementation.

---

## Implementation Status

All requirements (1-14) have been fully implemented:

- ✅ App shell, navigation, and language selection
- ✅ Email/password and Google Sign-In authentication
- ✅ Biometric authentication
- ✅ Critical bug fixes (Gemini service, Fact serialization, auth state)
- ✅ Offline mode and bookmarks
- ✅ Smart badges and gamification engine
- ✅ Push notifications and geofencing
- ✅ Social sharing and community features
- ✅ Itinerary planner with route optimization
- ✅ QR code check-in engine
- ✅ Heritage site discovery and detail screens
- ✅ Site search and filter functionality

---

## Notes

This document will be updated when new features are designed.
