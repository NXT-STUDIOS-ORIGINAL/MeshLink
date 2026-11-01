package com.meshlink.app.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meshlink.app.model.Chat
import com.meshlink.app.model.Message
import com.meshlink.app.model.MessageStatus
import com.meshlink.app.model.MessageType
import com.meshlink.app.model.User
import com.meshlink.app.network.MeshNetworkManager
import com.meshlink.app.repository.ChatRepository
import com.meshlink.app.repository.MessageRepository
import com.meshlink.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val networkManager: MeshNetworkManager,
    private val chatId: String
) : ViewModel() {

    private val _chat = MutableStateFlow<Chat?>(null)
    val chat = _chat.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageWithSender>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _newMessageText = MutableStateFlow("")
    val newMessageText = _newMessageText.asStateFlow()

    private val _otherUser = MutableStateFlow<User?>(null)
    val otherUser = _otherUser.asStateFlow()

    init {
        loadChat()
        loadMessages()
        loadOtherUser()
    }

    private fun loadChat() {
        viewModelScope.launch {
            chatRepository.observeChat(chatId)
                .collect { chat ->
                    _chat.value = chat
                }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _isLoading.value = true
            messageRepository.observeMessages(chatId)
                .map { messages ->
                    messages.map { message ->
                        MessageWithSender(
                            message = message,
                            sender = null // Will be populated from user repository
                        )
                    }
                }
                .collect { messagesWithSender ->
                    _messages.value = messagesWithSender
                    _isLoading.value = false
                }
        }
    }

    private fun loadOtherUser() {
        viewModelScope.launch {
            val currentUserId = networkManager.localUser?.id ?: return@launch
            val members = chatRepository.getMembers(chatId)
            val otherMember = members.find { it.userId != currentUserId }
            otherMember?.let { member ->
                userRepository.observeUser(member.userId)
                    .collect { user ->
                        _otherUser.value = user
                    }
            }
        }
    }

    fun onMessageTextChange(text: String) {
        _newMessageText.value = text
    }

    suspend fun sendMessage() {
        val text = _newMessageText.value.trim()
        if (text.isBlank()) return

        val currentUserId = networkManager.localUser?.id ?: return
        val message = Message(
            id = java.util.UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = currentUserId,
            content = text, // In real implementation, this would be encrypted
            type = MessageType.TEXT,
            timestamp = System.currentTimeMillis()
        )

        messageRepository.insertMessage(message)
        networkManager.sendMessage(currentUserId, message) // This will route through mesh
        _newMessageText.value = ""
    }

    suspend fun markAsRead() {
        val currentUserId = networkManager.localUser?.id ?: return
        messageRepository.markChatAsRead(chatId, currentUserId)
    }

    fun getOtherUserName(): String {
        return _otherUser.value?.displayName ?: _chat.value?.name ?: "Unknown"
    }
}

data class MessageWithSender(
    val message: Message,
    val sender: User?
)