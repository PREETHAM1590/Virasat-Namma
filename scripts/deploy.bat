@echo off
chcp 65001 >nul
setlocal

echo ==========================================
echo   Virasat - Firebase CLI Deploy
echo   Project: virasat-20dcb
echo ==========================================
echo.

REM Check Firebase CLI
firebase --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Firebase CLI not found.
    echo Install it: npm install -g firebase-tools
    pause
    exit /b 1
)

echo [1/3] Firebase CLI detected:
firebase --version
echo.

REM Check login status
firebase projects:list >nul 2>&1
if errorlevel 1 (
    echo [2/3] Not logged in. Running firebase login...
    firebase login
) else (
    echo [2/3] Already logged in to Firebase.
)
echo.

REM Deploy
echo [3/3] Deploying Firestore rules and indexes...
cd /d "%~dp0\.."
firebase deploy --only firestore --project virasat-20dcb

if errorlevel 1 (
    echo.
    echo [ERROR] Deployment failed.
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   Deployment successful!
echo ==========================================
echo.
echo Next steps:
echo   1. Run the Android app to auto-seed data
echo   2. Or run: cd scripts ^&^& node seed-firestore.js
echo.
pause
endlocal
