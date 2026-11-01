package com.meshlink.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "chats")
@Serializable
data class Chat(
    @PrimaryKey
    val id: String, // Unique chat ID
    val type: ChatType,
    val name: String? = null, // For groups and rooms
    val avatarHash: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val createdBy: String, // User ID of creator
    val isEncrypted: Boolean = true,
    val metadata: String = "{}" // JSON for extensibility
)

enum class ChatType {
    PRIVATE,    // 1-to-1 private chat
    GROUP,      // Private group chat
    PUBLIC_ROOM // Public chatroom (nearby)
}