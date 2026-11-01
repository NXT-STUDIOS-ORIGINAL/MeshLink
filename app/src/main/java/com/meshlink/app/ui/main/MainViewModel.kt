package com.meshlink.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meshlink.app.repository.ProfileRepository
import com.meshlink.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _hasProfile = MutableStateFlow(false)
    val hasProfile = _hasProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        checkProfile()
    }

    private fun checkProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _hasProfile.value = profileRepository.getProfile() != null
            _isLoading.value = false
        }
    }

    fun refreshProfile() {
        checkProfile()
    }
}