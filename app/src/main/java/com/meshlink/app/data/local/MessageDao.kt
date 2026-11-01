package com.meshlink.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.meshlink.app.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<Message>)

    @Update
    suspend fun update(message: Message)

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getById(id: String): Message?

    @Query("SELECT * FROM messages WHERE id = :id")
    fun observeById(id: String): Flow<Message?>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC LIMIT :limit OFFSET :offset")
    suspend fun getMessagesByChat(chatId: String, limit: Int, offset: Int): List<Message>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun observeMessagesByChat(chatId: String): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMessage(chatId: String): Message?

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp DESC LIMIT 1")
    fun observeLatestMessage(chatId: String): Flow<Message?>

    @Query("SELECT COUNT(*) FROM messages WHERE chatId = :chatId AND status != 'READ' AND senderId != :currentUserId")
    suspend fun getUnreadCount(chatId: String, currentUserId: String): Int

    @Query("UPDATE messages SET status = :status WHERE chatId = :chatId AND senderId != :currentUserId AND status != 'READ'")
    suspend fun markAsRead(chatId: String, currentUserId: String, status: String)

    @Query("SELECT * FROM messages WHERE status = :status AND chatId = :chatId")
    suspend fun getMessagesByStatus(chatId: String, status: String): List<Message>

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun clearChatMessages(chatId: String)

    @Query("DELETE FROM messages WHERE expiresAt IS NOT NULL AND expiresAt < :now")
    suspend fun deleteExpiredMessages(now: Long)
}