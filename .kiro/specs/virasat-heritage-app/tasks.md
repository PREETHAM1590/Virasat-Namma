# Implementation Plan: Virasat Heritage App (Requirements 6–11)

## Overview

This plan converts the design for Requirements 6–11 into discrete, incremental coding tasks. Each task builds on the previous ones and ends with all components wired together. The implementation language is **Kotlin** with **Jetpack Compose**, targeting Android API 26–35.

Property-based tests use `io.kotest:kotest-property-jvm:5.9.1` in the `test/` source set (pure JVM, no Android runtime). Room instrumented tests use `Room.inMemoryDatabaseBuilder` in the `androidTest/` source set.

---

## Tasks

- [ ] 1. Add Kotest Property dependency and test scaffolding
  - Add `testImplementation("io.kotest:kotest-property-jvm:5.9.1")` and `testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")` to `app/build.gradle.kts`
  - Add `testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")` if not already present
  - Configure `tasks.withType<Test> { useJUnitPlatform() }` in `app/build.gradle.kts` so Kotest tests are discovered
  - Create the `app/src/test/java/com/example/virasat/` directory structure for JVM unit tests
  - _Requirements: 6.4, 6.6, 7.9, 8.10, 11.11_

- [ ] 2. Critical Bug Fixes — GeminiHeritageService and Fact model (Requirement 6)
  - [ ] 2.1 Remove certificate pinner from `GeminiHeritageService`
    - Open `data/service/GeminiHeritageService.kt`
    - Remove the `.certificatePinner(...)` block from the `OkHttpClient.Builder()` chain so the system trust store is used
    - Verify no other `CertificatePinner` references remain in the file
    - _Requirements: 6.1_

  - [ ] 2.2 Fix JSON injection in `GeminiHeritageService.callGemini`
    - Replace the string-interpolation payload builder with `org.json.JSONObject` / `JSONArray` structured construction as specified in design §1.2
    - Extract a package-internal `fun buildPayload(prompt: String): String` function that constructs and returns the JSON string — this makes it testable without an HTTP call
    - Ensure `callGemini` calls `buildPayload(prompt)` internally
    - _Requirements: 6.2, 6.5_

  - [ ] 2.3 Write property test for Gemini payload JSON safety (Property 1)
    - **Property 1: Gemini Payload JSON Safety**
    - Create `test/.../GeminiPayloadPropertyTest.kt`
    - Use `checkAll(100, Arb.string())` to verify `buildPayload(prompt)` produces valid JSON and round-trips the `text` field
    - Annotate: `// Feature: virasat-heritage-app, Property 1: Gemini payload JSON safety`
    - **Validates: Requirements 6.2, 6.5**

  - [ ] 2.4 Annotate `Fact` data class with `@Serializable`
    - Open `data/model/HeritageSite.kt`
    - Add `@kotlinx.serialization.Serializable` to the `Fact` data class
    - Add `@SerialName` annotations to all four fields (`"id"`, `"title"`, `"desc"`, `"unlocked"`) as specified in design §1.4
    - Add `id("org.jetbrains.kotlin.plugin.serialization")` plugin to `app/build.gradle.kts` if not already present
    - _Requirements: 6.4_

  - [ ] 2.5 Write property test for Fact serialization round-trip (Property 2)
    - **Property 2: Fact Serialization Round-Trip**
    - Create `test/.../FactSerializationPropertyTest.kt`
    - Define `arbFact()` Arb that generates `Fact` with random strings and booleans
    - Use `checkAll(100, Arb.list(arbFact(), 0..20))` to verify `Converters().toFactList` → `fromFactList` round-trip
    - Annotate: `// Feature: virasat-heritage-app, Property 2: Fact serialization round-trip`
    - **Validates: Requirements 6.4, 6.6**

  - [ ] 2.6 Fix reactive auth state in `MainActivity`
    - Replace `remember { FirebaseAuthService.isLoggedIn }` with `FirebaseAuthService.authStateFlow().collectAsState(initial = FirebaseAuthService.currentUser)`
    - Update `startDest` derivation to use `authUser` (nullable `FirebaseUser?`) instead of `isLoggedIn`
    - Add a `LaunchedEffect(authUser)` block that navigates to `"login"` and clears the back stack when `authUser` becomes `null` after initial composition
    - _Requirements: 6.3_

- [ ] 3. Checkpoint — Verify bug fixes compile and existing tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 4. Offline Mode & Bookmarks — Data layer (Requirement 7)
  - [ ] 4.1 Create `BookmarkEntity` and `BookmarkDao`
    - Create `data/local/BookmarkEntity.kt` with `@Entity(tableName = "bookmarks")` as specified in design §2.1
    - Create `data/local/BookmarkDao.kt` with `insert`, `delete`, `exists`, and `observeAll` methods as specified in design §2.2
    - _Requirements: 7.1, 7.2, 7.3_

  - [ ] 4.2 Migrate `VirasatDatabase` from version 3 to version 4
    - Open `data/local/VirasatDatabase.kt`
    - Add `BookmarkEntity::class` to the `@Database` entities list
    - Bump `version` from `3` to `4`
    - Define `MIGRATION_3_4` with the `CREATE TABLE IF NOT EXISTS bookmarks` SQL as specified in design §2.8
    - Replace any `fallbackToDestructiveMigration()` call with `.addMigrations(MIGRATION_3_4)`
    - Expose `bookmarkDao(): BookmarkDao` abstract function on the database class
    - _Requirements: 7.1, 7.2, 7.3_

  - [ ] 4.3 Implement bookmark methods in `RoomHeritageRepository`
    - Inject `BookmarkDao` into `RoomHeritageRepository` (retrieve from `VirasatDatabase`)
    - Implement `toggleBookmark(siteId)`, `isBookmarked(siteId)`, and `observeBookmarks()` using `BookmarkDao` as specified in design §2.3
    - Add the three methods to the `HeritageRepository` interface if not already declared
    - _Requirements: 7.1, 7.2, 7.3_

  - [ ] 4.4 Write property test for bookmark toggle invariant (Property 3)
    - **Property 3: Bookmark Toggle Invariant**
    - Create `androidTest/.../BookmarkTogglePropertyTest.kt`
    - Use `Room.inMemoryDatabaseBuilder` to create a test database
    - Use `checkAll(100, Arb.string(1..50), Arb.int(1..5))` to verify that calling `toggleBookmark` an even number of times (`n * 2`) leaves `isBookmarked` returning `false`
    - Annotate: `// Feature: virasat-heritage-app, Property 3: Bookmark toggle invariant`
    - **Validates: Requirements 7.1, 7.9**

- [ ] 5. Offline Mode & Bookmarks — Network layer and UI (Requirement 7)
  - [ ] 5.1 Create `NetworkMonitor`
    - Create `data/service/NetworkMonitor.kt` implementing `ConnectivityManager.NetworkCallback` as specified in design §2.4
    - Expose `isOnline: StateFlow<Boolean>` with synchronous initial value from `isCurrentlyOnline()`
    - Instantiate `NetworkMonitor` as a singleton in `VirasatApplication` and register/unregister in `onCreate`/`onTerminate`
    - _Requirements: 7.6_

  - [ ] 5.2 Configure Coil disk cache in `VirasatApplication`
    - In `VirasatApplication.onCreate()`, build a Coil `ImageLoader` with `DiskCache` pointing to `cacheDir/image_cache` at 5% max size, with both disk and memory cache policies enabled
    - Call `Coil.setImageLoader(imageLoader)` to register it globally
    - _Requirements: 7.7_

  - [ ] 5.3 Create `OfflineViewModel` with bookmark sync
    - Create `viewmodel/OfflineViewModel.kt` as an `AndroidViewModel`
    - Inject `NetworkMonitor` and `RoomHeritageRepository` via `RepositoryProvider`
    - Collect `networkMonitor.isOnline`, filtering for `true`, and call `syncBookmarksFromFirestore()` as specified in design §2.5
    - Expose `bookmarkedSites: StateFlow<List<HeritageSite>>` by combining `observeBookmarks()` with `getSiteById` lookups
    - Expose `isOnline: StateFlow<Boolean>` forwarded from `NetworkMonitor`
    - _Requirements: 7.4, 7.5, 7.6_

  - [ ] 5.4 Implement `OfflineScreen`
    - Create or update `ui/screens/OfflineScreen.kt` to consume `OfflineViewModel`
    - Display a "You are offline" banner when `isOnline` is `false`
    - Show bookmarked `HeritageSite` cards in a `LazyColumn` using cached Room data
    - Show an empty-state message when the bookmark list is empty
    - Navigate to `site_detail/{siteId}` on card tap
    - Wire `OfflineScreen` into `MainActivity` NavHost at route `"offline"` (already present as stub — replace with real composable)
    - _Requirements: 7.5, 7.6, 7.8_

- [ ] 6. Checkpoint — Verify offline/bookmark flow compiles and Room migration succeeds
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 7. Smart Badges & Gamification Engine (Requirement 8)
  - [ ] 7.1 Create badge domain model: `BadgeCondition`, `BadgeDefinition`, `BadgeCatalogue`, `BadgeResult`, `QuizResult`
    - Create `data/model/Badge.kt` containing the `BadgeCondition` sealed class, `BadgeDefinition` data class, `BadgeCatalogue` singleton, and `BadgeResult` data class as specified in design §§3.1–3.3
    - Create `data/model/QuizResult.kt` with `siteId: String`, `score: Int`, `completedAt: Long` fields
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5, 8.6, 8.7_

  - [ ] 7.2 Implement `BadgeEngine`
    - Create `domain/BadgeEngine.kt` as a Kotlin `object`
    - Implement `evaluate(checkIns, unlockedFacts, quizResults, aiTourUsed): List<BadgeResult>` as a pure function with no I/O or coroutines, exactly as specified in design §3.4
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5, 8.6, 8.7, 8.10_

  - [ ] 7.3 Write property test for BadgeEngine determinism (Property 4)
    - **Property 4: BadgeEngine Determinism**
    - Create `test/.../BadgeEngineDeterminismPropertyTest.kt`
    - Define `arbCheckIns()`, `arbUnlockedFacts()`, `arbQuizResults()` Arb generators
    - Use `checkAll(100, arbCheckIns(), arbUnlockedFacts(), arbQuizResults(), Arb.boolean())` to verify calling `BadgeEngine.evaluate` twice with identical inputs returns structurally equal lists
    - Annotate: `// Feature: virasat-heritage-app, Property 4: BadgeEngine determinism`
    - **Validates: Requirements 8.2, 8.3, 8.4, 8.5, 8.6, 8.7, 8.10**

  - [ ] 7.4 Create `BadgesViewModel`
    - Create `viewmodel/BadgesViewModel.kt` as an `AndroidViewModel`
    - Combine `repo.getAllCheckIns()` and `repo.getAllUnlockedFacts()` flows
    - Load `quizResults` from SharedPreferences and `aiTourUsed` flag from SharedPreferences
    - Call `BadgeEngine.evaluate(...)` and expose results as `badges: StateFlow<List<BadgeResult>>`
    - _Requirements: 8.1, 8.8, 8.9_

  - [ ] 7.5 Update `BadgesScreen` to consume `BadgesViewModel`
    - Refactor `ui/screens/BadgesScreen.kt` to obtain `BadgesViewModel` via `viewModel()`
    - Render locked badges with a `LinearProgressIndicator` showing `progress.toFloat() / target` and a `"${result.progress} / ${result.target}"` label
    - Detect newly unlocked badges by comparing previous and current `badges` state; show a `Snackbar` congratulatory message on unlock
    - _Requirements: 8.1, 8.8, 8.9_

- [ ] 8. Checkpoint — Verify badge engine and screen compile correctly
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 9. Push Notifications & Geofencing (Requirement 9)
  - [ ] 9.1 Add FCM and WorkManager dependencies, declare permissions and manifest entries
    - Add `implementation("com.google.firebase:firebase-messaging-ktx")` to `app/build.gradle.kts` (on Firebase BOM)
    - Add `implementation("androidx.work:work-runtime-ktx:2.9.0")` for offline notification queuing
    - Add `<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />` to `AndroidManifest.xml`
    - Add `<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />` to `AndroidManifest.xml`
    - Add the `virasat://` deep link `<intent-filter>` to `MainActivity` in `AndroidManifest.xml` as specified in design §4.5
    - _Requirements: 9.1, 9.2, 9.5_

  - [ ] 9.2 Create `NotificationPreferences` and `NotificationHelper`
    - Create `util/NotificationPreferences.kt` as a Kotlin `object` with `isEnabled` and `setEnabled` as specified in design §4.4
    - Create `util/NotificationHelper.kt` as a Kotlin `object` with `createChannels`, `showProximityNotification`, `showBadgeNotification`, and `showRemoteNotification` as specified in design §4.2
    - Create three notification channels: `virasat_proximity` (HIGH), `virasat_weekly` (DEFAULT), `virasat_badges` (DEFAULT)
    - Each `show*` method must check `NotificationPreferences.isEnabled` before posting
    - _Requirements: 9.4, 9.5, 9.6, 9.7, 9.8_

  - [ ] 9.3 Create `VirasatFirebaseMessagingService`
    - Create `service/VirasatFirebaseMessagingService.kt` extending `FirebaseMessagingService` as specified in design §4.1
    - Implement `onNewToken` to update `users/{uid}/fcmToken` in Firestore
    - Implement `onMessageReceived` to delegate to `NotificationHelper.showRemoteNotification`
    - Register the service in `AndroidManifest.xml` with `<intent-filter>` for `com.google.firebase.MESSAGING_EVENT`
    - _Requirements: 9.1, 9.3_

  - [ ] 9.4 Create `GeofenceManager`, `GeofenceBroadcastReceiver`, and `ProximityNotificationWorker`
    - Create `service/GeofenceManager.kt` with `registerAll(sites)` using a 500 m radius and `GEOFENCE_TRANSITION_ENTER` as specified in design §4.3
    - Create `service/GeofenceBroadcastReceiver.kt` that handles `GeofencingEvent`, extracts the triggering site ID, and calls `NotificationHelper.showProximityNotification`
    - Create `service/ProximityNotificationWorker.kt` as a `CoroutineWorker` that posts the queued proximity notification; used when the device is offline at trigger time (enqueued with `NetworkType.CONNECTED` constraint)
    - Register `GeofenceBroadcastReceiver` in `AndroidManifest.xml`
    - _Requirements: 9.2, 9.9_

  - [ ] 9.5 Request `POST_NOTIFICATIONS` permission and wire deep links in `MainActivity`
    - In `MainActivity.onCreate`, use `ActivityResultContracts.RequestPermission` to request `POST_NOTIFICATIONS` before calling `NotificationHelper.createChannels`
    - Call `GeofenceManager(this).registerAll(sites)` after sites are loaded in `HomeViewModel` or `MainActivity`
    - Add deep link handling in `MainActivity.onCreate`: inspect `intent?.data` and navigate to `site_detail/{id}`, `badges`, or `itinerary/{id}` as specified in design §4.5
    - _Requirements: 9.5, 9.6, 9.7_

  - [ ] 9.6 Add notification category toggles to `SettingsScreen`
    - Update `ui/screens/SettingsScreen.kt` to include three `Switch` composables for proximity, weekly facts, and badge-unlock notification categories
    - Each switch reads from and writes to `NotificationPreferences` using the category keys `"proximity"`, `"weekly"`, `"badges"`
    - _Requirements: 9.8_

- [ ] 10. Checkpoint — Verify notification infrastructure compiles and manifest is valid
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 11. Social Sharing & Community (Requirement 10)
  - [ ] 11.1 Create `CommunityPost` domain model and `CommunityRepository`
    - Create `data/model/CommunityPost.kt` with all required fields as specified in design §5.1
    - Create `data/repository/CommunityRepository.kt` with `observePosts()` (Firestore snapshot listener as `callbackFlow`) and `submitPost(post, photoBitmap?)` with Firebase Storage upload as specified in design §5.2
    - Add `implementation("com.google.firebase:firebase-storage-ktx")` to `app/build.gradle.kts` (on Firebase BOM)
    - _Requirements: 10.1, 10.2, 10.3, 10.10_

  - [ ] 11.2 Create `CommunityViewModel`
    - Create `viewmodel/CommunityViewModel.kt` as an `AndroidViewModel` consuming `CommunityRepository` as specified in design §5.3
    - Expose `posts: StateFlow<List<CommunityPost>>` via `stateIn(WhileSubscribed(5_000))`
    - Expose `submitPost(post, photoBitmap?)` delegating to the repository
    - _Requirements: 10.1, 10.8_

  - [ ] 11.3 Update `CommunityScreen` to consume `CommunityViewModel`
    - Refactor `ui/screens/CommunityScreen.kt` to obtain `CommunityViewModel` via `viewModel()`
    - Replace any local `CommunityPost` data class with the domain model
    - Add a `FloatingActionButton` that opens a bottom sheet for composing a new post (text field with 500-char limit + character counter, optional photo picker)
    - Show an empty-state invitation card when `posts` is empty
    - _Requirements: 10.1, 10.8, 10.9_

  - [ ] 11.4 Implement `CheckInCardRenderer` and check-in share flow
    - Create `ui/components/CheckInCardRenderer.kt` with a `@Composable` function that renders a 1080×1080 card (site image, stamp icon, site name, user name) and returns a `Bitmap` via `drawToBitmap()`
    - Create `util/ShareUtils.kt` with `shareCheckInCard(context, bitmap)` that writes the bitmap to `cacheDir`, obtains a `FileProvider` URI, and launches the system share sheet
    - Declare `FileProvider` in `AndroidManifest.xml` with `android:authorities="${applicationId}.fileprovider"` and a `file_paths.xml` resource
    - Wire the share button in `CheckInSuccessScreen` to call `shareCheckInCard`
    - _Requirements: 10.2, 10.3_

  - [ ] 11.5 Create `ReviewsViewModel` and update `ReviewsScreen`
    - Create `viewmodel/ReviewsViewModel.kt` as an `AndroidViewModel` with `submitReview(siteId, text, photoBitmap?)` as specified in design §5.6
    - Enforce `text.length in 1..500` with `require()`; upload photo to `reviews/{siteId}/{uuid}.jpg` in Firebase Storage
    - Update `ui/screens/ReviewsScreen.kt` to consume `ReviewsViewModel`, add a character counter on the text field, and enforce `maxLength = 500`
    - _Requirements: 10.4, 10.5, 10.9_

  - [ ] 11.6 Create `LeaderboardViewModel` and update `LeaderboardScreen`
    - Create `viewmodel/LeaderboardViewModel.kt` as an `AndroidViewModel` with a real-time Firestore snapshot listener ordered by `checkInCount` descending (top 50) as specified in design §5.7
    - Create `data/model/LeaderboardEntry.kt` data class
    - Update `ui/screens/LeaderboardScreen.kt` to consume `LeaderboardViewModel` and highlight the current user's row when `entry.uid == viewModel.currentUid`
    - _Requirements: 10.6, 10.7_

- [ ] 12. Checkpoint — Verify community and leaderboard screens compile
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 13. Itinerary Planner — Domain and data layer (Requirement 11)
  - [ ] 13.1 Create `Itinerary` data class and `ItineraryRepository`
    - Create `data/model/Itinerary.kt` with `id`, `name`, `siteIds`, `createdAt` fields as specified in design §6.1
    - Create `data/repository/ItineraryRepository.kt` with `save(itinerary): String`, `getAll(): Flow<List<Itinerary>>`, and `getById(id): Itinerary?` backed by `users/{uid}/itineraries` Firestore sub-collection
    - _Requirements: 11.4, 11.5, 11.8_

  - [ ] 13.2 Implement `ItineraryPlanner`
    - Create `domain/ItineraryPlanner.kt` as a Kotlin `object`
    - Implement `haversineDistance(lat1, lon1, lat2, lon2): Double` using the Haversine formula (Earth radius 6371 km)
    - Implement `totalDistance(sites: List<HeritageSite>): Double` as the sum of consecutive `haversineDistance` calls
    - Implement `nearestNeighbour(sites: List<HeritageSite>): List<HeritageSite>` — return input unchanged for 0 or 1 sites; otherwise apply the greedy nearest-neighbour algorithm starting from `sites[0]`
    - All three functions must be pure (no I/O, no state)
    - _Requirements: 11.2, 11.3, 11.9_

  - [ ] 13.3 Write property test for Haversine distance symmetry (Property 5)
    - **Property 5: Haversine Distance Symmetry**
    - Create `test/.../HaversineSymmetryPropertyTest.kt`
    - Use `checkAll(100, Arb.double(-90.0, 90.0), Arb.double(-180.0, 180.0), Arb.double(-90.0, 90.0), Arb.double(-180.0, 180.0))` to verify `abs(d1 - d2) < 1e-9`
    - Annotate: `// Feature: virasat-heritage-app, Property 5: Haversine distance symmetry`
    - **Validates: Requirements 11.3, 11.11**

  - [ ] 13.4 Write property test for nearest-neighbour optimisation invariant (Property 6)
    - **Property 6: Nearest-Neighbour Optimisation Invariant**
    - Create `test/.../NearestNeighbourPropertyTest.kt`
    - Define `arbHeritageSite()` Arb generating `HeritageSite` with random lat/lon in valid ranges
    - Use `checkAll(100, Arb.list(arbHeritageSite(), 2..10))` to verify `totalDistance(nearestNeighbour(sites)) <= totalDistance(sites) + 1e-9`
    - Annotate: `// Feature: virasat-heritage-app, Property 6: Nearest-neighbour optimisation invariant`
    - **Validates: Requirements 11.2, 11.10**

- [ ] 14. Itinerary Planner — ViewModel and UI (Requirement 11)
  - [ ] 14.1 Create `ItineraryViewModel`
    - Create `viewmodel/ItineraryViewModel.kt` as an `AndroidViewModel`
    - Inject `ItineraryRepository` and `HeritageRepository` via `RepositoryProvider`
    - Expose `itineraries: StateFlow<List<Itinerary>>` from `ItineraryRepository.getAll()`
    - Expose `currentSites: StateFlow<List<HeritageSite>>` for the itinerary being built (resolved from `siteIds` via `HeritageRepository`)
    - Implement `addSite(site)` that appends the site and re-runs `ItineraryPlanner.nearestNeighbour` on the updated list
    - Implement `saveItinerary(name)` that validates name length (1–100 chars) and calls `ItineraryRepository.save`
    - Implement `exportDeepLink(itineraryId): String` returning `"virasat://itinerary/$itineraryId"`
    - _Requirements: 11.1, 11.2, 11.4, 11.5, 11.7_

  - [ ] 14.2 Implement `ItineraryScreen`
    - Update `ui/screens/ItineraryScreen.kt` to consume `ItineraryViewModel` via `viewModel()`
    - Add a search bar that filters the full site catalogue and allows adding sites to the current itinerary
    - Display the ordered site list with `haversineDistance` between consecutive pairs shown in kilometres
    - Show a name text field (max 100 chars) and a Save button
    - Display all saved itineraries in a separate section; tapping a site navigates to `site_detail/{siteId}`
    - Add a Share button that calls `viewModel.exportDeepLink` and invokes the system share sheet with the deep link URL
    - Show a single site without distance estimate when the itinerary has fewer than 2 sites
    - _Requirements: 11.1, 11.2, 11.3, 11.5, 11.6, 11.7, 11.9_

  - [ ] 14.3 Handle `virasat://itinerary/{itineraryId}` deep link
    - In `MainActivity`, extend the deep link handler to detect `uri.host == "itinerary"` and navigate to `"itinerary/${uri.lastPathSegment}"`
    - Update the `"itinerary"` NavHost composable to accept an optional `{itineraryId}` argument and load the matching itinerary from `ItineraryRepository.getById`
    - Show an "Itinerary not found" message when `getById` returns `null`
    - _Requirements: 11.7, 11.8_

- [ ] 15. Final checkpoint — Ensure all tests pass and all features are wired end-to-end
  - Ensure all tests pass, ask the user if questions arise.

---

## Notes

- Tasks marked with `*` are optional and can be skipped for a faster MVP; all 6 property tests are optional sub-tasks.
- Each task references specific requirements for traceability.
- Property tests run in the JVM `test/` source set (no emulator needed); Room toggle tests run in `androidTest/`.
- `BadgeEngine`, `ItineraryPlanner`, `Converters`, and `GeminiHeritageService.buildPayload` are pure functions — test them without Android dependencies.
- The Room database migration from v3 → v4 is non-destructive; existing check-in and unlocked-fact data is preserved.
- `firebase-messaging-ktx` and `firebase-storage-ktx` are already on the Firebase BOM — only the `implementation(...)` line needs adding, no version pin required.

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1"] },
    { "id": 1, "tasks": ["2.1", "2.4", "4.1", "7.1", "9.1", "11.1", "13.1"] },
    { "id": 2, "tasks": ["2.2", "2.6", "4.2", "7.2", "9.2", "11.2", "13.2"] },
    { "id": 3, "tasks": ["2.3", "2.5", "4.3", "7.3", "9.3", "11.3", "13.3"] },
    { "id": 4, "tasks": ["4.4", "5.1", "7.4", "9.4", "11.4", "11.5", "13.4"] },
    { "id": 5, "tasks": ["5.2", "5.3", "7.5", "9.5", "11.6", "14.1"] },
    { "id": 6, "tasks": ["5.4", "9.6", "14.2"] },
    { "id": 7, "tasks": ["14.3"] }
  ]
}
```
