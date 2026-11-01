package com.meshlink.app.ui.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meshlink.app.model.Chat
import com.meshlink.app.model.ChatType
import com.meshlink.app.model.Message
import com.meshlink.app.network.MeshNetworkManager
import com.meshlink.app.repository.ChatRepository
import com.meshlink.app.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RoomsViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val networkManager: MeshNetworkManager
) : ViewModel() {

    private val _rooms = MutableStateFlow<List<RoomWithPreview>>(emptyList())
    val rooms = _rooms.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedRoom = MutableStateFlow<Chat?>(null)
    val selectedRoom = _selectedRoom.asStateFlow()

    init {
        loadRooms()
    }

    private fun loadRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            chatRepository.observeChatsByType(ChatType.PUBLIC_ROOM)
                .map { chats ->
                    chats.map { chat ->
                        RoomWithPreview(
                            chat = chat,
                            lastMessage = null,
                            unreadCount = 0,
                            participantCount = 0
                        )
                    }
                }
                .collect { roomPreviews ->
                    _rooms.value = roomPreviews
                    _isLoading.value = false
                }
        }
    }

    fun onRoomClick(room: Chat) {
        _selectedRoom.value = room
    }

    fun clearSelection() {
        _selectedRoom.value = null
    }

    suspend fun createRoom(name: String): Chat? {
        val currentUserId = networkManager.localUser?.id ?: return null

        val chatId = java.util.UUID.randomUUID().toString()
        val chat = Chat(
            id = chatId,
            type = ChatType.PUBLIC_ROOM,
            name = name,
            createdBy = currentUserId
        )

        chatRepository.upsertChat(chat)
        chatRepository.addMember(com.meshlink.app.model.ChatMember(chatId, currentUserId, com.meshlink.app.model.MemberRole.OWNER))

        return chat
    }

    suspend fun joinRoom(roomId: String): Boolean {
        val currentUserId = networkManager.localUser?.id ?: return false
        val chat = chatRepository.getChat(roomId) ?: return false

        if (chat.type != ChatType.PUBLIC_ROOM) return false

        chatRepository.addMember(com.meshlink.app.model.ChatMember(roomId, currentUserId, com.meshlink.app.model.MemberRole.MEMBER))
        return true
    }

    suspend fun leaveRoom(roomId: String): Boolean {
        val currentUserId = networkManager.localUser?.id ?: return false
        chatRepository.removeMember(roomId, currentUserId)
        return true
    }
}

data class RoomWithPreview(
    val chat: Chat,
    val lastMessage: Message?,
    val unreadCount: Int,
    val participantCount: Int
)