package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StoryItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryViewerScreen(
    stories: List<StoryItem>,
    initialIndex: Int = 0,
    onClose: () -> Unit,
    onStoryChatCTA: (StoryItem) -> Unit,
    currentLanguage: String = "en",
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    var currentIndex by remember { mutableIntStateOf(initialIndex.coerceIn(0, stories.size - 1)) }
    val currentStory = stories.getOrNull(currentIndex) ?: stories.first()
    var isPaused by remember { mutableStateOf(false) }
    var showViewersSheet by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(currentIndex, isPaused) {
        if (!isPaused) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
            )
            // Auto advance
            if (currentIndex < stories.size - 1) {
                currentIndex += 1
            } else {
                onClose()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    currentStory.backgroundGradientHexes.map { Color(it) }
                )
            )
            .pointerInput(currentIndex) {
                detectTapGestures(
                    onPress = {
                        isPaused = true
                        tryAwaitRelease()
                        isPaused = false
                    },
                    onTap = { offset ->
                        val screenWidth = size.width
                        if (offset.x < screenWidth * 0.35f) {
                            // Previous
                            if (currentIndex > 0) currentIndex -= 1
                        } else {
                            // Next
                            if (currentIndex < stories.size - 1) currentIndex += 1 else onClose()
                        }
                    }
                )
            }
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("story_viewer_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Segmented Progress Bars
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                stories.forEachIndexed { idx, _ ->
                    val segmentProgress = when {
                        idx < currentIndex -> 1f
                        idx == currentIndex -> progress.value
                        else -> 0f
                    }

                    LinearProgressIndicator(
                        progress = { segmentProgress },
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = if (currentStory.isSponsored) AdGold else MintAccentLight,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )
                }
            }

            // Top Header: User avatar, Name, Sponsored badge, Close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(currentStory.avatarColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentStory.userName.take(1),
                            color = SoftCream,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentStory.userName,
                                color = SoftCream,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (currentStory.isSponsored) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = AdGold
                                ) {
                                    Text(
                                        text = "SPONSORED",
                                        color = ForestGreenBg,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "⏳ Expires in 24h",
                            color = SoftCream.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_story_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close story",
                        tint = SoftCream
                    )
                }
            }

            // Story Main Content Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentStory.textContent ?: "Shared Media",
                    color = SoftCream,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
            }

            // Bottom CTA or Reply bar
            if (currentStory.isSponsored) {
                // Sponsored Call To Action
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { onStoryChatCTA(currentStory) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintAccent,
                            contentColor = ForestGreenBg
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("story_sponsored_cta_button")
                    ) {
                        Icon(imageVector = Icons.Default.Chat, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentStory.ctaText ?: "Chat on NexChat",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else if (currentStory.userId == "me") {
                // Own story: Viewers button
                Surface(
                    color = ForestGreenBg.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isPaused = true
                            showViewersSheet = true
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Viewers",
                            tint = MintAccentLight
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${currentStory.viewersCount} Viewers (Tap to see list)",
                            color = SoftCream,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                // Reply Bar for friend's story
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = replyText,
                        onValueChange = {
                            replyText = it
                            isPaused = it.isNotEmpty()
                        },
                        placeholder = {
                            Text(
                                text = if (currentLanguage == "bn") "উত্তর দিন..." else "Reply to story...",
                                color = SoftCream.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = ForestGreenBg.copy(alpha = 0.6f),
                            unfocusedContainerColor = ForestGreenBg.copy(alpha = 0.5f),
                            focusedTextColor = SoftCream,
                            unfocusedTextColor = SoftCream,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (replyText.isNotBlank()) {
                                replyText = ""
                                isPaused = false
                                onClose()
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MintAccent)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send reply",
                            tint = ForestGreenBg
                        )
                    }
                }
            }
        }
    }

    if (showViewersSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showViewersSheet = false
                isPaused = false
            },
            containerColor = ForestGreenSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Story Viewers (${currentStory.viewers.size})",
                    color = SoftCream,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))

                currentStory.viewers.forEach { viewer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(viewer.avatarColorHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = viewer.name.take(1), color = SoftCream, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = viewer.name, color = SoftCream, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Viewed 2h ago", color = SoftCreamFaint, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
