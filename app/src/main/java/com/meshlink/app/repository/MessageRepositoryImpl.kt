package com.meshlink.app.repository

import com.meshlink.app.data.local.MessageDao
import com.meshlink.app.model.Message
import com.meshlink.app.model.MessageStatus
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(private val messageDao: MessageDao) : MessageRepository {

    override suspend fun getMessage(id: String): Message? = messageDao.getById(id)

    override fun observeMessage(id: String): Flow<Message?> = messageDao.observeById(id)

    override suspend fun getMessages(chatId: String, limit: Int, offset: Int): List<Message> =
        messageDao.getMessagesByChat(chatId, limit, offset)

    override fun observeMessages(chatId: String): Flow<List<Message>> = messageDao.observeMessagesByChat(chatId)

    override suspend fun getLatestMessage(chatId: String): Message? = messageDao.getLatestMessage(chatId)

    override fun observeLatestMessage(chatId: String): Flow<Message?> = messageDao.observeLatestMessage(chatId)

    override suspend fun getUnreadCount(chatId: String, currentUserId: String): Int =
        messageDao.getUnreadCount(chatId, currentUserId)

    override suspend fun insertMessage(message: Message) = messageDao.insert(message)

    override suspend fun insertMessages(messages: List<Message>) = messageDao.insertAll(messages)

    override suspend fun updateMessage(message: Message) = messageDao.update(message)

    override suspend fun updateMessageStatus(messageId: String, status: MessageStatus) {
        messageDao.getById(messageId)?.let { msg ->
            messageDao.update(msg.copy(status = status))
        }
    }

    override suspend fun markChatAsRead(chatId: String, currentUserId: String) =
        messageDao.markAsRead(chatId, currentUserId, MessageStatus.READ.name)

    override suspend fun deleteMessage(id: String) = messageDao.delete(id)

    override suspend fun clearChatMessages(chatId: String) = messageDao.clearChatMessages(chatId)

    override suspend fun deleteExpiredMessages() = messageDao.deleteExpiredMessages(System.currentTimeMillis())
}