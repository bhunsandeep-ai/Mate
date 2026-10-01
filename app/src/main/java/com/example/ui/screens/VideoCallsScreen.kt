package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun VideoCallsScreen(
    callRecords: List<CallRecord>,
    onStartVideoCall: (User) -> Unit,
    currentLanguage: String = "en",
    modifier: Modifier = Modifier
) {
    val videoRecords = callRecords.filter { it.medium == CallMedium.VIDEO }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ForestGreenBg,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (videoRecords.isNotEmpty()) onStartVideoCall(videoRecords.first().contact)
                },
                containerColor = MintAccent,
                contentColor = ForestGreenBg,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .testTag("floating_new_videocall_button")
            ) {
                Icon(
                    imageVector = Icons.Default.VideoCall,
                    contentDescription = "New Video Call"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
        ) {
            item {
                Text(
                    text = if (currentLanguage == "bn") "ভিডিও কলের ইতিহাস" else "Video Call History",
                    color = SoftCreamMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(videoRecords) { call ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStartVideoCall(call.contact) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .testTag("videocall_record_${call.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(call.contact.avatarColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = call.contact.name.take(1),
                            color = SoftCream,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = call.contact.name,
                            color = if (call.direction == CallDirection.MISSED) ExpiringTimerRed else SoftCream,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (call.direction) {
                                    CallDirection.INCOMING -> Icons.AutoMirrored.Filled.CallReceived
                                    CallDirection.OUTGOING -> Icons.AutoMirrored.Filled.CallMade
                                    CallDirection.MISSED -> Icons.AutoMirrored.Filled.CallMissed
                                },
                                contentDescription = null,
                                tint = if (call.direction == CallDirection.MISSED) ExpiringTimerRed else MintAccentLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = call.timestamp + (call.durationText?.let { " • $it" } ?: ""),
                                color = SoftCreamMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = { onStartVideoCall(call.contact) },
                        modifier = Modifier.testTag("videocall_action_${call.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video Call",
                            tint = MintAccentLight
                        )
                    }
                }

                Divider(
                    color = ForestGreenBorder.copy(alpha = 0.4f),
                    thickness = 0.6.dp,
                    modifier = Modifier.padding(start = 80.dp, end = 16.dp)
                )
            }
        }
    }
}
