package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class NavTab {
    CHAT,
    CALL,
    VIDEO_CALL,
    ADVERTISE
}

@Composable
fun FloatingBottomNav(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    currentLanguage: String = "en",
    unreadChatCount: Int = 3,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = ForestGreenSurface,
            shadowElevation = 12.dp,
            modifier = Modifier
                .border(1.dp, ForestGreenBorder, RoundedCornerShape(32.dp))
                .shadow(16.dp, RoundedCornerShape(32.dp), ambientColor = Color.Black, spotColor = MintAccentGlow)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavTabItem(
                    label = if (currentLanguage == "bn") "চ্যাট" else "Chat",
                    selectedIcon = Icons.Filled.ChatBubble,
                    unselectedIcon = Icons.Outlined.ChatBubbleOutline,
                    isSelected = selectedTab == NavTab.CHAT,
                    badgeCount = unreadChatCount,
                    tag = "nav_chat_tab",
                    onClick = { onTabSelected(NavTab.CHAT) }
                )

                NavTabItem(
                    label = if (currentLanguage == "bn") "কল" else "Call",
                    selectedIcon = Icons.Filled.Phone,
                    unselectedIcon = Icons.Outlined.Phone,
                    isSelected = selectedTab == NavTab.CALL,
                    tag = "nav_call_tab",
                    onClick = { onTabSelected(NavTab.CALL) }
                )

                NavTabItem(
                    label = if (currentLanguage == "bn") "ভিডিও" else "Video",
                    selectedIcon = Icons.Filled.Videocam,
                    unselectedIcon = Icons.Outlined.Videocam,
                    isSelected = selectedTab == NavTab.VIDEO_CALL,
                    tag = "nav_video_tab",
                    onClick = { onTabSelected(NavTab.VIDEO_CALL) }
                )

                NavTabItem(
                    label = if (currentLanguage == "bn") "প্রচার" else "Advertise",
                    selectedIcon = Icons.Filled.Campaign,
                    unselectedIcon = Icons.Outlined.Campaign,
                    isSelected = selectedTab == NavTab.ADVERTISE,
                    accentColor = AdGold,
                    tag = "nav_advertise_tab",
                    onClick = { onTabSelected(NavTab.ADVERTISE) }
                )
            }
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
    accentColor: Color = MintAccent,
    tag: String
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) accentColor.copy(alpha = 0.16f) else Color.Transparent,
        label = "navBg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else SoftCreamMuted,
        label = "navColor"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box {
                Icon(
                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )

                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .offset(x = 10.dp, y = (-4).dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(MintAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badgeCount.toString(),
                            color = ForestGreenBg,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (isSelected) {
                Text(
                    text = label,
                    color = contentColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
