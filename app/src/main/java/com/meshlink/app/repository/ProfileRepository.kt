package com.meshlink.app.repository

import com.meshlink.app.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profileFlow: Flow<UserProfile?>

    suspend fun getProfile(): UserProfile?
    suspend fun saveProfile(profile: UserProfile)
    suspend fun updateDisplayName(name: String)
    suspend fun updateAvatar(avatarHash: String)
    suspend fun clearProfile()
    suspend fun hasProfile(): Boolean
}