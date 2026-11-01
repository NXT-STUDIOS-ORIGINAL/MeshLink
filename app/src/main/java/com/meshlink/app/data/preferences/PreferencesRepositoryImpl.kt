package com.meshlink.app.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesKeys
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import com.meshlink.app.model.UserProfile
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesRepositoryImpl(private val context: Context) : PreferencesRepository {

    private val dataStore: RxDataStore<Preferences> = RxPreferenceDataStoreBuilder(context, "meshlink_prefs").build()

    // Keys
    private val KEY_PROFILE = preferencesKey<UserProfile>("user_profile", object : PreferencesKeys.PreferencesKey<UserProfile> {
        override fun get(preferences: Preferences): UserProfile? {
            val json = preferences[stringPreferencesKey("profile_json")] ?: return null
            return Json.decodeFromString(UserProfile.serializer(), json)
        }
        override fun put(preferences: MutablePreferences, value: UserProfile) {
            preferences[stringPreferencesKey("profile_json")] = Json.encodeToString(UserProfile.serializer(), value)
        }
    })

    private val KEY_BLUETOOTH_ENABLED = preferencesKey("bluetooth_enabled", true)
    private val KEY_WIFI_DIRECT_ENABLED = preferencesKey("wifi_direct_enabled", true)
    private val KEY_MESH_RELAY_ENABLED = preferencesKey("mesh_relay_enabled", true)
    private val KEY_MAX_HOP_COUNT = preferencesKey("max_hop_count", 3)
    private val KEY_MESSAGE_RETENTION_DAYS = preferencesKey("message_retention_days", 30)
    private val KEY_THEME = preferencesKey("theme", "system")
    private val KEY_LANGUAGE = preferencesKey("language", "en")
    private val KEY_NOTIFICATIONS_ENABLED = preferencesKey("notifications_enabled", true)
    private val KEY_SOUND_ENABLED = preferencesKey("sound_enabled", true)
    private val KEY_VIBRATION_ENABLED = preferencesKey("vibration_enabled", true)
    private val KEY_BIOMETRIC_ENABLED = preferencesKey("biometric_enabled", false)
    private val KEY_AUTO_LOCK_TIMEOUT = preferencesKey("auto_lock_timeout", 15)
    private val KEY_DEBUG_MODE = preferencesKey("debug_mode", false)

    override val profileFlow: Flow<UserProfile?> = flow {
        dataStore.data
            .map { it[KEY_PROFILE] }
            .toKotlinFlow()
            .collect { emit(it) }
    }

    override suspend fun saveProfile(profile: UserProfile) {
        dataStore.updateDataAsync { it[KEY_PROFILE] = profile }.await()
    }

    override suspend fun clearProfile() {
        dataStore.updateDataAsync { it[KEY_PROFILE] = null }.await()
    }

    override suspend fun getProfile(): UserProfile? {
        return dataStore.data.firstOrNull()?.get(KEY_PROFILE)
    }

    override suspend fun setBluetoothEnabled(enabled: Boolean) {
        dataStore.updateDataAsync { it[KEY_BLUETOOTH_ENABLED] = enabled }.await()
    }

    override suspend fun isBluetoothEnabled(): Boolean {
        return dataStore.data.firstOrNull()?.get(KEY_BLUETOOTH_ENABLED) ?: true
    }

    override suspend fun setWifiDirectEnabled(enabled: Boolean) {
        dataStore.updateDataAsync { it[KEY_WIFI_DIRECT_ENABLED] = enabled }.await()
    }

    override suspend fun isWifiDirectEnabled(): Boolean {
        return dataStore.data.firstOrNull()?.get(KEY_WIFI_DIRECT_ENABLED) ?: true
    }

    override suspend fun setMeshRelayEnabled(enabled: Boolean) {
        dataStore.updateDataAsync { it[KEY_MESH_RELAY_ENABLED] = enabled }.await()
    }

    override suspend fun isMeshRelayEnabled(): Boolean {
        return dataStore.data.firstOrNull()?.get(KEY_MESH_RELAY_ENABLED) ?: true
    }

    override suspend fun setMaxHopCount(count: Int) {
        dataStore.updateDataAsync { it[KEY_MAX_HOP_COUNT] = count.coerceIn(1, 10) }.await()
    }

    override suspend fun getMaxHopCount(): Int {
        return dataStore.data.firstOrNull()?.get(KEY_MAX_HOP_COUNT) ?: 3
    }

    override suspend fun setMessageRetentionDays(days: Int) {
        dataStore.updateDataAsync { it[KEY_MESSAGE_RETENTION_DAYS] = days.coerceIn(1, 365) }.await()
    }

    override suspend fun getMessageRetentionDays(): Int {
        return dataStore.data.firstOrNull()?.get(KEY_MESSAGE_RETENTION_DAYS) ?: 30
    }

    override suspend fun setTheme(theme: String) {
        dataStore.updateDataAsync { it[KEY_THEME] = theme }.await()
    }

    override suspend fun getTheme(): String {
        return dataStore.data.firstOrNull()?.get(KEY_THEME) ?: "system"
    }

    override suspend fun setLanguage(language: String) {
        dataStore.updateDataAsync { it[KEY_LANGUAGE] = language }.await()
    }

    override suspend fun getLanguage(): String {
        return dataStore.data.firstOrNull()?.get(KEY_LANGUAGE) ?: "en"
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.updateDataAsync { it[KEY_NOTIFICATIONS_ENABLED] = enabled }.await()
    }

    override suspend fun areNotificationsEnabled(): Boolean {
        return dataStore.data.firstOrNull()?.get(KEY_NOTIFICATIONS_ENABLED) ?: true
    }

    override suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.updateDataAsync { it[KEY_SOUND_ENABLED] = enabled }.await()
    }

    override suspend fun isSoundEnabled(): Boolean {
        return dataStore.data.firstOrNull()?.get(KEY_SOUND_ENABLED) ?: true
    }

    override suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.updateDataAsync { it[KEY_VIBRATION_ENABLED] = enabled }.await()
    }

    override suspend fun isVibrationEnabled(): Boolean {
        return dataStore.data.firstOrNull()?.get(KEY_VIBRATION_ENABLED) ?: true
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.updateDataAsync { it[KEY_BIOMETRIC_ENABLED] = enabled }.await()
    }

    override suspend fun isBiometricEnabled(): Boolean {
        return dataStore.data.firstOrNull()?.get(KEY_BIOMETRIC_ENABLED) ?: false
    }

    override suspend fun setAutoLockTimeout(minutes: Int) {
        dataStore.updateDataAsync { it[KEY_AUTO_LOCK_TIMEOUT] = minutes.coerceIn(1, 60) }.await()
    }

    override suspend fun getAutoLockTimeout(): Int {
        return dataStore.data.firstOrNull()?.get(KEY_AUTO_LOCK_TIMEOUT) ?: 15
    }

    override suspend fun setDebugMode(enabled: Boolean) {
        dataStore.updateDataAsync { it[KEY_DEBUG_MODE] = enabled }.await()
    }

    override suspend fun isDebugMode(): Boolean {
        return dataStore.data.firstOrNull()?.get(KEY_DEBUG_MODE) ?: false
    }
}