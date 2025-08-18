# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# --- Keep Hilt / DI generated classes ---
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-dontwarn dagger.hilt.**

# --- Keep Room Entities and DAO annotations ---
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keep @androidx.room.* class * { *; }
-keep class ** extends androidx.room.RoomDatabase

# --- Keep Kotlinx Serialization (if used) ---
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# --- Keep WorkManager Workers (Hilt) ---
-keep class androidx.hilt.work.HiltWorkerFactory { *; }
-keep class ** extends androidx.work.ListenableWorker { *; }

# --- Coil recommended keeps ---
-keep class coil.** { *; }
-dontwarn coil.**

# --- MPAndroidChart ---
-keep class com.github.mikephil.charting.** { *; }
-dontwarn com.github.mikephil.charting.**

# --- OkHttp/Retrofit (if used) ---
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**