package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.StoryItem
import com.example.model.StoryMediaType
import com.example.ui.theme.*

@Composable
fun CreateStoryDialog(
    onDismiss: () -> Unit,
    onPublishStory: (StoryItem) -> Unit,
    currentLanguage: String = "en"
) {
    val gradientPalettes = listOf(
        listOf(0xFF0F392B, 0xFF1E5E47), // Deep Forest
        listOf(0xFF1E3A8A, 0xFF2563EB), // Deep Indigo
        listOf(0xFF831843, 0xFFBE185D), // Magenta Wine
        listOf(0xFF78350F, 0xFFB45309), // Amber Bronze
        listOf(0xFF4C1D95, 0xFF6D28D9)  // Royal Violet
    )

    var selectedPaletteIndex by remember { mutableIntStateOf(0) }
    var storyText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        gradientPalettes[selectedPaletteIndex].map { Color(it) }
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.3f))
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel", tint = SoftCream)
                    }

                    // Background Color Cycle Button
                    IconButton(
                        onClick = {
                            selectedPaletteIndex = (selectedPaletteIndex + 1) % gradientPalettes.size
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.3f))
                    ) {
                        Icon(imageVector = Icons.Default.Palette, contentDescription = "Change Color", tint = SoftCream)
                    }
                }

                Spacer(modifier = Modifier.weight(0.5f))

                // Center Story Text Field
                TextField(
                    value = storyText,
                    onValueChange = { storyText = it },
                    placeholder = {
                        Text(
                            text = if (currentLanguage == "bn") "আপনার মনের কথা লিখুন...\n(২৪ ঘণ্টা পর মুছে যাবে)"
                            else "Type your story...\n(Automatically disappears in 24h)",
                            color = SoftCream.copy(alpha = 0.6f),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = SoftCream,
                        unfocusedTextColor = SoftCream,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_story_input")
                )

                Spacer(modifier = Modifier.weight(1f))

                // Publish Button
                Button(
                    onClick = {
                        if (storyText.isNotBlank()) {
                            val now = System.currentTimeMillis()
                            val newStory = StoryItem(
                                id = "story_${System.currentTimeMillis()}",
                                userId = "me",
                                userName = "My Story",
                                mediaType = StoryMediaType.TEXT,
                                textContent = storyText.trim(),
                                backgroundGradientHexes = gradientPalettes[selectedPaletteIndex],
                                createdAtEpochMs = now,
                                expiresAtEpochMs = now + (24 * 60 * 60 * 1000L),
                                isSeen = true,
                                viewersCount = 0
                            )
                            onPublishStory(newStory)
                        }
                    },
                    enabled = storyText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintAccent,
                        contentColor = ForestGreenBg
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("publish_story_button")
                ) {
                    Text(
                        text = if (currentLanguage == "bn") "গল্প প্রকাশ করুন (২৪ ঘণ্টা)" else "Share to My Story (24h)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
