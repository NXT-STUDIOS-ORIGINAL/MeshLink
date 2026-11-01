package com.meshlink.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.meshlink.app.model.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: Chat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<Chat>)

    @Update
    suspend fun update(chat: Chat)

    @Query("SELECT * FROM chats WHERE id = :id")
    suspend fun getById(id: String): Chat?

    @Query("SELECT * FROM chats WHERE id = :id")
    fun observeById(id: String): Flow<Chat?>

    @Query("SELECT * FROM chats ORDER BY updatedAt DESC")
    suspend fun getAllChats(): List<Chat>

    @Query("SELECT * FROM chats ORDER BY updatedAt DESC")
    fun observeAllChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE type = :type ORDER BY updatedAt DESC")
    suspend fun getChatsByType(type: String): List<Chat>

    @Query("SELECT * FROM chats WHERE type = :type ORDER BY updatedAt DESC")
    fun observeChatsByType(type: String): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE id IN (SELECT chatId FROM chat_members WHERE userId = :userId) ORDER BY updatedAt DESC")
    suspend fun getUserChats(userId: String): List<Chat>

    @Query("SELECT * FROM chats WHERE id IN (SELECT chatId FROM chat_members WHERE userId = :userId) ORDER BY updatedAt DESC")
    fun observeUserChats(userId: String): Flow<List<Chat>>

    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun delete(id: String)
}