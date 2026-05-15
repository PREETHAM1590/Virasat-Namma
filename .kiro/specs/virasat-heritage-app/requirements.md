# Requirements Document

## Introduction

Virasat (ವಿರಾಸತ್ — meaning "Heritage" in Kannada) is a cultural tourism companion Android application for exploring Karnataka's rich heritage sites. The app serves travellers, students, history enthusiasts, and cultural tourists by providing immersive discovery, AI-powered narration, gamified check-ins, offline access, and a multilingual experience in English and Kannada.

The application is built with Kotlin and Jetpack Compose targeting Android API 26–35. It integrates Firebase (Firestore, Authentication, Analytics), Room (local persistence), Google Maps, ML Kit (QR scanning), Gemini AI (REST), CameraX, and biometric authentication to deliver a full-featured heritage exploration platform.

This document specifies the remaining functional requirements for new features and enhancements to the Virasat application.

---

## Glossary

- **App**: The Virasat Android application.
- **User**: An authenticated person using the App.
- **HeritageSite**: A Karnataka cultural or natural landmark with structured metadata.
- **Repository**: The HeritageRepository interface (data access layer) backed by either FirebaseHeritageRepository (online) or RoomHeritageRepository (offline).
- **Room_DB**: The local SQLite database (VirasatDatabase v4) managed by Room.
- **Gemini_Service**: The GeminiHeritageService singleton that communicates with the Gemini 2.0 Flash REST API.
- **NetworkMonitor**: A service class wrapping `ConnectivityManager.NetworkCallback` that exposes `isOnline: StateFlow<Boolean>`.

---

## Requirements

_No pending requirements. All features have been implemented._

---

## Notes

- Requirements 1-5 (App shell, navigation, language, authentication, biometrics) are fully implemented.
- Requirements 6-11 (Bug fixes, offline mode, badges, notifications, social features, itinerary planner) are fully implemented.
- Requirements 12-14 (QR check-in, heritage discovery, search/filter) are fully implemented.

This document will be updated when new feature requirements are defined.
