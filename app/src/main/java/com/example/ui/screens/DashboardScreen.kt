package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.model.*
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IllyViewModel

@Composable
fun DashboardScreen(
    viewModel: IllyViewModel,
    modifier: Modifier = Modifier
) {
    val activeEmployee by viewModel.repository.activeEmployee.collectAsState()
    val announcements by viewModel.repository.announcements.collectAsState()
    val tasks by viewModel.repository.tasks.collectAsState()
    val meetings by viewModel.repository.meetings.collectAsState()
    val isPunchedIn by viewModel.repository.isPunchedIn.collectAsState()
    val liveWorkingSeconds by viewModel.liveWorkingSeconds.collectAsState()

    val hours = liveWorkingSeconds / 3600
    val minutes = (liveWorkingSeconds % 3600) / 60
    val seconds = liveWorkingSeconds % 60
    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Welcome Hero Banner
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = IllySurfaceDark,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(listOf(IllyRed, IllyBlue))
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    IllyRed.copy(alpha = 0.12f),
                                    IllyBlue.copy(alpha = 0.12f)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusPill(
                                text = "ONE TEAM • ONE SYSTEM • ONE ILLY",
                                textColor = IllyBlue,
                                backgroundColor = IllyBlue.copy(alpha = 0.2f),
                                icon = Icons.Default.Shield
                            )
                            StatusPill(
                                text = activeEmployee.workMode.label.split(" ").first(),
                                textColor = IllySuccess,
                                backgroundColor = IllySuccess.copy(alpha = 0.15f),
                                icon = Icons.Default.Circle
                            )
                        }

                        Text(
                            text = "Welcome back, ${activeEmployee.name}!",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )

                        Text(
                            text = "${activeEmployee.title} • ${activeEmployee.department.displayName}",
                            color = IllyTextSecondary,
                            fontSize = 13.sp
                        )

                        // Quick Clock Bar inside Hero
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(IllyCardGlass)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = if (isPunchedIn) IllySuccess else IllyTextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (isPunchedIn) "Logged Today: $formattedTime" else "Clock Paused",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Button(
                                onClick = { viewModel.repository.togglePunchClock() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPunchedIn) IllyRed.copy(alpha = 0.8f) else IllySuccess
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(
                                    text = if (isPunchedIn) "Clock Out" else "Punch In",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // CEO / EXECUTIVE COMMAND CENTER (Conditional on role)
        if (activeEmployee.role.isLeadership) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = IllyRed)
                            Text(
                                text = "CEO & Executive Command Center",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        TextButton(onClick = { viewModel.navigateTo(AppScreen.AUDIT_LOGS) }) {
                            Text("Audit Trail", color = IllyBlue, fontSize = 12.sp)
                        }
                    }

                    // Key Business Metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "Staff Headcount",
                            value = "248",
                            subtext = "+12 this month",
                            color = IllyBlue,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "Sprint Velocity",
                            value = "94.2%",
                            subtext = "Sprint 42 on track",
                            color = IllySuccess,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "Monthly Burn",
                            value = "$145K",
                            subtext = "Runway 28 mos",
                            color = IllyRed,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Executive Action Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.showBroadcastDialog.value = true },
                            colors = ButtonDefaults.buttonColors(containerColor = IllyRed),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("broadcast_action_button")
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Broadcast", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.showCreateTaskDialog.value = true },
                            border = androidx.compose.foundation.BorderStroke(1.dp, IllyBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = IllyBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Assign Task", color = IllyBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // High Priority Announcements
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Company Announcements",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${announcements.size} Broadcasts",
                        color = IllyTextSecondary,
                        fontSize = 12.sp
                    )
                }

                announcements.take(2).forEach { ann ->
                    GlassCard(
                        borderColor = if (ann.isCritical) IllyRed.copy(alpha = 0.6f) else IllyBorder,
                        backgroundColor = if (ann.isCritical) IllyRed.copy(alpha = 0.08f) else IllyCardGlass
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusPill(
                                text = ann.category,
                                textColor = if (ann.isCritical) IllyRed else IllyBlue,
                                backgroundColor = (if (ann.isCritical) IllyRed else IllyBlue).copy(alpha = 0.15f)
                            )
                            Text(text = ann.date, color = IllyTextSecondary, fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = ann.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ann.content,
                            color = IllyTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "By ${ann.authorName} (${ann.authorRole})",
                                color = IllyTextMuted,
                                fontSize = 11.sp
                            )

                            if (ann.isAcknowledgedByMe) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = IllySuccess,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Acknowledged (${ann.acknowledgesCount})", color = IllySuccess, fontSize = 11.sp)
                                }
                            } else {
                                TextButton(
                                    onClick = { viewModel.repository.acknowledgeAnnouncement(ann.id) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Acknowledge", color = IllyBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Tasks / Sprint Goals
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Tasks & Deliverables",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { viewModel.navigateTo(AppScreen.PROJECTS) }) {
                        Text("View Board", color = IllyBlue, fontSize = 12.sp)
                    }
                }

                tasks.take(3).forEach { task ->
                    GlassCard(onClick = { viewModel.navigateTo(AppScreen.PROJECTS) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusPill(
                                text = task.projectTitle.split(" ").take(2).joinToString(" "),
                                textColor = IllyBlue,
                                backgroundColor = IllyBlue.copy(alpha = 0.15f)
                            )
                            StatusPill(
                                text = task.status.label,
                                textColor = when (task.status) {
                                    TaskStatus.DONE -> IllySuccess
                                    TaskStatus.IN_PROGRESS -> IllyBlue
                                    TaskStatus.IN_REVIEW -> IllyWarning
                                    TaskStatus.BACKLOG -> IllyTextSecondary
                                },
                                backgroundColor = when (task.status) {
                                    TaskStatus.DONE -> IllySuccess.copy(alpha = 0.15f)
                                    TaskStatus.IN_PROGRESS -> IllyBlue.copy(alpha = 0.15f)
                                    TaskStatus.IN_REVIEW -> IllyWarning.copy(alpha = 0.15f)
                                    TaskStatus.BACKLOG -> IllyBorder
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = task.title,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Assignee: ${task.assigneeName}",
                                color = IllyTextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Due: ${task.dueDate}",
                                color = IllyTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Today's Meetings
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Today's Schedule & Meetings",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                meetings.filter { it.isToday }.forEach { meeting ->
                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = meeting.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${meeting.time} • Host: ${meeting.host}",
                                    color = IllyTextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            Button(
                                onClick = { /* Instant join */ },
                                colors = ButtonDefaults.buttonColors(containerColor = IllyBlue),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Join", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtext: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = IllyCardGlass,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, color = IllyTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            Text(text = value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text(text = subtext, color = color, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
