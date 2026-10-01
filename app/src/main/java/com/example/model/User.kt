package com.example.model

data class User(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val avatarUrl: String? = null,
    val avatarColorHex: Long = 0xFF10B981,
    val about: String = "Available on NexChat",
    val isOnline: Boolean = false,
    val lastSeenText: String = "Just now",
    val isVerifiedBusiness: Boolean = false,
    val preferredLanguage: String = "en" // "en" or "bn"
)
