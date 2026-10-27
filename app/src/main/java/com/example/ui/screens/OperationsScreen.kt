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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.*
import com.example.viewmodel.IllyViewModel

@Composable
fun OperationsScreen(
    viewModel: IllyViewModel,
    modifier: Modifier = Modifier
) {
    val activeEmployee by viewModel.repository.activeEmployee.collectAsState()
    val leaveRequests by viewModel.repository.leaveRequests.collectAsState()
    val expenseClaims by viewModel.repository.expenseClaims.collectAsState()
    val payrollSlips by viewModel.repository.payrollSlips.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Leaves", "Expenses", "Payroll", "Performance")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Operations Header
        item {
            Column {
                Text(
                    text = "Operations & HR Portal",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Leaves, reimbursements, payroll records, and performance OKRs",
                    color = IllyTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Subtabs Bar
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = IllySurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, IllyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    tabs.forEachIndexed { index, tabTitle ->
                        val isSelected = selectedTab == index
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) IllyRed else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = index }
                        ) {
                            Text(
                                text = tabTitle,
                                color = if (isSelected) Color.White else IllyTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // TAB 0: LEAVES & PERMISSIONS
        if (selectedTab == 0) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Leave Balances (2026)",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = { viewModel.showLeaveDialog.value = true },
                        colors = ButtonDefaults.buttonColors(containerColor = IllyBlue),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Request Leave", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Balances Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BalancePill("Annual Vacation", "14", "20 Total", IllyBlue, Modifier.weight(1f))
                    BalancePill("Sick / Wellness", "8", "10 Total", IllySuccess, Modifier.weight(1f))
                    BalancePill("Casual Leave", "6", "7 Total", IllyWarning, Modifier.weight(1f))
                }
            }

            item {
                Text(
                    text = "Leave Requests & Approvals",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(leaveRequests, key = { it.id }) { req ->
                GlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusPill(
                            text = req.type.label,
                            textColor = IllyBlue,
                            backgroundColor = IllyBlue.copy(alpha = 0.15f)
                        )
                        StatusPill(
                            text = req.status.label,
                            textColor = when (req.status) {
                                ApprovalStatus.APPROVED -> IllySuccess
                                ApprovalStatus.PENDING -> IllyWarning
                                ApprovalStatus.REJECTED -> IllyRed
                            },
                            backgroundColor = when (req.status) {
                                ApprovalStatus.APPROVED -> IllySuccess.copy(alpha = 0.15f)
                                ApprovalStatus.PENDING -> IllyWarning.copy(alpha = 0.15f)
                                ApprovalStatus.REJECTED -> IllyRed.copy(alpha = 0.15f)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${req.employeeName} (${req.employeeRole})",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Dates: ${req.startDate} to ${req.endDate} (${req.daysCount} days)",
                        color = IllyTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${req.reason}\"",
                        color = IllyWhiteDim,
                        fontSize = 12.sp
                    )

                    if (!req.reviewerComment.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Note: ${req.reviewerComment}",
                            color = IllySuccess,
                            fontSize = 11.sp
                        )
                    }

                    // Leadership Approval Action Buttons
                    if (activeEmployee.role.canApproveRequests && req.status == ApprovalStatus.PENDING) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.repository.reviewLeaveRequest(req.id, true, "Approved by ${activeEmployee.name}") },
                                colors = ButtonDefaults.buttonColors(containerColor = IllySuccess),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Approve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { viewModel.repository.reviewLeaveRequest(req.id, false, "Declined due to sprint schedule") },
                                border = androidx.compose.foundation.BorderStroke(1.dp, IllyRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Decline", color = IllyRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: EXPENSES & REIMBURSEMENTS
        if (selectedTab == 1) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Expense Claims",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = { viewModel.showExpenseDialog.value = true },
                        colors = ButtonDefaults.buttonColors(containerColor = IllySuccess),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Claim", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(expenseClaims, key = { it.id }) { claim ->
                GlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusPill(
                            text = claim.category.label,
                            textColor = IllySuccess,
                            backgroundColor = IllySuccess.copy(alpha = 0.15f)
                        )
                        StatusPill(
                            text = claim.status.label,
                            textColor = if (claim.status == ApprovalStatus.APPROVED) IllySuccess else IllyWarning,
                            backgroundColor = (if (claim.status == ApprovalStatus.APPROVED) IllySuccess else IllyWarning).copy(alpha = 0.15f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = claim.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Claimant: ${claim.employeeName} • ${claim.submittedDate}",
                            color = IllyTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = String.format("$%,.2f", claim.amount),
                            color = IllySuccess,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }

                    // Leadership Approval
                    if (activeEmployee.role.canApproveRequests && claim.status == ApprovalStatus.PENDING) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.repository.reviewExpenseClaim(claim.id, true) },
                                colors = ButtonDefaults.buttonColors(containerColor = IllySuccess),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Approve Claim", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { viewModel.repository.reviewExpenseClaim(claim.id, false) },
                                border = androidx.compose.foundation.BorderStroke(1.dp, IllyRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reject", color = IllyRed, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: PAYROLL RECORDS
        if (selectedTab == 2) {
            item {
                Text(
                    text = "ILLY Payroll & Disbursal Records",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(payrollSlips, key = { it.id }) { slip ->
                val targetEmp = viewModel.repository.allEmployees.find { it.id == slip.employeeId } ?: activeEmployee

                GlassCard(onClick = { viewModel.selectedPaySlip.value = slip }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = slip.period,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        StatusPill(
                            text = slip.paymentStatus,
                            textColor = IllySuccess,
                            backgroundColor = IllySuccess.copy(alpha = 0.15f),
                            icon = Icons.Default.CheckCircle
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${targetEmp.name} (${targetEmp.title})",
                        color = IllyTextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Net Disbursed: ${String.format("$%,.2f", slip.netSalary)}",
                            color = IllySuccess,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "View Slip →",
                            color = IllyBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // TAB 3: PERFORMANCE & OKRs
        if (selectedTab == 3) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Q4 Performance & Quarterly OKRs",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${activeEmployee.name}'s Rating",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Evaluation by Engineering Committee",
                                    color = IllyTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = IllyWarning, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("4.9 / 5.0", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Core Competencies & Feedback:", color = IllyTextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"Exemplary system architecture contributions, consistent 94%+ sprint velocity, and mentorship to junior team members in the ILLY Academy.\"",
                            color = IllyWhiteDim,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        // OKR 1
                        OkrProgressRow("Deliver Zero-Trust Cloud Platform", 0.88f)
                        Spacer(modifier = Modifier.height(6.dp))
                        OkrProgressRow("Gemini 3.8 Flash Assistant Integration", 0.95f)
                        Spacer(modifier = Modifier.height(6.dp))
                        OkrProgressRow("Compose Edge-to-Edge Navigation Standard", 1.0f)
                    }
                }
            }
        }
    }
}

@Composable
private fun OkrProgressRow(title: String, ratio: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = Color.White, fontSize = 11.sp)
            Text(text = "${(ratio * 100).toInt()}%", color = IllyBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(
            progress = { ratio },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape),
            color = if (ratio >= 1.0f) IllySuccess else IllyBlue,
            trackColor = IllyBorder
        )
    }
}

@Composable
private fun BalancePill(label: String, remaining: String, total: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = remaining, color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text(text = label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = total, color = IllyTextSecondary, fontSize = 9.sp)
        }
    }
}
