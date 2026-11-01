package com.meshlink.app.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String, // Unique device ID
    val displayName: String,
    val publicKey: String, // Base64 encoded Ed25519 public key
    val privateKey: String, // Base64 encoded Ed25519 private key (encrypted at rest)
    val avatarHash: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val version: Int = 1
) {
    companion object {
        fun createDefault(id: String, displayName: String, publicKey: String, privateKey: String): UserProfile {
            return UserProfile(
                id = id,
                displayName = displayName,
                publicKey = publicKey,
                privateKey = privateKey
            )
        }
    }
}