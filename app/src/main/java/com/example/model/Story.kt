package com.example.model

data class StoryItem(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatarUrl: String? = null,
    val avatarColorHex: Long = 0xFF10B981,
    val isSponsored: Boolean = false,
    val sponsorBusinessName: String? = null,
    val mediaType: StoryMediaType = StoryMediaType.IMAGE,
    val mediaUrl: String? = null,
    val textContent: String? = null,
    val backgroundGradientHexes: List<Long> = listOf(0xFF0F392B, 0xFF1E5E47),
    val ctaText: String? = null,
    val ctaLink: String? = null,
    val createdAtEpochMs: Long = System.currentTimeMillis(),
    val expiresAtEpochMs: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000L),
    val isSeen: Boolean = false,
    val viewersCount: Int = 0,
    val viewers: List<User> = emptyList()
)

enum class StoryMediaType {
    TEXT,
    IMAGE,
    VIDEO
}
