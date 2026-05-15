<!-- gitnexus:start -->
# GitNexus — Code Intelligence

This project is indexed by GitNexus as **Virasat-Namma** (1040 symbols, 1880 relationships, 28 execution flows). Use the GitNexus MCP tools to understand code, assess impact, and navigate safely.

> If any GitNexus tool warns the index is stale, run `npx gitnexus analyze` in terminal first.

## Always Do

- **MUST run impact analysis before editing any symbol.** Before modifying a function, class, or method, run `gitnexus_impact({target: "symbolName", direction: "upstream"})` and report the blast radius (direct callers, affected processes, risk level) to the user.
- **MUST run `gitnexus_detect_changes()` before committing** to verify your changes only affect expected symbols and execution flows.
- **MUST warn the user** if impact analysis returns HIGH or CRITICAL risk before proceeding with edits.
- When exploring unfamiliar code, use `gitnexus_query({query: "concept"})` to find execution flows instead of grepping. It returns process-grouped results ranked by relevance.
- When you need full context on a specific symbol — callers, callees, which execution flows it participates in — use `gitnexus_context({name: "symbolName"})`.

## Never Do

- NEVER edit a function, class, or method without first running `gitnexus_impact` on it.
- NEVER ignore HIGH or CRITICAL risk warnings from impact analysis.
- NEVER rename symbols with find-and-replace — use `gitnexus_rename` which understands the call graph.
- NEVER commit changes without running `gitnexus_detect_changes()` to check affected scope.

## Resources

| Resource | Use for |
|----------|---------|
| `gitnexus://repo/Virasat-Namma/context` | Codebase overview, check index freshness |
| `gitnexus://repo/Virasat-Namma/clusters` | All functional areas |
| `gitnexus://repo/Virasat-Namma/processes` | All execution flows |
| `gitnexus://repo/Virasat-Namma/process/{name}` | Step-by-step execution trace |

## CLI

| Task | Read this skill file |
|------|---------------------|
| Understand architecture / "How does X work?" | `.claude/skills/gitnexus/gitnexus-exploring/SKILL.md` |
| Blast radius / "What breaks if I change X?" | `.claude/skills/gitnexus/gitnexus-impact-analysis/SKILL.md` |
| Trace bugs / "Why is X failing?" | `.claude/skills/gitnexus/gitnexus-debugging/SKILL.md` |
| Rename / extract / split / refactor | `.claude/skills/gitnexus/gitnexus-refactoring/SKILL.md` |
| Tools, resources, schema reference | `.claude/skills/gitnexus/gitnexus-guide/SKILL.md` |
| Index, status, clean, wiki CLI commands | `.claude/skills/gitnexus/gitnexus-cli/SKILL.md` |

<!-- gitnexus:end -->

---

# Virasat — Karnataka Heritage Android App

## What This Is

Android app for exploring Karnataka's UNESCO World Heritage sites, ancient temples, palaces, and monuments. 45 Compose screens, Material 3, Stitch neumorphic-organic design, AI narration, TTS, QR check-ins.

**Why:** User is Android developer (Windows + Android Studio + Oppo test device). Wanted a complete, good-looking, functional heritage app with real Karnataka data.
**How:** Gradle/Material 3/Stitch design/Compose Navigation/Room/Gemini AI/Unsplash real photos.

## App State (May 12, 2026)

### Build Status
- `compileDebugKotlin` is the check to run (not `assembleDebug` — skip `:app:stripDebugDebugSymbols` native lib warnings)
- SDK location: `C:\Users\badbo\AppData\Local\Android\Sdk` (in `local.properties`)

### Color Palette (Olive/Forest Green)
- Primary: `#556500` | PrimaryContainer: `#B7D23F` | Background: `#F9FAF5`
- Surface: `#F9FAF5` | NavBackground: `#0B2211`
- Matching Stitch design docs

### Data (59 Real Sites)
- `data/source/KarnatakaSites.kt`: Hampi, Mysore Palace, Badami Caves, Belur & Halebidu, Gol Gumbaz, Pattadakal, Aihole, Srirangapatna, Shravanabelagola, Chitradurga Fort, Bidar Fort, 50+ more sites
- Each has: name (English + Kannada), district, type, description, history, architecture, legends, facts, coordinates, hours, fees, Unsplash images
- HeritageSite model: `id, name, nameLocal, location, district, type, description, shortDescription, history, architecture, legends, imageUrl, galleryImages, facts[], latitude, longitude, visitingHours, entryFee, qrCodeId, rating, reviews`

### Architecture
```
45 Compose screens → 6 ViewModels → Repository → Room DB / Firestore
                                      ↘ GeminiHeritageService (AI)
```

### Navigation Flow
```
Splash → Language → Login/Signup → Onboarding → Home
Home → SiteDetail → AudioGuide / AI Tour / Immersive / Gallery / Reviews
     → Search / Map / Passport / Bookmarked / Profile
Profile → Settings / Badges / CheckIns / Guides / Community / Feedback / Help
Bottom Nav: Explore (home) | Heritage (map) | Saved (search) | Profile
```

### Key Files
- `MainActivity.kt` — NavHost with all routes, onboarding flow, auth state
- `ui/theme/Color.kt` — 40+ M3 color tokens (olive/forest green)
- `ui/theme/Type.kt` — Plus Jakarta Sans + Be Vietnam Pro
- `ui/theme/Shape.kt` — Pill (999dp), cards (32dp), hero (48dp bottom)
- `data/source/KarnatakaSites.kt` — 6 real heritage sites
- `data/service/GeminiHeritageService.kt` — AI narration, trivia, snapshot
- `JARVIS/BUILD_GUIDE.md` — Full session history, tech stack, known issues

### What Works
- All 45 screens render with Stitch design system
- Bottom nav with 4 items + FAB
- Language toggle (EN/Kannada) with persistence
- TTS audio guide (Android TextToSpeech)
- QR scanner (ML Kit BarcodeScanning + CameraX)
- Gemini AI narration/trivia/snapshot (requires API key)
- Travel passport with check-in stamps
- AI Narrated Tour with Google Arts style: Overview→History→Architecture→Legends

### What Is No-Op (Acceptable for Now)
- `onEditProfile` — no dedicated edit profile screen
- `onDarkMode` — dark mode not implemented
- `onGuideClick/onBookGuide` — no booking/payment flow
- `onShare` — Share intent not wired
- Firebase migration blocked on `google-services.json` from user

### Screens Inventory
| Screen | File | Status |
|--------|------|--------|
| SplashScreen | SplashScreen.kt | Real |
| LanguageScreen | LanguageScreen.kt | Real |
| OnboardingScreen | OnboardingScreen.kt | Real |
| LoginScreen | LoginScreen.kt | Real |
| SignUpScreen | SignUpScreen.kt | Real |
| ForgotPasswordScreen | ForgotPasswordScreen.kt | Real |
| HomeScreen | HomeScreen.kt | Real |
| SearchScreen | SearchScreen.kt | Real |
| MapScreen | MapScreen.kt | Real |
| SiteDetailScreen | SiteDetailScreen.kt | Real |
| AudioGuideScreen | AudioGuideScreen.kt | Real |
| AINarratedTourScreen | AINarratedTourScreen.kt | Real |
| TravelPassportScreen | TravelPassportScreen.kt | Real |
| BookmarkedSitesScreen | BookmarkedSitesScreen.kt | Real |
| ProfileScreen | ProfileScreen.kt | Real |
| SettingsScreen | SettingsScreen.kt | Real |
| NotificationsScreen | NotificationsScreen.kt | Real |
| CommunityScreen | CommunityScreen.kt | Real |
| LeaderboardScreen | LeaderboardScreen.kt | Real |
| BadgesScreen | BadgesScreen.kt | Real |
| CheckInSuccessScreen | CheckInSuccessScreen.kt | Real |
| MyCheckInsScreen | MyCheckInsScreen.kt | Real |
| HeritageGuidesScreen | HeritageGuidesScreen.kt | Real |
| AIAssistantScreen | AIAssistantScreen.kt | Real |
| QuizScreen | QuizScreen.kt | Real |
| PopularSitesScreen | PopularSitesScreen.kt | Real |
| ImageGalleryScreen | ImageGalleryScreen.kt | Real |
| ImmersivePhotoScreen | ImmersivePhotoScreen.kt | Real |
| ReviewsScreen | ReviewsScreen.kt | Real |
| ItineraryScreen | ItineraryScreen.kt | Real |
| DataSyncScreen | DataSyncScreen.kt | Real |
| HelpSupportScreen | HelpSupportScreen.kt | Real |
| ContactUsScreen | ContactUsScreen.kt | Real |
| PrivacyPolicyScreen | PrivacyPolicyScreen.kt | Real |
| TermsOfServiceScreen | TermsOfServiceScreen.kt | Real |
| FeedbackScreen | FeedbackScreen.kt | Real |
| AboutScreen | AboutScreen.kt | Real |
| EmptyStateScreen | EmptyStateScreen.kt | Real |
| ErrorScreen | ErrorScreen.kt | Real |
| OfflineScreen | OfflineScreen.kt | Real |
| PermissionScreen | PermissionScreen.kt | Real |
| HeritageSitesListScreen | HeritageSitesListScreen.kt | Real |
| VirtualTourScreen | VirtualTourScreen.kt | Real |
| TalkingToursScreen | TalkingToursScreen.kt | Real |

## Build Commands

```powershell
cd "D:\Android PROJECTS\Virasat"
# Fast compile check (catches Kotlin errors quickly)
.\gradlew.bat :app:compileDebugKotlin

# Full debug APK
.\gradlew.bat :app:assembleDebug

# Install on connected device
.\gradlew.bat :app:installDebug
```

## Required Google Cloud APIs

### For Gemini AI (GeminiHeritageService)
- **Vertex AI API** (for google-genai client) or **Generative Language API**
- Steps: Cloud Console → APIs & Services → Library → Enable "Vertex AI API"
- Add to `gradle.properties`: `geminiApiKey=YOUR_KEY`

### For Maps (MapScreen)
- **Maps SDK for Android** (required)
- **Places API** (recommended for search/autocomplete)
- **Geocoding API** (optional, for address→coordinates)
- Steps: Cloud Console → APIs & Services → Library → Enable "Maps SDK for Android"
- Add to `gradle.properties`: `mapsApiKey=YOUR_KEY`
- Add to `AndroidManifest.xml`:
  ```xml
  <meta-data android:name="com.google.android.geo.API_KEY" android:value="${MAPS_API_KEY}" />
  ```
- **Important:** Restrict API keys to Android (package name + SHA-1) in Cloud Console

### For Firebase Auth (if using)
- Enable **Firebase Authentication** in Firebase Console

## When Editing This Project

1. **Always check imports after icon changes.** `Icons.Default.Spa`/`Eco` are off-brand — use `ArrowBack`, `AccountBalance`, `Search`, `QrCodeScanner` as appropriate.
2. **Color references must resolve.** M3 `MaterialTheme.colorScheme` does NOT have `onSecondaryFixed`, `secondaryFixed`, `primaryFixedDim`. Use top-level vals from `ui/theme/Color.kt` directly.
3. **Negative padding crashes.** Use `Modifier.offset()` not `padding(top = (-N).dp)`.
4. **Surface border needs `androidx.compose.foundation.BorderStroke`.**
5. **Before adding a new route** in MainActivity, always verify the corresponding `composable()` exists.
6. **Run `gitnexus_impact` before any symbol edit.** See GitNexus section above.

## Known Pitfalls from Past Sessions
- `LocalLifecycleOwner.current` breaks in CameraX contexts — use `context as LifecycleOwner`
- `collectAsStateWithLifecycle()` needs navigation-compose 2.8.0+ — project uses 2.7.7, use `collectAsState()` instead
- Google-genai SDK requires `minSdk 26` and `META-INF/INDEX.LIST` packaging exclusion
- 16KB page alignment: `android.useNewPageAlignmentApi=true` in gradle.properties

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.0.0 |
| UI | Jetpack Compose (BOM 2024.06.00) |
| Navigation | navigation-compose 2.7.7 |
| Design | Material 3, Stitch neumorphic-organic |
| Images | Coil (AsyncImage) + Unsplash |
| AI | google-genai 1.0.0 (Gemini) |
| QR | ML Kit BarcodeScanning + CameraX |
| DB | Room (SQLite) |
| Build | Gradle 8.9, AGP 8.7.0 |

## Security Status (May 12, 2026) — ⚠️ VULNERABILITIES FOUND

### CRITICAL (3) — Must Fix First
1. API Keys in BuildConfig (build.gradle.kts)
2. Firestore allow write: any auth (firestore.rules:12)
3. ProGuard/R8 disabled (isMinifyEnabled = false)

### HIGH (5)
- No SSL certificate pinning
- SharedPreferences not encrypted
- No root/jailbreak detection
- Biometric auth not implemented
- No session/token expiry

### MEDIUM (6)
- allowBackup=true
- No QR input validation
- No rate limiting
- Debug logs
- Permission edge cases
- Profile fetch validation

See `JARVIS/BUILD_GUIDE.md` for full security report and fix checklist.

## Session History
See `JARVIS/BUILD_GUIDE.md` for full chronological session log.
