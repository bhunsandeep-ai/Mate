package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.AdStatus
import com.example.model.Advertisement
import com.example.ui.theme.*

@Composable
fun AdvertiseScreen(
    ads: List<Advertisement>,
    onCreateAdClick: () -> Unit,
    currentLanguage: String = "en",
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredAds = when (selectedFilter) {
        "ACTIVE" -> ads.filter { it.status == AdStatus.ACTIVE }
        "PENDING" -> ads.filter { it.status == AdStatus.PENDING }
        "EXPIRED" -> ads.filter { it.status == AdStatus.EXPIRED }
        else -> ads
    }

    val totalImpressions = ads.sumOf { it.impressions }
    val totalClicks = ads.sumOf { it.clicks }
    val totalChats = ads.sumOf { it.chatsInitiated }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ForestGreenBg),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Hero Promotion Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(AdGold, ForestGreenBorder)),
                        RoundedCornerShape(24.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AdGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "NEXCHAT BUSINESS",
                                color = AdGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (currentLanguage == "bn") "আপনার ব্যবসার প্রচার করুন" else "Grow Your Business Locally",
                        color = SoftCream,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (currentLanguage == "bn")
                            "স্থানীয় গ্রাহকদের কাছে আপনার পণ্যের স্পন্সরড স্টোরি ও কার্ড পৌঁছে দিন। প্যাকেজ শুরু মাত্র ₹৯৯ থেকে।"
                        else
                            "Reach local customers across India with discrete Sponsored Stories & Chat Cards. Packages start at ₹99.",
                        color = SoftCreamMuted,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onCreateAdClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintAccent,
                            contentColor = ForestGreenBg
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("create_ad_button")
                    ) {
                        Icon(imageVector = Icons.Default.AddBusiness, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == "bn") "বিজ্ঞাপন তৈরি করুন" else "Create Advertisement",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Live Analytics Overview
        item {
            Text(
                text = if (currentLanguage == "bn") "বিজ্ঞাপনের বিশ্লেষণ" else "Advertising Performance",
                color = SoftCream,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Impressions",
                    value = "%,d".format(totalImpressions),
                    icon = Icons.Default.Visibility,
                    tint = MintAccentLight,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Clicks",
                    value = "%,d".format(totalClicks),
                    icon = Icons.Default.AdsClick,
                    tint = AdGold,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Chats",
                    value = "%,d".format(totalChats),
                    icon = Icons.Default.Chat,
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
        }

        // Tab Filter Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentLanguage == "bn") "আমার বিজ্ঞাপনগুলি" else "My Campaigns",
                    color = SoftCream,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChipItem(label = "All", isSelected = selectedFilter == "ALL") { selectedFilter = "ALL" }
                    FilterChipItem(label = "Active", isSelected = selectedFilter == "ACTIVE") { selectedFilter = "ACTIVE" }
                    FilterChipItem(label = "Pending", isSelected = selectedFilter == "PENDING") { selectedFilter = "PENDING" }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Campaign Rows
        items(filteredAds) { ad ->
            CampaignCard(ad = ad)
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ForestGreenSurface),
        modifier = modifier.border(1.dp, ForestGreenBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = SoftCream, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = title, color = SoftCreamFaint, fontSize = 11.sp)
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MintAccent else ForestGreenSurfaceVariant,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) ForestGreenBg else SoftCreamMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun CampaignCard(ad: Advertisement) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ForestGreenSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ForestGreenBorder, RoundedCornerShape(18.dp))
            .testTag("campaign_card_${ad.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = ad.businessName,
                        color = SoftCream,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ad.businessCategory,
                        color = SoftCreamFaint,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (ad.status) {
                        AdStatus.ACTIVE -> MintAccent.copy(alpha = 0.2f)
                        AdStatus.PENDING -> AdGold.copy(alpha = 0.2f)
                        AdStatus.EXPIRED -> Color.Gray.copy(alpha = 0.2f)
                        AdStatus.REJECTED -> ExpiringTimerRed.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = ad.status.name,
                        color = when (ad.status) {
                            AdStatus.ACTIVE -> MintAccentLight
                            AdStatus.PENDING -> AdGold
                            AdStatus.EXPIRED -> SoftCreamFaint
                            AdStatus.REJECTED -> ExpiringTimerRed
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = ad.title,
                color = MintAccentLight,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = ad.description,
                color = SoftCreamMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Package: ${ad.durationLabel} (₹${ad.budgetInr})",
                    color = AdGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "${ad.impressions} Views • ${ad.clicks} Clicks",
                    color = SoftCreamFaint,
                    fontSize = 11.sp
                )
            }
        }
    }
}
