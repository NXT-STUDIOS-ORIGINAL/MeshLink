package com.meshlink.app

import android.app.Application
import androidx.room.Room
import com.meshlink.app.data.local.MeshLinkDatabase
import com.meshlink.app.data.preferences.PreferencesRepository
import com.meshlink.app.data.preferences.PreferencesRepositoryImpl
import com.meshlink.app.network.MeshNetworkManager
import com.meshlink.app.network.MeshNetworkManagerImpl
import com.meshlink.app.repository.ChatRepository
import com.meshlink.app.repository.ChatRepositoryImpl
import com.meshlink.app.repository.MessageRepository
import com.meshlink.app.repository.MessageRepositoryImpl
import com.meshlink.app.repository.ProfileRepository
import com.meshlink.app.repository.ProfileRepositoryImpl
import com.meshlink.app.repository.UserRepository
import com.meshlink.app.repository.UserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MeshLinkApplication : Application() {

    private val scope = CoroutineScope(SupervisorJob())

    // Repositories
    lateinit var profileRepository: ProfileRepository
    lateinit var userRepository: UserRepository
    lateinit var chatRepository: ChatRepository
    lateinit var messageRepository: MessageRepository
    lateinit var preferencesRepository: PreferencesRepository
    lateinit var networkManager: MeshNetworkManager

    // Database
    lateinit var database: MeshLinkDatabase

    override fun onCreate() {
        super.onCreate()

        // Initialize database
        database = Room.databaseBuilder(
            applicationContext,
            MeshLinkDatabase::class.java,
            "meshlink.db"
        ).build()

        // Initialize repositories
        preferencesRepository = PreferencesRepositoryImpl(this)
        profileRepository = ProfileRepositoryImpl(preferencesRepository)
        userRepository = UserRepositoryImpl(database.userDao())
        chatRepository = ChatRepositoryImpl(database.chatDao(), database.chatMemberDao())
        messageRepository = MessageRepositoryImpl(database.messageDao())

        // Initialize network manager
        networkManager = MeshNetworkManagerImpl(
            applicationContext,
            profileRepository,
            userRepository,
            chatRepository,
            messageRepository
        )
    }

    override fun onTerminate() {
        scope.cancel()
        database.close()
        super.onTerminate()
    }

    companion object {
        private var instance: MeshLinkApplication? = null

        fun getInstance(): MeshLinkApplication {
            return instance!!
        }
    }

    init {
        instance = this
    }
}