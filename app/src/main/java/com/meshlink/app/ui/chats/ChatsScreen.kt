package com.meshlink.app.ui.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meshlink.app.R
import com.meshlink.app.model.Chat
import com.meshlink.app.model.ChatType
import com.meshlink.app.model.Message
import com.meshlink.app.ui.theme.MeshLinkTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel = viewModel(),
    onChatClick: (Chat) -> Unit = {}
) {
    val chats by viewModel.chats.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState()

    MeshLinkTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.private_chats)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                actions = {
                    IconButton(onClick = { /* TODO: Show new chat dialog */ }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Add,
                            contentDescription = stringResource(R.string.new_chat),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )

            if (isLoading) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            } else if (chats.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        text = stringResource(R.string.no_chats_yet),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = stringResource(R.string.start_new_conversation),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(chats) { chatPreview ->
                        ChatItem(
                            chat = chatPreview.chat,
                            lastMessage = chatPreview.lastMessage,
                            unreadCount = chatPreview.unreadCount,
                            otherUser = chatPreview.otherUser,
                            onClick = { onChatClick(chatPreview.chat) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    lastMessage: Message?,
    unreadCount: Int,
    otherUser: com.meshlink.app.model.User?,
    onClick: () -> Unit
) {
    val displayName = if (chat.type == ChatType.PRIVATE) {
        otherUser?.displayName ?: chat.name ?: "Unknown"
    } else {
        chat.name ?: "Unnamed Chat"
    }

    val lastMessageText = lastMessage?.content ?: "No messages yet"
    val lastMessageTime = lastMessage?.timestamp?.let { formatTime(it) } ?: ""

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = if (chat.type == ChatType.PRIVATE) {
                            androidx.compose.material.icons.Icons.Default.Person
                        } else {
                            androidx.compose.material.icons.Icons.Default.Group
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(12.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    androidx.compose.foundation.layout.Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = displayName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                        )
                        Text(
                            text = lastMessageTime,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = lastMessageText,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                    )
                }
            }

            if (unreadCount > 0) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp)
                ) {
                    Text(
                        text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = androidx.compose.ui.text.TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val dayMs = 24 * 60 * 60 * 1000L

    return if (diff < dayMs) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
    } else if (diff < 7 * dayMs) {
        SimpleDateFormat("EEE", Locale.getDefault()).format(Date(timestamp))
    } else {
        SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(Date(timestamp))
    }
}