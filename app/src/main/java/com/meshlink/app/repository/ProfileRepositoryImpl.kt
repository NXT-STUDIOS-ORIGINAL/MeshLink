package com.meshlink.app.repository

import com.meshlink.app.data.preferences.PreferencesRepository
import com.meshlink.app.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(private val preferencesRepository: PreferencesRepository) : ProfileRepository {

    override val profileFlow: Flow<UserProfile?> = preferencesRepository.profileFlow

    override suspend fun getProfile(): UserProfile? = preferencesRepository.getProfile()

    override suspend fun saveProfile(profile: UserProfile) = preferencesRepository.saveProfile(profile)

    override suspend fun updateDisplayName(name: String) {
        getProfile()?.let { profile ->
            preferencesRepository.saveProfile(profile.copy(displayName = name))
        }
    }

    override suspend fun updateAvatar(avatarHash: String) {
        getProfile()?.let { profile ->
            preferencesRepository.saveProfile(profile.copy(avatarHash = avatarHash))
        }
    }

    override suspend fun clearProfile() = preferencesRepository.clearProfile()

    override suspend fun hasProfile(): Boolean = getProfile() != null
}