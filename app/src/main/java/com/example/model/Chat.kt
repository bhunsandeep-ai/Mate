package com.example.model

enum class MessageType {
    TEXT,
    IMAGE,
    VOICE,
    DOCUMENT
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    EXPIRED
}

data class Message(
    val id: String,
    val senderId: String,
    val conversationId: String,
    val type: MessageType = MessageType.TEXT,
    val text: String? = null,
    val mediaUrl: String? = null,
    val mediaDurationSec: Int? = null,
    val createdAtEpochMs: Long = System.currentTimeMillis(),
    val expiresAtEpochMs: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000L), // 24 hours
    val status: MessageStatus = MessageStatus.SENT,
    val reactions: Map<String, String> = emptyMap(), // userId -> emoji
    val replyToText: String? = null
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() >= expiresAtEpochMs

    val remainingHours: Int
        get() {
            val remainingMs = expiresAtEpochMs - System.currentTimeMillis()
            return if (remainingMs > 0) (remainingMs / (1000 * 60 * 60)).toInt() else 0
        }
}

data class Conversation(
    val id: String,
    val participant: User,
    val lastMessage: Message,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false
)
