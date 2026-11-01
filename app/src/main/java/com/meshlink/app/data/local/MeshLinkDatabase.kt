package com.meshlink.app.data.local

import androidx.room.*
import com.meshlink.app.model.Chat
import com.meshlink.app.model.ChatMember
import com.meshlink.app.model.Message
import com.meshlink.app.model.User

@Database(
    entities = [
        User::class,
        Chat::class,
        ChatMember::class,
        Message::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MeshLinkDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun chatMemberDao(): ChatMemberDao
    abstract fun messageDao(): MessageDao
}