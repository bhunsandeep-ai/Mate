package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockDataProvider
import com.example.model.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    conversation: Conversation,
    onBackClick: () -> Unit,
    onStartVoiceCall: (User) -> Unit,
    onStartVideoCall: (User) -> Unit,
    currentLanguage: String = "en",
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClick() }

    var messages by remember {
        mutableStateOf(MockDataProvider.getConversationMessages(conversation.id))
    }
    var inputText by remember { mutableStateOf("") }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var showEmojiPicker by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ForestGreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(conversation.participant.avatarColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = conversation.participant.name.take(1),
                                    color = SoftCream,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (conversation.participant.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(OnlineIndicator)
                                        .border(1.5.dp, ForestGreenBg, CircleShape)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = conversation.participant.name,
                                color = SoftCream,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (conversation.participant.isOnline) "Online" else conversation.participant.lastSeenText,
                                color = if (conversation.participant.isOnline) MintAccentLight else SoftCreamFaint,
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("conversation_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = SoftCream
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onStartVoiceCall(conversation.participant) },
                        modifier = Modifier.testTag("chat_voice_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Voice Call",
                            tint = MintAccentLight
                        )
                    }
                    IconButton(
                        onClick = { onStartVideoCall(conversation.participant) },
                        modifier = Modifier.testTag("chat_video_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video Call",
                            tint = MintAccentLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreenSurface,
                    navigationIconContentColor = SoftCream,
                    titleContentColor = SoftCream,
                    actionIconContentColor = MintAccentLight
                )
            )
        },
        bottomBar = {
            // Message Composer
            Surface(
                color = ForestGreenSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                Column {
                    if (showEmojiPicker) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("😊", "❤️", "👍", "🙏", "🔥", "😂", "☕", "🎉").forEach { emoji ->
                                Text(
                                    text = emoji,
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .clickable {
                                            inputText += emoji
                                        }
                                        .padding(4.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showEmojiPicker = !showEmojiPicker },
                            modifier = Modifier.testTag("emoji_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (showEmojiPicker) Icons.Default.Keyboard else Icons.Default.SentimentSatisfiedAlt,
                                contentDescription = "Emoji picker",
                                tint = MintAccentLight
                            )
                        }

                        IconButton(
                            onClick = { showAttachmentSheet = true },
                            modifier = Modifier.testTag("attachment_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = "Attach file",
                                tint = SoftCreamMuted
                            )
                        }

                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text(
                                    text = if (currentLanguage == "bn") "মেসেজ লিখুন..." else "Message (disappears in 24h)...",
                                    color = SoftCreamFaint,
                                    fontSize = 14.sp
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = ForestGreenSurfaceVariant,
                                unfocusedContainerColor = ForestGreenSurfaceVariant,
                                focusedTextColor = SoftCream,
                                unfocusedTextColor = SoftCream,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 44.dp, max = 100.dp)
                                .testTag("message_input_field")
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        if (inputText.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    val now = System.currentTimeMillis()
                                    val newMsg = Message(
                                        id = "msg_${System.currentTimeMillis()}",
                                        senderId = "me",
                                        conversationId = conversation.id,
                                        text = inputText.trim(),
                                        createdAtEpochMs = now,
                                        expiresAtEpochMs = now + (24 * 60 * 60 * 1000L),
                                        status = MessageStatus.SENT
                                    )
                                    messages = messages + newMsg
                                    inputText = ""
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(MintAccent)
                                    .testTag("send_message_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    tint = ForestGreenBg,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    // Voice note simulation
                                    val now = System.currentTimeMillis()
                                    val voiceMsg = Message(
                                        id = "msg_${System.currentTimeMillis()}",
                                        senderId = "me",
                                        conversationId = conversation.id,
                                        type = MessageType.VOICE,
                                        text = "Voice note (0:15)",
                                        mediaDurationSec = 15,
                                        createdAtEpochMs = now,
                                        expiresAtEpochMs = now + (24 * 60 * 60 * 1000L),
                                        status = MessageStatus.SENT
                                    )
                                    messages = messages + voiceMsg
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreenSurfaceVariant)
                                    .testTag("voice_record_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice note",
                                    tint = MintAccentLight,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Prominent 24-Hour Expiration Banner
            Surface(
                color = ExpiringTimerBg,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LockClock,
                        contentDescription = "Disappearing timer",
                        tint = ExpiringTimerRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentLanguage == "bn")
                            "এই চ্যাটের সব মেসেজ পাঠানোর ২৪ ঘণ্টা পর অদৃশ্য হয়ে যাবে।"
                        else
                            "Messages in this chat automatically disappear 24 hours after being sent.",
                        color = SoftCream,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { message ->
                    val isMe = message.senderId == "me"
                    ChatBubbleItem(message = message, isMe = isMe)
                }
            }
        }
    }

    if (showAttachmentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAttachmentSheet = false },
            containerColor = ForestGreenSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Share Content",
                    color = SoftCream,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AttachmentOptionItem(icon = Icons.Default.Image, label = "Gallery", color = Color(0xFF3B82F6)) {
                        showAttachmentSheet = false
                    }
                    AttachmentOptionItem(icon = Icons.Default.CameraAlt, label = "Camera", color = Color(0xFF10B981)) {
                        showAttachmentSheet = false
                    }
                    AttachmentOptionItem(icon = Icons.Default.Description, label = "Document", color = Color(0xFFF59E0B)) {
                        showAttachmentSheet = false
                    }
                    AttachmentOptionItem(icon = Icons.Default.LocationOn, label = "Location", color = Color(0xFFEF4444)) {
                        showAttachmentSheet = false
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AttachmentOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(1.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, color = SoftCreamMuted, fontSize = 12.sp)
    }
}

@Composable
private fun ChatBubbleItem(message: Message, isMe: Boolean) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(message.createdAtEpochMs))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isMe) ForestGreenSurfaceVariant else ForestGreenSurface
                )
                .border(
                    1.dp,
                    if (isMe) MintAccent.copy(alpha = 0.3f) else ForestGreenBorder,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                if (message.type == MessageType.VOICE) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        var isPlaying by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MintAccent)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play voice message",
                                tint = ForestGreenBg,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Simulated audio waveform bars
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf(6, 12, 18, 14, 8, 20, 16, 10, 14, 8, 12).forEach { heightDp ->
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(heightDp.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(MintAccentLight)
                                )
                            }
                        }

                        Text(
                            text = "${message.mediaDurationSec ?: 20}s",
                            color = SoftCreamMuted,
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Text(
                        text = message.text ?: "",
                        color = SoftCream,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Bottom row: Remaining 24h tag + Time + Status check
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⏳ ${message.remainingHours}h left",
                        color = ExpiringTimerRed,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = formattedTime,
                        color = SoftCreamFaint,
                        fontSize = 10.sp
                    )

                    if (isMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = when (message.status) {
                                MessageStatus.READ -> Icons.Default.DoneAll
                                MessageStatus.DELIVERED -> Icons.Default.DoneAll
                                else -> Icons.Default.Done
                            },
                            contentDescription = null,
                            tint = if (message.status == MessageStatus.READ) MintAccentLight else SoftCreamFaint,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }

        // Reactions display
        if (message.reactions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .offset(y = (-6).dp)
                    .padding(horizontal = 6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ForestGreenSurface,
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestGreenBorder))
                ) {
                    Text(
                        text = message.reactions.values.joinToString(" "),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
