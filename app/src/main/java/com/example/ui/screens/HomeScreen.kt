package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.MockDataProvider
import com.example.model.*
import com.example.ui.components.FloatingBottomNav
import com.example.ui.components.NavTab
import com.example.ui.components.NexTopBar
import com.example.ui.components.StoryTray
import com.example.ui.theme.ForestGreenBg

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(NavTab.CHAT) }
    var currentLanguage by remember { mutableStateOf("en") }
    var searchQuery by remember { mutableStateOf("") }

    // Data states
    var conversations by remember { mutableStateOf(MockDataProvider.getInitialConversations()) }
    var stories by remember { mutableStateOf(MockDataProvider.getInitialStories()) }
    var callRecords by remember { mutableStateOf(MockDataProvider.getCallHistory()) }
    var advertisements by remember { mutableStateOf(MockDataProvider.getMyAdvertisements()) }

    // Screen navigation states
    var activeConversation by remember { mutableStateOf<Conversation?>(null) }
    var activeCallUser by remember { mutableStateOf<Pair<User, CallMedium>?>(null) }
    var viewingStoryIndex by remember { mutableStateOf<Int?>(null) }
    var showCreateStoryDialog by remember { mutableStateOf(false) }
    var showCreateAdDialog by remember { mutableStateOf(false) }
    var showProfileScreen by remember { mutableStateOf(false) }
    var showPrivacyPolicyScreen by remember { mutableStateOf(false) }

    when {
        // Active Call Overlay (Voice / Video)
        activeCallUser != null -> {
            val (user, medium) = activeCallUser!!
            ActiveCallScreen(
                user = user,
                medium = medium,
                onEndCall = { activeCallUser = null }
            )
        }

        // Active Story Viewer Overlay
        viewingStoryIndex != null -> {
            StoryViewerScreen(
                stories = stories,
                initialIndex = viewingStoryIndex!!,
                onClose = { viewingStoryIndex = null },
                onStoryChatCTA = { story ->
                    viewingStoryIndex = null
                    val existing = conversations.firstOrNull { it.participant.id == story.userId }
                    if (existing != null) {
                        activeConversation = existing
                    } else {
                        // Create conversation with business
                        val newConv = Conversation(
                            id = "conv_${story.userId}",
                            participant = User(
                                id = story.userId,
                                name = story.sponsorBusinessName ?: story.userName,
                                phoneNumber = "+91 98300 98765",
                                avatarColorHex = story.avatarColorHex,
                                isVerifiedBusiness = true
                            ),
                            lastMessage = Message(
                                id = "intro_msg",
                                senderId = story.userId,
                                conversationId = "conv_${story.userId}",
                                text = "Namaskar! How can we assist you with our products today?",
                                createdAtEpochMs = System.currentTimeMillis(),
                                expiresAtEpochMs = System.currentTimeMillis() + (24 * 60 * 60 * 1000L),
                                status = MessageStatus.READ
                            )
                        )
                        conversations = listOf(newConv) + conversations
                        activeConversation = newConv
                    }
                },
                currentLanguage = currentLanguage
            )
        }

        // Privacy Policy Screen
        showPrivacyPolicyScreen -> {
            PrivacyPolicyScreen(
                onBackClick = { showPrivacyPolicyScreen = false }
            )
        }

        // Profile Screen
        showProfileScreen -> {
            ProfileScreen(
                currentLanguage = currentLanguage,
                onLanguageChange = { currentLanguage = it },
                onViewPrivacyPolicy = { showPrivacyPolicyScreen = true },
                onBackClick = { showProfileScreen = false }
            )
        }

        // Active Chat Conversation Screen
        activeConversation != null -> {
            ConversationScreen(
                conversation = activeConversation!!,
                onBackClick = { activeConversation = null },
                onStartVoiceCall = { user -> activeCallUser = Pair(user, CallMedium.VOICE) },
                onStartVideoCall = { user -> activeCallUser = Pair(user, CallMedium.VIDEO) },
                currentLanguage = currentLanguage
            )
        }

        // Main Home Screen Shell
        else -> {
            Scaffold(
                modifier = modifier
                    .fillMaxSize()
                    .background(ForestGreenBg),
                containerColor = ForestGreenBg,
                topBar = {
                    NexTopBar(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onProfileClick = { showProfileScreen = true },
                        currentLanguage = currentLanguage,
                        onLanguageToggle = {
                            currentLanguage = if (currentLanguage == "en") "bn" else "en"
                        }
                    )
                },
                bottomBar = {
                    FloatingBottomNav(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                        currentLanguage = currentLanguage,
                        unreadChatCount = conversations.sumOf { it.unreadCount }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Top Story Tray (Always visible in Chat tab for instant access)
                    if (selectedTab == NavTab.CHAT && searchQuery.isBlank()) {
                        StoryTray(
                            stories = stories,
                            onStoryClick = { clickedStory ->
                                val idx = stories.indexOfFirst { it.id == clickedStory.id }
                                viewingStoryIndex = if (idx >= 0) idx else 0
                            },
                            onAddStoryClick = { showCreateStoryDialog = true },
                            currentLanguage = currentLanguage
                        )
                    }

                    // Selected Tab Content
                    when (selectedTab) {
                        NavTab.CHAT -> {
                            ChatListScreen(
                                conversations = conversations,
                                searchQuery = searchQuery,
                                onConversationClick = { conv ->
                                    activeConversation = conv
                                },
                                onChatWithBusiness = { ad ->
                                    val bizUser = User(
                                        id = "biz_${ad.id}",
                                        name = ad.businessName,
                                        phoneNumber = ad.phone,
                                        avatarColorHex = 0xFFD97706,
                                        isVerifiedBusiness = true
                                    )
                                    val conv = Conversation(
                                        id = "conv_biz_${ad.id}",
                                        participant = bizUser,
                                        lastMessage = Message(
                                            id = "biz_welcome",
                                            senderId = bizUser.id,
                                            conversationId = "conv_biz_${ad.id}",
                                            text = "Welcome to ${ad.businessName}! Enquiring about: ${ad.title}?",
                                            createdAtEpochMs = System.currentTimeMillis(),
                                            expiresAtEpochMs = System.currentTimeMillis() + (24 * 60 * 60 * 1000L),
                                            status = MessageStatus.READ
                                        )
                                    )
                                    conversations = listOf(conv) + conversations.filter { it.id != conv.id }
                                    activeConversation = conv
                                },
                                currentLanguage = currentLanguage
                            )
                        }

                        NavTab.CALL -> {
                            CallsScreen(
                                callRecords = callRecords,
                                onStartCall = { user ->
                                    activeCallUser = Pair(user, CallMedium.VOICE)
                                },
                                currentLanguage = currentLanguage
                            )
                        }

                        NavTab.VIDEO_CALL -> {
                            VideoCallsScreen(
                                callRecords = callRecords,
                                onStartVideoCall = { user ->
                                    activeCallUser = Pair(user, CallMedium.VIDEO)
                                },
                                currentLanguage = currentLanguage
                            )
                        }

                        NavTab.ADVERTISE -> {
                            AdvertiseScreen(
                                ads = advertisements,
                                onCreateAdClick = { showCreateAdDialog = true },
                                currentLanguage = currentLanguage
                            )
                        }
                    }
                }
            }
        }
    }

    // Create Story Dialog
    if (showCreateStoryDialog) {
        CreateStoryDialog(
            onDismiss = { showCreateStoryDialog = false },
            onPublishStory = { newStory ->
                stories = listOf(newStory) + stories.filter { it.userId != "me" }
                showCreateStoryDialog = false
            },
            currentLanguage = currentLanguage
        )
    }

    // Create Advertisement Dialog
    if (showCreateAdDialog) {
        CreateAdDialog(
            onDismiss = { showCreateAdDialog = false },
            onSubmitAd = { newAd ->
                advertisements = listOf(newAd) + advertisements
                showCreateAdDialog = false
            },
            currentLanguage = currentLanguage
        )
    }
}
