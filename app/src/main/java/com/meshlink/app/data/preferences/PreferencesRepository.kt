package com.meshlink.app.data.preferences

import com.meshlink.app.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val profileFlow: Flow<UserProfile?>

    suspend fun saveProfile(profile: UserProfile)
    suspend fun clearProfile()
    suspend fun getProfile(): UserProfile?

    // Network preferences
    suspend fun setBluetoothEnabled(enabled: Boolean)
    suspend fun isBluetoothEnabled(): Boolean

    suspend fun setWifiDirectEnabled(enabled: Boolean)
    suspend fun isWifiDirectEnabled(): Boolean

    suspend fun setMeshRelayEnabled(enabled: Boolean)
    suspend fun isMeshRelayEnabled(): Boolean

    suspend fun setMaxHopCount(count: Int)
    suspend fun getMaxHopCount(): Int

    suspend fun setMessageRetentionDays(days: Int)
    suspend fun getMessageRetentionDays(): Int

    // UI preferences
    suspend fun setTheme(theme: String) // "system", "light", "dark"
    suspend fun getTheme(): String

    suspend fun setLanguage(language: String)
    suspend fun getLanguage(): String

    // Notification preferences
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun areNotificationsEnabled(): Boolean

    suspend fun setSoundEnabled(enabled: Boolean)
    suspend fun isSoundEnabled(): Boolean

    suspend fun setVibrationEnabled(enabled: Boolean)
    suspend fun isVibrationEnabled(): Boolean

    // Security preferences
    suspend fun setBiometricEnabled(enabled: Boolean)
    suspend fun isBiometricEnabled(): Boolean

    suspend fun setAutoLockTimeout(minutes: Int)
    suspend fun getAutoLockTimeout(): Int

    // Debug/Development
    suspend fun setDebugMode(enabled: Boolean)
    suspend fun isDebugMode(): Boolean
}