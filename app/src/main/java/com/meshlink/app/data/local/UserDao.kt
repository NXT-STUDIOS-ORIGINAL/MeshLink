package com.meshlink.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.meshlink.app.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    fun observeById(id: String): Flow<User?>

    @Query("SELECT * FROM users WHERE isLocal = 1 LIMIT 1")
    suspend fun getLocalUser(): User?

    @Query("SELECT * FROM users WHERE isLocal = 1 LIMIT 1")
    fun observeLocalUser(): Flow<User?>

    @Query("SELECT * FROM users WHERE isLocal = 0 ORDER BY lastSeen DESC")
    suspend fun getAllContacts(): List<User>

    @Query("SELECT * FROM users WHERE isLocal = 0 ORDER BY lastSeen DESC")
    fun observeAllContacts(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE status = :status AND isLocal = 0")
    suspend fun getUsersByStatus(status: String): List<User>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM users WHERE isLocal = 0")
    suspend fun clearContacts()
}