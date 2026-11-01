package com.meshlink.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meshlink.app.MeshLinkApplication
import com.meshlink.app.network.MeshNetworkManager
import com.meshlink.app.repository.ChatRepository
import com.meshlink.app.repository.MessageRepository
import com.meshlink.app.repository.ProfileRepository
import com.meshlink.app.repository.UserRepository
import com.meshlink.app.ui.chat.ChatViewModel
import com.meshlink.app.ui.groups.GroupsViewModel
import com.meshlink.app.ui.main.MainViewModel
import com.meshlink.app.ui.nearby.NearbyViewModel
import com.meshlink.app.ui.rooms.RoomsViewModel
import com.meshlink.app.ui.settings.SettingsViewModel

class ViewModelFactory(private val app: MeshLinkApplication) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> MainViewModel(app.profileRepository, app.userRepository) as T
            NearbyViewModel::class.java -> NearbyViewModel(app.networkManager, app.userRepository) as T
            ChatsViewModel::class.java -> ChatsViewModel(app.chatRepository, app.messageRepository, app.userRepository, app.networkManager) as T
            GroupsViewModel::class.java -> GroupsViewModel(app.chatRepository, app.messageRepository, app.userRepository, app.networkManager) as T
            RoomsViewModel::class.java -> RoomsViewModel(app.chatRepository, app.messageRepository, app.networkManager) as T
            SettingsViewModel::class.java -> SettingsViewModel(app.profileRepository, app.preferencesRepository, app.networkManager) as T
            ChatViewModel::class.java -> {
                // ChatViewModel needs chatId, handle separately
                throw IllegalArgumentException("ChatViewModel requires chatId parameter")
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}