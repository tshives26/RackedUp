# 💪 **RackedUp** - Master Project Guide & Implementation Roadmap

## 📱 Project Overview

**RackedUp** is a modern, offline-first fitness tracking app built with the latest Android technologies. It's designed for serious athletes, trainers, and fitness enthusiasts who demand flexibility, precision, privacy, and style. Built on top of a robust foundation with a fully redesigned UI using Jetpack Compose, dynamic data visualization, wearable sync, and AI-powered recommendations — all without compromising user privacy.

### 🎯 Core Vision
- **Offline-first, Privacy-focused**: No data collection, no ads, 100% usable offline
- **AI-augmented, Not AI-dependent**: Smart when you want it, off when you don't
- **Compose-Native**: Future-proof, sleek, and efficient
- **Fitness Nerd-Friendly**: Full control, deep stats, no fluff
- **Highly Customizable**: User-defined workouts, exercises, templates, schedules

### 🤝 Target Users
- Personal Trainers & Coaches
- Powerlifters, Bodybuilders, Hyrox/Athletes
- Fitness hobbyists who hate cookie-cutter apps
- Users who value data ownership and privacy

---

## 🧱 Technology Stack

| Layer                  | Stack                                                                       |
| ---------------------- | --------------------------------------------------------------------------- |
| **Language**           | Kotlin                                                                      |
| **UI Framework**       | Jetpack Compose + Material 3 + Accompanist                                  |
| **Navigation**         | Jetpack Navigation Compose                                                  |
| **Architecture**       | MVVM + ViewModel + Kotlin StateFlow + Hilt (DI)                             |
| **Local Storage**      | Room (with DAO) + DataStore (preferences)                                   |
| **Cloud Integration**  | Optional: Google Services (Auth + Fitness), no Firebase/Firestore currently |
| **Health Sync**        | Google Fit API + Health Connect                                             |
| **Media Handling**     | Coil for Compose + CameraX                                                  |
| **Analytics & Crash**  | None currently implemented (planned for future)                             |
| **Push Notifications** | WorkManager for local reminders and notifications                           |
| **AI Integration**     | Gemini API / OpenAI for form coaching, recommendations, smart stats         |
| **Charts**             | MPAndroidChart + Vico + ComposeCharts                                       |
| **Testing**            | Compose Test APIs + Espresso + JUnit + Mockk + Robolectric                  |

---

## 🏗️ Architecture Details

### **MVVM + Compose Architecture**
```
View (Composables) ↔ ViewModel ↔ Repository ↔ DAO ↔ Database
                                    ↓
                              DataStore/Preferences
```

### **Technology Integration**
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + StateFlow + Hilt
- **Database**: Room + TypeConverters
- **Navigation**: Navigation Compose
- **Async**: Coroutines + Flow
- **Serialization**: Kotlinx Serialization
- **Image Loading**: Coil Compose
- **Charts**: Vico + MPAndroidChart
- **Testing**: Compose Testing + Hilt Testing

---

## 📁 Project Structure

```
app/src/main/java/com/chilluminati/rackedup/
├── core/
│   ├── progression/       # Progression calculation utilities
│   ├── sound/            # Sound management
│   └── util/             # Constants, extensions, utilities
├── data/
│   ├── database/         # Room database implementation
│   │   ├── dao/          # Data Access Objects
│   │   ├── entity/       # Database entities
│   │   └── converter/    # Type converters
│   └── repository/       # Repository layer implementations
├── di/                   # Hilt dependency injection modules
├── presentation/         # UI layer
│   ├── components/       # Reusable UI components
│   ├── dashboard/        # Dashboard screen
│   ├── exercises/        # Exercise library screens
│   ├── navigation/       # Navigation setup
│   ├── progress/         # Progress tracking screens
│   ├── programs/         # Program management screens
│   ├── profile/          # Profile and settings screens
│   ├── theme/           # Material 3 theming
│   └── workouts/        # Workout tracking screens
├── MainActivity.kt      # Main activity with Compose setup
├── RackedUpApplication.kt # Application class with Hilt
├── prefetch/            # Image prefetching workers
├── reminders/           # Auto backup and reminder system
└── ui/                  # Legacy UI components (dashboard, home, notifications)
```

---

## 🎯 Core Features & Functionality

### 🏋️‍♂️ **Workout Tracking**
- **Exercise Types**: Strength, Cardio, Isometric
- **Custom Exercises**: Add photos, videos, variations
- **Integrated Stopwatch + Workout Timer**: Custom rest timer, set countdown, auto-start
- **Real-time Health Data**: Pulls step count, calories, HR, etc. via Health Connect
- **Wearable Support**: Google Fit & Wear OS sync

### 📊 **Progress & Analytics**
- **Charts with MPAndroidChart or ComposeCharts**:
  - 1RM tracking, Volume Load, PR tracking
  - Body measurements (waist, arms, chest, etc.)
  - Weight and fat % trends
- **Progress Photos**: Before/after galleries, front/side views
- **Workout History**: Deep filtering by exercise, date, PR, tags

### 📆 **Program & Routine Builder**
- **Visual Workout Builder (Drag & Drop Style)**
- Set progression models: linear, double progression, % of 1RM
- Add supersets, circuits, drop sets
- Schedule routines with calendar view
- Program completion tracking

### 💾 **Data Management**
- **Automatic Backups**: Daily/weekly with local & Google Drive sync
- **CSV/ZIP Export + Import**
- **Multi-user Profiles**: One device, many users
- **OpenScale & 3rd Party Import Support**

### 🤖 **Smart Features**
- **AI Recommendations**:
  - Suggests workouts based on progress, fatigue, and muscle balance
  - Detects plateaus and offers alternative plans
- **Form Feedback** (Future): Pose Estimation with MediaPipe or AI Coach Mode
- **Recovery & Fatigue Tracker**: Optional HRV integration

### 🧠 **UX & Personalization**
- **Material 3 Dynamic Theming**: Follows system wallpaper or user-selected palette
- **Dark/Light Mode**
- **Custom Font (Inter) + Typography Control**
- **Smooth Page Transitions & Animations**
- **Swipe-to-Log & Tap-to-Edit UI Interactions**
- **Bottom Nav + FAB + Modal Sheets**

---

## ✅ **COMPLETED IMPLEMENTATION - Master List**

### 🏗️ **Core Architecture**
- ✅ **MVVM Architecture** with Jetpack Compose
- ✅ **Hilt Dependency Injection** configured
- ✅ **Room Database** with comprehensive entities and DAOs
- ✅ **Material 3 Design System** with dynamic colors
- ✅ **Navigation Compose** with bottom navigation
- ✅ **Modern Build Configuration** with version catalogs

### 🎨 **UI Implementation**
- ✅ **Complete Navigation System** with 5 main screens
- ✅ **Dashboard Screen** - Overview and quick actions
- ✅ **Workouts Screen** - History, templates, and quick start
- ✅ **Progress Screen** - Analytics, charts, and tracking
- ✅ **Programs Screen** - Workout programs and builder
- ✅ **Profile Screen** - Settings and user management
- ✅ **Secondary Screens** - Exercise library, settings, data management
- ✅ **Active Workout Screen** - Live workout tracking
- ✅ **Detail Screens** - Workout and exercise details

### 🗄️ **Database Schema**
- ✅ **Exercise Management** - Custom exercises with metadata
- ✅ **Workout Tracking** - Sessions, exercises, and sets
- ✅ **Program System** - Structured workout programs
- ✅ **Progress Tracking** - Personal records and body measurements
- ✅ **User Profiles** - Multi-user support with preferences

### 💡 **Business Logic & Data Flow**
- ✅ **ViewModels implemented**: `DashboardViewModel`, `WorkoutsViewModel`, `ProgressViewModel`, `ProgramsViewModel`, `ProfileViewModel`
- ✅ **Repository layer**: `WorkoutRepository`, `ExerciseRepository`, `ProgressRepository`, `UserProfileRepository`
- ✅ **Database integration**: DAOs connected to repositories; real data flow; error handling and loading states; project compiles cleanly

### 🏋️ **Workout Tracking**
- ✅ **Active workout**: real workout timer with rest periods; set logging (weight/reps); exercise switching; workout completion and save
- ✅ **Real workout data display**
- ✅ **Workout detail screen (view)**

### 📚 **Exercise Library**
- ✅ **Search and filtering** implemented

### 📊 **Progress & Analytics**
- ✅ **Charts with MPAndroidChart**
- ✅ **1RM tracking**, **volume load time-series**, **body measurement trends**, **weight/body-fat tracking**

### 🏗️ **Program Builder** (Complete Implementation)
- ✅ **Visual builder**: drag & drop ordering; set/rep/weight configuration; superset/circuit creation; rest timer configuration
- ✅ **Program templates**: Starting Strength, 5/3/1, PPL, etc.
- ✅ **Progression models**: linear, percentage-based, double progression, RPE-based; auto-weight calculations; automatic deload detection/scheduling
- ✅ **Scheduling & tracking**: calendar-based workout scheduling; program completion tracking
- ✅ **Key files**: `NewProgramBuilderScreen.kt`, `ProgramsScreen.kt`, `ProgramsViewModel.kt`, `ProgramTemplatesSystem.kt`, `ProgressionCalculator.kt`

### 💾 **Data Management & Settings**
- ✅ **Import/Export system**: CSV export for workouts; ZIP backup/restore; OpenScale CSV import compatibility
- ✅ **Multi-profile support**: profile switching; data isolation; create/edit/delete; backup/restore; profile statistics
- ✅ **Settings & preferences**: unit preferences (kg/lbs, km/miles); 11 color themes; notification settings; privacy/data controls
- ✅ **Additional data tooling**: storage usage monitoring; cache management; data reset; file picker integration
- ✅ **Key files**: `DataManagementRepository.kt`, `DataManagementViewModel.kt`, `DataManagementScreen.kt`, `MultiProfileRepository.kt`, `MultiProfileScreen.kt`, `MultiProfileViewModel.kt`, `CreateProfileScreen.kt`, `CreateProfileViewModel.kt`, `SettingsScreen.kt`, `SettingsRepository.kt`, `SettingsViewModel.kt`

### 🚀 **App Infrastructure & Performance**
- ✅ **Adaptive splash screen** (Android 12+) via `Theme.RackedUp.Splash`
- ✅ **Edge-to-edge & status bar** handled in `RackedUpTheme`; activity is resizable for large screens
- ✅ **Global Coil image loader** with memory/disk caches in `RackedUpApplication`
- ✅ **StrictMode** in debug builds
- ✅ **Notification channels** created once at startup
- ✅ **Profile Installer** dependency added (baseline profile ready)
- ✅ **SplashScreen** dependency integrated; release build/resource shrinking enabled
- ✅ **ProGuard/R8 rules** added for Hilt/Room/Coil/MPAndroidChart/Workers

---

## 🚧 **REMAINING WORK - Prioritized Implementation Roadmap**

*Organized from Easiest ➝ Hardest*

### **Phase A: Core UX Improvements (Weeks 1-2)**
1. **Workout history filters**: filter by date, exercise, and type — Remaining
2. **Workout details editing**: edit past workouts and sets — Remaining
3. **UI polish pass**: add micro-animations; accessibility improvements — Partial
4. **Rest timer notifications**: notification on rest completion during active workouts — Remaining
5. **Basic PR detection**: detect new personal records on workout save — Remaining
6. **PR celebration notifications**: lightweight celebration UX/notification — Remaining
7. **Exercise-specific PR tracking**: track PRs per exercise — Remaining

### **Phase B: Enhanced Exercise & Timer Features (Weeks 3-4)**
8. **Exercise history tracking**: per-exercise past set history view/quick access — Remaining
9. **Custom exercises with photos/videos**: create and manage custom exercises with media — Remaining
10. **Exercise variations & substitutions**: link alternates and allow substitutes during planning/active workout — Remaining
11. **Interval training timers**: configurable intervals beyond rest timer — Remaining
12. **Auto-start next set**: optional auto start after rest completes — Remaining

### **Phase C: Progress Photos & Media (Weeks 4-5)**
13. **Progress photos — camera integration**: capture/store photos in-app — Remaining
14. **Progress photos — gallery**: before/after gallery UI — Remaining
15. **Progress photos — privacy controls**: album privacy and visibility settings — Remaining

### **Phase D: Cloud Integration & Testing (Weeks 5-6)**
16. **Google Drive integration**: backup/restore to Drive — Partial
17. **Unit tests**: ViewModels and key repositories — Remaining
18. **UI tests**: critical user flows — Remaining
19. **Integration & performance tests**: end-to-end + perf/regression — Remaining

### **Phase E: Health Connect Integration (Weeks 6-7)**
20. **Health Connect — weight**: read/write body weight — Remaining
21. **Health Connect — steps & calories**: basic activity sync — Remaining
22. **Health Connect — heart rate**: read HR during workouts — Remaining
23. **Health Connect — sleep**: read sleep for recovery metrics — Remaining

### **Phase F: Advanced Analytics (Week 7)**
24. **PR history & trends**: timelines, graphs, and insights — Remaining

### **Phase G: AI Integration (Weeks 8-9)**
25. **AI — workout recommendations (Gemini API)**: personalized suggestions — Remaining
26. **AI — form feedback suggestions**: heuristic/model-assisted tips — Remaining
27. **AI — recovery recommendations**: load management and rest guidance — Remaining
28. **AI — plateau detection**: time-series detection of stalls — Remaining

### **Phase H: Platform Extensions (Weeks 10-12)**
29. **Wear OS companion**: quick logging, HR monitoring, notifications — Remaining
30. **Social features**: sharing, community programs, leaderboards, trainer/client — Remaining

### **Phase I: Advanced ML (Future)**
31. **Pose estimation (MediaPipe)**: form feedback, rep counting, exercise recognition — Remaining

---

## 🎯 **Milestones & Quality Gates**

- **Milestone A (Core UX)**: Items 1–7 complete (history filters, edit, polish, timer notifications, basic PR flow)
- **Milestone B (Enhanced Features)**: Items 8–12 complete (custom exercises, exercise alternates, timers)
- **Milestone C (Media & Photos)**: Items 13–15 complete (progress photos)
- **Milestone D (Reliability & Cloud)**: Items 16–19 complete (Drive, tests)
- **Milestone E (Health Connect)**: Items 20–23 complete
- **Milestone F (Advanced Analytics)**: Item 24 complete
- **Milestone G (AI v1)**: Items 25–28 complete
- **Milestone H (Platforms & Social)**: Items 29–30 complete
- **Milestone I (Advanced ML)**: Item 31 complete

---

## 🗄️ **Database Relationships**

```
Exercise (1) ←→ (M) WorkoutExercise (M) ←→ (1) Workout
                       ↓ (1:M)
                   ExerciseSet

Program (1) ←→ (M) ProgramDay (1) ←→ (M) ProgramExercise
                                           ↓ (M:1)
                                       Exercise

Exercise (1) ←→ (M) PersonalRecord
UserProfile (1) ←→ (M) BodyMeasurement
```

---

## 🎨 **UI Component Structure**

### **Core Components**
- `FeaturePlaceholderCard` - For unimplemented features
- `QuickStatCard` - For dashboard statistics
- `RecentWorkoutCard` - For workout listings
- `EmptyStateCard` - For empty data states
- `RackedUpBottomNavigation` - Main navigation

### **Theme System**
- Dynamic Material 3 colors
- Custom typography with Inter font family
- Consistent spacing and elevation
- Support for dark/light modes

### **Navigation Flow**
```
Dashboard (Home)
├── Start Workout → Active Workout → Workout Complete → Progress
├── View Workouts → Workouts → Workout Detail → Exercise Detail
├── View Progress → Progress → Chart Details
└── Quick Actions → Various Screens

Bottom Navigation:
├── Dashboard - Overview and quick actions
├── Workouts - History and logging
├── Progress - Analytics and tracking  
├── Programs - Routine management
└── Profile - Settings and user data
```

---

## 🛠️ **Development Workflow**

### **Setup**
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on device/emulator

### **Key Commands**
```bash
# Build debug (use safe build script on Windows)
./gradlew assembleDebug
# OR use the safe build script for Windows file locking issues:
build_safe.bat

# Run tests
./gradlew test

# Run UI tests  
./gradlew connectedAndroidTest

# Build release
./gradlew assembleRelease
```

### **Windows Build Issues**
If you encounter file locking errors on Windows:
1. **Use the safe build script**: `build_safe.bat`
2. **Close Android Studio** before building
3. **Run as administrator** if issues persist
4. **Check antivirus settings** - some antivirus software can lock files
5. **Restart computer** if all else fails

### **Known Issues**
- **File Locking**: Windows sometimes locks `R.jar` files during build
- **Deprecation Warnings**: Room plugin shows deprecation warnings (harmless)
- **Gradle Daemon**: May need to be stopped with `./gradlew --stop`

### **Code Style**
- **Kotlin Coding Conventions**
- **Compose Best Practices**
- **Material 3 Guidelines**
- **Clean Architecture Principles**

---

## 🧪 **Testing Strategy**

### **Unit Tests**
- ViewModel business logic
- Repository data handling  
- Utility functions
- Database operations

### **Integration Tests**
- Database migrations
- Repository integration
- End-to-end workflows

### **UI Tests**
- Compose UI testing
- Navigation flows
- User interactions
- Accessibility testing

---

## 📦 **Build Configuration**

### **Gradle Modules**
- `app` - Main application module
- Modern Gradle with version catalogs
- Kotlin DSL for build scripts
- Proguard configuration for release

### **Dependencies**
- **UI**: Compose BOM, Material 3, Navigation
- **Architecture**: Hilt, Room, DataStore, WorkManager
- **Network**: Retrofit, OkHttp, Serialization
- **Media**: Coil, CameraX, Accompanist
- **Charts**: Vico, MPAndroidChart
- **Google**: Play Services, Health Connect
- **Testing**: JUnit, Mockk, Compose Testing

---

## 📱 **Google Play Store Requirements**

### **App Store Listing**
- **App Title**: RackedUp: Gym & Fitness Tracker
- **Short Description** (80 chars): Powerful offline gym tracker with AI coaching & beautiful Material 3 design
- **Keywords**: fitness, gym, workout, tracker, strength, bodybuilding, powerlifting, exercise, health, analytics, charts, progress, ai, coaching, material design, offline

### **Technical Requirements**
- **Target SDK**: 36 (Android 15)
- **Minimum SDK**: 26 (Android 8.0)
- **App Bundle**: Required (.aab format)
- **Size Limit**: 150MB max

### **Visual Assets Needed**
- App Icon: 512x512 px (adaptive)
- Screenshots: 2-8 phone screenshots (1080x1920 px min)
- Feature Graphic: 1024x500 px
- Optional tablet screenshots and promo video

### **Privacy & Legal**
- Privacy Policy URL: `https://rackedup.app/privacy-policy`
- Terms of Service URL: `https://rackedup.app/terms-of-service`
- Content Rating: Teen (13+)
- No inappropriate content

### **Release Strategy**
1. **Internal Testing**: Development team
2. **Closed Testing**: 100 beta testers
3. **Open Testing**: 1000 public beta users
4. **Phased Rollout**: 1% → 5% → 20% → 50% → 100%
5. **Target Timeline**: 5 weeks total

---

## 📊 **Success Metrics & KPIs**

### **Launch Targets**
- **Downloads**: 10K in first month
- **Rating**: Maintain 4.5+ stars
- **Retention**: 60% Day 1, 30% Day 7, 15% Day 30
- **Crashes**: < 0.5% crash rate
- **ANRs**: < 0.1% ANR rate

### **User Engagement**
- **Daily Active Users**: 40% of installs
- **Session Length**: Average 15+ minutes
- **Feature Adoption**: 80% use workout tracking, 60% use analytics

### **Weekly Milestones**
- **Week 2**: User can log complete workout
- **Week 4**: User can view progress charts
- **Week 6**: User can create custom programs
- **Week 8**: App ready for beta testing

---

## 🚀 **Deployment Targets**

| Platform    | Status        | Notes                        |
| ----------- | ------------- | ---------------------------- |
| Android 8.0+ | ✅ Supported   | Primary target platform      |
| Wear OS     | ⏳ Planned     | Companion app in roadmap     |
| Tablets     | ✅ Optimized   | Responsive design ready      |
| Android TV  | ❌ Not planned | Not in scope                 |
| iOS         | ❌ No plans    | Android-first approach       |

---

## 🔐 **Privacy & Security**

### **Core Privacy Principles**
- 100% usable offline
- No forced cloud sync
- Permissions only when needed (camera, storage, sensors)
- No personal data collection
- Local data storage only
- Optional cloud backup (user-controlled)

### **Required Permissions**
```xml
<!-- Essential permissions -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- Health & Fitness -->
<uses-permission android:name="android.permission.BODY_SENSORS" />
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />
<uses-permission android:name="health.connect.client.permission.READ_STEPS" />

<!-- Optional -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## 📝 **Contributing Guidelines**

### **Branch Strategy**
- `main` - Production ready code
- `develop` - Integration branch
- `feature/*` - Feature development
- `bugfix/*` - Bug fixes
- `release/*` - Release preparation

### **Commit Convention**
```
feat: add workout timer functionality
fix: resolve crash in exercise selection
docs: update README with setup instructions
style: format code according to conventions
refactor: restructure workout data models
test: add unit tests for workout service
```

### **Pull Request Process**
1. Create feature branch from `develop`
2. Implement feature with tests
3. Update documentation if needed
4. Submit PR with description
5. Code review and approval
6. Merge to `develop`

---

## 💰 **Monetization Strategy**

### **Initial Launch**
- **Free app** with full features
- No ads or subscriptions
- Focus on user adoption

### **Future Considerations**
- **Premium features**: Advanced AI coaching, enhanced cloud sync
- **One-time purchase**: Remove any limitations
- **No ads**: Maintain clean, focused experience

---

## 🛠️ **Post-Launch Support**

### **Update Schedule**
- **Hot fixes**: Within 24-48 hours for critical issues
- **Minor updates**: Bi-weekly for features and improvements
- **Major updates**: Monthly for significant new features

### **Community Management**
- **Play Store reviews**: Daily response to user feedback
- **Support channels**: Email, GitHub issues, Discord community
- **Feature requests**: Regular community polls and surveys

---

## 📞 **Support & Resources**

### **Documentation**
- [Android Developer Docs](https://developer.android.com)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Material 3 Guidelines](https://m3.material.io)
- [Hilt Documentation](https://dagger.dev/hilt)

### **Community**
- GitHub Discussions for feature requests
- Issues for bug reports
- Discord community for chat
- Email support for urgent issues

---

## 💼 **License & Open Source**

- **License**: BSD 3-Clause License
- **Repository**: Open source development
- **Community**: Welcome contributions from fitness and Android communities

---

## ✅ **Pre-Launch Checklist**

### **Development**
- [ ] All core features implemented and tested
- [ ] Performance optimization completed
- [ ] Security audit passed
- [ ] Accessibility testing completed
- [ ] Multi-device testing (phones, tablets)

### **Legal & Compliance**
- [ ] Privacy policy finalized and published
- [ ] Terms of service created
- [ ] GDPR compliance verified
- [ ] Health data handling compliance verified

### **Store Assets**
- [ ] App icon designed and approved
- [ ] Screenshots captured and optimized
- [ ] Feature graphic created
- [ ] App description written and localized
- [ ] Promo video produced (optional)

### **Testing**
- [ ] Alpha testing completed
- [ ] Beta testing with external users
- [ ] Performance benchmarking
- [ ] Security penetration testing
- [ ] Accessibility audit

### **Release Preparation**
- [ ] App bundle signed and uploaded
- [ ] Store listing configured
- [ ] Release notes prepared
- [ ] Marketing materials ready
- [ ] Support documentation updated

---

**Happy Coding! 💪🚀**

*Built with ❤️ for the fitness community*

**RackedUp - Where Strength Meets Smart Technology**
