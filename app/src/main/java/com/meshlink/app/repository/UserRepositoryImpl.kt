package com.meshlink.app.repository

import com.meshlink.app.data.local.UserDao
import com.meshlink.app.model.User
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override suspend fun getUser(id: String): User? = userDao.getById(id)

    override fun observeUser(id: String): Flow<User?> = userDao.observeById(id)

    override suspend fun getLocalUser(): User? = userDao.getLocalUser()

    override fun observeLocalUser(): Flow<User?> = userDao.observeLocalUser()

    override suspend fun getAllContacts(): List<User> = userDao.getAllContacts()

    override fun observeAllContacts(): Flow<List<User>> = userDao.observeAllContacts()

    override suspend fun upsertUser(user: User) = userDao.insert(user)

    override suspend fun updateUserStatus(userId: String, status: UserStatus) {
        userDao.getById(userId)?.let { user ->
            userDao.insert(user.copy(status = com.meshlink.app.model.UserStatus.valueOf(status.name)))
        }
    }

    override suspend fun deleteUser(id: String) = userDao.delete(id)

    override suspend fun clearContacts() = userDao.clearContacts()
}