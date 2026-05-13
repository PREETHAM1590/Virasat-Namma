# Virasat Firebase CLI Setup

## Prerequisites

1. Install Node.js: https://nodejs.org
2. Install Firebase CLI:
   ```bash
   npm install -g firebase-tools
   ```
3. Login to Firebase:
   ```bash
   firebase login
   ```

## Project Configuration

Project files already created:
- `.firebaserc` — maps to `virasat-20dcb`
- `firebase.json` — Firestore rules + indexes config
- `firestore.rules` — security rules
- `firestore.indexes.json` — composite indexes (empty, auto-indexes handle queries)

## Deploy Firestore Rules & Indexes

```bash
firebase deploy --only firestore --project virasat-20dcb
```

Or use the provided batch script:
```bash
cd scripts
./deploy-firestore.bat
```

## Seed Data (Option A: CLI Script)

Install deps and run the Node.js seeder:
```bash
cd scripts
npm install
node seed-firestore.js
```

## Seed Data (Option B: App Auto-Seeder) **Recommended**

Just run the Android app. `FirestoreSeeder` in `VirasatApplication.kt` uploads all `KarnatakaSites` on first launch automatically.

## Verify in Firebase Console

1. Open https://console.firebase.google.com/project/virasat-20dcb/firestore
2. Check the `sites` collection has documents
3. Check `checkins` and `unlockedFacts` collections

## Useful CLI Commands

```bash
# Deploy only rules
firebase deploy --only firestore:rules

# Deploy only indexes
firebase deploy --only firestore:indexes

# Get current rules
firebase firestore:rules:download --project virasat-20dcb

# List collections
firebase firestore:documents:list --project virasat-20dcb

# Delete all documents in sites collection (DANGER)
firebase firestore:delete --all-collections --project virasat-20dcb
```
