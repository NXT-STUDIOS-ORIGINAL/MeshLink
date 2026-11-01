package com.meshlink.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.ViewModelProvider
import com.meshlink.app.MeshLinkApplication
import com.meshlink.app.network.MeshNetworkManager
import com.meshlink.app.repository.ChatRepository
import com.meshlink.app.repository.MessageRepository
import com.meshlink.app.repository.ProfileRepository
import com.meshlink.app.repository.UserRepository
import com.meshlink.app.ui.main.MainViewModel
import com.meshlink.app.ui.navigation.MeshLinkNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as MeshLinkApplication

            val profileRepository = app.profileRepository
            val userRepository = app.userRepository
            val chatRepository = app.chatRepository
            val messageRepository = app.messageRepository
            val networkManager = app.networkManager

            MeshLinkNavHost()
        }
    }

    private fun enableEdgeToEdge() {
        // Enable edge-to-edge display
        window.setStatusBarColor(android.graphics.Color.TRANSPARENT)
        window.setNavigationBarColor(android.graphics.Color.TRANSPARENT)
        window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}