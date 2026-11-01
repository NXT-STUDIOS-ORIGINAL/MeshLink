package com.meshlink.app.ui.groups

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GroupsViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val networkManager: MeshNetworkManager
) : ViewModel() {

    private val _groups = MutableStateFlow<List<GroupWithPreview>>(emptyList())
    val groups = _groups.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedGroup = MutableStateFlow<Chat?>(null)
    val selectedGroup = _selectedGroup.asStateFlow()

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _isLoading.value = true
            chatRepository.observeChatsByType(ChatType.GROUP)
                .map { chats ->
                    chats.map { chat ->
                        GroupWithPreview(
                            chat = chat,
                            lastMessage = null,
                            unreadCount = 0,
                            memberCount = 0
                        )
                    }
                }
                .collect { groupPreviews ->
                    _groups.value = groupPreviews
                    _isLoading.value = false
                }
        }
    }

    fun onGroupClick(group: Chat) {
        _selectedGroup.value = group
    }

    fun clearSelection() {
        _selectedGroup.value = null
    }

    suspend fun createGroup(name: String, memberIds: List<String>): Chat? {
        val currentUserId = networkManager.localUser?.id ?: return null
        val allMemberIds = (memberIds + currentUserId).distinct()

        val chatId = java.util.UUID.randomUUID().toString()
        val chat = Chat(
            id = chatId,
            type = ChatType.GROUP,
            name = name,
            createdBy = currentUserId
        )

        chatRepository.upsertChat(chat)
        chatRepository.addMember(com.meshlink.app.model.ChatMember(chatId, currentUserId, com.meshlink.app.model.MemberRole.OWNER))

        allMemberIds.filter { it != currentUserId }.forEach { memberId ->
            chatRepository.addMember(com.meshlink.app.model.ChatMember(chatId, memberId, com.meshlink.app.model.MemberRole.MEMBER))
        }

        return chat
    }
}

data class GroupWithPreview(
    val chat: Chat,
    val lastMessage: Message?,
    val unreadCount: Int,
    val memberCount: Int
)