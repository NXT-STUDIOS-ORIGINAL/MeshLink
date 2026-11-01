package com.meshlink.app.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meshlink.app.model.Chat
import com.meshlink.app.model.ChatType
import com.meshlink.app.model.Message
import com.meshlink.app.model.User
import com.meshlink.app.network.MeshNetworkManager
import com.meshlink.app.repository.ChatRepository
import com.meshlink.app.repository.MessageRepository
import com.meshlink.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatsViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val networkManager: MeshNetworkManager
) : ViewModel() {

    private val _chats = MutableStateFlow<List<ChatWithPreview>>(emptyList())
    val chats = _chats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedChat = MutableStateFlow<Chat?>(null)
    val selectedChat = _selectedChat.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            _isLoading.value = true
            chatRepository.observeUserChats(getCurrentUserId())
                .map { chats ->
                    chats.map { chat ->
                        ChatWithPreview(
                            chat = chat,
                            lastMessage = null, // Will be populated by combine
                            unreadCount = 0,
                            otherUser = null // For private chats
                        )
                    }
                }
                .collect { chatPreviews ->
                    _chats.value = chatPreviews
                    _isLoading.value = false
                }
        }
    }

    private fun getCurrentUserId(): String {
        return networkManager.localUser?.id ?: ""
    }

    fun onChatClick(chat: Chat) {
        _selectedChat.value = chat
    }

    fun clearSelection() {
        _selectedChat.value = null
    }

    suspend fun createPrivateChat(otherUserId: String): Chat? {
        // Check if private chat already exists
        val currentUserId = getCurrentUserId()
        val existingChats = chatRepository.getUserChats(currentUserId)
        val privateChat = existingChats.find { chat ->
            chat.type == ChatType.PRIVATE &&
            chatRepository.isMember(chat.id, otherUserId) &&
            chatRepository.isMember(chat.id, currentUserId)
        }

        if (privateChat != null) {
            return privateChat
        }

        // Create new private chat
        val chatId = java.util.UUID.randomUUID().toString()
        val chat = Chat(
            id = chatId,
            type = ChatType.PRIVATE,
            createdBy = currentUserId
        )

        chatRepository.upsertChat(chat)
        chatRepository.addMember(com.meshlink.app.model.ChatMember(chatId, currentUserId, com.meshlink.app.model.MemberRole.OWNER))
        chatRepository.addMember(com.meshlink.app.model.ChatMember(chatId, otherUserId, com.meshlink.app.model.MemberRole.MEMBER))

        return chat
    }
}

data class ChatWithPreview(
    val chat: Chat,
    val lastMessage: Message?,
    val unreadCount: Int,
    val otherUser: User?
)