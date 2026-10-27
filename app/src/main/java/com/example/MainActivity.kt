package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.IllyBgDark
import com.example.ui.theme.MyApplicationTheme
import com.example.util.configureAppCheck
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IllyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check configuration with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val vm: IllyViewModel = viewModel()
                val currentScreen by vm.currentScreen.collectAsState()
                val activeEmployee by vm.repository.activeEmployee.collectAsState()
                val showRoleDialog by vm.showRoleDialog.collectAsState()
                val showCreateTaskDialog by vm.showCreateTaskDialog.collectAsState()
                val showBroadcastDialog by vm.showBroadcastDialog.collectAsState()
                val showLeaveDialog by vm.showLeaveDialog.collectAsState()
                val showExpenseDialog by vm.showExpenseDialog.collectAsState()
                val showNotificationsSheet by vm.showNotificationsSheet.collectAsState()
                val selectedPaySlip by vm.selectedPaySlip.collectAsState()

                // Handle back button on secondary screens
                BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
                    vm.navigateTo(AppScreen.DASHBOARD)
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(IllyBgDark),
                    containerColor = IllyBgDark,
                    topBar = {
                        IllyTopBar(
                            viewModel = vm,
                            activeEmployee = activeEmployee,
                            onOpenRoleDialog = { vm.showRoleDialog.value = true },
                            onOpenNotifications = { vm.showNotificationsSheet.value = true }
                        )
                    },
                    bottomBar = {
                        IllyBottomNavigationBar(
                            currentScreen = currentScreen,
                            onNavigate = { vm.navigateTo(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            AppScreen.DASHBOARD -> DashboardScreen(viewModel = vm)
                            AppScreen.PROJECTS -> ProjectsScreen(viewModel = vm)
                            AppScreen.CHAT -> ChatScreen(viewModel = vm)
                            AppScreen.ATTENDANCE -> AttendanceScreen(viewModel = vm)
                            AppScreen.OPERATIONS -> OperationsScreen(viewModel = vm)
                            AppScreen.ACADEMY -> AcademyScreen(viewModel = vm)
                            AppScreen.AI_ASSISTANT -> AiAssistantScreen(viewModel = vm)
                            AppScreen.AUDIT_LOGS -> AuditLogsScreen(viewModel = vm)
                            AppScreen.PROFILE -> DashboardScreen(viewModel = vm)
                        }
                    }

                    // Dialogs & Sheets
                    if (showRoleDialog) {
                        RoleSwitcherDialog(
                            allEmployees = vm.repository.allEmployees,
                            activeEmployee = activeEmployee,
                            onSelectEmployee = { vm.switchRole(it) },
                            onDismiss = { vm.showRoleDialog.value = false }
                        )
                    }

                    if (showCreateTaskDialog) {
                        val projects by vm.repository.projects.collectAsState()
                        CreateTaskDialog(
                            projects = projects,
                            employees = vm.repository.allEmployees,
                            onDismiss = { vm.showCreateTaskDialog.value = false },
                            onCreateTask = { title, desc, projId, assigneeId, priority, due, est ->
                                vm.repository.createTask(title, desc, projId, assigneeId, priority, due, est)
                            }
                        )
                    }

                    if (showBroadcastDialog) {
                        BroadcastAnnouncementDialog(
                            onDismiss = { vm.showBroadcastDialog.value = false },
                            onBroadcast = { title, content, isCritical ->
                                vm.repository.broadcastAnnouncement(title, content, isCritical)
                            }
                        )
                    }

                    if (showLeaveDialog) {
                        LeaveRequestDialog(
                            onDismiss = { vm.showLeaveDialog.value = false },
                            onSubmit = { type, start, end, days, reason ->
                                vm.repository.submitLeaveRequest(type, start, end, days, reason)
                            }
                        )
                    }

                    if (showExpenseDialog) {
                        ExpenseClaimDialog(
                            onDismiss = { vm.showExpenseDialog.value = false },
                            onSubmit = { title, cat, amt, ref ->
                                vm.repository.submitExpenseClaim(title, cat, amt, ref)
                            }
                        )
                    }

                    if (selectedPaySlip != null) {
                        PaySlipDetailsDialog(
                            slip = selectedPaySlip!!,
                            employee = activeEmployee,
                            onDismiss = { vm.selectedPaySlip.value = null }
                        )
                    }

                    if (showNotificationsSheet) {
                        val notifs by vm.repository.notifications.collectAsState()
                        NotificationsSheet(
                            notifications = notifs,
                            onMarkAllRead = { vm.repository.markNotificationsAsRead() },
                            onDismiss = { vm.showNotificationsSheet.value = false }
                        )
                    }
                }
            }
        }
    }
}
