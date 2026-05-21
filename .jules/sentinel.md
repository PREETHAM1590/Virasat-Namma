# Sentinel Security Journal

## 2026-05-21 - API Response Body Leakage in Logcat
**Vulnerability:** GeminiHeritageService logged full HTTP error response bodies and exception messages via `Log.w()`. Error responses from AI APIs can contain echoed API keys, internal server paths, rate-limit details, and auth token information.
**Learning:** The logging was added for debugging convenience but remained in production code. On Android, logcat output is readable via ADB and (on older devices) by other apps with READ_LOGS permission.
**Prevention:** Never log response bodies or exception messages from external API calls. Log only the HTTP status code. Use a debug-only logging wrapper if verbose logging is needed during development.
