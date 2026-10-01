package com.example.model

enum class CallMedium {
    VOICE,
    VIDEO
}

enum class CallDirection {
    INCOMING,
    OUTGOING,
    MISSED
}

data class CallRecord(
    val id: String,
    val contact: User,
    val medium: CallMedium,
    val direction: CallDirection,
    val timestamp: String,
    val durationText: String? = null,
    val createdAtEpochMs: Long = System.currentTimeMillis()
)
