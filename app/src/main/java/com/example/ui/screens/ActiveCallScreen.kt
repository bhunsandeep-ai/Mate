package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.model.CallMedium
import com.example.model.User
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ActiveCallScreen(
    user: User,
    medium: CallMedium,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onEndCall() }

    var callDurationSec by remember { mutableIntStateOf(0) }
    var isConnected by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }
    var isVideoOn by remember { mutableStateOf(true) }
    var isFrontCamera by remember { mutableStateOf(true) }

    // Call state simulation: "Calling..." for 2 seconds, then "Connected" with live timer
    LaunchedEffect(Unit) {
        delay(2000)
        isConnected = true
        while (true) {
            delay(1000)
            callDurationSec += 1
        }
    }

    val minutes = callDurationSec / 60
    val seconds = callDurationSec % 60
    val formattedDuration = "%02d:%02d".format(minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ForestGreenBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("active_call_screen")
    ) {
        if (medium == CallMedium.VIDEO && isConnected && isVideoOn) {
            // Simulated Remote Video Feed with rich gradient & avatar
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF133E2B), ForestGreenBg)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color(user.avatarColorHex))
                            .border(3.dp, MintAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.take(1),
                            color = SoftCream,
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Remote Video Stream (HD)",
                        color = MintAccentLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Small Self Preview PIP
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(20.dp)
                        .width(110.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ForestGreenSurfaceVariant)
                        .border(1.5.dp, MintAccent, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isFrontCamera) Icons.Default.CameraFront else Icons.Default.CameraRear,
                            contentDescription = "Camera PIP",
                            tint = MintAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You",
                            color = SoftCream,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // Voice Call UI (or Video when camera is turned off)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color(user.avatarColorHex))
                        .border(3.dp, MintAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.take(1),
                        color = SoftCream,
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = user.name,
                    color = SoftCream,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (!isConnected) "Connecting to +91..." else formattedDuration,
                    color = if (!isConnected) MintAccentLight else SoftCreamMuted,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ForestGreenSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = MintAccentLight,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Secured WebRTC Peer-to-Peer",
                            color = SoftCreamMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Call Control Bar at bottom
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = ForestGreenSurface,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .border(1.dp, ForestGreenBorder, RoundedCornerShape(32.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mute
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (isMuted) Color(0xFFEF4444) else ForestGreenSurfaceVariant)
                        .testTag("call_mute_toggle")
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = SoftCream
                    )
                }

                // Speaker
                IconButton(
                    onClick = { isSpeakerOn = !isSpeakerOn },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (isSpeakerOn) MintAccent else ForestGreenSurfaceVariant)
                        .testTag("call_speaker_toggle")
                ) {
                    Icon(
                        imageVector = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                        contentDescription = "Speaker",
                        tint = if (isSpeakerOn) ForestGreenBg else SoftCream
                    )
                }

                // Camera Toggle & Flip for Video Calls
                if (medium == CallMedium.VIDEO) {
                    IconButton(
                        onClick = { isVideoOn = !isVideoOn },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (!isVideoOn) Color(0xFFEF4444) else ForestGreenSurfaceVariant)
                            .testTag("call_camera_toggle")
                    ) {
                        Icon(
                            imageVector = if (isVideoOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                            contentDescription = "Camera",
                            tint = SoftCream
                        )
                    }

                    IconButton(
                        onClick = { isFrontCamera = !isFrontCamera },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(ForestGreenSurfaceVariant)
                            .testTag("call_flip_camera")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlipCameraAndroid,
                            contentDescription = "Switch Camera",
                            tint = SoftCream
                        )
                    }
                }

                // End Call Button
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDC2626))
                        .testTag("end_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = SoftCream,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
