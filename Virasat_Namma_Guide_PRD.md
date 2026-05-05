# Virasat-Namma Guide

**Smart Heritage Guide - Tourism & Travel App**  
**Android App Development using GenAI**  
**Comprehensive Project Requirement Document**

---

## Document Information

| Field | Details |
|---|---|
| Program | MindMatrix VTU Internship Program |
| Project Title Number | 81 |
| Project Name | Virasat-Namma Guide |
| Application Type | Smart Heritage Tourism & Cultural Discovery App |
| Platform | Android |
| Core Stack | Kotlin, Jetpack Compose, Room Database, Google ML Kit, MediaPlayer |
| Document Type | PRD - Project Requirement Document |

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Purpose and Goals](#2-purpose-and-goals)
3. [Scope of the Application](#3-scope-of-the-application)
4. [Target Users and Personas](#4-target-users-and-personas)
5. [Functional Requirements](#5-functional-requirements)
6. [Non-Functional Requirements](#6-non-functional-requirements)
7. [System Architecture](#7-system-architecture)
8. [Database Schema](#8-database-schema)
9. [User Flow](#9-user-flow)
10. [Success Criteria](#10-success-criteria)
11. [Future Scope and Enhancement Ideas](#11-future-scope-and-enhancement-ideas)
12. [Next Action Items](#12-next-action-items)

---

## 1. Project Overview

### Project Title

**Virasat-Namma Guide - A Smart Heritage Tourism & Cultural Discovery App**

### Objective

Virasat-Namma is a Smart Heritage Guide that turns a smartphone into a digital historian. The app helps users discover Karnataka's hidden historical gems using location-based discovery, rich bilingual storytelling, QR-based hidden fact unlocks, and a digital travel passport - all built using Kotlin, Jetpack Compose, Room Database, and Google ML Kit.

### Core Idea

> "Rediscover Karnataka's soul - one heritage site at a time."

The app acts as a digital tour guide for cultural and historical sites across Karnataka. It focuses on storytelling by delivering the history, architectural significance, and local legends of sites that are forgotten or lack physical signage.

---

## 2. Purpose and Goals

### Purpose

To create an immersive, culturally rich Android app that:

1. Surfaces Karnataka's undiscovered heritage sites through simulated location-based discovery.
2. Delivers bilingual Kannada and English rich descriptions and audio guides for each site.
3. Demonstrates proficiency in Google ML Kit, Room DB, MediaPlayer, and multi-language support.
4. Offers a portfolio-worthy project at the intersection of culture, tourism, and technology.

### Goals

1. Enable discovery of heritage sites within a configurable radius using simulated GPS data.
2. Allow QR code scanning to unlock hidden historical facts for each site.
3. Provide audio guides using Android MediaPlayer with reliable play/pause lifecycle handling.
4. Persist user check-ins in a local Room database as a digital travel passport.
5. Support seamless switching between Kannada and English using `strings.xml` localization.

---

## 3. Scope of the Application

### In-Scope Functionalities

1. Heritage site discovery using simulated location data.
2. Bilingual information hub in Kannada and English for each site.
3. QR code scanning to unlock hidden facts via Google ML Kit.
4. Audio guide playback using MediaPlayer with play/pause controls.
5. Digital travel passport with check-in persistence in Room DB.
6. Site detail pages with history, architecture, and local legends.

### Out of Scope for This Internship

1. Live GPS integration with real-time location tracking.
2. User accounts, login, or cloud sync.
3. Crowdsourced content submission from users.
4. Augmented Reality overlays on monument views.

These items can be considered future enhancements.

---

## 4. Target Users and Personas

### Primary Users

1. **Heritage Tourists** - Travellers interested in Karnataka's historical and cultural sites.
2. **Students and Educators** - Learners seeking local history through interactive, engaging content.
3. **Local Community Members** - Residents curious about their district's cultural roots.

### User Persona Example

| Field | Details |
|---|---|
| Name | Ananya Sharma |
| Age | 21 |
| Role | History Student, Bengaluru |
| Goal | Explore Karnataka temples and understand their architectural history. |
| Pain Points | Tourist sites lack signage; information is only available in English online. |
| Expectation | An app that tells stories in Kannada, lets her check in to sites she visited, and surprises her with hidden facts. |

---

## 5. Functional Requirements

### 5.1 Site Discovery

1. Display a list or map of heritage sites within a configurable radius using simulated coordinate data.
2. Each site entry must show the site name, district, thumbnail, and simulated distance.
3. Users should be able to filter sites by district, era, or type such as temple, inscription, fort, and similar categories.

### 5.2 Information Hub

1. Each heritage site must have a rich detail page.
2. The detail page must include History, Architectural Significance, and Local Legends sections.
3. Users must be able to switch between Kannada and English using `strings.xml` localization.
4. Each site detail page should display a gallery of curated site images stored as static or local assets.

### 5.3 QR Scanner

1. The app must integrate Google ML Kit Barcode Scanning to read QR codes placed at mock heritage sites.
2. Each QR code must encode a unique Site ID.
3. Scanning a valid QR code must open the matching site detail page.
4. A Hidden Fact section must be unlocked after a successful QR scan.
5. The unlocked hidden fact must be stored locally.

### 5.4 Audio Guide

1. Each site must have an associated audio clip stored as a pre-recorded MP3 or WAV asset.
2. The app must integrate MediaPlayer for audio guide playback.
3. Audio controls must include play, pause, stop, and seek.
4. Audio playback must handle app lifecycle changes correctly, including pausing on background and releasing resources on destroy.

### 5.5 Check-In and Travel Passport

1. Users must be able to check in to a heritage site manually or through QR scan interaction.
2. Check-in status must be persisted in Room DB.
3. Check-in data must survive app restart.
4. The Travel Passport screen must show all checked-in sites with date and badge details.

---

## 6. Non-Functional Requirements

| Requirement | Description |
|---|---|
| Performance | Site list and detail screens must load within 2 seconds. |
| Usability | Interface must be intuitive for non-tech-savvy heritage visitors. |
| Reliability | Check-in and unlocked facts must persist even after app restarts. |
| Localization | Full Kannada and English support must be implemented through `strings.xml` with an easy toggle. |
| Audio Stability | MediaPlayer must not crash on orientation change or app backgrounding. |
| Scalability | Architecture must support future addition of new districts and heritage sites. |

---

## 7. System Architecture

### Architecture Pattern

**MVVM - Model View ViewModel**

### Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| UI | Jetpack Compose | Declarative UI with temple-inspired theming. |
| Language | Kotlin | Primary development language. |
| Database | Room DB | Local persistence for check-ins and unlocked facts. |
| QR Scanning | Google ML Kit Barcode | Site ID recognition from QR codes. |
| Audio | MediaPlayer | Heritage audio guide playback. |
| Localization | `strings.xml` - Kannada and English | Bilingual content support. |
| Data | Local JSON / Assets | Simulated site data and audio files. |

### Data Flow

```text
User -> Compose UI -> ViewModel -> Repository -> Room DB / Local Assets / ML Kit
```

---

## 8. Database Schema

| Table | Fields | Description |
|---|---|---|
| HeritageSite | `id` (PK), `name`, `district`, `era`, `type`, `description_en`, `description_kn`, `lat`, `lng`, `image_path`, `audio_path` | Master list of heritage sites loaded from local JSON. |
| CheckIn | `id` (PK), `site_id` (FK), `checkin_date`, `method` (`manual` / `qr`) | Stores user check-ins per site. |
| UnlockedFact | `id` (PK), `site_id` (FK), `fact_en`, `fact_kn`, `unlocked_date` | Stores hidden facts unlocked through QR scan. |

---

## 9. User Flow

1. Splash Screen.
2. Language Selection - Kannada or English.
3. Home Screen - Site Discovery List filtered by radius or district.
4. Site Detail Page - History, Architecture, and Legends tabs.
5. Audio Guide - Play or pause history narration.
6. QR Scanner - Scan mock QR code and unlock Hidden Fact.
7. Check-In - Mark site as visited and save to Travel Passport.
8. Travel Passport - View all visited sites and earned badges.

---

## 10. Success Criteria

1. QR scanner correctly reads the Site ID and navigates to the matching site detail page.
2. Audio guide plays and pauses without crashing the app across orientation changes and backgrounding.
3. Check-in data is persisted in Room DB and remains visible in the Travel Passport after app restart.
4. Language toggle switches all UI text between Kannada and English without requiring an app restart.
5. UI visually reflects an Indian temple architecture theme so reviewers can recognise the inspiration.
6. Site discovery returns at least 5 mock heritage sites with simulated distance data.

---

## 11. Future Scope and Enhancement Ideas

1. Live GPS integration for real-time nearby site discovery.
2. Crowdsourced content so locals can submit site stories and images.
3. AR overlays showing reconstructed versions of ruined monuments.
4. Firebase sync for cross-device travel passport continuity.
5. Gamification through district completion badges and leaderboard.
6. Offline-first caching for use in areas with no connectivity.

---

## 12. Next Action Items

1. Finalize the list of 5 to 8 Karnataka heritage sites to include in the MVP dataset.
2. Create bilingual descriptions in English and Kannada for each site.
3. Prepare wireframes and submit them to the mentor for approval.
4. Assign team roles for UI, QR/ML Kit, Database, Audio, and Content.
5. Begin Week 1 setup in Android Studio with project structure and data models.
6. Maintain a weekly progress journal for mentor review meetings.

---

## Appendix: MVP Feature Checklist

| Feature | Required for MVP | Notes |
|---|---:|---|
| Simulated heritage site discovery | Yes | Minimum 5 mock sites. |
| Kannada and English content | Yes | Implement with `strings.xml`. |
| QR scanner | Yes | Use Google ML Kit Barcode Scanning. |
| Hidden fact unlock | Yes | Store unlock status locally. |
| Audio guide | Yes | Use MediaPlayer with lifecycle handling. |
| Travel Passport | Yes | Persist check-ins in Room DB. |
| Live GPS | No | Future enhancement. |
| User login/cloud sync | No | Future enhancement. |
| AR overlays | No | Future enhancement. |

