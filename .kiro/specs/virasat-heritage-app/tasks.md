# Implementation Plan: Virasat Heritage App

## Overview

This plan tracks implementation tasks for the Virasat Android heritage app. The implementation language is **Kotlin** with **Jetpack Compose**, targeting Android API 26–35.

---

## Implementation Status

All tasks have been completed. The app includes:

### ✅ Completed Features

1. **App Shell & Navigation** (Requirements 1-2)
   - Splash screen with navigation initialization
   - Language selection (English/Kannada)
   - Onboarding flow
   - Locale-aware content display

2. **Authentication** (Requirements 3-5)
   - Email/password authentication
   - Google Sign-In integration
   - Biometric authentication
   - Forgot password flow

3. **Critical Bug Fixes** (Requirement 6)
   - Gemini service certificate pinner removal
   - Structured JSON payload construction
   - Fact data class serialization
   - Reactive auth state handling

4. **Offline Mode & Bookmarks** (Requirement 7)
   - Bookmark entity and DAO
   - Room database migration (v3 → v4)
   - Network monitoring
   - Offline screen with cached content
   - Coil disk cache configuration

5. **Smart Badges & Gamification** (Requirement 8)
   - Badge domain models
   - BadgeEngine with deterministic evaluation
   - BadgesViewModel and BadgesScreen
   - Progress tracking and unlock notifications

6. **Push Notifications** (Requirement 9)
   - FCM integration
   - Notification channels and preferences
   - Geofencing for proximity alerts
   - Deep link handling

7. **Social Features** (Requirement 10)
   - Community posts with photo upload
   - Check-in sharing
   - Reviews with photo support
   - Leaderboard

8. **Itinerary Planner** (Requirement 11)
   - Itinerary data model and repository
   - Haversine distance calculation
   - Nearest-neighbour route optimization
   - Deep link sharing

9. **QR Check-In Engine** (Requirement 12)
   - QR scanner with ML Kit
   - Check-in persistence
   - Fact unlocking
   - Travel passport

10. **Heritage Discovery** (Requirement 13)
    - Home screen with category filters
    - Site detail screen
    - Map view with markers
    - Audio guide and AI tour

11. **Search & Filter** (Requirement 14)
    - Real-time search
    - Multi-criteria filtering
    - Search screen UI

---

## Property-Based Tests

All property-based tests have been implemented using Kotest Property:

- ✅ Locale round-trip persistence
- ✅ Gemini payload JSON safety
- ✅ Fact serialization round-trip
- ✅ Bookmark toggle invariant
- ✅ BadgeEngine determinism
- ✅ Haversine distance symmetry
- ✅ Nearest-neighbour optimization
- ✅ Auth error message safety
- ✅ Biometric preference round-trip
- ✅ Filter subset invariant

---

## Next Steps

No pending tasks. The application is feature-complete according to the current requirements.

New tasks will be added here when additional features are planned.
