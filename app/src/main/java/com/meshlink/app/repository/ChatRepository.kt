package com.meshlink.app.repository

import com.meshlink.app.model.Chat
import com.meshlink.app.model.ChatMember
import com.meshlink.app.model.ChatType
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChat(id: String): Chat?
    fun observeChat(id: String): Flow<Chat?>
    suspend fun getAllChats(): List<Chat>
    fun observeAllChats(): Flow<List<Chat>>
    suspend fun getChatsByType(type: ChatType): List<Chat>
    fun observeChatsByType(type: ChatType): Flow<List<Chat>>
    suspend fun getUserChats(userId: String): List<Chat>
    fun observeUserChats(userId: String): Flow<List<Chat>>
    suspend fun upsertChat(chat: Chat)
    suspend fun updateChat(chat: Chat)
    suspend fun deleteChat(id: String)

    // Members
    suspend fun getMembers(chatId: String): List<ChatMember>
    fun observeMembers(chatId: String): Flow<List<ChatMember>>
    suspend fun addMember(member: ChatMember)
    suspend fun removeMember(chatId: String, userId: String)
    suspend fun updateMember(member: ChatMember)
    suspend fun isMember(chatId: String, userId: String): Boolean
}