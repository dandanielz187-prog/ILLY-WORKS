package com.example.ui.components

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
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.*

// Create Task Dialog
@Composable
fun CreateTaskDialog(
    projects: List<Project>,
    employees: List<Employee>,
    onDismiss: () -> Unit,
    onCreateTask: (String, String, String, String, TaskPriority, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedProjectId by remember { mutableStateOf(projects.firstOrNull()?.id ?: "") }
    var selectedAssigneeId by remember { mutableStateOf(employees.firstOrNull()?.id ?: "") }
    var priority by remember { mutableStateOf(TaskPriority.HIGH) }
    var dueDate by remember { mutableStateOf("Nov 15, 2026") }
    var estimatedHours by remember { mutableStateOf("16") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = IllySurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, IllyBorderHighlight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Assign New Task",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = IllyTextSecondary)
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    placeholder = { Text("e.g. Implement Compose Insets") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("task_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IllyBlue,
                        unfocusedBorderColor = IllyBorder
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Requirements") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IllyBlue,
                        unfocusedBorderColor = IllyBorder
                    )
                )

                // Assignee Selector
                Text("Assign To:", color = IllyTextSecondary, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    employees.take(4).forEach { emp ->
                        val isSelected = emp.id == selectedAssigneeId
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) IllyBlue.copy(alpha = 0.2f) else IllyCardGlass,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) IllyBlue else IllyBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedAssigneeId = emp.id }
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(emp.avatarColorHex)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = emp.name.take(1),
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = emp.name.split(" ").first(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                // Priority Selection
                Text("Priority Level:", color = IllyTextSecondary, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskPriority.values().forEach { prio ->
                        val isSelected = prio == priority
                        val col = when (prio) {
                            TaskPriority.CRITICAL -> IllyRed
                            TaskPriority.HIGH -> IllyWarning
                            TaskPriority.MEDIUM -> IllyBlue
                            TaskPriority.LOW -> IllySuccess
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) col.copy(alpha = 0.2f) else IllyCardGlass,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) col else IllyBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { priority = prio }
                        ) {
                            Text(
                                text = prio.label,
                                color = if (isSelected) col else IllyTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onCreateTask(
                                title,
                                description,
                                selectedProjectId,
                                selectedAssigneeId,
                                priority,
                                dueDate,
                                estimatedHours.toIntOrNull() ?: 10
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_task_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IllyRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Create & Assign Task", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Broadcast Announcement Dialog (For CEO & Leadership)
@Composable
fun BroadcastAnnouncementDialog(
    onDismiss: () -> Unit,
    onBroadcast: (String, String, Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isCritical by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = IllySurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, IllyRed.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = null, tint = IllyRed)
                        Text(
                            text = "Executive Broadcast",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = IllyTextSecondary)
                    }
                }

                Text(
                    text = "This will send an immediate company-wide broadcast notification to all ILLY employees.",
                    color = IllyTextSecondary,
                    fontSize = 12.sp
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Announcement Headline") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IllyRed,
                        unfocusedBorderColor = IllyBorder
                    )
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Broadcast Body & Action Items") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IllyRed,
                        unfocusedBorderColor = IllyBorder
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isCritical = !isCritical }
                ) {
                    Checkbox(
                        checked = isCritical,
                        onCheckedChange = { isCritical = it },
                        colors = CheckboxDefaults.colors(checkedColor = IllyRed)
                    )
                    Text("Mark as High-Priority / Critical Alert", color = Color.White, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            onBroadcast(title, content, isCritical)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_broadcast_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IllyRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Publish Company Broadcast", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Leave Request Dialog
@Composable
fun LeaveRequestDialog(
    onDismiss: () -> Unit,
    onSubmit: (LeaveType, String, String, Int, String) -> Unit
) {
    var leaveType by remember { mutableStateOf(LeaveType.ANNUAL) }
    var startDate by remember { mutableStateOf("Nov 18, 2026") }
    var endDate by remember { mutableStateOf("Nov 20, 2026") }
    var daysCount by remember { mutableStateOf("3") }
    var reason by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = IllySurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, IllyBorderHighlight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Request Leave / Permission",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = IllyTextSecondary)
                    }
                }

                Text("Leave Category:", color = IllyTextSecondary, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(LeaveType.ANNUAL, LeaveType.SICK, LeaveType.REMOTE_DAY).forEach { type ->
                        val isSelected = type == leaveType
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) IllyBlue.copy(alpha = 0.2f) else IllyCardGlass,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) IllyBlue else IllyBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { leaveType = type }
                        ) {
                            Text(
                                text = type.label.split(" ").first(),
                                color = if (isSelected) IllyBlue else IllyTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Start Date") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("End Date") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason & Project Coverage") },
                    placeholder = { Text("e.g. Taking family time, Alex covering PRs") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Button(
                    onClick = {
                        if (reason.isNotBlank()) {
                            onSubmit(leaveType, startDate, endDate, daysCount.toIntOrNull() ?: 1, reason)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_leave_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IllyBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Submit for Approval", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Expense Claim Dialog
@Composable
fun ExpenseClaimDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, ExpenseCategory, Double, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ExpenseCategory.SOFTWARE) }
    var amount by remember { mutableStateOf("") }
    var receiptRef by remember { mutableStateOf("RECEIPT_ILLY_OCT2026.pdf") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = IllySurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, IllyBorderHighlight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Submit Reimbursement",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = IllyTextSecondary)
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Expense Item / Purpose") },
                    placeholder = { Text("e.g. Cloud API Credits, Figma License") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount ($ USD)") },
                    placeholder = { Text("e.g. 150.00") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Category:", color = IllyTextSecondary, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(ExpenseCategory.SOFTWARE, ExpenseCategory.HARDWARE, ExpenseCategory.TEAM_MEAL).forEach { cat ->
                        val isSelected = cat == category
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) IllySuccess.copy(alpha = 0.2f) else IllyCardGlass,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) IllySuccess else IllyBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { category = cat }
                        ) {
                            Text(
                                text = cat.label.split(" ").first(),
                                color = if (isSelected) IllySuccess else IllyTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        val parsed = amount.toDoubleOrNull() ?: 0.0
                        if (title.isNotBlank() && parsed > 0.0) {
                            onSubmit(title, category, parsed, receiptRef)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_expense_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IllySuccess),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Submit Expense Claim", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Pay Slip Details Dialog
@Composable
fun PaySlipDetailsDialog(
    slip: PayrollSlip,
    employee: Employee,
    onDismiss: () -> Unit
) {
    var downloaded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = IllySurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, IllyBorderHighlight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ILLY Pay Statement",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Period: ${slip.period} • ID: ${employee.employeeCode}",
                            color = IllyTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = IllyTextSecondary)
                    }
                }

                Surface(
                    color = IllyCardGlass,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IllyBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PayRow("Employee Name", employee.name)
                        PayRow("Role / Title", employee.title)
                        PayRow("Base Salary", String.format("$%,.2f", slip.baseSalary))
                        PayRow("Performance Bonus", String.format("+$%,.2f", slip.performanceBonus), IllySuccess)
                        PayRow("Tech & Wellness Allowance", String.format("+$%,.2f", slip.techAllowance), IllySuccess)
                        Divider(color = IllyBorder, thickness = 1.dp)
                        PayRow("Income Tax Withholding", String.format("-$%,.2f", slip.taxDeduction), IllyRed)
                        PayRow("Health & Medical Coverage", String.format("-$%,.2f", slip.healthInsurance), IllyRed)
                        Divider(color = IllyBorder, thickness = 1.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("NET DISBURSED", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                            Text(
                                text = String.format("$%,.2f", slip.netSalary),
                                color = IllySuccess,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { downloaded = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (downloaded) IllySuccess else IllyBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            if (downloaded) Icons.Default.Check else Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (downloaded) "Downloaded PDF" else "Download Slip")
                    }
                }
            }
        }
    }
}

@Composable
private fun PayRow(label: String, value: String, valueColor: Color = Color.White) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = IllyTextSecondary, fontSize = 12.sp)
        Text(text = value, color = valueColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

// Notifications Sheet / Dialog
@Composable
fun NotificationsSheet(
    notifications: List<NotificationItem>,
    onMarkAllRead: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = IllySurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, IllyBorderHighlight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ILLY Notifications",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onMarkAllRead) {
                        Text("Mark Read", color = IllyBlue, fontSize = 12.sp)
                    }
                }

                LazyColumn(
                    modifier = Modifier.heightIn(max = 350.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications) { notif ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (notif.isRead) IllyCardGlass else IllyBlue.copy(alpha = 0.1f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (notif.isRead) IllyBorder else IllyBlue.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = notif.title,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = notif.timeAgo,
                                        color = IllyTextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notif.message,
                                    color = IllyTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
