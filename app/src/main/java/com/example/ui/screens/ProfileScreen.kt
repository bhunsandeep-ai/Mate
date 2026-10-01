package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    onViewPrivacyPolicy: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClick() }

    var readReceiptsEnabled by remember { mutableStateOf(true) }
    var selectedLastSeen by remember { mutableStateOf("Everyone") }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    var userName by remember { mutableStateOf(MockDataProvider.currentUser.name) }
    var userAbout by remember { mutableStateOf(MockDataProvider.currentUser.about) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ForestGreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (currentLanguage == "bn") "প্রোফাইল ও সেটিংস" else "Profile & Settings",
                        color = SoftCream,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("profile_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = SoftCream
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreenSurface,
                    navigationIconContentColor = SoftCream,
                    titleContentColor = SoftCream
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Profile Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForestGreenBorder, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(Color(MockDataProvider.currentUser.avatarColorHex))
                            .border(3.dp, MintAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(1),
                            color = SoftCream,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userName,
                        color = SoftCream,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = MockDataProvider.currentUser.phoneNumber,
                        color = MintAccentLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = userAbout,
                        color = SoftCreamMuted,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { showEditProfileDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MintAccentLight),
                        border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MintAccent)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Edit Profile", fontSize = 13.sp)
                    }
                }
            }

            // Language Selector Section
            SettingSectionHeader(title = if (currentLanguage == "bn") "ভাষা নির্বাচন" else "Language")
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForestGreenBorder, RoundedCornerShape(18.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Application Language", color = SoftCream, fontSize = 14.sp)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LanguageChip(
                            label = "English",
                            isSelected = currentLanguage == "en",
                            onClick = { onLanguageChange("en") }
                        )
                        LanguageChip(
                            label = "বাংলা (Bengali)",
                            isSelected = currentLanguage == "bn",
                            onClick = { onLanguageChange("bn") }
                        )
                    }
                }
            }

            // Privacy Controls Section
            SettingSectionHeader(title = if (currentLanguage == "bn") "গোপনীয়তা ও নিরাপত্তা" else "Privacy & Disappearing Chat")
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForestGreenBorder, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // 24-Hour Timer Active
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "24-Hour Auto Expiration", color = SoftCream, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Enforced server-side on all conversations", color = SoftCreamFaint, fontSize = 11.sp)
                        }
                        Surface(shape = RoundedCornerShape(8.dp), color = ExpiringTimerBg) {
                            Text(
                                text = "LOCKED: 24H",
                                color = ExpiringTimerRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Divider(color = ForestGreenBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    // Read Receipts Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Read Receipts", color = SoftCream, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Show blue checkmarks when messages are read", color = SoftCreamFaint, fontSize = 11.sp)
                        }
                        Switch(
                            checked = readReceiptsEnabled,
                            onCheckedChange = { readReceiptsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ForestGreenBg,
                                checkedTrackColor = MintAccent,
                                uncheckedThumbColor = SoftCreamMuted,
                                uncheckedTrackColor = ForestGreenSurfaceVariant
                            )
                        )
                    }

                    Divider(color = ForestGreenBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    // Last Seen
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Last Seen & Online", color = SoftCream, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Visible to: $selectedLastSeen", color = SoftCreamFaint, fontSize = 11.sp)
                        }
                        TextButton(
                            onClick = {
                                selectedLastSeen = when (selectedLastSeen) {
                                    "Everyone" -> "Contacts"
                                    "Contacts" -> "Nobody"
                                    else -> "Everyone"
                                }
                            }
                        ) {
                            Text(text = selectedLastSeen, color = MintAccentLight, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Legal & Terms
            SettingSectionHeader(title = "Information & Support")
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForestGreenBorder, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewPrivacyPolicy() }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Privacy Policy & 24h Deletion Policy", color = SoftCream, fontSize = 14.sp)
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = SoftCreamMuted)
                    }

                    Divider(color = ForestGreenBorder.copy(alpha = 0.5f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Terms of Service", color = SoftCream, fontSize = 14.sp)
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = SoftCreamMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text(text = "Edit Profile", color = SoftCream) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Display Name") }
                    )
                    OutlinedTextField(
                        value = userAbout,
                        onValueChange = { userAbout = it },
                        label = { Text("About Status") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showEditProfileDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MintAccent, contentColor = ForestGreenBg)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = SoftCreamMuted)
                }
            },
            containerColor = ForestGreenSurface
        )
    }
}

@Composable
private fun SettingSectionHeader(title: String) {
    Text(
        text = title,
        color = SoftCreamMuted,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
    )
}

@Composable
private fun LanguageChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MintAccent else ForestGreenSurfaceVariant,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) ForestGreenBg else SoftCreamMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
