package com.meshlink.app.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("chatId"), Index("senderId"), Index("timestamp")]
)
@Serializable
data class Message(
    @PrimaryKey
    val id: String, // UUID
    val chatId: String,
    val senderId: String?,
    val content: String, // Encrypted content
    val type: MessageType = MessageType.TEXT,
    val timestamp: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null, // For ephemeral messages
    val status: MessageStatus = MessageStatus.SENDING,
    val replyToId: String? = null, // For replies
    val hopCount: Int = 0, // Mesh network hop count
    val metadata: String = "{}" // JSON for extensibility (attachments, etc.)
)

enum class MessageType {
    TEXT,
    IMAGE,
    FILE,
    VOICE,
    SYSTEM
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED,
    EXPIRED
}