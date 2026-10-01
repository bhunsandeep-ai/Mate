package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StoryItem
import com.example.ui.theme.*

@Composable
fun StoryTray(
    stories: List<StoryItem>,
    onStoryClick: (StoryItem) -> Unit,
    onAddStoryClick: () -> Unit,
    currentLanguage: String = "en",
    modifier: Modifier = Modifier
) {
    Surface(
        color = ForestGreenBg,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // First Item: Add / My Story
                item {
                    val myStory = stories.firstOrNull { it.userId == "me" }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(68.dp)
                            .clickable {
                                if (myStory != null) onStoryClick(myStory) else onAddStoryClick()
                            }
                            .testTag("my_story_tray_item")
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            // Avatar container with ring if exists
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .then(
                                        if (myStory != null) {
                                            Modifier.border(
                                                2.5.dp,
                                                Brush.sweepGradient(listOf(MintAccentLight, MintAccentGlow, MintAccentLight)),
                                                CircleShape
                                            )
                                        } else {
                                            Modifier.border(1.5.dp, ForestGreenBorder, CircleShape)
                                        }
                                    )
                                    .padding(3.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0F3A29)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "You",
                                    color = SoftCream,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Plus badge
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(MintAccent)
                                    .clickable { onAddStoryClick() }
                                    .border(1.5.dp, ForestGreenBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add story",
                                    tint = ForestGreenBg,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (currentLanguage == "bn") "আমার গল্প" else "My Story",
                            color = SoftCream,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Friends & Business Stories
                items(stories.filter { it.userId != "me" }) { story ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(68.dp)
                            .clickable { onStoryClick(story) }
                            .testTag("story_item_${story.id}")
                    ) {
                        val ringBrush = when {
                            story.isSponsored -> Brush.linearGradient(listOf(AdGold, Color(0xFFFBBF24)))
                            !story.isSeen -> Brush.linearGradient(listOf(MintAccentLight, MintAccentGlow))
                            else -> Brush.linearGradient(listOf(ForestGreenBorder, ForestGreenSurfaceVariant))
                        }

                        Box(
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .border(2.5.dp, ringBrush, CircleShape)
                                    .padding(3.dp)
                                    .clip(CircleShape)
                                    .background(Color(story.avatarColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = story.userName.take(1),
                                    color = SoftCream,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (story.isSponsored) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AdGold,
                                    modifier = Modifier.offset(y = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = ForestGreenBg,
                                            modifier = Modifier.size(8.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "AD",
                                            color = ForestGreenBg,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(if (story.isSponsored) 8.dp else 6.dp))

                        Text(
                            text = story.userName,
                            color = if (story.isSeen) SoftCreamMuted else SoftCream,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = if (!story.isSeen) FontWeight.SemiBold else FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
