package com.meshlink.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meshlink.app.data.preferences.PreferencesRepository
import com.meshlink.app.model.UserProfile
import com.meshlink.app.network.MeshNetworkManager
import com.meshlink.app.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val profileRepository: ProfileRepository,
    private val preferencesRepository: PreferencesRepository,
    private val networkManager: MeshNetworkManager
) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile = _profile.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing = _isEditing.asStateFlow()

    // Network settings
    private val _bluetoothEnabled = MutableStateFlow(true)
    val bluetoothEnabled = _bluetoothEnabled.asStateFlow()

    private val _wifiDirectEnabled = MutableStateFlow(true)
    val wifiDirectEnabled = _wifiDirectEnabled.asStateFlow()

    private val _meshRelayEnabled = MutableStateFlow(true)
    val meshRelayEnabled = _meshRelayEnabled.asStateFlow()

    private val _maxHopCount = MutableStateFlow(3)
    val maxHopCount = _maxHopCount.asStateFlow()

    // UI settings
    private val _theme = MutableStateFlow("system")
    val theme = _theme.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled = _notificationsEnabled.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled = _soundEnabled.asStateFlow()

    private val _vibrationEnabled = MutableStateFlow(true)
    val vibrationEnabled = _vibrationEnabled.asStateFlow()

    // Security settings
    private val _biometricEnabled = MutableStateFlow(false)
    val biometricEnabled = _biometricEnabled.asStateFlow()

    private val _autoLockTimeout = MutableStateFlow(15)
    val autoLockTimeout = _autoLockTimeout.asStateFlow()

    // Debug
    private val _debugMode = MutableStateFlow(false)
    val debugMode = _debugMode.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _profile.value = profileRepository.getProfile()

            _bluetoothEnabled.value = preferencesRepository.isBluetoothEnabled()
            _wifiDirectEnabled.value = preferencesRepository.isWifiDirectEnabled()
            _meshRelayEnabled.value = preferencesRepository.isMeshRelayEnabled()
            _maxHopCount.value = preferencesRepository.getMaxHopCount()
            _theme.value = preferencesRepository.getTheme()
            _notificationsEnabled.value = preferencesRepository.areNotificationsEnabled()
            _soundEnabled.value = preferencesRepository.isSoundEnabled()
            _vibrationEnabled.value = preferencesRepository.isVibrationEnabled()
            _biometricEnabled.value = preferencesRepository.isBiometricEnabled()
            _autoLockTimeout.value = preferencesRepository.getAutoLockTimeout()
            _debugMode.value = preferencesRepository.isDebugMode()
        }
    }

    fun setEditing(editing: Boolean) {
        _isEditing.value = editing
    }

    suspend fun saveProfile(profile: UserProfile) {
        profileRepository.saveProfile(profile)
        _profile.value = profile
    }

    suspend fun updateDisplayName(name: String) {
        profileRepository.updateDisplayName(name)
        _profile.value = _profile.value?.copy(displayName = name)
    }

    suspend fun setBluetoothEnabled(enabled: Boolean) {
        _bluetoothEnabled.value = enabled
        preferencesRepository.setBluetoothEnabled(enabled)
    }

    suspend fun setWifiDirectEnabled(enabled: Boolean) {
        _wifiDirectEnabled.value = enabled
        preferencesRepository.setWifiDirectEnabled(enabled)
    }

    suspend fun setMeshRelayEnabled(enabled: Boolean) {
        _meshRelayEnabled.value = enabled
        preferencesRepository.setMeshRelayEnabled(enabled)
    }

    suspend fun setMaxHopCount(count: Int) {
        _maxHopCount.value = count
        preferencesRepository.setMaxHopCount(count)
    }

    suspend fun setTheme(theme: String) {
        _theme.value = theme
        preferencesRepository.setTheme(theme)
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        preferencesRepository.setNotificationsEnabled(enabled)
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        _soundEnabled.value = enabled
        preferencesRepository.setSoundEnabled(enabled)
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        _vibrationEnabled.value = enabled
        preferencesRepository.setVibrationEnabled(enabled)
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        _biometricEnabled.value = enabled
        preferencesRepository.setBiometricEnabled(enabled)
    }

    suspend fun setAutoLockTimeout(minutes: Int) {
        _autoLockTimeout.value = minutes
        preferencesRepository.setAutoLockTimeout(minutes)
    }

    suspend fun setDebugMode(enabled: Boolean) {
        _debugMode.value = enabled
        preferencesRepository.setDebugMode(enabled)
    }

    fun getNetworkState(): String {
        // networkManager.networkState is a Flow, cannot use in when expression directly
        // Return placeholder until proper state observation is implemented
        return "Unknown"
    }
}