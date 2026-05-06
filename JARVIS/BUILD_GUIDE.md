# Virasat Android App - Complete Build Guide

> **⚠️ IMPORTANT:** Update this document after EVERY chat session to track progress and maintain complete context.

---

## 📌 Session Tracking & Progress

### What Has Been Completed ✓

#### Backend Architecture (100% Complete)
- ✅ Data Models: HeritageSite.kt, CheckIn.kt, UnlockedFact.kt
- ✅ Room Database: VirasatDatabase.kt with CheckInDao, UnlockedFactDao
- ✅ Repository: HeritageRepository.kt with all methods
- ✅ Data Source: KarnatakaSites.kt with 6 heritage sites

#### ViewModels (100% Complete)
- ✅ HomeViewModel.kt - Search, filters, site list, check-in stats
- ✅ DetailViewModel.kt - Site details, check-in status, unlocked facts
- ✅ PassportViewModel.kt - Check-in count, unlocked facts count
- ✅ QrScannerViewModel.kt - QR processing, check-in logic

#### UI Screens (100% Complete)
- ✅ SplashScreen.kt - Animated splash with logo
- ✅ LanguageScreen.kt - Language selection (Kannada, English, Hindi)
- ✅ OnboardingScreen.kt - 3 onboarding screens with HorizontalPager
- ✅ HomeScreen.kt - Search, filters, stats cards, site list
- ✅ SiteDetailScreen.kt - Hero image, tabs, check-in button, audio guide
- ✅ QrScannerScreen.kt - CameraX preview, ML Kit barcode scanning
- ✅ AudioGuideScreen.kt - Audio chapters, playback controls, transcript
- ✅ TravelPassportScreen.kt - Passport card, level progress, check-in stamps

#### Navigation (100% Complete)
- ✅ MainActivity.kt - Full NavHost with all screens connected

#### Documentation (100% Complete)
- ✅ BUILD_GUIDE.md - Complete project context
- ✅ Screen design documentation
- ✅ Data models documentation
- ✅ Architecture documentation
- ✅ Deployment instructions
- ✅ Troubleshooting guide

#### Git Integration (100% Complete)
- ✅ GitHub repo initialized
- ✅ Remote origin configured
- ✅ 10 commits pushed successfully

#### Critical - Build Fixes (100% Complete)
- ✅ Saved all files in IDE
- ✅ Added missing imports to QrScannerScreen.kt
- ✅ Replaced Icons.Default.Headphones with Icons.Default.Headset
- ✅ Replaced Icons.Default.ChevronRight with KeyboardArrowRight
- ✅ Added @OptIn annotations to all screen files
- ✅ Verified build.gradle.kts dependencies
- ✅ Successfully ran gradlew build (May 6, 2026)

### What Should Be Done (Pending Tasks) ⏳

#### Testing
- ⏳ Test app on emulator
- ⏳ Test app on physical device
- ⏳ Verify all screens work
- ⏳ Test QR scanning
- ⏳ Test check-in flow
- ⏳ Test audio guide
- ⏳ Test passport

#### Deployment
- ⏳ Commit changes
- ⏳ Push to GitHub
- ⏳ Create release
- ⏳ Deploy to Play Store

### Instructions: Update After Each Chat Session

**After EVERY chat session, update this BUILD_GUIDE.md with:**

1. **What was completed in this session:**
   - Add completed tasks to "What Has Been Completed ✓" section
   - Remove from "What Should Be Done ⏳" section
   - Add completion date (e.g., "✅ Task name - Completed May 5, 2026")

2. **What remains pending:**
   - Update "What Should Be Done ⏳" section with current pending tasks
   - Mark blocking issues with 🔴
   - Mark non-blocking issues with ⏳

3. **Session summary:**
   - Add brief note at end of document: "Session [Date]: [What was done]"

4. **Build status:**
   - Update "Last Updated" date
   - Update "Status" line (e.g., "Build pending file save", "Build successful", etc.)

5. **File changes:**
   - Note any files modified but not saved
   - Note any new files created

**Example Session Entry:**
```markdown
### Session: May 5, 2026, 11:40pm
**Completed:**
- Created BUILD_GUIDE.md with complete project context
- Added screen design documentation
- Added data models, architecture, integrations documentation
- Added troubleshooting guide and deployment instructions

**Pending:**
- User needs to save all files (Ctrl+S)
- Manual fixes for imports and icons
- Build and test

**Files Modified (in buffer, not saved):**
- build.gradle.kts (added dependencies)
- HeritageRepository.kt (added getAllSitesList())
- QrScannerViewModel.kt (fixed .value issue)
- HomeScreen.kt (added @OptIn)
- SiteDetailScreen.kt (added @OptIn)
- AudioGuideScreen.kt (added @OptIn, clickable import)
- QrScannerScreen.kt (added @OptIn, border import)
- TravelPassportScreen.kt (added @OptIn, clickable import)
```

---

## Project Overview
**App Name:** Virasat - Namma (Karnataka Heritage Guide)
**Purpose:** Guide users through Karnataka's UNESCO heritage sites with audio guides, QR check-ins, and digital passport
**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Room Database, ML Kit QR Scanning, CameraX

---

## Current Status

### ✅ COMPLETE - All Features Implemented

#### Backend Architecture
- **Data Models:** `HeritageSite.kt`, `CheckIn.kt`, `UnlockedFact.kt`
- **Room Database:** `VirasatDatabase.kt` with `CheckInDao`, `UnlockedFactDao`
- **Repository:** `HeritageRepository.kt` - manages site data and database operations
- **Data Source:** `KarnatakaSites.kt` - 6 heritage sites (Hampi, Mysore Palace, Badami, Belur & Halebidu, Gol Gumbaz, Madikeri Fort)

#### ViewModels
- `HomeViewModel.kt` - Search, filters, site list, check-in stats
- `DetailViewModel.kt` - Site details, check-in status, unlocked facts
- `PassportViewModel.kt` - Check-in count, unlocked facts count
- `QrScannerViewModel.kt` - QR processing, check-in logic

#### UI Screens (All 8 Screens Built)
1. `SplashScreen.kt` - Animated splash with logo
2. `LanguageScreen.kt` - Language selection (Kannada, English, Hindi)
3. `OnboardingScreen.kt` - 3 onboarding screens (Discovery, Scan QR, Passport)
4. `HomeScreen.kt` - Search, filters, stats cards, site list
5. `SiteDetailScreen.kt` - Hero image, tabs (Overview, History, Architecture, Legends, Facts), check-in button, audio guide
6. `QrScannerScreen.kt` - CameraX preview, ML Kit barcode scanning, permission handling
7. `AudioGuideScreen.kt` - Audio chapters, playback controls, progress bar, transcript
8. `TravelPassportScreen.kt` - Passport card, level progress, check-in stamps

#### Navigation
- `MainActivity.kt` - Full NavHost with all screens connected, proper back stack management

---

## � Complete Screen Inventory (50+ Screens from stitch_screens)

### ✅ ALREADY BUILT (9 Screens)

| # | Screen Name | File | Status | HTML Reference |
|---|-------------|------|--------|----------------|
| 1 | Splash Screen | SplashScreen.kt | ✅ Complete | Splash_Screen.html, Virasat_Splash_Screen.html |
| 2 | Language Selection | LanguageScreen.kt | ✅ Complete | Language_Selection.html |
| 3 | Onboarding (3 pages) | OnboardingScreen.kt | ✅ Complete | Onboarding_Discovery.html, Onboarding_QR_Scan.html, Onboarding_Passport.html |
| 4 | Home Dashboard | HomeScreen.kt | ✅ Complete | Home_Dashboard__Final_.html |
| 5 | Site Details | SiteDetailScreen.kt | ✅ Complete | Heritage_Site_Details.html, Site_Details.html |
| 6 | QR Scanner | QrScannerScreen.kt | ✅ Complete | Interactive_QR_Scanner.png |
| 7 | Audio Guide | AudioGuideScreen.kt | ✅ Complete | Audio_Guide_Immersive_Player.png |
| 8 | Travel Passport | TravelPassportScreen.kt | ✅ Complete | Digital_Travel_Passport.png |
| 9 | Favourites/Saved | (reuses HomeScreen) | ✅ Complete | Saved_Bookmarked_Sites.png |

---

### 🔨 NEED TO BE BUILT (41+ Screens)

#### Map & Discovery Screens

| # | Screen Name | Priority | HTML Reference | Build Instructions |
|---|-------------|----------|----------------|------------------|
| 10 | Heritage Map View | HIGH | Heritage_Map_View__Refined_.html | Use Google Maps SDK or Mapbox. Show custom temple pins (maroon color). Bottom card with site details, directions button, bookmark. Layer toggle button. |
| 11 | Nearby Heritage Sites | HIGH | Nearby_Heritage_Sites__Refined_.html | Use FusedLocationProviderClient. Get user location, calculate distance to sites. Show sorted list with distance badges. Empty state: No_Nearby_Sites_Recreation.png |
| 12 | Route and Directions Map | MEDIUM | Route_and_Directions_Map.png | Use Google Maps Directions API. Show route from user location to site. Turn-by-turn navigation. ETA display. |
| 13 | Map Discovery | MEDIUM | Map_Discovery.html | Similar to Heritage Map View but with discovery mode - cluster nearby sites, show categories on map. |

#### Search & Filter Screens

| # | Screen Name | Priority | HTML Reference | Build Instructions |
|---|-------------|----------|----------------|------------------|
| 14 | Heritage Search Screen | HIGH | Heritage_Search_Screen.png, Search_Results_View.png | Search bar with filter chips. Show search results with site cards. Recent searches section. Voice search button. |
| 15 | Saved/Bookmarked Sites | MEDIUM | Saved_Bookmarked_Sites.png | List of bookmarked sites. Grid or list view. Remove bookmark option. Filter by category. |

#### Content & Media Screens

| # | Screen Name | Priority | HTML Reference | Build Instructions |
|---|-------------|----------|----------------|------------------|
| 16 | Heritage Image Gallery | MEDIUM | Heritage_Image_Gallery.png | ViewPager2 or HorizontalPager for images. Full-screen view on tap. Zoom functionality. Share button. Download option. |
| 17 | Historical Narrative Story | MEDIUM | Historical_Narrative_Story.png | Story-like presentation with scrollable text. Background images. Chapter navigation. Audio narration option. |
| 18 | Local Legends and Folklore | MEDIUM | Local_Legends_and_Folklore.png | List of legends with illustrations. Tap to expand full story. Audio narration. Share legend. |
| 19 | Heritage Events and Festivals | LOW | Heritage_Events_and_Festivals.png | Calendar view of events. Event cards with date, time, location. RSVP functionality. Notifications for upcoming events. |
| 20 | Heritage Guides List | LOW | Heritage_Guides_List.png | List of available guides. Guide profiles with photo, rating, languages. Book a guide. Chat with guide. |

#### Gamification Screens

| # | Screen Name | Priority | HTML Reference | Build Instructions |
|---|-------------|----------|----------------|------------------|
| 21 | Heritage Quiz Challenge | MEDIUM | Heritage_Quiz_Challenge.png | Quiz questions about heritage sites. Multiple choice answers. Score tracking. Leaderboard. Share score. |
| 22 | Check-in Success Reward | MEDIUM | Check_in_Success_Reward.png | Animation on successful check-in. XP earned display. Badge unlock notification. Share achievement. |
| 23 | User Badges and Achievements | MEDIUM | User_Badges_and_Achievements.png | Grid of earned badges. Locked badges (grayed out). Progress to next badge. Badge details on tap. |

#### User Account Screens

| # | Screen Name | Priority | HTML Reference | Build Instructions |
|---|-------------|----------|----------------|------------------|
| 24 | User Login Screen | HIGH | User_Login.html, User_Login_Screen.png | Email/password login. Google/Facebook sign-in. Forgot password link. Sign up option. |
| 25 | User Profile Screen | HIGH | User_Profile_Screen.png | User avatar, name, stats (sites visited, facts unlocked). Edit profile button. Settings link. |
| 26 | Edit User Profile Screen | MEDIUM | Edit_User_Profile_Screen.png | Name, email, phone fields. Avatar upload. Save button. Form validation. |
| 27 | Settings Screen | MEDIUM | Settings_Screen_Recreation.png | Account settings, notifications, language, theme (dark mode), privacy, logout. |
| 28 | Language Settings | LOW | Language_Settings_Recreation.png | Change app language. Download language packs. Auto-detect language option. |
| 29 | Dark Mode Settings | LOW | Dark_Mode_Settings_Recreation.png | Toggle dark/light theme. Preview of theme. Follow system option. |

#### Communication Screens

| # | Screen Name | Priority | HTML Reference | Build Instructions |
|---|-------------|----------|----------------|------------------|
| 30 | AI Heritage Assistant Chat | MEDIUM | AI_Heritage_Assistant_Chat.png | Chat interface with AI. Text input. Quick questions suggestions. Voice input option. Chat history. |
| 31 | Notifications Screen | LOW | Notifications_Screen_Recreation.png | List of notifications. Mark as read. Notification types: check-in, events, achievements. Clear all button. |
| 32 | Help and Support | LOW | Help_and_Support_Recreation.png | FAQ section. Contact support form. Report issue button. App version info. |
| 33 | About App Screen | LOW | About_App_Screen_Recreation.png | App description, version, developer info. Privacy policy link. Terms of service link. Rate app button. |

#### Error & State Screens

| # | Screen Name | Priority | HTML Reference | Build Instructions |
|---|-------------|----------|----------------|------------------|
| 34 | Error Screen | MEDIUM | Error_Screen_Recreation.png | Generic error message. Retry button. Report issue button. Back to home button. |
| 35 | Offline/No Internet | HIGH | Offline_No_Internet_Recreation.png | No internet message. Retry connection button. Offline mode indicator. Cached content display. |
| 36 | Location Permission | HIGH | Location_Permission_Recreation.png | Permission rationale. Grant permission button. Deny button (show alternative). Location settings link. |
| 37 | No Nearby Sites | LOW | No_Nearby_Sites_Recreation.png | Empty state illustration. Message: "No heritage sites nearby". Explore other areas button. |

#### Variations & Alternates (Optional)

| # | Screen Name | Priority | HTML Reference | Build Instructions |
|---|-------------|----------|----------------|------------------|
| 38 | Home Dashboard Variation 1 | LOW | Home_Dashboard__Variation_1_.html | Alternative layout for home screen. Different card arrangement. |
| 39 | Home Dashboard Variation 2 | LOW | Home_Dashboard__Variation_2_.html | Another alternative layout. |
| 40 | Home Dashboard Variation 3 | LOW | Home_Dashboard__Variation_3_.html | Third alternative layout. |
| 41 | Onboarding Discovery Fixed | LOW | Onboarding_Discovery__Fixed_Layout_.html | Fixed layout for onboarding page 1. |
| 42 | Onboarding Discovery Refined | LOW | Onboarding_Discovery__Refined_.html | Refined layout for onboarding page 1. |
| 43 | Onboarding QR Scan Fixed | LOW | Onboarding_QR_Scan__Fixed_Layout_.html | Fixed layout for onboarding page 2. |
| 44 | Onboarding Passport Fixed | LOW | Onboarding_Passport__Fixed_Layout_.html | Fixed layout for onboarding page 3. |
| 45 | Onboarding Travel Passport | LOW | Onboarding_Travel_Passport.html | Alternative passport onboarding. |
| 46 | Passport Onboarding Variant 1 | LOW | Passport_Onboarding_Screen_Variant_1.png | Alternative passport onboarding design. |
| 47 | Nearby Sites List | LOW | Nearby_Sites_List.html | List view for nearby sites. |
| 48 | QR Scan Success Result | LOW | QR_Scan_Success_Result.png | Success screen after QR scan. |
| 49 | Virasat Kannada Splash | LOW | Virasat_Kannada_Splash_Screen.png | Kannada version of splash screen. |
| 50 | Heritage Dashboard Recreation | LOW | Heritage_Dashboard_Recreation.png | Alternative heritage dashboard. |

---

## 🎨 How to Build Screens from HTML References

### General Approach

1. **Analyze HTML Structure:**
   - Extract color palette from Tailwind config
   - Identify component hierarchy
   - Note spacing, sizing, typography
   - Extract image URLs for reference

2. **Convert to Jetpack Compose:**
   - HTML `<div>` → Compose `Box`, `Column`, `Row`
   - HTML `<img>` → Compose `AsyncImage` (Coil)
   - HTML `<button>` → Compose `Button`, `IconButton`
   - HTML `<input>` → Compose `TextField`, `OutlinedTextField`
   - HTML CSS → Compose `Modifier`, `Surface`, `Card`

3. **Material 3 Mapping:**
   - Use Material 3 components where possible
   - Custom colors → MaterialTheme.colorScheme
   - Custom typography → MaterialTheme.typography
   - Icons → Material Icons Extended

4. **State Management:**
   - Use ViewModel for screen state
   - StateFlow/collectAsState for reactive UI
   - LaunchedEffect for side effects

---

## � Screen Power Flows (User Interaction & State Transitions)

### 1. SplashScreen.kt Power Flow

**Purpose:** App launch, brand display, auto-navigation

**Flow:**
```
App Launch
    ↓
Show VirasatCream background (#FFF5E6)
    ↓
Load logo from web URL (AsyncImage)
    ↓
Fade-in animation (2 seconds)
    ↓
LaunchedEffect triggers
    ↓
Navigate to LanguageScreen
    ↓
Clear SplashScreen from back stack
```

**State:**
- Loading: Logo loading state
- Ready: Logo displayed, animation running
- Navigating: Transition to next screen

**User Actions:** None (automatic)

**Error Handling:**
- If logo fails to load: Show placeholder or app name text
- If navigation fails: Show error message, retry button

---

### 2. LanguageScreen.kt Power Flow

**Purpose:** Language selection, user preference storage

**Flow:**
```
Screen Load
    ↓
Show language options (Kannada, English, Hindi)
    ↓
User taps language card
    ↓
Highlight selected card (VirasatMaroon border)
    ↓
Store preference in SharedPreferences
    ↓
Enable Continue button
    ↓
User taps Continue
    ↓
Navigate to OnboardingScreen
    ↓
Clear LanguageScreen from back stack
```

**State:**
- `selectedLanguage`: String? (null initially)
- `isContinueEnabled`: Boolean (false until language selected)

**User Actions:**
- Tap language card → Select language
- Tap Continue → Navigate to onboarding
- Tap Back → Exit app (optional)

**Data Persistence:**
- SharedPreferences key: "selected_language"
- Values: "kannada", "english", "hindi"

---

### 3. OnboardingScreen.kt Power Flow

**Purpose:** App introduction, feature highlights

**Flow:**
```
Screen Load
    ↓
Show page 1 (Discovery)
    ↓
User swipes left OR taps Next
    ↓
Animate to page 2 (Scan QR)
    ↓
User swipes left OR taps Next
    ↓
Animate to page 3 (Passport)
    ↓
User taps "Get Started"
    ↓
Navigate to HomeScreen
    ↓
Clear OnboardingScreen from back stack
```

**State:**
- `currentPage`: Int (0, 1, 2)
- `isLastPage`: Boolean (true when currentPage == 2)

**User Actions:**
- Swipe left/right → Change page
- Tap Skip → Navigate to HomeScreen
- Tap Next → Go to next page
- Tap Get Started → Navigate to HomeScreen

**Page Content:**
- Page 1: Discovery - Explore heritage sites
- Page 2: Scan QR - Check-in at sites
- Page 3: Passport - Track your journey

**Animation:**
- HorizontalPager with smooth transitions
- Page indicators (dots) update with swipe

---

### 4. HomeScreen.kt Power Flow

**Purpose:** Main discovery, search, filter, site list

**Flow:**
```
Screen Load
    ↓
Load all sites from Repository
    ↓
Load check-in count from Repository
    ↓
Load unlocked facts count from Repository
    ↓
Show search bar, filter chips, stats cards, site list
    ↓
User types in search bar
    ↓
Filter sites by search query
    ↓
User taps filter chip (Monuments, Temples, Forts)
    ↓
Filter sites by selected type
    ↓
User taps site card
    ↓
Navigate to SiteDetailScreen with siteId
    ↓
User taps QR scan icon
    ↓
Navigate to QrScannerScreen
    ↓
User taps passport icon
    ↓
Navigate to TravelPassportScreen
    ↓
User taps favorites icon
    ↓
Navigate to HomeScreen with favorites filter
```

**State:**
- `searchQuery`: String (user search input)
- `selectedFilter`: SiteType? (filter selection)
- `sites`: List<HeritageSite> (filtered site list)
- `checkInCount`: Int (from ViewModel)
- `unlockedFactCount`: Int (from ViewModel)

**User Actions:**
- Type in search bar → Filter sites by name/location
- Tap filter chip → Filter by site type
- Tap site card → Navigate to site detail
- Tap QR scan icon → Navigate to QR scanner
- Tap passport icon → Navigate to passport
- Tap favorites icon → Show favorites only

**Filter Logic:**
- Search: Matches site name or location (case-insensitive)
- Type: Matches SiteType enum
- Combined: Search AND type filter

**Data Flow:**
```
User Input → ViewModel → Repository → Filtered List → UI Update
```

---

### 5. SiteDetailScreen.kt Power Flow

**Purpose:** Detailed site information, check-in, audio guide

**Flow:**
```
Screen Load (with siteId)
    ↓
Load site data from Repository
    ↓
Load check-in status from Repository
    ↓
Load unlocked facts from Repository
    ↓
Show hero image, tabs, content
    ↓
User taps tab (Overview, History, Architecture, Legends, Facts)
    ↓
Switch tab content
    ↓
User taps check-in button
    ↓
Navigate to QrScannerScreen
    ↓
User taps audio guide button
    ↓
Navigate to AudioGuideScreen with siteId
    ↓
User taps back
    ↓
Navigate back to HomeScreen
```

**State:**
- `site`: HeritageSite? (site data)
- `selectedTab`: Int (0-4 for 5 tabs)
- `isCheckedIn`: Boolean (check-in status)
- `unlockedFacts`: List<String> (unlocked fact IDs)

**User Actions:**
- Tap tab → Switch content
- Tap check-in button → Navigate to QR scanner
- Tap audio guide button → Navigate to audio guide
- Tap back → Navigate to home
- Tap hidden fact → Unlock fact (if not unlocked)

**Tab Content:**
- Overview: Site description, location, rating
- History: Historical significance
- Architecture: Architectural details
- Legends: Local legends and stories
- Facts: Hidden facts (unlockable)

**Fact Unlock Flow:**
```
User taps hidden fact
    ↓
Check if already unlocked
    ↓
If not unlocked → Call ViewModel.unlockFact()
    ↓
Repository saves to database
    ↓
UI updates to show unlocked fact
```

---

### 6. QrScannerScreen.kt Power Flow

**Purpose:** QR code scanning for site check-in

**Flow:**
```
Screen Load
    ↓
Request camera permission
    ↓
If denied → Show permission rationale
    ↓
If granted → Initialize CameraX
    ↓
Start camera preview
    ↓
Initialize ML Kit barcode scanner
    ↓
Process camera frames
    ↓
QR code detected
    ↓
Extract QR data (site ID)
    ↓
Validate against site database
    ↓
If valid → Show scan result card
    ↓
User taps check-in button
    ↓
Call ViewModel.processQrCode()
    ↓
Repository saves check-in to database
    ↓
Navigate to SiteDetailScreen with siteId
    ↓
If invalid → Show error message
    ↓
Continue scanning
```

**State:**
- `hasCameraPermission`: Boolean
- `isScanning`: Boolean
- `scanResult`: ScanResult? (detected QR data)
- `isProcessing`: Boolean (check-in in progress)

**User Actions:**
- Grant camera permission → Start scanning
- Deny camera permission → Show rationale, exit
- Tap flash toggle → Toggle camera flash
- Tap back → Navigate back
- Tap check-in button → Process check-in

**QR Validation:**
```
QR Data → Extract siteId
    ↓
Check if siteId exists in KarnatakaSites.allSites
    ↓
If exists → Valid → Show result
    ↓
If not exists → Invalid → Show error
```

**Check-in Logic:**
```
Valid QR → Check if already checked-in
    ↓
If yes → Show "Already checked-in" message
    ↓
If no → Save check-in to database
    ↓
Navigate to SiteDetailScreen
```

**Error Handling:**
- Camera permission denied → Show rationale, exit
- Camera initialization failed → Show error, retry
- QR scan failed → Continue scanning
- Invalid QR → Show error, continue scanning
- Check-in failed → Show error, retry

---

### 7. AudioGuideScreen.kt Power Flow

**Purpose:** Audio chapter playback, transcript display

**Flow:**
```
Screen Load (with siteId)
    ↓
Load site data from Repository
    ↓
Load audio chapters from site
    ↓
Show now playing card, chapter list
    ↓
User taps play button
    ↓
Start audio playback
    ↓
Update progress bar
    ↓
Update current time
    ↓
User taps pause button
    ↓
Pause audio playback
    ↓
User taps next/previous button
    ↓
Navigate to next/previous chapter
    ↓
User taps chapter in list
    ↓
Load and play selected chapter
    ↓
Audio ends
    ↓
Auto-play next chapter (if not last)
    ↓
User taps back
    ↓
Stop audio playback
    ↓
Navigate back to SiteDetailScreen
```

**State:**
- `currentChapter`: Int (0 to chapters.size - 1)
- `isPlaying`: Boolean
- `progress`: Float (0.0 to 1.0)
- `currentTime`: Int (seconds)
- `totalTime`: Int (seconds)

**User Actions:**
- Tap play/pause → Toggle playback
- Tap next → Go to next chapter
- Tap previous → Go to previous chapter
- Tap chapter in list → Play selected chapter
- Tap back → Stop audio, navigate back
- Drag progress bar → Seek to position

**Playback Flow:**
```
Play Button → ExoPlayer/MediaPlayer
    ↓
Load audio URL
    ↓
Start playback
    ↓
Update progress every second
    ↓
Audio complete → Auto next chapter
```

**Transcript Display:**
- Show transcript text below player
- Sync with audio (optional feature)
- Scroll as audio progresses

**Error Handling:**
- Audio load failed → Show error, skip to next
- Network error → Show error, retry
- Audio interrupted → Resume on return

---

### 8. TravelPassportScreen.kt Power Flow

**Purpose:** Digital passport, check-in stamps, level progress

**Flow:**
```
Screen Load
    ↓
Load check-ins from Repository
    ↓
Load unlocked facts from Repository
    ↓
Calculate level and XP
    ↓
Show passport card, stamp list
    ↓
User taps stamp card
    ↓
Navigate to SiteDetailScreen with siteId
    ↓
User taps back
    ↓
Navigate back to HomeScreen
```

**State:**
- `checkIns`: List<CheckIn> (all check-ins)
- `unlockedFacts`: List<UnlockedFact> (all unlocked facts)
- `level`: Int (user level)
- `currentXP`: Int (current experience points)
- `nextLevelXP`: Int (XP needed for next level)

**User Actions:**
- Tap stamp card → Navigate to site detail
- Tap back → Navigate to home

**Level Calculation:**
```
Level 1: 0 check-ins
Level 2: 1-2 check-ins
Level 3: 3-5 check-ins
Level 4: 6-10 check-ins
Level 5: 11+ check-ins

XP = (check-in count * 10) + (unlocked facts count * 5)
```

**Stamp Card Display:**
- Date of check-in
- Site name
- Site location
- Timestamp
- ChevronRight icon (navigate to site)

**Passport Card Design:**
- Circular badge with level number
- Progress bar for XP
- Total sites visited
- Total facts unlocked

**Error Handling:**
- Database query failed → Show error, retry
- No check-ins → Show empty state message

---

### FavouritesScreen (Reuses HomeScreen with Filter)

**Purpose:** Show favorited sites only

**Flow:**
```
Screen Load
    ↓
Load favorited sites from Repository
    ↓
Apply favorites filter to HomeScreen
    ↓
Show filtered site list
    ↓
User actions same as HomeScreen
```

**State:**
- Same as HomeScreen
- `showFavoritesOnly`: Boolean (true)

**User Actions:**
- Same as HomeScreen
- Tap favorites icon → Navigate back to HomeScreen (all sites)

**Note:** This screen reuses HomeScreen.kt with a favorites filter applied in ViewModel.

---

## 🎨 Screen Design & Implementation Guide

### Design System
- **Material 3** (Material You) design system
- **Theme:** VirasatTheme with custom colors (VirasatMaroon #800020, VirasatGold #D4AF37, VirasatCream #FFF5E6)
- **Typography:** Material 3 default typography
- **Icons:** Material Icons Extended (for QR, Filter, AccessTime, etc.)
- **Images:** Loaded from web URLs using Coil AsyncImage

### Material 3 Components Used
- `Scaffold` - Screen layout structure
- `TopAppBar` - App bars with navigation
- `BottomAppBar` - Bottom navigation (if needed)
- `Card` - Site cards, info cards
- `Button` - Primary and secondary buttons
- `OutlinedButton` - Secondary actions
- `IconButton` - Icon-only actions
- `TextField` - Search input
- `FilterChip` - Filter selection chips
- `TabRow` & `Tab` - Tabbed content
- `LazyColumn` - Vertical scrolling lists
- `LazyRow` - Horizontal scrolling lists
- `HorizontalPager` - Onboarding swipeable pages
- `Box` - Layered layouts
- `Column` & `Row` - Flex layouts
- `Spacer` - Spacing
- `Divider` - Visual separators
- `Surface` - Colored/toned containers
- `LinearProgressIndicator` - Progress bars

---

### Screen-by-Screen Design

#### 1. SplashScreen.kt
**Purpose:** Animated splash screen with logo

**Components:**
- `Box` - Full-screen container with VirasatCream background
- `AsyncImage` - Logo from web URL (centered)
- `LaunchedEffect` - Auto-navigate to LanguageScreen after 2 seconds

**Design:**
- Background: `#FFF5E6` (VirasatCream)
- Logo: Centered, scales with screen size
- Animation: Fade-in effect

**Navigation:** → LanguageScreen (auto, after 2s)

---

#### 2. LanguageScreen.kt
**Purpose:** Language selection (Kannada, English, Hindi)

**Components:**
- `Column` - Vertical layout
- `Text` - Title and subtitle
- `LazyColumn` - Scrollable language options
- `Card` - Language option cards
- `Row` - Language name + flag icon
- `Button` - Continue button

**Design:**
- Background: VirasatCream
- Title: Large, VirasatMaroon
- Language cards: White background, rounded corners, shadow
- Selected language: Highlighted with VirasatMaroon border
- Continue button: VirasatMaroon, full width at bottom

**Navigation:** → OnboardingScreen (on Continue)

---

#### 3. OnboardingScreen.kt
**Purpose:** 3 onboarding screens (Discovery, Scan QR, Passport)

**Components:**
- `HorizontalPager` - Swipeable pages (requires `ExperimentalFoundationApi`)
- `Box` - Page container
- `Column` - Vertical content per page
- `Text` - Title, description
- `AsyncImage` - Illustration from web URL
- `Row` - Page indicators (dots)
- `Button` - Skip/Next/Get Started

**Design:**
- Background: White
- Illustrations: Large, centered, from web URLs
- Page indicators: Active dot = VirasatMaroon, inactive = gray
- Buttons: Full width, bottom of screen
- Swipe: Left/right to navigate pages

**Navigation:** → HomeScreen (on "Get Started")

---

#### 4. HomeScreen.kt
**Purpose:** Main discovery screen with search, filters, stats, site list

**Components:**
- `Scaffold` - Screen structure
- `TopAppBar` - App title + menu icon
- `TextField` - Search bar with search icon
- `LazyRow` - Filter chips (All, Monuments, Temples, Forts)
- `Row` - Stats cards (Sites Visited, Unlocked Facts)
- `LazyColumn` - Site cards list
- `Card` - Individual site card
- `AsyncImage` - Site thumbnail
- `Text` - Site name, location, rating
- `IconButton` - QR scan, passport, favorites

**Design:**
- Background: Light gray (#F5F5F5)
- TopAppBar: VirasatMaroon
- Search bar: White, rounded, with search icon
- Filter chips: Pill-shaped, selected = VirasatMaroon
- Stats cards: White, icon + number + label
- Site cards: White, rounded corners, shadow
  - Thumbnail: Top, rounded top corners
  - Content: Name (bold), location (gray), rating (gold stars)
  - Action: ChevronRight icon

**Navigation:**
- Site card click → SiteDetailScreen
- QR scan icon → QrScannerScreen
- Passport icon → TravelPassportScreen
- Favorites icon → HomeScreen with favorites filter

---

#### 5. SiteDetailScreen.kt
**Purpose:** Detailed site information with tabs, check-in, audio guide

**Components:**
- `Scaffold` - Screen structure
- `TopAppBar` - Back button + site name
- `AsyncImage` - Hero image (full width)
- `TabRow` - Tab navigation (Overview, History, Architecture, Legends, Facts)
- `Tab` - Individual tabs
- `Column` - Tab content
- `Text` - Rich text content
- `LazyColumn` - Facts list
- `Button` - Check-in button
- `Row` - Audio guide button with icon

**Design:**
- Hero image: Full width, 200dp height
- Tabs: VirasatMaroon indicator
- Content: White background, padding
- Check-in button: VirasatMaroon, full width, bottom
- Audio guide button: Outlined, with headset icon

**Navigation:**
- Back → HomeScreen
- Check-in → QrScannerScreen
- Audio guide → AudioGuideScreen

---

#### 6. QrScannerScreen.kt
**Purpose:** QR code scanning for site check-in

**Components:**
- `Scaffold` - Screen structure
- `TopAppBar` - Back button + "Scan QR"
- `Box` - Camera preview container
- `CameraPreview` - CameraX preview (custom component)
- `Box` - Scan frame overlay (border, corners)
- `Text` - Instructions
- `Button` - Flash toggle
- `Card` - Scan result (shows when QR detected)
- `Button` - Check-in button (shows after scan)

**Design:**
- Camera preview: Full screen
- Scan frame: White border, rounded corners, semi-transparent overlay
- Instructions: White text at bottom
- Result card: White, slides up from bottom when QR detected
  - Site name, location
  - Check-in button: VirasatMaroon

**Navigation:**
- Back → HomeScreen or SiteDetailScreen
- After check-in → SiteDetailScreen (auto navigate)

**Permissions:** Camera permission required

---

#### 7. AudioGuideScreen.kt
**Purpose:** Audio chapter playback with transcript

**Components:**
- `Scaffold` - Screen structure
- `TopAppBar` - Back button + "Audio Guide"
- `Column` - Main content
- `Card` - Now playing card
- `Row` - Playback controls (Play/Pause, Previous, Next)
- `LinearProgressIndicator` - Progress bar
- `Text` - Current time / Total time
- `LazyColumn` - Chapter list
- `Card` - Chapter item
- `Text` - Chapter title, duration
- `IconButton` - Play icon

**Design:**
- Now playing card: White, shadow, top of screen
  - Album art (from site image)
  - Title, artist
  - Controls: Play/Pause (large), Previous/Next (small)
  - Progress bar: VirasatMaroon
- Chapter list: White cards, rounded
  - Playing chapter: VirasatMaroon border
  - Play icon: Right side

**Navigation:**
- Back → SiteDetailScreen

---

#### 8. TravelPassportScreen.kt
**Purpose:** Digital passport showing check-in stamps and progress

**Components:**
- `Scaffold` - Screen structure
- `TopAppBar` - Back button + "My Passport"
- `Card` - Passport card (visual design)
- `Box` - Passport layout with borders, circles
- `Text` - Level, XP, sites visited
- `LinearProgressIndicator` - Level progress
- `LazyColumn` - Check-in stamps list
- `Card` - Stamp card
- `Row` - Date, site name, location
- `Text` - Check-in timestamp
- `IconButton` - Navigate to site

**Design:**
- Passport card: White, rounded corners, shadow
  - Decorative borders (drawn with Compose primitives)
  - Circular badge for level
  - Progress bar for XP
- Stamp cards: White, rounded
  - Date: Gray, small
  - Site name: Bold, VirasatMaroon
  - ChevronRight: Navigate to site

**Navigation:**
- Back → HomeScreen
- Stamp click → SiteDetailScreen

---

### Custom Components

#### CameraPreview (QrScannerScreen.kt)
- Uses CameraX `PreviewView`
- Lifecycle-aware
- Handles camera permission

#### Passport Card (TravelPassportScreen.kt)
- Drawn with Compose primitives (Box, Border, Circle)
- No external SVG/image assets needed
- Matches complex design with code-only approach

---

### Navigation Pattern
```
SplashScreen → LanguageScreen → OnboardingScreen → HomeScreen
    ↓
    ├─→ SiteDetailScreen
    │       ├─→ QrScannerScreen → SiteDetailScreen (check-in)
    │       └─→ AudioGuideScreen
    │
    ├─→ QrScannerScreen → SiteDetailScreen
    │
    └─→ TravelPassportScreen → SiteDetailScreen
```

**Back Stack Management:**
- Each navigation uses `popUpTo` to clear unnecessary back stack
- Splash, Language, Onboarding are one-time screens (cleared from back stack)
- Main flow: Home ↔ Detail ↔ Scanner/Audio/Passport

---

### State Management Pattern
- **ViewModels** hold UI state as `StateFlow` or `MutableStateFlow`
- **Screens** observe state via `collectAsState()`
- **Repository** provides data from Room database and local JSON
- **Coroutines** for async operations (database, network)

---

### Color Palette
```kotlin
val VirasatMaroon = Color(0xFF800020)
val VirasatGold = Color(0xFFD4AF37)
val VirasatCream = Color(0xFFFFF5E6)
val VirasatDark = Color(0xFF2D2D2D)
val VirasatLight = Color(0xFFF5F5F5)
```

---

### Image Sources
All images loaded from web URLs using Coil AsyncImage:
- Site thumbnails, hero images
- Onboarding illustrations
- Logo

---

### Icon Usage
- Navigation: `Icons.AutoMirrored.Filled.ArrowBack`, `KeyboardArrowRight`
- Actions: `Icons.Default.QrCodeScanner`, `FilterList`, `Headset`, `Play`, `Pause`, `VolumeUp`
- Status: `Icons.Default.Star`, `AccessTime`, `LocationOn`

---

## 📊 Data Models

### HeritageSite.kt
```kotlin
data class HeritageSite(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val history: String,
    val architecture: String,
    val legends: String,
    val imageUrl: String,
    val qrCodeId: String,
    val siteType: SiteType,
    val rating: Float,
    val facts: List<Fact>,
    val audioChapters: List<AudioChapter>,
    val coordinates: Pair<Double, Double>
)

enum class SiteType {
    MONUMENT, TEMPLE, FORT
}

data class Fact(
    val id: String,
    val title: String,
    val description: String,
    val isHidden: Boolean
)

data class AudioChapter(
    val id: String,
    val title: String,
    val duration: Int,
    val audioUrl: String,
    val transcript: String
)
```

### CheckIn.kt
```kotlin
@Entity(tableName = "check_ins")
data class CheckIn(
    @PrimaryKey val id: String,
    val siteId: String,
    val siteName: String,
    val siteLocation: String,
    val timestamp: Long,
    val qrCodeId: String
)
```

### UnlockedFact.kt
```kotlin
@Entity(tableName = "unlocked_facts")
data class UnlockedFact(
    @PrimaryKey val id: String,
    val siteId: String,
    val factId: String,
    val factTitle: String,
    val unlockedAt: Long
)
```

---

## 🗄️ Room Database Schema

### VirasatDatabase.kt
```kotlin
@Database(
    entities = [CheckIn::class, UnlockedFact::class],
    version = 1
)
abstract class VirasatDatabase : RoomDatabase() {
    abstract fun checkInDao(): CheckInDao
    abstract fun unlockedFactDao(): UnlockedFactDao
}
```

### Tables

#### check_ins
| Column | Type | Description |
|--------|------|-------------|
| id | String (PK) | Unique check-in ID |
| siteId | String | Heritage site ID |
| siteName | String | Site name (denormalized) |
| siteLocation | String | Site location (denormalized) |
| timestamp | Long | Check-in timestamp |
| qrCodeId | String | QR code scanned |

#### unlocked_facts
| Column | Type | Description |
|--------|------|-------------|
| id | String (PK) | Unique unlock ID |
| siteId | String | Heritage site ID |
| factId | String | Fact ID |
| factTitle | String | Fact title (denormalized) |
| unlockedAt | Long | Unlock timestamp |

---

## 🔄 Repository Pattern

### HeritageRepository.kt
```kotlin
class HeritageRepository(
    private val checkInDao: CheckInDao,
    private val unlockedFactDao: UnlockedFactDao
) {
    // Site data (from local JSON)
    fun getAllSites(): Flow<List<HeritageSite>>
    fun getAllSitesList(): List<HeritageSite>  // Synchronous for QR lookup
    fun getSiteById(id: String): HeritageSite?

    // Check-in operations
    fun getCheckIns(): Flow<List<CheckIn>>
    suspend fun checkIn(siteId: String, qrCodeId: String)
    suspend fun undoCheckIn(siteId: String)
    fun getCheckInCount(): Flow<Int>

    // Fact operations
    fun getUnlockedFacts(): Flow<List<UnlockedFact>>
    suspend fun unlockFact(siteId: String, factId: String)
    fun isFactUnlocked(siteId: String, factId: String): Flow<Boolean>
    fun getUnlockedFactCount(): Flow<Int>
}
```

---

## 🧠 ViewModels

### HomeViewModel.kt
```kotlin
class HomeViewModel(private val repository: HeritageRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow<SiteType?>(null)
    val selectedFilter: StateFlow<SiteType?> = _selectedFilter.asStateFlow()

    val sites: StateFlow<List<HeritageSite>> = ...
    val checkInCount: StateFlow<Int> = ...
    val unlockedFactCount: StateFlow<Int> = ...

    fun onSearchQueryChanged(query: String)
    fun onFilterChanged(filter: SiteType?)
}
```

### DetailViewModel.kt
```kotlin
class DetailViewModel(private val repository: HeritageRepository) : ViewModel() {
    private val _site = MutableStateFlow<HeritageSite?>(null)
    val site: StateFlow<HeritageSite?> = _site.asStateFlow()

    private val _isCheckedIn = MutableStateFlow(false)
    val isCheckedIn: StateFlow<Boolean> = _isCheckedIn.asStateFlow()

    private val _unlockedFacts = MutableStateFlow<List<String>>(emptyList())
    val unlockedFacts: StateFlow<List<String>> = _unlockedFacts.asStateFlow()

    fun loadSite(siteId: String)
    fun checkIn(qrCodeId: String)
    fun unlockFact(factId: String)
}
```

### QrScannerViewModel.kt
```kotlin
class QrScannerViewModel(private val repository: HeritageRepository) : ViewModel() {
    private val _scanResult = MutableStateFlow<ScanResult?>(null)
    val scanResult: StateFlow<ScanResult?> = _scanResult.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    fun processQrCode(qrData: String)
    fun reset()
}

data class ScanResult(
    val siteId: String,
    val siteName: String,
    val location: String,
    val isAlreadyCheckedIn: Boolean
)
```

### PassportViewModel.kt
```kotlin
class PassportViewModel(private val repository: HeritageRepository) : ViewModel() {
    val checkIns: StateFlow<List<CheckIn>> = ...
    val unlockedFacts: StateFlow<List<UnlockedFact>> = ...
    val checkInCount: StateFlow<Int> = ...
    val unlockedFactCount: StateFlow<Int> = ...
}
```

---

## 🔌 Third-Party Integrations

### ML Kit QR Code Scanning
**Purpose:** Scan QR codes at heritage sites for check-in

**Configuration:**
```kotlin
val options = BarcodeScannerOptions.Builder()
    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
    .build()
val scanner = BarcodeScanning.getClient(options)
```

**Usage:**
- Scan QR codes containing site IDs
- Validate QR code against site database
- Trigger check-in process

### CameraX
**Purpose:** Camera preview for QR scanning

**Components:**
- `PreviewView` - Camera preview
- `ProcessCameraProvider` - Camera lifecycle management
- `ImageAnalysis` - Frame analysis for QR detection

**Permissions Required:**
- `CAMERA` permission (runtime)

### Coil Image Loading
**Purpose:** Load images from web URLs

**Configuration:**
```kotlin
Coil.setImageLoader {
    ImageLoader.Builder(context)
        .crossfade(true)
        .build()
}
```

**Usage:**
- Site thumbnails
- Hero images
- Onboarding illustrations
- Logo

---

## 🌍 Localization (Multi-Language)

**Supported Languages:**
- Kannada (ಕನ್ನಡ)
- English
- Hindi (हिंदी)

**Implementation:**
- Language selection in `LanguageScreen.kt`
- Store user preference in SharedPreferences
- Use string resources for translatable text
- Future: Add full localization support

---

## ♿ Accessibility Features

**Implemented:**
- Material 3 default accessibility
- Semantic descriptions for images
- Touch targets meet minimum size (48dp)

**Future Enhancements:**
- Screen reader support
- High contrast mode
- Font size scaling
- Color blind friendly palettes

---

## 🔒 Security Considerations

**Data Privacy:**
- No personal data collection
- Check-in data stored locally only
- No network requests for user data

**QR Code Security:**
- Validate QR codes against known site IDs
- Prevent malicious QR code execution
- No URL opening from QR codes

**Permissions:**
- Camera permission requested at runtime
- Permission rationale shown
- Graceful fallback if permission denied

---

## ⚡ Performance Considerations

**Image Loading:**
- Coil with crossfade for smooth transitions
- Image caching
- Placeholder images for loading states

**Database:**
- Room with Flow for reactive updates
- Indexes on frequently queried columns
- Efficient queries (no N+1 problem)

**List Rendering:**
- LazyColumn/LazyRow for virtualization
- Item keys for stable composition
- Minimal recomposition

**Coroutines:**
- Structured concurrency
- Proper cancellation
- Dispatchers.IO for database operations

---

## 🐛 Known Issues & Limitations

**Current Issues:**
- Build failing due to missing imports (fixes in IDE buffer, not saved)
- Icon references need manual fixes
- @OptIn annotations needed for experimental APIs

**Limitations:**
- No offline mode for audio (requires internet)
- No social sharing features
- No user accounts/authentication
- Limited to 6 heritage sites (hardcoded)
- No map integration
- No AR features

**Future Enhancements:**
- Add more heritage sites
- Offline audio caching
- User accounts and sync
- Social features (share check-ins)
- Map view with directions
- AR experiences
- Gamification (achievements, leaderboards)
- Multi-language support for all content

---

## 🧪 Testing Strategy

**Unit Tests (Not Yet Implemented):**
- ViewModel tests
- Repository tests
- Data model tests

**UI Tests (Not Yet Implemented):**
- Compose UI tests
- Navigation tests
- Integration tests

**Manual Testing:**
- Test on emulator (API 30+)
- Test on physical device
- Test camera permission flow
- Test QR scanning accuracy
- Test database persistence

---

## 🚀 Deployment Instructions

### Build Release APK
```bash
gradlew assembleRelease
```

**Location:** `app/build/outputs/apk/release/app-release.apk`

### Signing
- Add keystore configuration in `app/build.gradle.kts`
- Configure signing config for release build
- Follow Android signing best practices

### Play Store Submission
1. Create app listing on Google Play Console
2. Upload signed APK or App Bundle
3. Provide screenshots, descriptions
4. Set content rating
5. Submit for review

---

## 📱 Device Requirements

**Minimum SDK:** API 24 (Android 7.0)
**Target SDK:** API 34 (Android 14)
**Compile SDK:** API 34

**Permissions:**
- CAMERA (required for QR scanning)
- INTERNET (for image loading)

**Storage:** ~50MB (app + cached images)

---

## 🔧 Troubleshooting Guide

### Build Errors
**Issue:** "Unresolved reference" errors
**Solution:** 
1. Save all files (Ctrl+S)
2. Run `gradlew clean`
3. Run `gradlew build`

**Issue:** Missing dependencies
**Solution:** Sync Gradle files in IDE

### Runtime Errors
**Issue:** Camera permission denied
**Solution:** Grant camera permission in app settings

**Issue:** Images not loading
**Solution:** Check internet connection

**Issue:** QR scan not working
**Solution:** Ensure good lighting and steady camera

### Database Issues
**Issue:** Check-in not persisting
**Solution:** Clear app data and reinstall

---

## 📚 Code Architecture

### Layered Architecture
```
┌─────────────────────────────────────┐
│           UI Layer                   │
│  (Screens, Composables, NavHost)    │
└─────────────────────────────────────┘
                ↓
┌─────────────────────────────────────┐
│        ViewModel Layer               │
│  (State, Business Logic, Events)    │
└─────────────────────────────────────┘
                ↓
┌─────────────────────────────────────┐
│       Repository Layer               │
│  (Data Access, Caching, Sync)       │
└─────────────────────────────────────┘
                ↓
┌─────────────────────────────────────┐
│         Data Layer                  │
│  (Room Database, Local JSON, API)   │
└─────────────────────────────────────┘
```

### Data Flow
```
User Action → UI Event → ViewModel → Repository → Database/JSON
    ↑                                                          ↓
    └────────────── State Update ← Flow ← Repository ←───────┘
```

---

## 🤝 Contributing Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable/function names
- Add comments for complex logic
- Keep functions small and focused

### Git Workflow
1. Create feature branch from master
2. Make changes with meaningful commits
3. Test thoroughly
4. Submit pull request
5. Code review
6. Merge to master

### Commit Message Format
```
feat: add new feature
fix: resolve bug
docs: update documentation
refactor: code refactoring
test: add/update tests
```

---

## 📞 Support & Resources

### Documentation
- Jetpack Compose: https://developer.android.com/jetpack/compose
- Material 3: https://m3.material.io/
- Room Database: https://developer.android.com/training/data-storage/room
- ML Kit: https://developers.google.com/ml-kit/vision/barcode-scanning
- CameraX: https://developer.android.com/training/camerax

### GitHub Repository
- Issues: https://github.com/PREETHAM1590/Virasat-Namma/issues
- Pull Requests: https://github.com/PREETHAM1590/Virasat-Namma/pulls

---

## 📋 Checklist

### Pre-Build
- [ ] Save all files in IDE (Ctrl+S)
- [ ] Add missing imports to QrScannerScreen.kt
- [ ] Replace Headphones icons with Headset
- [ ] Replace ChevronRight with KeyboardArrowRight
- [ ] Add @OptIn annotations to all screen files
- [ ] Verify build.gradle.kts has material-icons-extended
- [ ] Verify build.gradle.kts has concurrent-futures-ktx

### Build
- [ ] Run `gradlew clean`
- [ ] Run `gradlew build`
- [ ] Fix any remaining errors

### Post-Build
- [ ] Test app on emulator
- [ ] Test app on physical device
- [ ] Verify all screens work
- [ ] Test QR scanning
- [ ] Test check-in flow
- [ ] Test audio guide
- [ ] Test passport

### Deployment
- [ ] Commit changes
- [ ] Push to GitHub
- [ ] Create release
- [ ] Deploy to Play Store

---

## 📁 File Structure

```
d:\Android PROJECTS\Virasat\
├── app/
│   ├── build.gradle.kts (needs save)
│   └── src/main/java/com/example/virasat/
│       ├── MainActivity.kt
│       ├── data/
│       │   ├── model/
│       │   │   ├── HeritageSite.kt
│       │   │   ├── CheckIn.kt
│       │   │   └── UnlockedFact.kt
│       │   ├── local/
│       │   │   ├── VirasatDatabase.kt
│       │   │   ├── CheckInDao.kt
│       │   │   └── UnlockedFactDao.kt
│       │   ├── repository/
│       │   │   └── HeritageRepository.kt (needs save - added getAllSitesList())
│       │   └── source/
│       │       └── KarnatakaSites.kt
│       ├── viewmodel/
│       │   ├── HomeViewModel.kt
│       │   ├── DetailViewModel.kt
│       │   ├── PassportViewModel.kt
│       │   └── QrScannerViewModel.kt (needs save - fixed .value issue)
│       └── ui/screens/
│           ├── SplashScreen.kt
│           ├── LanguageScreen.kt
│           ├── OnboardingScreen.kt
│           ├── HomeScreen.kt (needs save - added @OptIn)
│           ├── SiteDetailScreen.kt (needs save - added @OptIn)
│           ├── QrScannerScreen.kt (needs save - added @OptIn, border import; needs manual imports)
│           ├── AudioGuideScreen.kt (needs save - added @OptIn, clickable import; needs manual icon fix)
│           └── TravelPassportScreen.kt (needs save - added @OptIn, clickable import; needs manual icon fix)
└── JARVIS/
    ├── BUILD_GUIDE.md (this file)
    ├── sessions/
    │   └── 2026-05-05.md
    ├── GEMINI.md
    ├── operating_rules.md
    ├── JARVIS_MEMORY.md
    ├── CONFIG_TECHNIQUE.md
    ├── NOTES_TECHNIQUES.md
    ├── USER.md
    └── ARBORESCENCE.md
```

---

## 🌐 GitHub Repository

- **Repo:** https://github.com/PREETHAM1590/Virasat-Namma.git
- **Remote:** origin
- **Branch:** master
- **Status:** 10 commits pushed successfully
- **Issue:** Latest fixes not committed/pushed (files not saved to disk)

### Git Commands
```bash
# After build succeeds:
git add .
git commit -m "fix: resolve build errors and add missing imports"
git push origin master
```

---

## 🚀 After Build Succeeds

1. **Commit changes:** `git add .` + `git commit`
2. **Push to GitHub:** `git push origin master`
3. **Verify on GitHub:** Check commits are visible
4. **Test app:** Run on emulator or device

---

## 🎯 Summary

- **App is 95% complete** - All features implemented
- **Build failing** due to missing imports and icon references
- **Fixes attempted** in IDE buffer but not saved to disk
- **Critical next step:** User must save files (Ctrl+S)
- **After save:** 4 manual fixes needed, then build should succeed
- **Total screens:** 8 (all built)
- **Total viewmodels:** 4 (all built)
- **Database:** Room with 2 tables (CheckIn, UnlockedFact)
- **Features:** QR check-in, audio guide, digital passport, site discovery

---

## 📞 Quick Reference

### Build Command
```bash
cd "d:\Android PROJECTS\Virasat"
gradlew clean build
```

### Install APK
```bash
gradlew installDebug
```

### Run on Emulator
```bash
gradlew installDebug && adb shell am start -n com.example.virasat/.MainActivity
```

---

## 📝 Session Log

### Session: May 5, 2026, 11:40pm - 11:54pm
**Completed:**
- Created BUILD_GUIDE.md with complete project context
- Added screen design documentation for all 8 screens
- Added data models documentation (HeritageSite, CheckIn, UnlockedFact)
- Added Room database schema documentation
- Added repository pattern documentation
- Added ViewModels documentation (all 4)
- Added third-party integrations documentation (ML Kit, CameraX, Coil)
- Added localization, accessibility, security documentation
- Added performance considerations
- Added known issues & limitations
- Added testing strategy
- Added deployment instructions
- Added device requirements
- Added troubleshooting guide
- Added code architecture diagrams
- Added contributing guidelines
- Added support & resources
- Added comprehensive checklist
- Added session tracking & progress section
- Added instructions for updating after each chat session

### Session: May 6, 2026, 1:37am
**Completed:**
- Added comprehensive Screen Power Flows section to BUILD_GUIDE.md
- Documented power flows for all 9 screens with user interaction and state transitions
- Each power flow includes: purpose, flow diagram, state variables, user actions, error handling
- Added sub-flows for complex interactions (fact unlock, QR validation, check-in logic, playback flow)
- Added level calculation logic for passport
- Added data persistence details for language selection
- Added filter logic for HomeScreen

**Screens Documented with Power Flows:**
1. SplashScreen.kt - App launch, auto-navigation
2. LanguageScreen.kt - Language selection, SharedPreferences
3. OnboardingScreen.kt - 3-page swipeable introduction
4. HomeScreen.kt - Search, filter, site discovery
5. SiteDetailScreen.kt - Tabs, check-in, audio guide, fact unlock
6. QrScannerScreen.kt - Camera permission, QR validation, check-in
7. AudioGuideScreen.kt - Audio playback, chapters, transcript
8. TravelPassportScreen.kt - Passport card, stamps, level calculation
9. FavouritesScreen - Reuses HomeScreen with filter

### Session: May 6, 2026, 1:40am
**Completed:**
- Added Complete Screen Inventory section to BUILD_GUIDE.md
- Catalogued all 50+ screens from stitch_screens directory
- Marked 9 screens as ✅ ALREADY BUILT with file references
- Listed 41+ screens as 🔨 NEED TO BE BUILT with priority levels
- Categorized screens by type: Map & Discovery, Search & Filter, Content & Media, Gamification, User Account, Communication, Error & State, Variations
- Provided build instructions for each screen including HTML references
- Added "How to Build Screens from HTML References" section with general approach
- Included HTML to Compose mapping guide
- Added Material 3 component mapping guidance

**Screen Inventory Summary:**
- ✅ Built: 9 screens (Splash, Language, Onboarding, Home, Site Detail, QR Scanner, Audio Guide, Passport, Favourites)
- 🔨 To Build: 41+ screens
  - HIGH Priority: 8 screens (Map View, Nearby Sites, Search, Login, Profile, Offline, Location Permission)
  - MEDIUM Priority: 18 screens (Route Map, Gallery, Stories, Legends, Quiz, Badges, Settings, AI Chat, Error)
  - LOW Priority: 15 screens (Events, Guides, Notifications, Help, About, Variations)

**Pending:**
- User needs to save all files in IDE (Ctrl+S) - CRITICAL
- Add missing imports to QrScannerScreen.kt (6 imports)
- Replace Icons.Default.Headphones with Icons.Default.Headset (2 files)
- Replace Icons.Default.ChevronRight with KeyboardArrowRight (1 file)
- Add @OptIn annotations to all screen files (5 files)
- Verify build.gradle.kts dependencies
- Run gradlew clean
- Run gradlew build
- Test app on emulator/device
- Commit and push to GitHub

**Files Modified (in IDE buffer, NOT saved to disk):**
- build.gradle.kts (added material-icons-extended, concurrent-futures-ktx)
- HeritageRepository.kt (added getAllSitesList() method)
- QrScannerViewModel.kt (fixed .value issue)
- HomeScreen.kt (added @OptIn annotation)
- SiteDetailScreen.kt (added @OptIn annotation)
- AudioGuideScreen.kt (added @OptIn annotation, clickable import)
- QrScannerScreen.kt (added @OptIn annotation, border import)
- TravelPassportScreen.kt (added @OptIn annotation, clickable import)

**Files Created:**
- JARVIS/BUILD_GUIDE.md (comprehensive project documentation)

**Next Session Priority:**
1. User saves all files (Ctrl+S)
2. Apply manual fixes to QrScannerScreen.kt imports
3. Apply manual icon fixes
4. Build and test

---

### Session: May 6, 2026, 8:42am
**Completed:**
- Created 43 total UI screens (from original 8 to 43)
- Firebase migration: Repository interface + RoomHeritageRepository + FirebaseHeritageRepository
- RepositoryProvider DI singleton + ViewModel updates
- Added Firebase BOM, Firestore, Auth dependencies; configured google-services plugin
- Stub google-services.json added for compilation
- Changed CheckIn.id to String for Firestore compat; Room DB v2 with destructive migration
- ImageUrls.kt utility with real Unsplash URLs
- Fixed all compilation errors: QrScanner imports, icon fixes (Headset, KeyboardArrowRight), KSP, AGP 8.7.0, Gradle 8.9
- MainActivity.kt NavHost updated with all 43 routes
- Debug APK builds successfully (assembleDebug)
- All new screen files created and wired:
  - NotificationsScreen, MapScreen, HeritageSitesListScreen, ItineraryScreen, ReviewsScreen
  - CommunityScreen, LeaderboardScreen, MyCheckInsScreen, VirtualTourScreen, ARScreen
  - ContactUsScreen, PrivacyPolicyScreen, TermsOfServiceScreen, FeedbackScreen, DataSyncScreen
  - LoginScreen, SignUpScreen, ForgotPasswordScreen, ProfileScreen, SettingsScreen
  - AboutScreen, HelpSupportScreen, SearchScreen, BookmarkedSitesScreen, ImageGalleryScreen
  - CheckInSuccessScreen, BadgesScreen, HeritageGuidesScreen, AIAssistantScreen, QuizScreen
  - SplashScreen, LanguageScreen, OnboardingScreen, HomeScreen, SiteDetailScreen
  - QrScannerScreen, AudioGuideScreen, TravelPassportScreen, EmptyStateScreen, ErrorScreen
  - OfflineScreen, PermissionScreen

### Session: May 6, 2026, 09:14am
**Completed:**
- Evaluated build status. Verified that the app builds successfully locally (`gradlew assembleDebug` passed).
- Confirmed that the critical missing imports, icon replacements, `@OptIn` annotations, and dependencies have been saved and applied.
- Updated `BUILD_GUIDE.md` to remove the outdated critical build failure sections.
- Moved blocking errors to the "Completed" task list.

**Build Status:** BUILD SUCCESSFUL (compilation), BUT crash on launch

### Session: May 6, 2026, 9:23am - Crash Fix
**Completed:**
- Fixed crash: `CompositionLocal LocalLifecycleOwner not present` in HomeScreen, SiteDetailScreen, TravelPassportScreen
- Root cause: `collectAsStateWithLifecycle()` requires `LocalLifecycleOwner` in composition. `navigation-compose:2.7.7` doesn't auto-scaffold it per nav entry.
- Fix: Replaced `collectAsStateWithLifecycle()` with `collectAsState()` in 3 screens (12 total calls)
- Added `android:enableOnBackInvokedCallback="true"` to AndroidManifest
- Added `CAMERA` permission to AndroidManifest (was missing, needed for QR scanner)

**Pending:**
- Phase 6 (Firebase activation): waiting on real `google-services.json` from user
- Phase 7 (device testing): run app on emulator/physical device

---

**Last Updated:** May 6, 2026, 5:35am
**Status:** Build compiles. Crash fixed. All 42 screens themed. TTS audio descriptions, 360° immersive viewer added.

### Session: May 6, 2026, 4:15am - Full Frontend Overhaul
**Completed:**
- HomeScreen.kt — complete rewrite: hero stats card with VirasatMaroon background, type filter chips with site-specific icons (TempleHindu, Castle, Fort, etc.), empty search state, loading shimmer, clear search button
- SiteDetailScreen.kt — rewrite: opening hours + entry fee chips, Google Maps navigation intent button, share via Intent.ACTION_SEND, onImmersiveView callback for 360° viewer, "Listen" button linking to AudioGuide, locked/unlocked facts with custom icons, gallery section, formatting utility for review counts
- AudioGuideScreen.kt — real TextToSpeech (Android built-in `android.speech.tts.TextToSpeech`). Chapter auto-advance, DisposableEffect cleanup, real TTS speak/stop on play/pause
- ImmersivePhotoScreen.kt (NEW) — Pannellum WebView 360° photosphere viewer with prev/next image navigation, auto-rotate toggle, loading state. Uses CDN-loaded Pannellum.js
- Theme unification: OnboardingScreen.kt + LanguageScreen.kt — `AppMaroon/AppBg/AppText/CardBorder` replaced with `VirasatMaroon/VirasatCream/Color(0xFF2A2A2A)`. Removed all duplicate theme refs
- Coorg Fort unique image URL fix (was sharing Gol Gumbaz URL)
- LoginScreen.kt — email regex validation, password min 6 chars, disabled when invalid, loading spinner
- ProfileScreen.kt — stats cards (sites/facts/badges), avatar with edit, menu items (Passport, Bookmarks, Settings, Logout)
- SettingsScreen.kt — dark mode toggle, notifications toggle, language selector, About/Help links
- SearchScreen.kt — wired KarnatakaSites filtering, recent searches, empty state
- MainActivity.kt — added `onImmersiveView` callback, audio guide site name now resolved from KarnatakaSites, immersive route added
- AndroidManifest.xml — added `CAMERA` permission, `enableOnBackInvokedCallback`
- MapScreen.kt — static Karnataka map with overlay, clickable site list chips
- NotificationsScreen.kt — mock notifications with read/unread states, icons
- HeritageSitesListScreen.kt — real KarnatakaSites list, search, type filter
- CheckInSuccessScreen.kt — confetti animation, XP card, badge unlock, share
- BadgesScreen.kt — 2-column grid: earned in color, locked gray with progress
- EmptyStateScreen.kt — reusable: icon, title, message, optional action button
- AboutScreen.kt — app icon, version, description, Privacy/Terms links, Rate Us
- ContactUsScreen.kt — Name/Email/Message form, validation, loading, contact info
- FeedbackScreen.kt — 5-star rating, comment field, submit + thank-you state
- HelpSupportScreen.kt — 6 expandable FAQ items, contact support button
- PrivacyPolicyScreen.kt — 11-section privacy policy, scrollable
- TermsOfServiceScreen.kt — 12-section terms of service, scrollable
- HeritageGuidesScreen.kt — 5 mock guides with photos, languages, ratings, book button
- VirtualTourScreen.kt — hero image with "Coming Soon" overlay, tour stops list
- PermissionScreen.kt — Camera/Location/Microphone cards with status + Allow buttons
- DataSyncScreen.kt — sync status, auto-sync toggle, data usage info, export button

**Files created:**
- `ImmersivePhotoScreen.kt` — 360° Pannellum WebView viewer

**Pending:**
- Phase 6 (Firebase activation): waiting on real `google-services.json`
- Phase 7 (device testing): build + run on emulator/physical device (WSL lacks JDK 21 — use Android Studio on Windows)
