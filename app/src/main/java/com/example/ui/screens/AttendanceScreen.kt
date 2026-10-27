package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.WorkMode
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.*
import com.example.viewmodel.IllyViewModel

@Composable
fun AttendanceScreen(
    viewModel: IllyViewModel,
    modifier: Modifier = Modifier
) {
    val isPunchedIn by viewModel.repository.isPunchedIn.collectAsState()
    val punchInTime by viewModel.repository.punchInTime.collectAsState()
    val currentWorkMode by viewModel.repository.currentWorkMode.collectAsState()
    val liveWorkingSeconds by viewModel.liveWorkingSeconds.collectAsState()
    val activeEmployee by viewModel.repository.activeEmployee.collectAsState()

    val hours = liveWorkingSeconds / 3600
    val minutes = (liveWorkingSeconds % 3600) / 60
    val seconds = liveWorkingSeconds % 60
    val formattedClock = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Title
        item {
            Column {
                Text(
                    text = "Working Hours & Attendance",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Automated shift logging, break tracking, and geolocation",
                    color = IllyTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Live Clock Hero Card
        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = IllySurfaceDark,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isPunchedIn) IllyBlue else IllyBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusPill(
                            text = if (isPunchedIn) "Shift Active" else "Shift Paused",
                            textColor = if (isPunchedIn) IllySuccess else IllyTextMuted,
                            backgroundColor = if (isPunchedIn) IllySuccess.copy(alpha = 0.15f) else IllyBorder,
                            icon = Icons.Default.Circle
                        )
                        Text(
                            text = "Started: $punchInTime",
                            color = IllyTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    // Digital Running Timer
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        (if (isPunchedIn) IllyBlue else IllyBorder).copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(
                                2.dp,
                                Brush.linearGradient(
                                    listOf(
                                        if (isPunchedIn) IllyRed else IllyBorder,
                                        if (isPunchedIn) IllyBlue else IllyBorder
                                    )
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = formattedClock,
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "LOGGED TODAY",
                                color = IllyTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Punch In / Punch Out Primary Action
                    Button(
                        onClick = { viewModel.repository.togglePunchClock() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPunchedIn) IllyRed else IllySuccess
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("punch_clock_action_button")
                    ) {
                        Icon(
                            imageVector = if (isPunchedIn) Icons.Default.StopCircle else Icons.Default.PlayCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isPunchedIn) "PUNCH OUT (END SHIFT)" else "PUNCH IN (START SHIFT)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Break & Location Verification
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = IllyBlue, modifier = Modifier.size(14.dp))
                            Text(
                                text = "HQ Tech Tower (14th Fl)",
                                color = IllyTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Coffee, contentDescription = null, tint = IllyWarning, modifier = Modifier.size(14.dp))
                            Text(
                                text = "Break Time: 45m",
                                color = IllyTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Work Mode Selector (Office / Remote / Hybrid)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Select Work Mode",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WorkMode.values().forEach { mode ->
                        val isSelected = mode == currentWorkMode
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) IllyBlue.copy(alpha = 0.2f) else IllyCardGlass,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) IllyBlue else IllyBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.repository.setWorkMode(mode) }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = when (mode) {
                                        WorkMode.IN_OFFICE -> Icons.Default.Business
                                        WorkMode.REMOTE -> Icons.Default.Home
                                        WorkMode.HYBRID -> Icons.Default.SyncAlt
                                    },
                                    contentDescription = null,
                                    tint = if (isSelected) IllyBlue else IllyTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = mode.name.replace("_", " "),
                                    color = if (isSelected) Color.White else IllyTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Weekly Timesheet Breakdown
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Weekly Timesheet Breakdown",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                val days = listOf(
                    Triple("Monday, Oct 20", "8h 15m", 1.0f),
                    Triple("Tuesday, Oct 21", "8h 40m", 1.05f),
                    Triple("Wednesday, Oct 22", "7h 50m", 0.95f),
                    Triple("Thursday, Oct 23", "9h 10m (1h OT)", 1.15f),
                    Triple("Today (Friday)", formattedClock, (liveWorkingSeconds / 28800f).coerceIn(0.1f, 1.2f))
                )

                days.forEach { (day, logged, progressRatio) ->
                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = day, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(text = logged, color = IllySuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progressRatio.coerceAtMost(1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(CircleShape),
                            color = if (progressRatio > 1.0f) IllyWarning else IllyBlue,
                            trackColor = IllyBorder
                        )
                    }
                }
            }
        }
    }
}
