# Virasat Android App - Build Guide

> **IMPORTANT:** Update after EVERY session. Track real state.

---

## Current Status

**Last Updated:** May 12, 2026
**Build Status:** `./gradlew assembleDebug` passes ✅
**Screens:** 45 Kotlin screen files, all wired in NavHost
**Heritage Sites:** 59 Karnataka heritage sites
**Design System:** Stitch-generated neumorphic-organic (olive/forest green palette)
**Security:** ⚠️ 3 CRITICAL, 5 HIGH, 6 MEDIUM vulnerabilities found — See Security Analysis

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.0.0 |
| UI | Jetpack Compose (BOM 2024.05.00) |
| Navigation | navigation-compose 2.7.7 |
| Design | Material 3, Stitch neumorphic-organic |
| Images | Coil (AsyncImage) + Unsplash |
| AI | google-genai 1.0.0 (Gemini REST API) |
| QR | ML Kit BarcodeScanning + CameraX |
| DB | Room (SQLite) |
| Backend | Firebase (Firestore, Auth, Analytics) |
| Build | Gradle 8.9, AGP 8.7.0 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 |

---

## Architecture

```
45 Compose Screens → 6 ViewModels → Repository → Room DB / Firestore
                                        ↘ GeminiHeritageService (AI)
```

### Data Layer
- `data/model/` — HeritageSite, CheckIn, UnlockedFact, SiteType enum
- `data/local/` — VirasatDatabase, 3 DAOs, TypeConverters
- `data/repository/` — HeritageRepository (Firebase + Room implementations)
- `data/source/` — KarnatakaSites (59 sites), FirestoreDataSource, FirestoreSeeder

### Service Layer
- `GeminiHeritageService.kt` — AI narration, trivia, chat, vision analysis
- `FirebaseAuthService.kt` — Email + Google authentication
- `FirebaseAnalyticsHelper.kt` — Analytics tracking
- `WeatherService.kt` — Open-Meteo API integration

### ViewModel Layer
- HomeViewModel — Search, type filter, district filter
- DetailViewModel — Site loading, check-in, fact unlocking
- PassportViewModel — Check-in stamps, counts
- AuthViewModel — Login, signup, password reset, Google auth
- QrScannerViewModel — QR processing, check-in logic
- WeatherViewModel — Weather state management

### UI Layer
- 45 screens in `ui/screens/`
- Theme: Color.kt (olive/forest green), Type.kt, Shape.kt, Theme.kt
- Components: VirasatBottomNavBar, AnimatedWeatherWidget

---

## Navigation Flow

```
SplashScreen → LanguageScreen → OnboardingScreen (3 pages)
    ↓
LoginScreen → SignUpScreen → ForgotPasswordScreen
    ↓
HomeScreen (with BottomNavBar)
    ├── SiteDetailScreen
    │   ├── AudioGuideScreen (TTS)
    │   ├── AINarratedTourScreen (Gemini AI)
    │   ├── ImmersivePhotoScreen
    │   ├── ImageGalleryScreen
    │   ├── ReviewsScreen
    │   └── CheckInSuccessScreen
    ├── SearchScreen
    ├── MapScreen (Google Maps)
    ├── HeritageSitesListScreen
    ├── TravelPassportScreen (stamps)
    ├── QrScannerScreen (ML Kit)
    ├── BookmarkedSitesScreen
    ├── ProfileScreen
    │   ├── SettingsScreen
    │   ├── BadgesScreen
    │   ├── MyCheckInsScreen
    │   └── CommunityScreen
    ├── AIAssistantScreen (Gemini chat)
    ├── QuizScreen
    ├── ItineraryScreen
    └── NotificationsScreen

Utility Screens: EmptyState, Error, Offline, Permission, About, Privacy, Terms, Help, Contact, Feedback, DataSync
```

---

## Design System

### Color Palette (Olive/Forest Green)
- Primary: `#556500` | OnPrimary: `#FFFFFF` | PrimaryContainer: `#B7D23F`
- Secondary: `#4C644F` | OnSecondary: `#FFFFFF` | SecondaryContainer: `#CEEACE`
- Background: `#F9FAF5` | OnSurface: `#1A1C19`
- NavBackground: `#0B2211` (dark forest green pill nav bar)
- Error: `#BA1A1A`

### Typography
- Headlines: Plus Jakarta Sans (SansSerif, bold/semi-bold)
- Body: Be Vietnam Pro (SansSerif, regular/medium)
- Scale: displayLarge (48sp) → labelSmall (11sp)

### Shapes
- Pill (buttons/chips): `RoundedCornerShape(999.dp)`
- Cards: `RoundedCornerShape(24-32.dp)`
- Hero images: `RoundedCornerShape(36.dp)` bottom corners

---

## Key Files

| File | Purpose |
|------|---------|
| `MainActivity.kt` | NavHost with all 45+ routes, auth flow |
| `app/build.gradle.kts` | Dependencies, BuildConfig, SDK versions |
| `ui/theme/Color.kt` | 40+ M3 color tokens |
| `KarnatakaSites.kt` | 59 heritage sites with full data |
| `GeminiHeritageService.kt` | AI narration, trivia, chat |
| `FirestoreDataSource.kt` | Firestore CRUD operations |
| `firestore.rules` | Security rules (⚠️ needs fix) |

---

## Build Commands

```powershell
# Quick compile check (catches Kotlin errors)
.\gradlew.bat :app:compileDebugKotlin

# Full debug APK
.\gradlew.bat :app:assembleDebug

# Install on connected device
.\gradlew.bat :app:installDebug

# Clean build
.\gradlew.bat clean assembleDebug
```

---

## Security Analysis (CRITICAL)

### CRITICAL Vulnerabilities (3) — Must Fix First

| # | Issue | Location | Fix |
|---|-------|----------|-----|
| 1 | API Keys in BuildConfig | `build.gradle.kts:28-29` | Move to server proxy |
| 2 | Firestore allow write: any auth | `firestore.rules:12` | Restrict to admin |
| 3 | ProGuard/R8 disabled | `build.gradle.kts:45` | Enable minification |

### HIGH Vulnerabilities (5)

| # | Issue | Fix |
|---|-------|-----|
| 4 | No SSL certificate pinning | Add pinning |
| 5 | SharedPreferences not encrypted | Use Security Crypto |
| 6 | No root/jailbreak detection | Add SafetyNet |
| 7 | Biometric auth not implemented | Add fingerprint/face |
| 8 | No session/token expiry | Add token refresh |

### MEDIUM Vulnerabilities (6)

| # | Issue | Fix |
|---|-------|-----|
| 9 | allowBackup=true | Set to false |
| 10 | No QR input validation | Add sanitization |
| 11 | No rate limiting on AI | Add throttling |
| 12 | Debug logs in release | Remove logs |
| 13 | Camera permission edge cases | Add checks |
| 14 | Firebase profile fetch validation | Add error handling |

### Security Fixes Checklist

- [ ] Enable R8 ProGuard (`isMinifyEnabled = true`)
- [ ] Fix Firestore rules — admin-only write
- [ ] Add EncryptedSharedPreferences
- [ ] Add SSL certificate pinning
- [ ] Add biometric authentication
- [ ] Disable allowBackup in manifest
- [ ] Add QR input validation
- [ ] Add rate limiting

### Dependencies — All Safe ✅
- OkHttp 4.12.0 ✅ | Room 2.6.1 ✅ | Firebase BOM 33.1.0 ✅
- Coil 2.6.0 ✅ | ML Kit 17.2.0 ✅ | CameraX 1.3.3 ✅ | Guava 33.2.0 ✅

---

## What's Left

### Security Fixes (CRITICAL - Do First)
- [ ] Enable R8 ProGuard
- [ ] Fix Firestore rules
- [ ] Add encrypted storage
- [ ] Add SSL pinning
- [ ] Add biometric auth
- [ ] Disable allowBackup

### Testing
- [ ] Verify all 45 screens navigate
- [ ] Test QR scanning
- [ ] Test TTS audio guide
- [ ] Test Gemini AI narration
- [ ] Test check-in flow
- [ ] Test language persistence
- [ ] Test offline mode

### Firebase
- [ ] Provide google-services.json
- [ ] Configure Firestore
- [ ] Setup FCM notifications

### New Features
- [ ] Route & directions (Maps API)
- [ ] Heritage events calendar
- [ ] Social sharing
- [ ] Push notifications

### Deployment
- [ ] Create release keystore
- [ ] Build release APK/AAB
- [ ] Play Store listing

---

## Session History

### Session: May 5-6, 2026 — Full Implementation
- Created all 44 screens
- Added Material 3 design, bottom nav, language toggle
- Integrated Gemini AI SDK
- Added 360° viewer, TTS, AI narration/trivia
- Updated images to real Unsplash photos

### Session: May 7-8, 2026 — Stitch Design Migration
- Complete rewrite to Stitch-generated design
- Replaced colors with olive/forest green palette
- Fixed ~80 compilation errors
- Fixed LoginScreen runtime crash (negative padding)

### Session: May 8, 2026 — Content & Identity Polish
- Fixed all fake data → real Karnataka sites
- Added route for AINarratedTourScreen in NavHost
- Fixed CheckInSuccessScreen callback signature

### Session: May 12, 2026 — Security Analysis & Update
- Full security scan of 100+ files
- Found 14 vulnerabilities (3 critical, 5 high, 6 medium)
- Expanded KarnatakaSites from 6 to 59
- Added WeatherService, AnimatedWeatherWidget
- Updated BUILD_GUIDE.md with security report

---

## Quick Reference

| Item | Detail |
|------|--------|
| **SDK** | compileSdk 35, minSdk 26 |
| **Gradle** | 8.9, AGP 8.7.0 |
| **Kotlin** | 2.0.0 |
| **Compose BOM** | 2024.05.00 |
| **Navigation** | 2.7.7 |
| **AI SDK** | google-genai 1.0.0 |
| **Repo** | https://github.com/PREETHAM1590/Virasat-Namma.git |
| **Design** | Stitch project 4939368762707624040 |

### API Keys (local.properties)
```
geminiApiKey=YOUR_KEY
mapsApiKey=YOUR_KEY
```

### Firebase Config
- `google-services.json` required in `app/` directory
- Firestore rules in `firestore.rules`
- Auth: Email + Google Sign-In enabled