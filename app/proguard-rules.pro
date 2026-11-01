# MeshLink ProGuard Rules

# Keep application class
-keep class com.meshlink.app.MeshLinkApplication { *; }

# Keep Room entities
-keep class com.meshlink.app.model.** { *; }

# Keep repository interfaces
-keep interface com.meshlink.app.repository.** { *; }

# Keep network interfaces
-keep interface com.meshlink.app.network.** { *; }

# Keep ViewModels
-keep class com.meshlink.app.ui.**ViewModel { *; }

# Kotlin serialization
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# DataStore
-keep class androidx.datastore.** { *; }

# Coil
-keep class coil.** { *; }

# Coroutines
-keep class kotlinx.coroutines.** { *; }

# Navigation
-keep class androidx.navigation.** { *; }

# Compose
-keep class androidx.compose.** { *; }

# Material3
-keep class androidx.compose.material3.** { *; }