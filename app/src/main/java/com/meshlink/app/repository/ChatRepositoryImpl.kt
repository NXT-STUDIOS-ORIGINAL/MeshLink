package com.meshlink.app.repository

import com.meshlink.app.data.local.ChatDao
import com.meshlink.app.data.local.ChatMemberDao
import com.meshlink.app.model.Chat
import com.meshlink.app.model.ChatMember
import com.meshlink.app.model.ChatType
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl(
    private val chatDao: ChatDao,
    private val chatMemberDao: ChatMemberDao
) : ChatRepository {

    override suspend fun getChat(id: String): Chat? = chatDao.getById(id)

    override fun observeChat(id: String): Flow<Chat?> = chatDao.observeById(id)

    override suspend fun getAllChats(): List<Chat> = chatDao.getAllChats()

    override fun observeAllChats(): Flow<List<Chat>> = chatDao.observeAllChats()

    override suspend fun getChatsByType(type: ChatType): List<Chat> = chatDao.getChatsByType(type.name)

    override fun observeChatsByType(type: ChatType): Flow<List<Chat>> = chatDao.observeChatsByType(type.name)

    override suspend fun getUserChats(userId: String): List<Chat> = chatDao.getUserChats(userId)

    override fun observeUserChats(userId: String): Flow<List<Chat>> = chatDao.observeUserChats(userId)

    override suspend fun upsertChat(chat: Chat) = chatDao.insert(chat)

    override suspend fun updateChat(chat: Chat) = chatDao.update(chat)

    override suspend fun deleteChat(id: String) = chatDao.delete(id)

    override suspend fun getMembers(chatId: String): List<ChatMember> = chatMemberDao.getMembersByChat(chatId)

    override fun observeMembers(chatId: String): Flow<List<ChatMember>> = chatMemberDao.observeMembersByChat(chatId)

    override suspend fun addMember(member: ChatMember) = chatMemberDao.insert(member)

    override suspend fun removeMember(chatId: String, userId: String) = chatMemberDao.removeMember(chatId, userId)

    override suspend fun updateMember(member: ChatMember) = chatMemberDao.insert(member)

    override suspend fun isMember(chatId: String, userId: String): Boolean {
        return chatMemberDao.getByChatAndUser(chatId, userId) != null
    }
}