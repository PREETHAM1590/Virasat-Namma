# Virasat-Namma — Architecture Design

## High-Level Architecture Diagram

```mermaid
graph TB
    subgraph Presentation["Presentation Layer - 45 Compose Screens"]
        Screens["Jetpack Compose UI"]
        BottomNav["Bottom Nav: Explore | Heritage | Saved | Profile"]
    end

    subgraph ViewModels["ViewModel Layer"]
        HVM["HomeViewModel"]
        DVM["DetailViewModel"]
        SVM["SearchViewModel"]
        PVM["ProfileViewModel"]
        PAVM["PassportViewModel"]
        MVM["MapViewModel"]
    end

    subgraph Repositories["Repository Layer"]
        HSR["HeritageSiteRepository"]
        UR["UserRepository"]
        CR["CheckInRepository"]
        BR["BookmarkRepository"]
    end

    subgraph DataSources["Data Sources"]
        Room["Room DB - SQLite"]
        Firebase["Firebase - Auth, Firestore, Storage"]
        AI["AWS Nova LLM"]
        TTS["Android TTS"]
        MLKit["ML Kit - QR Scanner"]
    end

    Screens --> HVM
    Screens --> DVM
    Screens --> SVM
    Screens --> PVM
    Screens --> PAVM
    Screens --> MVM
    HVM --> HSR
    DVM --> HSR
    SVM --> HSR
    PVM --> UR
    PAVM --> CR
    MVM --> HSR
    DVM --> BR
    HSR --> Room
    HSR --> Firebase
    HSR --> AI
    UR --> Firebase
    CR --> Room
    BR --> Room
```

## Navigation Flow

```mermaid
graph LR
    Splash --> Language
    Language --> Login
    Login --> Onboarding
    Onboarding --> Home
    Home --> SiteDetail
    Home --> Search
    Home --> Map
    Home --> Profile
    SiteDetail --> AudioGuide
    SiteDetail --> AINarratedTour
    SiteDetail --> ImmersivePhoto
    SiteDetail --> Gallery
    SiteDetail --> Reviews
    SiteDetail --> Quiz
    SiteDetail --> VirtualTour
    SiteDetail --> TalkingTours
    Profile --> Settings
    Profile --> Badges
    Profile --> CheckIns
    Profile --> Community
    Profile --> Feedback
```

## Data Flow - AI Narration

```mermaid
sequenceDiagram
    participant U as User
    participant S as AudioGuideScreen
    participant VM as DetailViewModel
    participant AI as AWS Nova API
    participant TTS as TextToSpeech
    U->>S: Taps Audio Guide
    S->>VM: requestNarration(siteId)
    VM->>AI: POST /invoke with site context
    AI-->>VM: Structured JSON chapters
    VM-->>S: NarrationState update
    S->>TTS: speak(chapter.text)
    TTS-->>U: Audio playback
```

## Data Flow - QR Check-in

```mermaid
sequenceDiagram
    participant U as User
    participant Cam as CameraX
    participant ML as ML Kit
    participant VM as PassportViewModel
    participant DB as Room DB
    U->>Cam: Points camera at QR
    Cam->>ML: Frame analysis
    ML-->>VM: Decoded site_id
    VM->>DB: Insert CheckIn record
    VM->>DB: Insert UnlockedFact
    VM-->>U: CheckInSuccess plus Hidden Fact
```

## Database Schema

```mermaid
erDiagram
    HeritageSite {
        string id PK
        string name
        string nameLocal
        string district
        string type
        string description
        string history
        string architecture
        string legends
        string imageUrl
        float latitude
        float longitude
        string visitingHours
        string entryFee
        string qrCodeId
        float rating
    }
    CheckIn {
        string id PK
        string site_id FK
        date checkin_date
        string method
    }
    UnlockedFact {
        string id PK
        string site_id FK
        string fact_en
        string fact_kn
        date unlocked_date
    }
    Bookmark {
        string id PK
        string site_id FK
        date created_at
    }
    HeritageSite ||--o{ CheckIn : has
    HeritageSite ||--o{ UnlockedFact : has
    HeritageSite ||--o{ Bookmark : has
```

## Tech Stack

```mermaid
graph LR
    subgraph Frontend
        Kotlin["Kotlin 2.0.0"]
        Compose["Jetpack Compose"]
        M3["Material 3"]
        Coil["Coil Images"]
    end
    subgraph Backend
        FirebaseAuth["Firebase Auth"]
        Firestore["Cloud Firestore"]
        Storage["Firebase Storage"]
    end
    subgraph AILayer["AI Layer"]
        Nova["AWS Nova LLM"]
        Gemini["Gemini fallback"]
    end
    subgraph Local
        RoomDB["Room SQLite"]
        SP["SharedPreferences"]
        AndroidTTS["TextToSpeech"]
    end
    subgraph DevTools
        Hilt["Hilt DI"]
        Gradle["Gradle 8.9"]
        AGP["AGP 8.7.0"]
    end
```

## Module Structure

```
app/src/main/java/com/example/virasat/
├── data/
│   ├── db/               # Room database, DAOs, entities
│   ├── di/               # Hilt modules (RepositoryProvider)
│   ├── repository/       # Repository implementations
│   ├── service/          # API services (Weather, AI)
│   └── source/           # Static data (KarnatakaSites.kt - 59 sites)
├── di/                    # App-level DI modules
├── domain/                # Business logic / Use cases
├── ui/
│   ├── components/       # Reusable Compose components
│   ├── screens/          # 45 screen composables
│   └── theme/            # Colors, Typography, Shapes
├── viewmodel/            # 6 ViewModels
├── utils/                # Extensions, helpers
└── MainActivity.kt       # NavHost, auth state, routes
```

## Color System

| Token | Hex | Usage |
|-------|-----|-------|
| Primary | #556500 | Buttons, active states |
| PrimaryContainer | #B7D23F | Cards, highlights |
| Background | #F9FAF5 | Screen backgrounds |
| Surface | #F9FAF5 | Elevated surfaces |
| NavBackground | #0B2211 | Bottom navigation |

## Screens (45 Total)

| Category | Screens |
|----------|---------|
| Onboarding | Splash, Language, Onboarding, Login, SignUp, ForgotPassword |
| Core | Home, Search, Map, SiteDetail, PopularSites, HeritageSitesList |
| AI Features | AudioGuide, AINarratedTour, AIAssistant, Quiz, TalkingTours, VirtualTour |
| Media | ImageGallery, ImmersivePhoto, Reviews |
| Passport | TravelPassport, CheckInSuccess, MyCheckIns, Badges |
| Social | Community, Leaderboard, Bookmarked |
| Profile | Profile, Settings, Notifications, HeritageGuides, Itinerary |
| Support | HelpSupport, ContactUs, Feedback, About, PrivacyPolicy, TermsOfService |
| System | EmptyState, Error, Offline, Permission, DataSync |
