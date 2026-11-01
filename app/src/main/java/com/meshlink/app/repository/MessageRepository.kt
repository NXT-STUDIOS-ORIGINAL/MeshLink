package com.meshlink.app.repository

import com.meshlink.app.model.Message
import com.meshlink.app.model.MessageStatus
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessage(id: String): Message?
    fun observeMessage(id: String): Flow<Message?>
    suspend fun getMessages(chatId: String, limit: Int, offset: Int): List<Message>
    fun observeMessages(chatId: String): Flow<List<Message>>
    suspend fun getLatestMessage(chatId: String): Message?
    fun observeLatestMessage(chatId: String): Flow<Message?>
    suspend fun getUnreadCount(chatId: String, currentUserId: String): Int
    suspend fun insertMessage(message: Message)
    suspend fun insertMessages(messages: List<Message>)
    suspend fun updateMessage(message: Message)
    suspend fun updateMessageStatus(messageId: String, status: MessageStatus)
    suspend fun markChatAsRead(chatId: String, currentUserId: String)
    suspend fun deleteMessage(id: String)
    suspend fun clearChatMessages(chatId: String)
    suspend fun deleteExpiredMessages()
}