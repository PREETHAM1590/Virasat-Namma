# Virasat Android App - Build Guide

> **IMPORTANT:** Update after EVERY session. Track real state.

---

## Current Status

**Last Updated:** May 8, 2026
**Build Status:** `./gradlew assembleDebug` passes
**Screens:** 45 Kotlin screen files, all wired in NavHost
**Design System:** Stitch-generated neumorphic-organic (project 4939368762707624040)

---

## Completed

### Stitch Design Migration (May 8, 2026)
All 45 screens rewritten to match Google Stitch-generated design system.

**Color Palette (olive/lime, forest green, pale mint):**
- Primary: `#556500` | OnPrimary: `#FFFFFF` | PrimaryContainer: `#B7D23F` | OnPrimaryContainer: `#4A5800`
- Secondary: `#4C644F` | OnSecondary: `#FFFFFF` | SecondaryContainer: `#CEEACE` | OnSecondaryContainer: `#526A54`
- Surface: `#F9FAF5` | OnSurface: `#1A1C19` | SurfaceContainerLowest: `#FFFFFF` | SurfaceContainerLow: `#F3F4EF`
- NavBackground: `#0B2211` (dark forest green detached pill)
- Error: `#BA1A1A` | ErrorContainer: `#FFDAD6`
- Outline: `#767964` | OutlineVariant: `#C6C8B0`

**Typography:**
- Headline: Plus Jakarta Sans (48sp/700 displayLarge, 32sp/600 headlineLarge)
- Body: Be Vietnam Pro (18sp bodyLarge, 14sp/600 labelLarge)
- Full 15-style Material 3 typography scale

**Shapes:**
- Buttons/Chips: pill (`RoundedCornerShape(999.dp)`)
- Cards: 32dp radius (`RoundedCornerShape(32.dp)`)
- Hero images: 48dp radius bottom corners
- Inputs: pill shape with inset neumorphic shadow

**Navigation:**
- Detached floating pill nav bar in `#0B2211`, 16dp above bottom safe area
- 4 items: Explore, Heritage, Saved, Profile (filled icon for active)
- Center elevated FAB for primary action

**Theme files:** `ui/theme/Color.kt`, `Type.kt`, `Shape.kt`, `Theme.kt`

### All 45 Screens Wired in NavHost
- SplashScreen, LanguageScreen, OnboardingScreen (3 pages)
- LoginScreen, SignUpScreen, ForgotPasswordScreen
- HomeScreen, SiteDetailScreen, QrScannerScreen
- AudioGuideScreen, TravelPassportScreen, BookmarkedSitesScreen
- ProfileScreen, SettingsScreen, NotificationsScreen
- MapScreen, HeritageSitesListScreen, SearchScreen
- ReviewsScreen, CommunityScreen, LeaderboardScreen
- MyCheckInsScreen, ImageGalleryScreen
- CheckInSuccessScreen, BadgesScreen, HeritageGuidesScreen
- AIAssistantScreen, QuizScreen, VirtualTourScreen, ARScreen
- ImmersivePhotoScreen, AINarratedTourScreen (360° + Gemini AI)
- ContactUsScreen, PrivacyPolicyScreen, TermsOfServiceScreen
- FeedbackScreen, DataSyncScreen, HelpSupportScreen, AboutScreen
- EmptyStateScreen, ErrorScreen, OfflineScreen, PermissionScreen

### Navigation Flow
```
SplashScreen → LanguageScreen → OnboardingScreen → LoginScreen → SignUpScreen → HomeScreen
                                              (if onboarding_complete skip)
                                                      ↓
                                          ├── SiteDetailScreen
                                          │      ├── AudioGuideScreen
                                          │      ├── AINarratedTourScreen
                                          │      ├── ImmersivePhotoScreen
                                          │      └── ReviewsScreen
                                          ├── SearchScreen, MapScreen
                                          ├── HeritageSitesListScreen
                                          ├── TravelPassportScreen
                                          ├── CommunityScreen, LeaderboardScreen
                                          ├── BookmarkedSitesScreen
                                          └── ProfileScreen → SettingsScreen
```

### AI & Media Features
- **Gemini AI Tour** — `google-genai:1.0.0` SDK via BuildConfig key
- `GeminiHeritageService.kt`: narration, trivia, snapshot description
- `AINarratedTourScreen.kt`: 360° Pannellum viewer + TTS + AI controls
- **TTS**: Android `TextToSpeech` for audio narration in English/Kannada
- **Language toggle**: English/Kannada with SharedPreferences persistence
- **Focus modes**: overview, history, architecture, legends

### Core Architecture
- Data Models: HeritageSite.kt, CheckIn.kt, UnlockedFact.kt
- Room Database: VirasatDatabase.kt with CheckInDao, UnlockedFactDao
- Repository: HeritageRepository.kt
- Data Source: KarnatakaSites.kt — 6 heritage sites with real Unsplash images
- ViewModels: HomeViewModel, DetailViewModel, PassportViewModel, QrScannerViewModel

### Build Fixes Applied
- `collectAsStateWithLifecycle()` → `collectAsState()` for navigation-compose 2.7.7 compatibility
- `LocalLifecycleOwner.current` → `context as LifecycleOwner` in QrScannerScreen
- minSdk 26 for google-genai SDK (auto-value transitive dep)
- META-INF/INDEX.LIST packaging excludes for google-genai conflicts
- BuildConfig field for GEMINI_API_KEY
- 16KB page alignment: `android.useNewPageAlignmentApi=true` (AGP 8.7.0)

### Known Warnings (Non-blocking)
- Several `@Deprecated` icon warnings (RotateRight, VolumeUp, ArrowForward, List, Help) — use AutoMirrored variants. Safe to ignore for now.
- `updateConfiguration()` deprecated in LanguageScreen — functional.

---

## Runtime Fixes (May 8, 2026)

### Fixed: LoginScreen crash — `IllegalArgumentException: Padding must be non-negative`
Decorative blob offsets used `padding(top = (-64).dp)` which crashes at runtime.
**Fix:** Changed to `offset(x = 64.dp, y = (-64).dp)` using Modifier.offset().

### Fixed: ~80 compilation errors across 20+ files
Causes and fixes documented in `.claude/memory/compose_build_patterns.md`:
1. M3 color tokens (`onSecondaryFixed`, `secondaryFixed`, `primaryFixedDim`) not in `MaterialTheme.colorScheme` — use top-level vals from Color.kt
2. `Modifier.background()` ambiguity when color is unresolved — ensure color resolves to known `Color` type
3. `Surface` `border` parameter needs `androidx.compose.foundation.BorderStroke`, not `androidx.compose.foundation.border.BorderStroke`
4. Missing icon imports: `Camera` → `Videocam`, `PhotoCamera`/`ThreeSixty`/`Eco`/`AspectRatio` → available base icons
5. `zIndex`, `graphicsLayer`, `CornerSize`, `Brush`, `clickable`, `clip` — missing imports
6. `padding(bottom = 64.dp, horizontal = 24.dp)` invalid param order — use `padding(start=, end=, bottom=)`

---

## What's Left

### Testing (Device)
- [ ] Verify LoginScreen no longer crashes with negative padding fix
- [ ] Test on physical device — all 45 screens navigate without crash
- [ ] Verify QR scanning works
- [ ] Verify TTS audio guide
- [ ] Verify Gemini AI narration (requires API key)
- [ ] Test check-in flow end-to-end
- [ ] Test language persistence across restarts
- [ ] Test offline behavior

### Firebase
- [ ] User to provide real `google-services.json` (Firebase project)
- [ ] Firebase Auth (Email + Google sign-in)
- [ ] Firestore sync for check-ins
- [ ] FCM for notifications

### New Features
- [ ] Heritage Map View (Google Maps SDK)
- [ ] Nearby Heritage Sites (FusedLocationProvider)
- [ ] Route & Directions (Maps Directions API)
- [ ] Heritage Image Gallery (full-screen pager)
- [ ] Heritage Quiz Challenge
- [ ] User Badges & Achievements grid
- [ ] AI Heritage Assistant Chat
- [ ] Historical Narrative Story screens
- [ ] Local Legends and Folklore screens
- [ ] Heritage Events and Festivals calendar
- [ ] Map Discovery mode
- [ ] Offline mode (audio caching, offline-first)
- [ ] Social sharing features

### Deployment
- [ ] Create keystore for release signing
- [ ] Build release APK/AAB
- [ ] Google Play Store listing

---

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Clean build
./gradlew clean assembleDebug
```

---

## Quick Reference

| Item | Detail |
|------|--------|
| **SDK** | compileSdk 35, minSdk 26 |
| **Gradle** | 8.9, AGP 8.7.0 |
| **Kotlin** | 2.0.0 |
| **Compose** | BOM 2024.06.00 |
| **Navigation** | navigation-compose 2.7.7 |
| **AI SDK** | google-genai 1.0.0 |
| **Repo** | https://github.com/PREETHAM1590/Virasat-Namma.git |
| **Branch** | master |
| **Gemini Key** | Settings → Developer → Set Gemini API Key |
| **Design** | Stitch project 4939368762707624040 |

### Key Files
- `app/build.gradle.kts` — dependencies, BuildConfig, packaging
- `MainActivity.kt` — NavHost with 45 routes, language/onboarding flow
- `ui/theme/Color.kt` — Stitch M3 palette (40+ color tokens)
- `ui/theme/Type.kt` — Plus Jakarta Sans + Be Vietnam Pro typography
- `ui/theme/Shape.kt` — Neumorphic radii (8/16/24/32/48 dp)
- `ui/theme/Theme.kt` — StitchColorScheme wired to MaterialTheme
- `ui/components/VirasatBottomNavBar.kt` — Detached pill nav bar
- `KarnatakaSites.kt` — 6 heritage sites data
- `GeminiHeritageService.kt` — AI narration/trivia/snapshot
- `AINarratedTourScreen.kt` — 360° viewer + AI controls

### Architecture
```
UI Layer (45 screens) → ViewModels (4) → Repository → Room DB + KarnatakaSites
                                              ↘ GeminiHeritageService (API)
```

---

## Session Log

### Session: May 5-6, 2026 — Full Implementation
- Created BUILD_GUIDE.md
- Built all 44 screens
- Added Material 3 design system, bottom nav, language persistence
- Fixed LocalLifecycleOwner crashes
- Fixed Gemini SDK integration (google-genai 1.0.0)
- Added 360° viewer, TTS, AI narration/trivia/snapshot
- Updated all images to real Unsplash photos
- Security scan: clean
- Debug APK builds successfully

### Session: May 7-8, 2026 — Stitch Design Migration
- Complete rewrite of all 45 screens to match Stitch-generated design
- Replaced old maroon/gold/serif with olive/lime neumorphic-organic design
- Updated Color.kt, Type.kt, Shape.kt, Theme.kt with new tokens
- Added detached floating nav bar component
- Fixed ~80 compilation errors (missing imports, non-existent M3 colors, wrong icon names, Surface border type, negative padding)
- Fixed LoginScreen runtime crash (`padding` with negative values → `offset()`)
- Build passes clean, only deprecation warnings remain
