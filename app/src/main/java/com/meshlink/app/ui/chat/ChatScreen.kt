package com.meshlink.app.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meshlink.app.R
import com.meshlink.app.model.Message
import com.meshlink.app.model.MessageStatus
import com.meshlink.app.model.MessageType
import com.meshlink.app.model.User
import com.meshlink.app.ui.theme.MeshLinkTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val chat by viewModel.chat.observeAsState()
    val messages by viewModel.messages.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState()
    val newMessageText by viewModel.newMessageText.observeAsState()
    val otherUser by viewModel.otherUser.observeAsState()

    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    val displayName = otherUser?.displayName ?: chat?.name ?: "Unknown"

    MeshLinkTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = displayName,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                actions = {
                    IconButton(onClick = { /* TODO: Show chat info */ }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.InfoOutline,
                            contentDescription = stringResource(R.string.chat_info),
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
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    reverseLayout = true
                ) {
                    items(messages.reversed()) { messageWithSender ->
                        MessageBubble(
                            message = messageWithSender.message,
                            isOwn = messageWithSender.message.senderId == viewModel.networkManager.localUser?.id,
                            showSenderName = messageWithSender.message.type == MessageType.SYSTEM || false
                        )
                    }
                }

                // Message input
                MessageInput(
                    text = newMessageText,
                    onTextChange = { viewModel.onMessageTextChange(it) },
                    onSend = { viewModel.sendMessage() }
                )
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: Message,
    isOwn: Boolean,
    showSenderName: Boolean = false
) {
    val time = formatTime(message.timestamp)
    val statusIcon = when (message.status) {
        MessageStatus.SENDING -> androidx.compose.material.icons.Icons.Default.Schedule
        MessageStatus.SENT -> androidx.compose.material.icons.Icons.Default.Check
        MessageStatus.DELIVERED -> androidx.compose.material.icons.Icons.Default.DoubleCheck
        MessageStatus.READ -> androidx.compose.material.icons.Icons.Default.DoubleCheck
        MessageStatus.FAILED -> androidx.compose.material.icons.Icons.Default.ErrorOutline
        else -> androidx.compose.material.icons.Icons.Default.Check
    }
    val statusColor = when (message.status) {
        MessageStatus.READ -> MaterialTheme.colorScheme.primary
        MessageStatus.FAILED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val bubbleColor = if (isOwn) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val textColor = if (isOwn) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        if (!isOwn) {
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(48.dp))
        }

        Column(
            horizontalAlignment = if (isOwn) Alignment.End else Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            if (showSenderName && !isOwn) {
                Text(
                    text = "Unknown",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 4.dp)
                )
            }

            Card(
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = bubbleColor
                ),
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .constrainMaxWidth(300.dp)
            ) {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    if (message.type == MessageType.TEXT || message.type == MessageType.SYSTEM) {
                        Text(
                            text = message.content,
                            fontSize = 16.sp,
                            color = textColor
                        )
                    } else {
                        Text(
                            text = "[${message.type.name}]",
                            fontSize = 16.sp,
                            color = textColor.copy(alpha = 0.7f),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }

            androidx.compose.foundation.layout.Row(
                horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = time,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                if (isOwn) {
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(4.dp))
                    androidx.compose.material3.Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        if (isOwn) {
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@Composable
fun MessageInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.message),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                singleLine = true,
                colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank()
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Send,
                    contentDescription = stringResource(R.string.send),
                    tint = if (text.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
}