package com.meshlink.app.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meshlink.app.R
import com.meshlink.app.ui.chat.ChatScreen
import com.meshlink.app.ui.groups.GroupsScreen
import com.meshlink.app.ui.nearby.NearbyScreen
import com.meshlink.app.ui.rooms.RoomsScreen
import com.meshlink.app.ui.settings.SettingsScreen
import com.meshlink.app.ui.theme.MeshLinkTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToChat: (String) -> Unit = {},
    onNavigateToGroup: (String) -> Unit = {},
    onNavigateToRoom: (String) -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val hasProfile by viewModel.hasProfile.observeAsState()

    // Navigation state
    var selectedTab by remember { MutableStateFlow(0) }

    // If no profile, show setup or redirect to settings
    if (!hasProfile) {
        SettingsScreen(onProfileClick = onNavigateToProfile)
        return
    }

    MeshLinkTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Main content
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (selectedTab) {
                    0 -> NearbyScreen(onPeerClick = { peer ->
                        // Navigate to chat with peer
                        onNavigateToChat(peer.userId)
                    })
                    1 -> ChatsScreen(onChatClick = { chat ->
                        onNavigateToChat(chat.id)
                    })
                    2 -> GroupsScreen(onGroupClick = { group ->
                        onNavigateToGroup(group.id)
                    })
                    3 -> RoomsScreen(onRoomClick = { room ->
                        onNavigateToRoom(room.id)
                    })
                    4 -> SettingsScreen(onProfileClick = onNavigateToProfile)
                }
            }

            // Bottom Navigation Bar
            NavigationBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                val items = listOf(
                    NavigationItem(
                        icon = androidx.compose.material.icons.Icons.Default.Bluetooth,
                        label = stringResource(R.string.nearby),
                        route = 0
                    ),
                    NavigationItem(
                        icon = androidx.compose.material.icons.Icons.Default.ChatBubbleOutline,
                        label = stringResource(R.string.chats),
                        route = 1
                    ),
                    NavigationItem(
                        icon = androidx.compose.material.icons.Icons.Default.Group,
                        label = stringResource(R.string.groups),
                        route = 2
                    ),
                    NavigationItem(
                        icon = androidx.compose.material.icons.Icons.Default.Public,
                        label = stringResource(R.string.rooms),
                        route = 3
                    ),
                    NavigationItem(
                        icon = androidx.compose.material.icons.Icons.Default.Settings,
                        label = stringResource(R.string.settings),
                        route = 4
                    )
                )

                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 12.sp
                            )
                        },
                        selected = selectedTab == item.route,
                        onClick = { selectedTab = item.route },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    }
}

data class NavigationItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val route: Int
)