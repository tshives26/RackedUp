plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
    id("kotlin-kapt")
}

android {
    namespace = "com.chilluminati.rackedup"
    compileSdk = 36

    signingConfigs {
        create("release") {
            // Use environment variables for security
            val keystorePath = project.findProperty("RELEASE_STORE_FILE") as String?
            val keystorePassword = project.findProperty("RELEASE_STORE_PASSWORD") as String?
            val keyAlias = project.findProperty("RELEASE_KEY_ALIAS") as String?
            val keyPassword = project.findProperty("RELEASE_KEY_PASSWORD") as String?
            
            if (keystorePath != null && keystorePassword != null && keyAlias != null && keyPassword != null) {
                storeFile = file(keystorePath)
                storePassword = keystorePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            } else {
                // Fallback to debug keystore for development
                storeFile = file("${System.getProperty("user.home")}/.android/debug.keystore")
                storePassword = "android"
                this.keyAlias = "androiddebugkey"
                this.keyPassword = "android"
            }
        }
    }

    defaultConfig {
        applicationId = "com.chilluminati.rackedup"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-Xsuppress-version-warnings",
            "-Xcontext-receivers"
        )
    }
    
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get()
    }
    
    packaging {
        jniLibs {
            // Keep debug symbols to avoid strip warnings in debug builds
            keepDebugSymbols += listOf("**/*.so")
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

// Task: download latest combined exercises.json into assets at build time

val fetchExercisesJson by tasks.registering {
    group = "assets"
    description = "Fetch Free Exercise DB combined exercises.json into assets"
    // Make task incremental and skippable unless forced
    val destDir = file("src/main/assets/exercises")
    val destFile = file("src/main/assets/exercises/exercises.json")
    outputs.file(destFile)
    onlyIf {
        val refresh = project.findProperty("refreshExercises")?.toString()?.toBoolean() == true
        !destFile.exists() || refresh
    }
    doLast {
        destDir.mkdirs()
        val jsonText = resources.text.fromUri("https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/dist/exercises.json").asString()
        
        // Simple JSON validation - check for array brackets
        if (!jsonText.trim().startsWith("[") || !jsonText.trim().endsWith("]")) {
            throw GradleException("Downloaded exercises.json is not a valid JSON array")
        }
        
        destFile.writeText(jsonText)
        val fileSize = destFile.length()
        println("Fetched exercises.json → $fileSize bytes")
        if (fileSize == 0L) {
            throw GradleException("Downloaded exercises.json is empty")
        }
    }
}

tasks.named("preBuild").configure {
    dependsOn(fetchExercisesJson)
}



dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // Jetpack Compose BOM
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.compose.ui.text.google.fonts)
    implementation(libs.compose.material3.window.size)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    // Room
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.documentfile)

    // Network (Optional for future features)
    implementation(libs.bundles.network)

    // Image Loading
    implementation(libs.coil.compose)
    
    // Image Cropping
    implementation("com.github.yalantis:ucrop:2.2.11")

    // OkHttp for build-time import fallback and remote fetches
    implementation(libs.okhttp)

            // Startup performance
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.core.splashscreen)

    // Charts
    implementation(libs.bundles.charts)
    implementation(libs.mpandroidchart)

    // Google Services (Optional)
    implementation(libs.bundles.google.services)

    // Camera
    implementation(libs.bundles.camera)

    // Accompanist
    implementation(libs.accompanist.permissions)

    // Debug
    debugImplementation(libs.bundles.compose.debug)

    // Optional: runtime profiling for startup
    // baseline-prof files can be added later for further gains

    // Testing
    testImplementation(libs.bundles.testing)
    testImplementation(libs.robolectric)

    // Core library desugaring (java.time backport for lower APIs)
    coreLibraryDesugaring(libs.desugarJdkLibs)
    
    // Android Testing
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.android.testing)
    
    // Hilt Testing
    kaptAndroidTest(libs.hilt.compiler)
}