package com.example.model

enum class AdStatus {
    ACTIVE,
    PENDING,
    REJECTED,
    EXPIRED
}

enum class AdFormat {
    SPONSORED_CHAT_CARD,
    SPONSORED_STORY
}

data class Advertisement(
    val id: String,
    val businessName: String,
    val businessCategory: String,
    val title: String,
    val description: String,
    val ctaText: String = "View Offer",
    val phone: String = "+91 98300 12345",
    val website: String? = null,
    val targetCity: String = "Kolkata, WB",
    val budgetInr: Int = 499,
    val durationLabel: String = "7 Days Package",
    val format: AdFormat = AdFormat.SPONSORED_CHAT_CARD,
    val status: AdStatus = AdStatus.ACTIVE,
    val impressions: Int = 1420,
    val clicks: Int = 188,
    val chatsInitiated: Int = 34,
    val createdAtEpochMs: Long = System.currentTimeMillis()
)
