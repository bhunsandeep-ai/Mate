package com.example.data

import com.example.model.*

object MockDataProvider {
    val currentUser = User(
        id = "me",
        name = "Sandeep",
        phoneNumber = "+91 98765 43210",
        avatarColorHex = 0xFF10B981,
        about = "Building clean tech in India 🚀",
        isOnline = true,
        preferredLanguage = "en"
    )

    val userPriya = User(
        id = "user_priya",
        name = "Priya Sharma",
        phoneNumber = "+91 98310 11223",
        avatarColorHex = 0xFFEC4899,
        about = "Design & photography enthusiast ✨",
        isOnline = true,
        lastSeenText = "Online"
    )

    val userRahul = User(
        id = "user_rahul",
        name = "Rahul Mukherjee",
        phoneNumber = "+91 98320 22334",
        avatarColorHex = 0xFF3B82F6,
        about = "আজকের দিনটি সুন্দর হোক!",
        isOnline = false,
        lastSeenText = "Today at 09:20 AM"
    )

    val userSadia = User(
        id = "user_sadia",
        name = "Sadia Rahman",
        phoneNumber = "+91 98330 33445",
        avatarColorHex = 0xFF8B5CF6,
        about = "Book lover | Tea addict ☕",
        isOnline = false,
        lastSeenText = "Today at 08:50 AM"
    )

    val userAmit = User(
        id = "user_amit",
        name = "Amit Sen",
        phoneNumber = "+91 98340 44556",
        avatarColorHex = 0xFFF59E0B,
        about = "Work in progress 🛠️",
        isOnline = true,
        lastSeenText = "Online"
    )

    val businessMaaKali = User(
        id = "biz_maakali",
        name = "Maa Kali Bastralaya",
        phoneNumber = "+91 98300 98765",
        avatarColorHex = 0xFFD97706,
        about = "Wholesale & Retail Traditional Sarees, Gariahat, Kolkata",
        isVerifiedBusiness = true
    )

    fun getInitialConversations(): List<Conversation> {
        val now = System.currentTimeMillis()
        val h24 = 24 * 60 * 60 * 1000L

        return listOf(
            Conversation(
                id = "conv_priya",
                participant = userPriya,
                lastMessage = Message(
                    id = "msg_p1",
                    senderId = userPriya.id,
                    conversationId = "conv_priya",
                    type = MessageType.TEXT,
                    text = "Hi! কেমন আছো? Project presentation ready?",
                    createdAtEpochMs = now - (15 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (15 * 60 * 1000),
                    status = MessageStatus.READ
                ),
                unreadCount = 2,
                isPinned = true
            ),
            Conversation(
                id = "conv_rahul",
                participant = userRahul,
                lastMessage = Message(
                    id = "msg_r1",
                    senderId = userRahul.id,
                    conversationId = "conv_rahul",
                    type = MessageType.TEXT,
                    text = "আজ বিকেলে দেখা হবে তো? Coffee at College Street?",
                    createdAtEpochMs = now - (45 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (45 * 60 * 1000),
                    status = MessageStatus.DELIVERED
                ),
                unreadCount = 1
            ),
            Conversation(
                id = "conv_sadia",
                participant = userSadia,
                lastMessage = Message(
                    id = "msg_s1",
                    senderId = userSadia.id,
                    conversationId = "conv_sadia",
                    type = MessageType.TEXT,
                    text = "ঠিক আছে 😊 See you soon!",
                    createdAtEpochMs = now - (90 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (90 * 60 * 1000),
                    status = MessageStatus.READ
                ),
                unreadCount = 0
            ),
            Conversation(
                id = "conv_amit",
                participant = userAmit,
                lastMessage = Message(
                    id = "msg_a1",
                    senderId = "me",
                    conversationId = "conv_amit",
                    type = MessageType.VOICE,
                    text = "Voice message (0:28)",
                    mediaDurationSec = 28,
                    createdAtEpochMs = now - (180 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (180 * 60 * 1000),
                    status = MessageStatus.READ
                ),
                unreadCount = 0
            )
        )
    }

    fun getConversationMessages(conversationId: String): List<Message> {
        val now = System.currentTimeMillis()
        val h24 = 24 * 60 * 60 * 1000L

        return when (conversationId) {
            "conv_priya" -> listOf(
                Message(
                    id = "m1",
                    senderId = userPriya.id,
                    conversationId = "conv_priya",
                    text = "Namaskar Sandeep! Did you check the new wireframes?",
                    createdAtEpochMs = now - (120 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (120 * 60 * 1000),
                    status = MessageStatus.READ
                ),
                Message(
                    id = "m2",
                    senderId = "me",
                    conversationId = "conv_priya",
                    text = "Yes Priya, the deep green theme and mint accents look stunning! Very original.",
                    createdAtEpochMs = now - (110 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (110 * 60 * 1000),
                    status = MessageStatus.READ
                ),
                Message(
                    id = "m3",
                    senderId = userPriya.id,
                    conversationId = "conv_priya",
                    type = MessageType.VOICE,
                    text = "Voice note (0:34)",
                    mediaDurationSec = 34,
                    createdAtEpochMs = now - (35 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (35 * 60 * 1000),
                    status = MessageStatus.READ
                ),
                Message(
                    id = "m4",
                    senderId = userPriya.id,
                    conversationId = "conv_priya",
                    text = "Hi! কেমন আছো? Project presentation ready?",
                    createdAtEpochMs = now - (15 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (15 * 60 * 1000),
                    status = MessageStatus.READ,
                    reactions = mapOf("me" to "👍")
                )
            )
            "conv_rahul" -> listOf(
                Message(
                    id = "mr1",
                    senderId = "me",
                    conversationId = "conv_rahul",
                    text = "Rahul, what time are you coming over?",
                    createdAtEpochMs = now - (180 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (180 * 60 * 1000),
                    status = MessageStatus.READ
                ),
                Message(
                    id = "mr2",
                    senderId = userRahul.id,
                    conversationId = "conv_rahul",
                    text = "আজ বিকেলে দেখা হবে তো? Coffee at College Street?",
                    createdAtEpochMs = now - (45 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (45 * 60 * 1000),
                    status = MessageStatus.DELIVERED
                )
            )
            else -> listOf(
                Message(
                    id = "def1",
                    senderId = conversationId,
                    conversationId = conversationId,
                    text = "Hello! Remember, all messages automatically disappear after 24 hours.",
                    createdAtEpochMs = now - (60 * 60 * 1000),
                    expiresAtEpochMs = now + h24 - (60 * 60 * 1000),
                    status = MessageStatus.READ
                )
            )
        }
    }

    fun getInitialStories(): List<StoryItem> {
        val now = System.currentTimeMillis()
        val h24 = 24 * 60 * 60 * 1000L

        return listOf(
            // My Story
            StoryItem(
                id = "my_story_1",
                userId = "me",
                userName = "My Story",
                avatarColorHex = 0xFF10B981,
                isSponsored = false,
                mediaType = StoryMediaType.TEXT,
                textContent = "Excited to launch NexChat in India! Private & Disappearing 🌿",
                backgroundGradientHexes = listOf(0xFF0D3323, 0xFF19553C),
                createdAtEpochMs = now - (4 * 60 * 60 * 1000),
                expiresAtEpochMs = now + h24 - (4 * 60 * 60 * 1000),
                isSeen = true,
                viewersCount = 18,
                viewers = listOf(userPriya, userRahul, userSadia, userAmit)
            ),
            // Priya (Unseen)
            StoryItem(
                id = "story_priya",
                userId = userPriya.id,
                userName = "Priya",
                avatarColorHex = 0xFFEC4899,
                isSponsored = false,
                mediaType = StoryMediaType.TEXT,
                textContent = "Morning walk at Victoria Memorial 🏛️ Kolkata weather is wonderful today!",
                backgroundGradientHexes = listOf(0xFF831843, 0xFFBE185D),
                createdAtEpochMs = now - (2 * 60 * 60 * 1000),
                expiresAtEpochMs = now + h24 - (2 * 60 * 60 * 1000),
                isSeen = false
            ),
            // Sponsored Story (Maa Kali Bastralaya)
            StoryItem(
                id = "story_maakali",
                userId = businessMaaKali.id,
                userName = "Maa Kali",
                sponsorBusinessName = "Maa Kali Bastralaya",
                avatarColorHex = 0xFFD97706,
                isSponsored = true,
                mediaType = StoryMediaType.TEXT,
                textContent = "✨ Puja Special Pure Silk & Tant Collection! Direct wholesale prices starting at ₹499. Visit Gariahat store or chat with us.",
                backgroundGradientHexes = listOf(0xFF78350F, 0xFFB45309),
                ctaText = "Chat on NexChat",
                ctaLink = "chat://biz_maakali",
                createdAtEpochMs = now - (5 * 60 * 60 * 1000),
                expiresAtEpochMs = now + h24 - (5 * 60 * 60 * 1000),
                isSeen = false
            ),
            // Rahul (Seen)
            StoryItem(
                id = "story_rahul",
                userId = userRahul.id,
                userName = "Rahul",
                avatarColorHex = 0xFF3B82F6,
                isSponsored = false,
                mediaType = StoryMediaType.TEXT,
                textContent = "Off to Digha beach this weekend! 🌊🏖️",
                backgroundGradientHexes = listOf(0xFF1E3A8A, 0xFF2563EB),
                createdAtEpochMs = now - (10 * 60 * 60 * 1000),
                expiresAtEpochMs = now + h24 - (10 * 60 * 60 * 1000),
                isSeen = true
            ),
            // Sadia (Unseen)
            StoryItem(
                id = "story_sadia",
                userId = userSadia.id,
                userName = "Sadia",
                avatarColorHex = 0xFF8B5CF6,
                isSponsored = false,
                mediaType = StoryMediaType.TEXT,
                textContent = "Reading Rabindranath Tagore's Gitanjali over evening Darjeeling tea 📖☕",
                backgroundGradientHexes = listOf(0xFF4C1D95, 0xFF6D28D9),
                createdAtEpochMs = now - (1 * 60 * 60 * 1000),
                expiresAtEpochMs = now + h24 - (1 * 60 * 60 * 1000),
                isSeen = false
            )
        )
    }

    val sampleSponsoredAd = Advertisement(
        id = "ad_maakali_card",
        businessName = "Maa Kali Bastralaya",
        businessCategory = "Traditional Silk & Sarees",
        title = "Puja Special Handloom Sarees",
        description = "Exclusive discounts up to 30% on Dhakai Jamdani & Baluchari. Located at Gariahat Crossing.",
        ctaText = "View Offer",
        phone = "+91 98300 98765",
        targetCity = "Kolkata, WB",
        budgetInr = 499,
        format = AdFormat.SPONSORED_CHAT_CARD,
        status = AdStatus.ACTIVE,
        impressions = 2140,
        clicks = 265,
        chatsInitiated = 42
    )

    fun getCallHistory(): List<CallRecord> {
        return listOf(
            CallRecord(
                id = "call_1",
                contact = userPriya,
                medium = CallMedium.VOICE,
                direction = CallDirection.INCOMING,
                timestamp = "Today, 09:40 AM",
                durationText = "04:12"
            ),
            CallRecord(
                id = "call_2",
                contact = userRahul,
                medium = CallMedium.VIDEO,
                direction = CallDirection.MISSED,
                timestamp = "Today, 08:15 AM",
                durationText = null
            ),
            CallRecord(
                id = "call_3",
                contact = userSadia,
                medium = CallMedium.VOICE,
                direction = CallDirection.OUTGOING,
                timestamp = "Yesterday, 06:30 PM",
                durationText = "12:45"
            ),
            CallRecord(
                id = "call_4",
                contact = userAmit,
                medium = CallMedium.VIDEO,
                direction = CallDirection.INCOMING,
                timestamp = "Yesterday, 02:10 PM",
                durationText = "08:02"
            )
        )
    }

    fun getMyAdvertisements(): List<Advertisement> {
        return listOf(
            Advertisement(
                id = "ad_1",
                businessName = "Bengal Silk Crafts",
                businessCategory = "Handloom & Apparel",
                title = "Authentic Murshidabad Silk Sarees",
                description = "Pure silk certified handlooms with silk mark. Free delivery across Kolkata.",
                ctaText = "Chat with Shop",
                phone = "+91 98311 55667",
                targetCity = "Kolkata, WB",
                budgetInr = 499,
                durationLabel = "Weekly Package (₹499)",
                format = AdFormat.SPONSORED_CHAT_CARD,
                status = AdStatus.ACTIVE,
                impressions = 3280,
                clicks = 412,
                chatsInitiated = 68
            ),
            Advertisement(
                id = "ad_2",
                businessName = "Bengal Sweets & Cafe",
                businessCategory = "Food & Dining",
                title = "Fresh Nolen Gur Sandesh",
                description = "Winter festival special Bengali sweets made with authentic jaggery.",
                ctaText = "Order on Call",
                phone = "+91 98322 77889",
                targetCity = "Kolkata, Salt Lake",
                budgetInr = 99,
                durationLabel = "Daily Package (₹99)",
                format = AdFormat.SPONSORED_STORY,
                status = AdStatus.PENDING,
                impressions = 0,
                clicks = 0,
                chatsInitiated = 0
            ),
            Advertisement(
                id = "ad_3",
                businessName = "Kolkata Tech Hub",
                businessCategory = "Electronics Repair",
                title = "Same-Day Smartphone Repair",
                description = "Certified technicians for all major smartphone brands at Sector V.",
                ctaText = "Call Now",
                phone = "+91 98333 99001",
                targetCity = "Kolkata, WB",
                budgetInr = 999,
                durationLabel = "Monthly Package (₹999)",
                format = AdFormat.SPONSORED_CHAT_CARD,
                status = AdStatus.EXPIRED,
                impressions = 8940,
                clicks = 1120,
                chatsInitiated = 195
            )
        )
    }
}
