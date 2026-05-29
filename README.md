# KnowzyDemo

Android legal PDF analyzer with OCR, Groq-powered analysis, a dashboard, and a legal-rights guide.

## Local setup

1. Open the project in Android Studio.
2. Copy `local.properties.example` to `local.properties` if Android Studio has not created `local.properties`.
3. Keep your local Android SDK path in `sdk.dir`.
4. Paste your Groq API key after `GROQ_API_KEY=` in `local.properties`.
5. Build with:

```powershell
.\gradlew.bat assembleDebug
```

Example private `local.properties` entry:

```properties
GROQ_API_KEY=PASTE_YOUR_GROQ_API_KEY_HERE
```

`local.properties`, build output, and IDE-local files are ignored by Git. Do not commit API keys or APK files built with a real key.
