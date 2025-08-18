# 💪 RackedUp - Modern Fitness Tracking App

[![Android](https://img.shields.io/badge/Android-API%2026+-green.svg)](https://developer.android.com/about/versions/oreo)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5+-purple.svg)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Latest-orange.svg)](https://m3.material.io/)

**RackedUp** is a modern, offline-first fitness tracking app built with the latest Android technologies. Designed for serious athletes, trainers, and fitness enthusiasts who demand flexibility, precision, privacy, and style.

## 🎯 Features

### 🏋️‍♂️ **Workout Tracking**
- **Exercise Types**: Strength, Cardio, Isometric
- **Custom Exercises**: Add photos, videos, variations
- **Integrated Timer**: Custom rest timer, set countdown, auto-start
- **Real-time Tracking**: Live workout sessions with set logging
- **Wearable Support**: Google Fit & Wear OS sync

### 📊 **Progress & Analytics**
- **Advanced Charts**: 1RM tracking, Volume Load, PR tracking
- **Body Measurements**: Track waist, arms, chest, weight, body fat
- **Progress Photos**: Before/after galleries with privacy controls
- **Workout History**: Deep filtering by exercise, date, PR, tags

### 📆 **Program & Routine Builder**
- **Visual Workout Builder**: Drag & drop interface
- **Progression Models**: Linear, double progression, % of 1RM
- **Advanced Sets**: Supersets, circuits, drop sets
- **Scheduling**: Calendar-based workout planning
- **Program Templates**: Starting Strength, 5/3/1, PPL, and more

### 💾 **Data Management**
- **Offline-First**: 100% usable without internet
- **Multi-User Profiles**: One device, many users
- **Backup & Restore**: Local and cloud backup options
- **Import/Export**: CSV/ZIP support, OpenScale compatibility

### 🤖 **Smart Features**
- **AI Recommendations**: Personalized workout suggestions
- **Form Feedback**: AI-powered exercise guidance
- **Recovery Tracking**: Fatigue and recovery monitoring
- **Plateau Detection**: Smart progress analysis

### 🎨 **Modern UI/UX**
- **Material 3 Design**: Dynamic theming and colors
- **Dark/Light Mode**: Automatic system theme detection
- **Smooth Animations**: Fluid transitions and micro-interactions
- **Accessibility**: Full accessibility support

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

## 🎯 Core Features Status

### ✅ **Completed**
- Complete MVVM architecture with Jetpack Compose
- Room database with comprehensive entities
- Material 3 design system with dynamic colors
- Navigation system with bottom navigation
- Active workout tracking with timer
- Exercise library with search and filtering
- Progress charts and analytics
- Program builder with templates
- Multi-user profile support
- Data import/export functionality
- Settings and preferences management

### 🚧 **In Development**
- Workout history filters and editing
- Custom exercises with media support
- Progress photos with camera integration
- Health Connect integration
- AI-powered recommendations
- Wear OS companion app

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

## 📋 Development Roadmap

### Phase 1: Core UX Improvements
- [ ] Workout history filters
- [ ] Workout editing capabilities
- [ ] UI polish and animations
- [ ] PR detection and celebrations

### Phase 2: Enhanced Features
- [ ] Custom exercises with media
- [ ] Exercise variations and substitutions
- [ ] Advanced timer features
- [ ] Progress photos

### Phase 3: Integration & Testing
- [ ] Google Drive integration
- [ ] Comprehensive test suite
- [ ] Health Connect integration
- [ ] Performance optimization

### Phase 4: AI & Advanced Features
- [ ] AI workout recommendations
- [ ] Form feedback system
- [ ] Recovery tracking
- [ ] Wear OS companion

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

## 📞 Support

- **GitHub Issues**: For bug reports and feature requests
- **Discussions**: For general questions and community chat
- **Email**: For urgent issues and support

## 🌟 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=tshives26/RackedUp&type=Date)](https://star-history.com/#tshives26/RackedUp&Date)

---

**Built with ❤️ for the fitness community**

**RackedUp - Where Strength Meets Smart Technology**
