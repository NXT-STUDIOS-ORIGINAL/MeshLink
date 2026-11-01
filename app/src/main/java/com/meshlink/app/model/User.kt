package com.meshlink.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "users")
@Serializable
data class User(
    @PrimaryKey
    val id: String, // Public key hash / device ID
    val displayName: String,
    val publicKey: String, // Base64 encoded Ed25519 public key
    val avatarHash: String? = null, // Hash of avatar for caching
    val lastSeen: Long = System.currentTimeMillis(),
    val isLocal: Boolean = false, // True for the local user's own entry
    val status: UserStatus = UserStatus.ONLINE,
    val metadata: String = "{}" // JSON for extensibility
)

enum class UserStatus {
    ONLINE,
    OFFLINE,
    AWAY,
    BUSY
}