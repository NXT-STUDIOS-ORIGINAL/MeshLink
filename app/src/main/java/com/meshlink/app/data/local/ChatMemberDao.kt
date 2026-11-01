package com.meshlink.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meshlink.app.model.ChatMember
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: ChatMember)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(members: List<ChatMember>)

    @Query("SELECT * FROM chat_members WHERE chatId = :chatId AND userId = :userId")
    suspend fun getByChatAndUser(chatId: String, userId: String): ChatMember?

    @Query("SELECT * FROM chat_members WHERE chatId = :chatId")
    suspend fun getMembersByChat(chatId: String): List<ChatMember>

    @Query("SELECT * FROM chat_members WHERE chatId = :chatId")
    fun observeMembersByChat(chatId: String): Flow<List<ChatMember>>

    @Query("SELECT * FROM chat_members WHERE userId = :userId")
    suspend fun getChatsByUser(userId: String): List<ChatMember>

    @Query("DELETE FROM chat_members WHERE chatId = :chatId AND userId = :userId")
    suspend fun removeMember(chatId: String, userId: String)

    @Query("DELETE FROM chat_members WHERE chatId = :chatId")
    suspend fun clearMembers(chatId: String)
}