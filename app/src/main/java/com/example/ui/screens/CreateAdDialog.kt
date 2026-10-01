package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AdFormat
import com.example.model.AdStatus
import com.example.model.Advertisement
import com.example.ui.theme.*

@Composable
fun CreateAdDialog(
    onDismiss: () -> Unit,
    onSubmitAd: (Advertisement) -> Unit,
    currentLanguage: String = "en"
) {
    var businessName by remember { mutableStateOf("Bengal Handloom Emporium") }
    var category by remember { mutableStateOf("Traditional Sarees & Crafts") }
    var title by remember { mutableStateOf("Festive Silk Collection 2026") }
    var description by remember { mutableStateOf("Handwoven authentic silk with Silk Mark certificate. Free home delivery in Kolkata.") }
    var ctaText by remember { mutableStateOf("Chat on NexChat") }
    var phone by remember { mutableStateOf("+91 98310 99887") }
    var targetCity by remember { mutableStateOf("Kolkata, WB") }
    var selectedBudget by remember { mutableIntStateOf(499) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ForestGreenSurface,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .border(1.dp, ForestGreenBorder, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentLanguage == "bn") "নতুন বিজ্ঞাপন তৈরি করুন" else "Create Advertisement",
                            color = SoftCream,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Promote to local Indian audience",
                            color = SoftCreamFaint,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_create_ad_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SoftCream)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdTextField(
                        label = "Business Name",
                        value = businessName,
                        onValueChange = { businessName = it }
                    )

                    AdTextField(
                        label = "Category",
                        value = category,
                        onValueChange = { category = it }
                    )

                    AdTextField(
                        label = "Advertisement Title",
                        value = title,
                        onValueChange = { title = it }
                    )

                    AdTextField(
                        label = "Short Description",
                        value = description,
                        onValueChange = { description = it },
                        singleLine = false
                    )

                    AdTextField(
                        label = "Call-To-Action (CTA)",
                        value = ctaText,
                        onValueChange = { ctaText = it }
                    )

                    AdTextField(
                        label = "Contact Phone (+91)",
                        value = phone,
                        onValueChange = { phone = it }
                    )

                    AdTextField(
                        label = "Target City / Region",
                        value = targetCity,
                        onValueChange = { targetCity = it }
                    )

                    // Budget Package Selection in INR ₹
                    Text(
                        text = "Select Promotion Package",
                        color = SoftCream,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BudgetOption(
                            title = "Daily",
                            price = "₹99",
                            isSelected = selectedBudget == 99,
                            onClick = { selectedBudget = 99 },
                            modifier = Modifier.weight(1f)
                        )
                        BudgetOption(
                            title = "Weekly",
                            price = "₹499",
                            isSelected = selectedBudget == 499,
                            onClick = { selectedBudget = 499 },
                            modifier = Modifier.weight(1f)
                        )
                        BudgetOption(
                            title = "Monthly",
                            price = "₹999",
                            isSelected = selectedBudget == 999,
                            onClick = { selectedBudget = 999 },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Moderation Notice
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ForestGreenSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = MintAccentLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "All ads are reviewed by human moderators before publishing to protect user privacy.",
                                color = SoftCreamMuted,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Submit Button
                Button(
                    onClick = {
                        val newAd = Advertisement(
                            id = "ad_${System.currentTimeMillis()}",
                            businessName = businessName,
                            businessCategory = category,
                            title = title,
                            description = description,
                            ctaText = ctaText,
                            phone = phone,
                            targetCity = targetCity,
                            budgetInr = selectedBudget,
                            durationLabel = when (selectedBudget) {
                                99 -> "Daily Package (₹99)"
                                499 -> "Weekly Package (₹499)"
                                else -> "Monthly Package (₹999)"
                            },
                            format = AdFormat.SPONSORED_CHAT_CARD,
                            status = AdStatus.PENDING,
                            impressions = 0,
                            clicks = 0,
                            chatsInitiated = 0
                        )
                        onSubmitAd(newAd)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintAccent,
                        contentColor = ForestGreenBg
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_ad_button")
                ) {
                    Text(
                        text = "Submit for Moderation (₹$selectedBudget)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun BudgetOption(
    title: String,
    price: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MintAccent.copy(alpha = 0.15f) else ForestGreenSurfaceVariant
        ),
        modifier = modifier
            .border(
                1.5.dp,
                if (isSelected) MintAccent else ForestGreenBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, color = SoftCreamMuted, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = price, color = if (isSelected) MintAccentLight else SoftCream, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AdTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true
) {
    Column {
        Text(text = label, color = SoftCreamMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MintAccent,
                unfocusedBorderColor = ForestGreenBorder,
                focusedContainerColor = ForestGreenSurfaceVariant,
                unfocusedContainerColor = ForestGreenSurfaceVariant,
                focusedTextColor = SoftCream,
                unfocusedTextColor = SoftCream
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
