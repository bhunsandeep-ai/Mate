package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockDataProvider
import com.example.model.Advertisement
import com.example.model.Conversation
import com.example.model.MessageStatus
import com.example.model.MessageType
import com.example.ui.components.SponsoredChatCard
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatListScreen(
    conversations: List<Conversation>,
    searchQuery: String,
    onConversationClick: (Conversation) -> Unit,
    onChatWithBusiness: (Advertisement) -> Unit,
    currentLanguage: String = "en",
    modifier: Modifier = Modifier
) {
    val filteredConversations = if (searchQuery.isBlank()) {
        conversations
    } else {
        conversations.filter {
            it.participant.name.contains(searchQuery, ignoreCase = true) ||
            (it.lastMessage.text?.contains(searchQuery, ignoreCase = true) == true)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        itemsIndexed(filteredConversations) { index, conversation ->
            ChatRowItem(
                conversation = conversation,
                onClick = { onConversationClick(conversation) }
            )

            // Inject one elegant sponsored card after conversation #2 (non-intrusive)
            if (index == 1 && searchQuery.isBlank()) {
                SponsoredChatCard(
                    ad = MockDataProvider.sampleSponsoredAd,
                    onChatWithBusiness = onChatWithBusiness
                )
            }

            if (index < filteredConversations.size - 1) {
                Divider(
                    color = ForestGreenBorder.copy(alpha = 0.4f),
                    thickness = 0.6.dp,
                    modifier = Modifier.padding(start = 78.dp, end = 16.dp)
                )
            }
        }

        if (filteredConversations.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentLanguage == "bn") "কোনো চ্যাট পাওয়া যায়নি" else "No conversations found",
                        color = SoftCreamMuted,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ChatRowItem(
    conversation: Conversation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val participant = conversation.participant
    val lastMsg = conversation.lastMessage
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(lastMsg.createdAtEpochMs))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("chat_row_${conversation.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with Online indicator
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(participant.avatarColorHex)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = participant.name.take(1),
                    color = SoftCream,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (participant.isOnline) {
                Box(
                    modifier = Modifier
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(OnlineIndicator)
                        .border(2.dp, ForestGreenBg, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Name + Last Message Preview
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = participant.name,
                    color = SoftCream,
                    fontSize = 16.sp,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formattedTime,
                    color = if (conversation.unreadCount > 0) MintAccentLight else SoftCreamFaint,
                    fontSize = 11.sp,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sent/Delivered/Read indicator if user sent it
                if (lastMsg.senderId == "me") {
                    Icon(
                        imageVector = if (lastMsg.status == MessageStatus.READ) Icons.Default.DoneAll else Icons.Default.Done,
                        contentDescription = "Message status",
                        tint = if (lastMsg.status == MessageStatus.READ) MintAccentLight else SoftCreamFaint,
                        modifier = Modifier
                            .size(15.dp)
                            .padding(end = 4.dp)
                    )
                } else if (lastMsg.type == MessageType.VOICE) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice note",
                        tint = MintAccentLight,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(end = 4.dp)
                    )
                }

                Text(
                    text = lastMsg.text ?: "Shared media",
                    color = if (conversation.unreadCount > 0) SoftCream else SoftCreamMuted,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )

                // 24h Expiration badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ExpiringTimerBg,
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Disappearing",
                            tint = ExpiringTimerRed,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "24h",
                            color = ExpiringTimerRed,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Unread Badge
                if (conversation.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MintAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.unreadCount.toString(),
                            color = ForestGreenBg,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
