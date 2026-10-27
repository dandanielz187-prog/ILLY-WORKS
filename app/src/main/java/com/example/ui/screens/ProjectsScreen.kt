package com.example.ui.screens

import androidx.compose.animation.*
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
fun ProjectsScreen(
    viewModel: IllyViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.repository.projects.collectAsState()
    val tasks by viewModel.repository.tasks.collectAsState()
    val activeEmployee by viewModel.repository.activeEmployee.collectAsState()

    var selectedStatusFilter by remember { mutableStateOf<TaskStatus?>(null) }
    var selectedProjectFilter by remember { mutableStateOf<String?>("ALL") }

    val filteredTasks = tasks.filter { task ->
        (selectedStatusFilter == null || task.status == selectedStatusFilter) &&
        (selectedProjectFilter == "ALL" || selectedProjectFilter == null || task.projectId == selectedProjectFilter)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Header with "New Task" button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Projects & Sprint Board",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Manage sprints, deadlines, and task checklists",
                        color = IllyTextSecondary,
                        fontSize = 12.sp
                    )
                }

                if (activeEmployee.role.canManageProjects) {
                    Button(
                        onClick = { viewModel.showCreateTaskDialog.value = true },
                        colors = ButtonDefaults.buttonColors(containerColor = IllyRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("add_task_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Task", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active Projects Horizontal Scroll
        item {
            Text(
                text = "Active Initiatives",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    val isAll = selectedProjectFilter == "ALL"
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isAll) IllyBlue.copy(alpha = 0.2f) else IllyCardGlass,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isAll) IllyBlue else IllyBorder),
                        modifier = Modifier
                            .width(140.dp)
                            .height(130.dp)
                            .clickable { selectedProjectFilter = "ALL" }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatusPill(
                                text = "All Projects",
                                textColor = IllyBlue,
                                backgroundColor = IllyBlue.copy(alpha = 0.2f)
                            )
                            Text(
                                text = "${projects.size} Active Initiatives",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${tasks.size} total tasks",
                                color = IllyTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                items(projects) { proj ->
                    val isSelected = selectedProjectFilter == proj.id
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) IllyBlue.copy(alpha = 0.2f) else IllyCardGlass,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) IllyBlue else IllyBorder),
                        modifier = Modifier
                            .width(220.dp)
                            .height(130.dp)
                            .clickable { selectedProjectFilter = proj.id }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusPill(
                                    text = proj.code,
                                    textColor = IllyRed,
                                    backgroundColor = IllyRed.copy(alpha = 0.15f)
                                )
                                Text(
                                    text = "${proj.progress}%",
                                    color = IllySuccess,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = proj.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1
                            )

                            // Linear Progress Bar
                            LinearProgressIndicator(
                                progress = { proj.progress / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(CircleShape),
                                color = IllyBlue,
                                trackColor = IllyBorder
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Lead: ${proj.leadName.split(" ").first()}", color = IllyTextSecondary, fontSize = 10.sp)
                                Text(text = proj.deadline, color = IllyTextSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Status Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedStatusFilter == null,
                        onClick = { selectedStatusFilter = null },
                        label = { Text("All (${tasks.size})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IllyRed,
                            selectedLabelColor = Color.White,
                            containerColor = IllyCardGlass,
                            labelColor = IllyTextSecondary
                        )
                    )
                }

                items(TaskStatus.values()) { status ->
                    val isSelected = selectedStatusFilter == status
                    val count = tasks.count { it.status == status }
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedStatusFilter = if (isSelected) null else status },
                        label = { Text("${status.label} ($count)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IllyBlue,
                            selectedLabelColor = Color.White,
                            containerColor = IllyCardGlass,
                            labelColor = IllyTextSecondary
                        )
                    )
                }
            }
        }

        // Task Items List
        items(filteredTasks, key = { it.id }) { task ->
            TaskCard(
                task = task,
                onStatusChange = { newStatus ->
                    viewModel.repository.updateTaskStatus(task.id, newStatus)
                },
                onToggleChecklist = { checkId ->
                    viewModel.repository.toggleTaskChecklist(task.id, checkId)
                }
            )
        }
    }
}

@Composable
private fun TaskCard(
    task: TaskItem,
    onStatusChange: (TaskStatus) -> Unit,
    onToggleChecklist: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    GlassCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusPill(
                text = task.projectTitle,
                textColor = IllyBlue,
                backgroundColor = IllyBlue.copy(alpha = 0.15f)
            )

            // Priority Badge
            StatusPill(
                text = task.priority.label,
                textColor = when (task.priority) {
                    TaskPriority.CRITICAL -> IllyRed
                    TaskPriority.HIGH -> IllyWarning
                    TaskPriority.MEDIUM -> IllyBlue
                    TaskPriority.LOW -> IllySuccess
                },
                backgroundColor = when (task.priority) {
                    TaskPriority.CRITICAL -> IllyRed.copy(alpha = 0.2f)
                    TaskPriority.HIGH -> IllyWarning.copy(alpha = 0.2f)
                    TaskPriority.MEDIUM -> IllyBlue.copy(alpha = 0.2f)
                    TaskPriority.LOW -> IllySuccess.copy(alpha = 0.2f)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = task.title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = task.description,
            color = IllyTextSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = IllyTextSecondary, modifier = Modifier.size(14.dp))
                Text(text = task.assigneeName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = IllyTextSecondary, modifier = Modifier.size(12.dp))
                Text(text = task.dueDate, color = IllyTextSecondary, fontSize = 11.sp)
            }
        }

        // Subtasks & Checklist Preview
        if (task.checklist.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = IllyBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            val doneCount = task.checklist.count { it.isDone }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Checklist ($doneCount/${task.checklist.size} done)",
                    color = IllyTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = IllyTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    task.checklist.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleChecklist(item.id) }
                        ) {
                            Checkbox(
                                checked = item.isDone,
                                onCheckedChange = { onToggleChecklist(item.id) },
                                colors = CheckboxDefaults.colors(checkedColor = IllySuccess),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.title,
                                color = if (item.isDone) IllyTextMuted else Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Status Changer Pills
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TaskStatus.values().forEach { st ->
                val isCurrent = st == task.status
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isCurrent) IllyBlue else IllySurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isCurrent) IllyBlue else IllyBorder),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onStatusChange(st) }
                ) {
                    Text(
                        text = st.label.split(" ").first(),
                        color = if (isCurrent) Color.White else IllyTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}
