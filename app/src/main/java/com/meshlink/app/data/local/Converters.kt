package com.meshlink.app.data.local

import androidx.room.TypeConverter
import com.meshlink.app.model.ChatType
import com.meshlink.app.model.MemberRole
import com.meshlink.app.model.MessageStatus
import com.meshlink.app.model.MessageType
import com.meshlink.app.model.UserStatus

class Converters {

    @TypeConverter
    fun fromUserStatus(status: UserStatus?): String? = status?.name

    @TypeConverter
    fun toUserStatus(name: String?): UserStatus? = name?.let { UserStatus.valueOf(it) }

    @TypeConverter
    fun fromChatType(type: ChatType?): String? = type?.name

    @TypeConverter
    fun toChatType(name: String?): ChatType? = name?.let { ChatType.valueOf(it) }

    @TypeConverter
    fun fromMemberRole(role: MemberRole?): String? = role?.name

    @TypeConverter
    fun toMemberRole(name: String?): MemberRole? = name?.let { MemberRole.valueOf(it) }

    @TypeConverter
    fun fromMessageType(type: MessageType?): String? = type?.name

    @TypeConverter
    fun toMessageType(name: String?): MessageType? = name?.let { MessageType.valueOf(it) }

    @TypeConverter
    fun fromMessageStatus(status: MessageStatus?): String? = status?.name

    @TypeConverter
    fun toMessageStatus(name: String?): MessageStatus? = name?.let { MessageStatus.valueOf(it) }
}