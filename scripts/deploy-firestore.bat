@echo off
echo ========================================
echo  Virasat Firestore Deploy Script
echo ========================================
echo.

REM Check if firebase CLI is installed
firebase --version >nul 2>&1
if errorlevel 1 (
    echo Firebase CLI not found. Installing...
    call npm install -g firebase-tools
    if errorlevel 1 (
        echo Failed to install Firebase CLI. Please install Node.js first.
        pause
        exit /b 1
    )
)

echo Firebase CLI version:
firebase --version
echo.

REM Login if needed
echo Checking Firebase login status...
firebase projects:list >nul 2>&1
if errorlevel 1 (
    echo You need to login to Firebase.
    firebase login
)

echo.
echo Deploying Firestore rules and indexes...
firebase deploy --only firestore:rules,firestore:indexes --project virasat-20dcb

if errorlevel 1 (
    echo.
    echo Deployment failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo  Firestore rules deployed successfully!
echo ========================================
pause
