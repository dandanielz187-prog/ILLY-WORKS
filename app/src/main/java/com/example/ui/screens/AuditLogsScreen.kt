package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuditSeverity
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.*
import com.example.viewmodel.IllyViewModel

@Composable
fun AuditLogsScreen(
    viewModel: IllyViewModel,
    modifier: Modifier = Modifier
) {
    val auditLogs by viewModel.repository.auditLogs.collectAsState()
    val activeEmployee by viewModel.repository.activeEmployee.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = IllyRed)
                    Text(
                        text = "ILLY Security Audit Trail",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Text(
                    text = "Immutable compliance & activity logging for zero-trust oversight",
                    color = IllyTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        item {
            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Audit Status: ACTIVE",
                            color = IllySuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Signed by ${activeEmployee.name} (${activeEmployee.role.name})",
                            color = IllyTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    StatusPill(
                        text = "End-to-End Encrypted",
                        textColor = IllyBlue,
                        backgroundColor = IllyBlue.copy(alpha = 0.15f),
                        icon = Icons.Default.Lock
                    )
                }
            }
        }

        items(auditLogs, key = { it.id }) { log ->
            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusPill(
                        text = log.action,
                        textColor = when (log.severity) {
                            AuditSeverity.CRITICAL -> IllyRed
                            AuditSeverity.WARNING -> IllyWarning
                            AuditSeverity.INFO -> IllyBlue
                        },
                        backgroundColor = when (log.severity) {
                            AuditSeverity.CRITICAL -> IllyRed.copy(alpha = 0.2f)
                            AuditSeverity.WARNING -> IllyWarning.copy(alpha = 0.2f)
                            AuditSeverity.INFO -> IllyBlue.copy(alpha = 0.15f)
                        }
                    )
                    Text(text = log.timestamp, color = IllyTextSecondary, fontSize = 10.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = log.target,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Actor: ${log.actorName} (${log.actorRole})",
                        color = IllyTextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = log.ipAddress,
                        color = IllyTextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
