# 💪 RackedUp - Modern Fitness Tracking App

[![Android](https://img.shields.io/badge/Android-API%2026+-green.svg)](https://developer.android.com/about/versions/oreo)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5+-purple.svg)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Latest-orange.svg)](https://m3.material.io/)
[![Documentation](https://img.shields.io/badge/Documentation-GitHub%20Pages-blue.svg)](https://tshives26.github.io/RackedUp/)

**RackedUp** is a modern, offline-first fitness tracking app built with the latest Android technologies. Designed for serious athletes, trainers, and fitness enthusiasts who demand flexibility, precision, privacy, and style.

> 📖 **[Complete User Guide & Documentation](https://tshives26.github.io/RackedUp/)** - Comprehensive help and tutorials

## 🎯 Current Features

### ✅ **Implemented & Available**

#### 🏋️‍♂️ **Workout Tracking**
- **Active Workout Sessions**: Real-time workout tracking with timer
- **Set Logging**: Log weight, reps, and rest periods
- **Rest Timer**: Custom rest periods with notifications
- **Exercise Library**: Search and filter exercises
- **Workout History**: View past workout sessions

#### 📊 **Progress & Analytics**
- **Progress Charts**: Volume load, strength progression, workout density
- **Body Measurements**: Track weight, body fat, and custom measurements
- **Personal Records**: Track 1RM and volume PRs
- **Workout History**: Detailed workout tracking and filtering

#### 📋 **Program & Routine Builder**
- **Program Templates**: Starting Strength, 5/3/1, StrongLifts, PPL, and more
- **Custom Programs**: Create your own workout programs
- **Progression Models**: Linear, percentage-based, double progression
- **Program Tracking**: Track program completion and progress

#### 👤 **Profile & Settings**
- **Multi-User Profiles**: Multiple profiles on one device
- **Theme Customization**: 11 color themes, dark/light mode
- **Unit Preferences**: lbs/kg, miles/km conversion
- **Settings Management**: Comprehensive app preferences

#### 🔧 **Built-in Tools**
- **Plate Calculator**: Calculate weight plates for any target weight
- **Weight Converter**: Convert between pounds and kilograms

#### 💾 **Data Management**
- **Offline-First**: Works completely without internet
- **Data Export**: CSV export for workout data
- **Backup & Restore**: Local backup system
- **OpenScale Import**: Import data from OpenScale app

#### 📱 **Help & Documentation**
- **In-App Help**: Quick start guide, feature overview, troubleshooting
- **External Documentation**: Comprehensive user guide on GitHub Pages
- **Feedback System**: Direct feedback integration

### 🚧 **Planned Features**

#### 🤖 **Smart Features** (Future)
- **AI Recommendations**: Personalized workout suggestions
- **Form Feedback**: AI-powered exercise guidance
- **Recovery Tracking**: Fatigue and recovery monitoring
- **Plateau Detection**: Smart progress analysis

#### 📸 **Media & Photos** (Future)
- **Progress Photos**: Before/after galleries with privacy controls
- **Custom Exercise Media**: Add photos and videos to exercises
- **Camera Integration**: In-app photo capture

#### 🔗 **Health Integration** (Future)
- **Health Connect**: Sync with Android Health Connect
- **Google Fit**: Integration with Google Fit
- **Wear OS**: Companion app for smartwatches

#### 🎨 **Enhanced UI/UX** (Future)
- **Advanced Animations**: Micro-interactions and transitions
- **Custom Fonts**: Inter font family integration
- **Accessibility**: Enhanced accessibility features

## 🧱 Technology Stack

| Layer | Technology |
|-------|------------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Architecture** | MVVM + StateFlow + Hilt (DI) |
| **Database** | Room + DataStore |
| **Navigation** | Navigation Compose |
| **Charts** | MPAndroidChart + Vico |
| **Image Loading** | Coil for Compose |
| **Testing** | Compose Testing + JUnit + Mockk |

## 📱 Screenshots

*Screenshots will be added here*

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK API 26+ (Android 8.0)
- Kotlin 1.9+
- JDK 17

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/tshives26/RackedUp.git
   cd RackedUp
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it

3. **Sync and Build**
   - Wait for Gradle sync to complete
   - Build the project: `Build → Make Project`
   - Run on device/emulator: `Run → Run 'app'`

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run UI tests
./gradlew connectedAndroidTest
```

## 🏗️ Project Structure

```
app/src/main/java/com/chilluminati/rackedup/
├── core/                    # Core utilities and extensions
├── data/                    # Data layer (database, repositories)
│   ├── database/           # Room database implementation
│   └── repository/         # Repository implementations
├── di/                     # Hilt dependency injection
├── presentation/           # UI layer
│   ├── components/         # Reusable UI components
│   ├── dashboard/          # Dashboard screen
│   ├── exercises/          # Exercise library
│   ├── navigation/         # Navigation setup
│   ├── progress/           # Progress tracking
│   ├── programs/           # Program management
│   ├── profile/            # Settings and user management
│   ├── theme/              # Material 3 theming
│   └── workouts/           # Workout tracking
└── MainActivity.kt         # Main activity entry point
```

## 🗺️ Development Roadmap

### 🎯 **Phase 1: Core UX Improvements** (Current Priority)
- [ ] **Workout History Filters**: Filter by date, exercise, and type
- [ ] **Workout Editing**: Edit past workouts and sets
- [ ] **UI Polish**: Micro-animations and accessibility improvements
- [ ] **PR Detection**: Automatic personal record detection
- [ ] **PR Celebrations**: Lightweight celebration notifications
- [ ] **Rest Timer Notifications**: Enhanced timer notifications

### 🚀 **Phase 2: Enhanced Features** (Next)
- [ ] **Custom Exercise Media**: Add photos and videos to exercises
- [ ] **Exercise Variations**: Link alternates and substitutions
- [ ] **Advanced Timers**: Interval training and auto-start features
- [ ] **Exercise History**: Per-exercise past set history
- [ ] **Progress Photos**: Camera integration and gallery

### 🔗 **Phase 3: Health Integration** (Future)
- [ ] **Health Connect**: Weight, steps, calories, heart rate sync
- [ ] **Google Fit**: Integration with Google Fit ecosystem
- [ ] **Recovery Tracking**: HRV and sleep integration
- [ ] **Advanced Analytics**: PR trends and insights

### 🤖 **Phase 4: AI & Smart Features** (Future)
- [ ] **AI Recommendations**: Personalized workout suggestions
- [ ] **Form Feedback**: AI-powered exercise guidance
- [ ] **Plateau Detection**: Smart progress analysis
- [ ] **Recovery Recommendations**: Load management guidance

### 📱 **Phase 5: Platform Extensions** (Future)
- [ ] **Wear OS Companion**: Quick logging and HR monitoring
- [ ] **Social Features**: Sharing and community programs
- [ ] **Cloud Backup**: Google Drive integration
- [ ] **Advanced ML**: Pose estimation and form analysis

## 🤝 Contributing

We welcome contributions from the fitness and Android development communities!

### How to Contribute

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Add tests** for new functionality
5. **Commit your changes**
   ```bash
   git commit -m "feat: add amazing feature"
   ```
6. **Push to your branch**
   ```bash
   git push origin feature/amazing-feature
   ```
7. **Open a Pull Request**

### Code Style
- Follow Kotlin coding conventions
- Use Jetpack Compose best practices
- Follow Material 3 design guidelines
- Write unit tests for new features

## 📊 Current Status

### ✅ **Fully Implemented**
- **Complete MVVM Architecture**: Jetpack Compose + Material 3
- **Database System**: Room with comprehensive entities and DAOs
- **Navigation**: Bottom navigation with 5 main screens
- **Workout Tracking**: Active sessions, set logging, rest timers
- **Progress Analytics**: Charts, body measurements, PR tracking
- **Program Builder**: Templates, custom programs, progression models
- **Multi-User Profiles**: Profile switching and data isolation
- **Data Management**: Export, backup, OpenScale import
- **Settings & Preferences**: Themes, units, notifications
- **Built-in Tools**: Plate calculator, weight converter
- **Help System**: In-app help + external documentation

### 🚧 **In Active Development**
- Workout history filtering and editing
- Enhanced UI animations and polish
- PR detection and celebration system
- Custom exercise media support

## 🔐 Privacy & Security

RackedUp is built with privacy as a core principle:

- **100% Offline-First**: No data collection, no ads
- **Local Storage**: All data stays on your device
- **Optional Cloud**: User-controlled backup only
- **No Tracking**: No analytics or user tracking
- **Open Source**: Transparent code for community review

## 📄 License

This project is licensed under the BSD 3-Clause License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Android Team** for Jetpack Compose and Material 3
- **Fitness Community** for feedback and feature requests
- **Open Source Contributors** for the amazing libraries we use

## 📞 Support & Documentation

- **📖 [Complete User Guide](https://tshives26.github.io/RackedUp/)**: Comprehensive documentation and tutorials
- **🐛 [GitHub Issues](https://github.com/tshives26/RackedUp/issues)**: Bug reports and feature requests
- **💬 [GitHub Discussions](https://github.com/tshives26/RackedUp/discussions)**: Community chat and questions
- **📧 [Send Feedback](https://forms.gle/bHfMDc7qsnUn6F4j6)**: Direct feedback form

---

**Built with ❤️ for the fitness community**

**RackedUp - Where Strength Meets Smart Technology**
