package com.meshlink.app.repository

import com.meshlink.app.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(id: String): User?
    fun observeUser(id: String): Flow<User?>
    suspend fun getLocalUser(): User?
    fun observeLocalUser(): Flow<User?>
    suspend fun getAllContacts(): List<User>
    fun observeAllContacts(): Flow<List<User>>
    suspend fun upsertUser(user: User)
    suspend fun updateUserStatus(userId: String, status: UserStatus)
    suspend fun deleteUser(id: String)
    suspend fun clearContacts()
}

enum class UserStatus {
    ONLINE, OFFLINE, AWAY, BUSY
}