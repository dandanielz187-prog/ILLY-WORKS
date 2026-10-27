package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class IllyRepository {

    // Pre-seeded Team across all roles
    val allEmployees = listOf(
        Employee(
            id = "emp_ceo",
            employeeCode = "ILLY-001",
            name = "Daniel Illy",
            email = "daniel@illyprogramming.com",
            role = RoleEnum.CEO,
            department = DepartmentEnum.EXECUTIVE_BOARD,
            title = "Founder & Chief Executive Officer",
            phone = "+1 (555) 010-0001",
            location = "ILLY Executive Suite (15th Fl)",
            joinedDate = "Jan 2020",
            workMode = WorkMode.IN_OFFICE,
            avatarColorHex = 0xFFEF4444, // Red
            skills = listOf("Corporate Strategy", "Product Vision", "Venture Scaling", "AI Systems"),
            directReportsCount = 8
        ),
        Employee(
            id = "emp_vp",
            employeeCode = "ILLY-002",
            name = "Sarah Vance",
            email = "sarah.vance@illyprogramming.com",
            role = RoleEnum.EXECUTIVE,
            department = DepartmentEnum.CORE_ENGINEERING,
            title = "VP of Engineering & Systems",
            phone = "+1 (555) 010-0012",
            location = "ILLY Tech Tower (14th Fl)",
            joinedDate = "Mar 2021",
            workMode = WorkMode.HYBRID,
            avatarColorHex = 0xFF3B82F6, // Blue
            skills = listOf("Distributed Systems", "Cloud Arch", "Engineering Leadership", "Security"),
            directReportsCount = 24
        ),
        Employee(
            id = "emp_mgr",
            employeeCode = "ILLY-012",
            name = "Marcus Reed",
            email = "marcus.reed@illyprogramming.com",
            role = RoleEnum.MANAGER,
            department = DepartmentEnum.MOBILE_AND_CLOUD,
            title = "Engineering Manager - Platforms",
            phone = "+1 (555) 010-0044",
            location = "ILLY Tech Tower (14th Fl)",
            joinedDate = "Aug 2022",
            workMode = WorkMode.IN_OFFICE,
            avatarColorHex = 0xFF10B981, // Green
            skills = listOf("Agile Scrum", "Sprint Management", "Kotlin Multiplatform", "Kubernetes"),
            directReportsCount = 12
        ),
        Employee(
            id = "emp_lead",
            employeeCode = "ILLY-023",
            name = "Elena Rostova",
            email = "elena.rostova@illyprogramming.com",
            role = RoleEnum.TEAM_LEAD,
            department = DepartmentEnum.CORE_ENGINEERING,
            title = "Tech Lead - Backend & AI Pipelines",
            phone = "+1 (555) 010-0078",
            location = "Remote Secure (Berlin)",
            joinedDate = "Nov 2022",
            workMode = WorkMode.REMOTE,
            avatarColorHex = 0xFFA855F7, // Purple
            skills = listOf("Go", "Python", "gRPC", "Vector DBs", "Gemini AI API"),
            directReportsCount = 6
        ),
        Employee(
            id = "emp_dev",
            employeeCode = "ILLY-045",
            name = "Alex Chen",
            email = "alex.chen@illyprogramming.com",
            role = RoleEnum.DEVELOPER,
            department = DepartmentEnum.MOBILE_AND_CLOUD,
            title = "Senior Android / Jetpack Compose Dev",
            phone = "+1 (555) 010-0112",
            location = "ILLY Tech Tower (14th Fl)",
            joinedDate = "Feb 2023",
            workMode = WorkMode.HYBRID,
            avatarColorHex = 0xFF06B6D4, // Cyan
            skills = listOf("Kotlin", "Jetpack Compose", "Coroutines", "Room", "Material 3"),
            directReportsCount = 0
        ),
        Employee(
            id = "emp_dsgn",
            employeeCode = "ILLY-056",
            name = "Maya Lin",
            email = "maya.lin@illyprogramming.com",
            role = RoleEnum.DESIGNER,
            department = DepartmentEnum.UI_UX_DESIGN,
            title = "Lead Product & UI/UX Designer",
            phone = "+1 (555) 010-0145",
            location = "Remote Secure (San Francisco)",
            joinedDate = "Jul 2023",
            workMode = WorkMode.REMOTE,
            avatarColorHex = 0xFFEC4899, // Pink
            skills = listOf("Design Systems", "Figma Pro", "Motion Design", "Design Tokens", "Accessibility"),
            directReportsCount = 2
        ),
        Employee(
            id = "emp_intern",
            employeeCode = "ILLY-098",
            name = "Liam Brody",
            email = "liam.brody@illyprogramming.com",
            role = RoleEnum.INTERN,
            department = DepartmentEnum.ILLY_ACADEMY,
            title = "Software Engineering Intern",
            phone = "+1 (555) 010-0199",
            location = "ILLY Tech Tower (14th Fl)",
            joinedDate = "Sep 2026",
            workMode = WorkMode.IN_OFFICE,
            avatarColorHex = 0xFFF59E0B, // Amber
            skills = listOf("Kotlin Basics", "Compose Foundations", "Git", "Algorithms"),
            directReportsCount = 0
        )
    )

    // Current active logged in employee (defaults to CEO so user experiences command center first, but switchable)
    private val _activeEmployee = MutableStateFlow(allEmployees[0])
    val activeEmployee: StateFlow<Employee> = _activeEmployee.asStateFlow()

    fun switchActiveEmployee(employeeId: String) {
        val found = allEmployees.find { it.id == employeeId }
        if (found != null) {
            _activeEmployee.value = found
            addAuditLog(
                action = "ROLE_SESSION_SWITCH",
                target = "Switched active identity to ${found.name} (${found.role.name})",
                severity = AuditSeverity.INFO
            )
        }
    }

    // Projects
    private val _projects = MutableStateFlow(
        listOf(
            Project(
                id = "proj_cloud",
                title = "ILLY Cloud Platform 3.0",
                code = "PRJ-CLD",
                description = "Next-gen zero-trust microservice orchestration platform with real-time telemetries.",
                department = DepartmentEnum.CORE_ENGINEERING,
                progress = 78,
                budget = "$450,000",
                deadline = "Dec 15, 2026",
                leadName = "Sarah Vance",
                activeTasksCount = 14,
                teamMembersCount = 18
            ),
            Project(
                id = "proj_copilot",
                title = "ILLY AI Assistant & Copilot Engine",
                code = "PRJ-AIC",
                description = "Enterprise LLM system powered by Gemini 3.8 Flash for workflow automation & coding assistance.",
                department = DepartmentEnum.CORE_ENGINEERING,
                progress = 85,
                budget = "$320,000",
                deadline = "Nov 30, 2026",
                leadName = "Elena Rostova",
                activeTasksCount = 9,
                teamMembersCount = 11
            ),
            Project(
                id = "proj_mobile",
                title = "ILLY Mobile Workplace Suite",
                code = "PRJ-MBL",
                description = "State of the art Jetpack Compose mobile app connecting all ILLY branches worldwide.",
                department = DepartmentEnum.MOBILE_AND_CLOUD,
                progress = 92,
                budget = "$210,000",
                deadline = "Nov 10, 2026",
                leadName = "Marcus Reed",
                activeTasksCount = 6,
                teamMembersCount = 8
            ),
            Project(
                id = "proj_design",
                title = "ILLY Brand & Design System 2026",
                code = "PRJ-DSN",
                description = "Unified high-contrast dark aesthetic design tokens, glassmorphism cards, and M3 components.",
                department = DepartmentEnum.UI_UX_DESIGN,
                progress = 64,
                budget = "$120,000",
                deadline = "Jan 20, 2027",
                leadName = "Maya Lin",
                activeTasksCount = 8,
                teamMembersCount = 5
            )
        )
    )
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    // Tasks
    private val _tasks = MutableStateFlow(
        listOf(
            TaskItem(
                id = "tsk_1",
                projectId = "proj_mobile",
                projectTitle = "ILLY Mobile Workplace Suite",
                title = "Optimize App Check Attestation & Launch Flow",
                description = "Ensure App Check token synchronizes during cold boot and configure graceful fallback.",
                assigneeId = "emp_dev",
                assigneeName = "Alex Chen",
                priority = TaskPriority.CRITICAL,
                status = TaskStatus.IN_PROGRESS,
                dueDate = "Oct 28, 2026",
                estimatedHours = 12,
                loggedHours = 8,
                department = DepartmentEnum.MOBILE_AND_CLOUD,
                checklist = listOf(
                    ChecklistItem("chk_1", "Audit SharedPreferences debug key persistence", true),
                    ChecklistItem("chk_2", "Install Recaptcha provider for release builds", true),
                    ChecklistItem("chk_3", "Benchmark cold start latency under 400ms", false)
                )
            ),
            TaskItem(
                id = "tsk_2",
                projectId = "proj_copilot",
                projectTitle = "ILLY AI Assistant & Copilot Engine",
                title = "Deploy Gemini 3.8 Flash Streaming Assistant",
                description = "Implement low-latency streaming inference for work-related queries and policy Q&A.",
                assigneeId = "emp_lead",
                assigneeName = "Elena Rostova",
                priority = TaskPriority.HIGH,
                status = TaskStatus.IN_REVIEW,
                dueDate = "Oct 30, 2026",
                estimatedHours = 16,
                loggedHours = 14,
                department = DepartmentEnum.CORE_ENGINEERING,
                checklist = listOf(
                    ChecklistItem("chk_4", "Configure prompt system instructions for ILLY policies", true),
                    ChecklistItem("chk_5", "Add corporate knowledge fallback cache", true),
                    ChecklistItem("chk_6", "Sanitize markdown response blocks in UI", true)
                )
            ),
            TaskItem(
                id = "tsk_3",
                projectId = "proj_design",
                projectTitle = "ILLY Brand & Design System 2026",
                title = "Finalize Red & Cobalt Blue Glassmorphism Tokens",
                description = "Create cohesive component specifications for cards, buttons, badges and bottom navigation.",
                assigneeId = "emp_dsgn",
                assigneeName = "Maya Lin",
                priority = TaskPriority.HIGH,
                status = TaskStatus.DONE,
                dueDate = "Oct 25, 2026",
                estimatedHours = 20,
                loggedHours = 20,
                department = DepartmentEnum.UI_UX_DESIGN,
                checklist = listOf(
                    ChecklistItem("chk_7", "Define color tokens in Figma library", true),
                    ChecklistItem("chk_8", "Export SVG assets and adaptive app icon", true),
                    ChecklistItem("chk_9", "Hand off to mobile engineering team", true)
                )
            ),
            TaskItem(
                id = "tsk_4",
                projectId = "proj_cloud",
                projectTitle = "ILLY Cloud Platform 3.0",
                title = "Multi-Region Distributed Database Syncing",
                description = "Setup replication topology across US-East and EU-Central with automated failover.",
                assigneeId = "emp_mgr",
                assigneeName = "Marcus Reed",
                priority = TaskPriority.MEDIUM,
                status = TaskStatus.IN_PROGRESS,
                dueDate = "Nov 05, 2026",
                estimatedHours = 35,
                loggedHours = 18,
                department = DepartmentEnum.CORE_ENGINEERING,
                checklist = listOf(
                    ChecklistItem("chk_10", "Deploy test clusters in staging sandbox", true),
                    ChecklistItem("chk_11", "Simulate network partition recovery", false)
                )
            ),
            TaskItem(
                id = "tsk_5",
                projectId = "proj_mobile",
                projectTitle = "ILLY Mobile Workplace Suite",
                title = "Implement Working Hours & Geolocation Punch-Clock",
                description = "Build interactive timer with In-Office / Remote / Hybrid verification tags.",
                assigneeId = "emp_intern",
                assigneeName = "Liam Brody",
                priority = TaskPriority.MEDIUM,
                status = TaskStatus.BACKLOG,
                dueDate = "Nov 02, 2026",
                estimatedHours = 10,
                loggedHours = 2,
                department = DepartmentEnum.ILLY_ACADEMY,
                checklist = listOf(
                    ChecklistItem("chk_12", "Draft Compose clock card UI", true),
                    ChecklistItem("chk_13", "Hook up coroutine timer loop", false)
                )
            )
        )
    )
    val tasks: StateFlow<List<TaskItem>> = _tasks.asStateFlow()

    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) task.copy(status = newStatus) else task
        }
        addAuditLog(
            action = "TASK_STATUS_UPDATED",
            target = "Task $taskId changed to ${newStatus.label}",
            severity = AuditSeverity.INFO
        )
    }

    fun toggleTaskChecklist(taskId: String, checkId: String) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                val updatedCheck = task.checklist.map { item ->
                    if (item.id == checkId) item.copy(isDone = !item.isDone) else item
                }
                task.copy(checklist = updatedCheck)
            } else task
        }
    }

    fun createTask(
        title: String,
        description: String,
        projectId: String,
        assigneeId: String,
        priority: TaskPriority,
        dueDate: String,
        estimatedHours: Int
    ) {
        val proj = _projects.value.find { it.id == projectId } ?: _projects.value.first()
        val assignee = allEmployees.find { it.id == assigneeId } ?: _activeEmployee.value
        val newTask = TaskItem(
            id = "tsk_" + UUID.randomUUID().toString().take(6),
            projectId = proj.id,
            projectTitle = proj.title,
            title = title,
            description = description,
            assigneeId = assignee.id,
            assigneeName = assignee.name,
            priority = priority,
            status = TaskStatus.BACKLOG,
            dueDate = dueDate.ifBlank { "Nov 15, 2026" },
            estimatedHours = estimatedHours,
            loggedHours = 0,
            department = proj.department,
            checklist = listOf(
                ChecklistItem("c1", "Initial setup & requirements", false),
                ChecklistItem("c2", "Implementation & unit testing", false),
                ChecklistItem("c3", "Peer review & QA signoff", false)
            )
        )
        _tasks.value = listOf(newTask) + _tasks.value
        addAuditLog(
            action = "TASK_CREATED",
            target = "Created task '$title' assigned to ${assignee.name}",
            severity = AuditSeverity.INFO
        )
    }

    // Company-Wide Announcements
    private val _announcements = MutableStateFlow(
        listOf(
            Announcement(
                id = "ann_1",
                title = "ILLY Q4 All-Hands Meeting & Company Milestone",
                content = "Team, we have officially surpassed 1,000,000 active users across our client systems! Join us on Thursday at 3:00 PM EST for our strategic roadmap address and bonus distribution announcement.",
                authorName = "Daniel Illy",
                authorRole = "CEO",
                date = "Today, 09:30 AM",
                isCritical = true,
                acknowledgesCount = 289,
                isAcknowledgedByMe = true,
                category = "Executive Broadcast"
            ),
            Announcement(
                id = "ann_2",
                title = "New Remote Work & Equipment Stipend Policy",
                content = "Starting November 1st, all engineers and designers are eligible for an annual $1,500 home workstation and wellness upgrade. Check the Operations tab to submit claims.",
                authorName = "Sarah Vance",
                authorRole = "VP of Engineering",
                date = "Yesterday",
                isCritical = false,
                acknowledgesCount = 312,
                isAcknowledgedByMe = false,
                category = "HR & Workplace"
            ),
            Announcement(
                id = "ann_3",
                title = "ILLY Academy: New Gemini AI Certification Track",
                content = "Learn how to build multimodal agents and structured workflows with Gemini 3.8. Complete the course and quiz to earn your official ILLY AI Architect badge.",
                authorName = "Elena Rostova",
                authorRole = "Academy Lead",
                date = "Oct 24, 2026",
                isCritical = false,
                acknowledgesCount = 195,
                isAcknowledgedByMe = true,
                category = "Academy"
            )
        )
    )
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    fun acknowledgeAnnouncement(announcementId: String) {
        _announcements.value = _announcements.value.map { ann ->
            if (ann.id == announcementId && !ann.isAcknowledgedByMe) {
                ann.copy(
                    isAcknowledgedByMe = true,
                    acknowledgesCount = ann.acknowledgesCount + 1
                )
            } else ann
        }
    }

    fun broadcastAnnouncement(title: String, content: String, isCritical: Boolean) {
        val newAnn = Announcement(
            id = "ann_" + UUID.randomUUID().toString().take(6),
            title = title,
            content = content,
            authorName = _activeEmployee.value.name,
            authorRole = _activeEmployee.value.role.name,
            date = "Just now",
            isCritical = isCritical,
            acknowledgesCount = 1,
            isAcknowledgedByMe = true,
            category = "Executive Alert"
        )
        _announcements.value = listOf(newAnn) + _announcements.value
        addAuditLog(
            action = "EXECUTIVE_BROADCAST",
            target = "Broadcast announcement: '$title'",
            severity = if (isCritical) AuditSeverity.WARNING else AuditSeverity.INFO
        )
    }

    // Channels & Messages
    private val _channels = MutableStateFlow(
        listOf(
            ChatChannel("general", "#general", "Company-wide general discussions & coffee chat", false, 0, "Daniel: Welcome to the new sprint!"),
            ChatChannel("engineering", "#engineering", "Technical architecture, PRs, and system design", false, 2, "Elena: The AI pipeline PR is ready for review."),
            ChatChannel("design-system", "#design-system", "ILLY Brand visuals, UI components, and Figma links", false, 0, "Maya: Uploaded the new red & blue glass buttons."),
            ChatChannel("announcements", "#announcements", "Read-only broadcast channel for leadership updates", false, 1, "Executive Broadcast: Q4 All-Hands Meeting"),
            ChatChannel("dm_daniel", "Daniel Illy (CEO)", "Direct Message with CEO", true, 0, "Looking forward to your demo today."),
            ChatChannel("dm_sarah", "Sarah Vance (VP)", "Direct Message with VP of Eng", true, 1, "Can you review the architecture draft?"),
            ChatChannel("dm_alex", "Alex Chen (Senior Dev)", "Direct Message with Alex", true, 0, "Compose insets are all sorted.")
        )
    )
    val channels: StateFlow<List<ChatChannel>> = _channels.asStateFlow()

    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage("m1", "general", "emp_ceo", "Daniel Illy", "CEO", 0xFFEF4444, "Welcome everyone to ILLY WORKS! One Team. One System. One ILLY. 🚀", "09:00 AM", false, mapOf("🔥" to 18, "👏" to 24)),
            ChatMessage("m2", "general", "emp_vp", "Sarah Vance", "VP Eng", 0xFF3B82F6, "Let's push the new mobile and cloud release today. All systems green.", "09:05 AM", false, mapOf("🚀" to 12)),
            ChatMessage("m3", "engineering", "emp_mgr", "Marcus Reed", "Eng Manager", 0xFF10B981, "Sprint 42 kickoff is in 30 minutes in Meeting Room A / ILLY Meet link.", "10:15 AM", false),
            ChatMessage("m4", "engineering", "emp_lead", "Elena Rostova", "Tech Lead", 0xFFA855F7, "The Gemini 3.8 Flash model latency is under 400ms TTFT. Remarkable speed.", "10:18 AM", false, mapOf("⚡" to 9)),
            ChatMessage("m5", "engineering", "emp_dev", "Alex Chen", "Senior Dev", 0xFF06B6D4, "I've enabled edge-to-edge and safe insets across the entire applet navigation.", "10:22 AM", false, mapOf("❤️" to 7)),
            ChatMessage("m6", "dm_daniel", "emp_ceo", "Daniel Illy", "CEO", 0xFFEF4444, "Hi there, great progress on the workplace rollout. Let me know if you need any executive resources.", "Yesterday", false)
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun sendMessage(channelId: String, text: String) {
        val me = _activeEmployee.value
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val newMsg = ChatMessage(
            id = "msg_" + UUID.randomUUID().toString().take(6),
            channelId = channelId,
            senderId = me.id,
            senderName = me.name,
            senderRole = me.role.title,
            senderColorHex = me.avatarColorHex,
            text = text,
            timestamp = timeNow,
            isMe = true
        )
        _messages.value = _messages.value + newMsg
    }

    // Attendance & Working Hours
    private val _isPunchedIn = MutableStateFlow(true)
    val isPunchedIn: StateFlow<Boolean> = _isPunchedIn.asStateFlow()

    private val _punchInTime = MutableStateFlow("08:45 AM")
    val punchInTime: StateFlow<String> = _punchInTime.asStateFlow()

    private val _currentWorkMode = MutableStateFlow(WorkMode.IN_OFFICE)
    val currentWorkMode: StateFlow<WorkMode> = _currentWorkMode.asStateFlow()

    fun togglePunchClock() {
        val newPunched = !_isPunchedIn.value
        _isPunchedIn.value = newPunched
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        if (newPunched) {
            _punchInTime.value = timeNow
            addAuditLog(
                action = "ATTENDANCE_PUNCH_IN",
                target = "${_activeEmployee.value.name} punched in at $timeNow (${_currentWorkMode.value.label})",
                severity = AuditSeverity.INFO
            )
        } else {
            addAuditLog(
                action = "ATTENDANCE_PUNCH_OUT",
                target = "${_activeEmployee.value.name} punched out at $timeNow",
                severity = AuditSeverity.INFO
            )
        }
    }

    fun setWorkMode(mode: WorkMode) {
        _currentWorkMode.value = mode
    }

    // Leave & Permissions Requests
    private val _leaveRequests = MutableStateFlow(
        listOf(
            LeaveRequest(
                id = "lv_1",
                employeeId = "emp_dev",
                employeeName = "Alex Chen",
                employeeRole = "Senior Dev",
                type = LeaveType.ANNUAL,
                startDate = "Nov 12, 2026",
                endDate = "Nov 15, 2026",
                daysCount = 4,
                reason = "Family gathering and long weekend vacation.",
                status = ApprovalStatus.PENDING,
                submittedAt = "Oct 24, 2026"
            ),
            LeaveRequest(
                id = "lv_2",
                employeeId = "emp_intern",
                employeeName = "Liam Brody",
                employeeRole = "Intern",
                type = LeaveType.REMOTE_DAY,
                startDate = "Oct 30, 2026",
                endDate = "Oct 30, 2026",
                daysCount = 1,
                reason = "Attending university capstone presentation.",
                status = ApprovalStatus.APPROVED,
                reviewerComment = "Approved. Ensure task deliverables are logged.",
                submittedAt = "Oct 22, 2026"
            ),
            LeaveRequest(
                id = "lv_3",
                employeeId = "emp_dsgn",
                employeeName = "Maya Lin",
                employeeRole = "Lead Designer",
                type = LeaveType.SICK,
                startDate = "Oct 18, 2026",
                endDate = "Oct 19, 2026",
                daysCount = 2,
                reason = "Severe migraine and medical checkup.",
                status = ApprovalStatus.APPROVED,
                reviewerComment = "Get well soon!",
                submittedAt = "Oct 18, 2026"
            )
        )
    )
    val leaveRequests: StateFlow<List<LeaveRequest>> = _leaveRequests.asStateFlow()

    fun submitLeaveRequest(type: LeaveType, startDate: String, endDate: String, days: Int, reason: String) {
        val me = _activeEmployee.value
        val newReq = LeaveRequest(
            id = "lv_" + UUID.randomUUID().toString().take(6),
            employeeId = me.id,
            employeeName = me.name,
            employeeRole = me.role.title,
            type = type,
            startDate = startDate,
            endDate = endDate,
            daysCount = days,
            reason = reason,
            status = ApprovalStatus.PENDING,
            submittedAt = "Just now"
        )
        _leaveRequests.value = listOf(newReq) + _leaveRequests.value
        addAuditLog(
            action = "LEAVE_REQUEST_SUBMITTED",
            target = "${me.name} requested $days days of ${type.label}",
            severity = AuditSeverity.INFO
        )
    }

    fun reviewLeaveRequest(leaveId: String, isApproved: Boolean, comment: String) {
        _leaveRequests.value = _leaveRequests.value.map { req ->
            if (req.id == leaveId) {
                req.copy(
                    status = if (isApproved) ApprovalStatus.APPROVED else ApprovalStatus.REJECTED,
                    reviewerComment = comment.ifBlank { if (isApproved) "Approved by ${_activeEmployee.value.name}" else "Declined" }
                )
            } else req
        }
        addAuditLog(
            action = if (isApproved) "LEAVE_APPROVED" else "LEAVE_REJECTED",
            target = "Leave $leaveId reviewed by ${_activeEmployee.value.name}",
            severity = AuditSeverity.INFO
        )
    }

    // Expense & Reimbursement Claims
    private val _expenseClaims = MutableStateFlow(
        listOf(
            ExpenseClaim(
                id = "exp_1",
                employeeId = "emp_dev",
                employeeName = "Alex Chen",
                title = "JetBrains All Products Annual License",
                category = ExpenseCategory.SOFTWARE,
                amount = 289.00,
                receiptReference = "INV-JB-2026-9912.pdf",
                status = ApprovalStatus.APPROVED,
                submittedDate = "Oct 20, 2026"
            ),
            ExpenseClaim(
                id = "exp_2",
                employeeId = "emp_dsgn",
                employeeName = "Maya Lin",
                title = "Wacom Intuos Pro Graphic Tablet",
                category = ExpenseCategory.HARDWARE,
                amount = 379.99,
                receiptReference = "AMZN-REC-8841.pdf",
                status = ApprovalStatus.PENDING,
                submittedDate = "Oct 25, 2026"
            ),
            ExpenseClaim(
                id = "exp_3",
                employeeId = "emp_mgr",
                employeeName = "Marcus Reed",
                title = "Sprint 41 Post-Launch Team Dinner",
                category = ExpenseCategory.TEAM_MEAL,
                amount = 412.50,
                receiptReference = "BISTRO-RECEIPT-541.pdf",
                status = ApprovalStatus.PENDING,
                submittedDate = "Yesterday"
            )
        )
    )
    val expenseClaims: StateFlow<List<ExpenseClaim>> = _expenseClaims.asStateFlow()

    fun submitExpenseClaim(title: String, category: ExpenseCategory, amount: Double, receiptRef: String) {
        val me = _activeEmployee.value
        val newClaim = ExpenseClaim(
            id = "exp_" + UUID.randomUUID().toString().take(6),
            employeeId = me.id,
            employeeName = me.name,
            title = title,
            category = category,
            amount = amount,
            receiptReference = receiptRef.ifBlank { "RECEIPT_${System.currentTimeMillis()}.pdf" },
            status = ApprovalStatus.PENDING,
            submittedDate = "Today"
        )
        _expenseClaims.value = listOf(newClaim) + _expenseClaims.value
        addAuditLog(
            action = "EXPENSE_CLAIM_SUBMITTED",
            target = "${me.name} submitted claim for $$amount ($title)",
            severity = AuditSeverity.INFO
        )
    }

    fun reviewExpenseClaim(expenseId: String, isApproved: Boolean) {
        _expenseClaims.value = _expenseClaims.value.map { exp ->
            if (exp.id == expenseId) {
                exp.copy(status = if (isApproved) ApprovalStatus.APPROVED else ApprovalStatus.REJECTED)
            } else exp
        }
        addAuditLog(
            action = if (isApproved) "EXPENSE_APPROVED" else "EXPENSE_REJECTED",
            target = "Expense $expenseId reviewed by ${_activeEmployee.value.name}",
            severity = AuditSeverity.INFO
        )
    }

    // Payroll & Pay Slips
    private val _payrollSlips = MutableStateFlow(
        listOf(
            PayrollSlip("pay_1", "emp_ceo", "October 2026", 18500.0, 4500.0, 1000.0, 5200.0, 450.0, 18350.0),
            PayrollSlip("pay_2", "emp_vp", "October 2026", 14200.0, 3200.0, 800.0, 3900.0, 450.0, 13850.0),
            PayrollSlip("pay_3", "emp_mgr", "October 2026", 11000.0, 2000.0, 600.0, 2800.0, 450.0, 10350.0),
            PayrollSlip("pay_4", "emp_lead", "October 2026", 9500.0, 1800.0, 500.0, 2400.0, 400.0, 9000.0),
            PayrollSlip("pay_5", "emp_dev", "October 2026", 8200.0, 1200.0, 400.0, 1950.0, 400.0, 7450.0),
            PayrollSlip("pay_6", "emp_dsgn", "October 2026", 7800.0, 1000.0, 400.0, 1800.0, 400.0, 7000.0),
            PayrollSlip("pay_7", "emp_intern", "October 2026", 3200.0, 400.0, 200.0, 450.0, 200.0, 3150.0)
        )
    )
    val payrollSlips: StateFlow<List<PayrollSlip>> = _payrollSlips.asStateFlow()

    // ILLY Academy
    private val _academyCourses = MutableStateFlow(
        listOf(
            AcademyCourse(
                id = "acad_1",
                title = "Advanced Jetpack Compose 2026",
                instructor = "Alex Chen & Android Guild",
                category = "Mobile Engineering",
                duration = "14 Hours",
                modulesCount = 8,
                completedModules = 6,
                isCertified = false,
                description = "Master Compose 1.7+, edge-to-edge system insets, custom canvas layouts, and fluid performance."
            ),
            AcademyCourse(
                id = "acad_2",
                title = "Gemini 3.8 Flash & Multimodal AI Agents",
                instructor = "Elena Rostova (Tech Lead)",
                category = "Artificial Intelligence",
                duration = "18 Hours",
                modulesCount = 10,
                completedModules = 10,
                isCertified = true,
                description = "Structured JSON outputs, function calling, streaming tokens, and zero-trust App Check integration."
            ),
            AcademyCourse(
                id = "acad_3",
                title = "ILLY Dark Glassmorphic Design Standards",
                instructor = "Maya Lin (Design Lead)",
                category = "Design Systems",
                duration = "8 Hours",
                modulesCount = 5,
                completedModules = 3,
                isCertified = false,
                description = "Cohesive color contrast using Red (#EF4444) and Blue (#3B82F6), accessibility, and micro-animations."
            ),
            AcademyCourse(
                id = "acad_4",
                title = "High-Concurreny Cloud Microservices",
                instructor = "Sarah Vance (VP Eng)",
                category = "Cloud Architecture",
                duration = "22 Hours",
                modulesCount = 12,
                completedModules = 4,
                isCertified = false,
                description = "Kubernetes orchestration, distributed caching, and zero-downtime rolling deployments."
            )
        )
    )
    val academyCourses: StateFlow<List<AcademyCourse>> = _academyCourses.asStateFlow()

    fun completeModule(courseId: String) {
        _academyCourses.value = _academyCourses.value.map { course ->
            if (course.id == courseId && course.completedModules < course.modulesCount) {
                val nextCompleted = course.completedModules + 1
                course.copy(
                    completedModules = nextCompleted,
                    isCertified = nextCompleted == course.modulesCount
                )
            } else course
        }
    }

    // Document Sharing & Policies
    private val _documents = MutableStateFlow(
        listOf(
            DocumentItem("doc_1", "ILLY Employee Handbook & Culture 2026", "Operations", "4.2 MB", "Oct 15, 2026"),
            DocumentItem("doc_2", "Engineering Architecture & Code Quality Guide", "Engineering", "8.9 MB", "Oct 20, 2026"),
            DocumentItem("doc_3", "Data Privacy, Security & NDA Agreement", "Legal & Security", "2.1 MB", "Sep 01, 2026"),
            DocumentItem("doc_4", "ILLY Brand Guidelines & Vector Asset Pack", "Design", "45.0 MB", "Oct 10, 2026"),
            DocumentItem("doc_5", "Remote Work & Travel Reimbursement Policy", "Finance", "1.8 MB", "Oct 22, 2026")
        )
    )
    val documents: StateFlow<List<DocumentItem>> = _documents.asStateFlow()

    // Meetings
    private val _meetings = MutableStateFlow(
        listOf(
            MeetingItem("mtg_1", "ILLY Daily Executive & Tech Sync", "Sarah Vance", "10:00 AM - 10:30 AM", 30, "https://meet.illy.dev/daily-sync", 14, true),
            MeetingItem("mtg_2", "Sprint 42 Architecture Review", "Marcus Reed", "01:30 PM - 02:30 PM", 60, "https://meet.illy.dev/arch-rev", 8, true),
            MeetingItem("mtg_3", "Mobile UI Glassmorphism Demo", "Maya Lin", "04:00 PM - 04:45 PM", 45, "https://meet.illy.dev/ui-demo", 6, true),
            MeetingItem("mtg_4", "Q4 Board Strategy Session", "Daniel Illy", "Tomorrow, 11:00 AM", 90, "https://meet.illy.dev/board", 5, false)
        )
    )
    val meetings: StateFlow<List<MeetingItem>> = _meetings.asStateFlow()

    // Security & Audit Logs
    private val _auditLogs = MutableStateFlow(
        listOf(
            AuditLog("aud_1", "Today, 10:22 AM", "Alex Chen", "Senior Dev", "REPOSITORY_PUSH", "git.illy.dev/mobile-workplace", "192.168.1.104", AuditSeverity.INFO),
            AuditLog("aud_2", "Today, 09:30 AM", "Daniel Illy", "CEO", "EXECUTIVE_BROADCAST", "Announcement #ann_1 Published", "10.0.0.1 (VPN HQ)", AuditSeverity.INFO),
            AuditLog("aud_3", "Today, 08:45 AM", "Daniel Illy", "CEO", "SECURITY_LOGIN_SUCCESS", "Executive SSO via Jetpack Credential", "10.0.0.1", AuditSeverity.INFO),
            AuditLog("aud_4", "Yesterday, 06:14 PM", "System", "Automated Watcher", "DATABASE_BACKUP_COMPLETED", "Encrypted Cloud Snapshot #9921", "Internal", AuditSeverity.INFO),
            AuditLog("aud_5", "Yesterday, 02:40 PM", "Marcus Reed", "Eng Manager", "PAYROLL_AUDIT_CHECK", "Payroll Access Verified", "192.168.1.52", AuditSeverity.WARNING)
        )
    )
    val auditLogs: StateFlow<List<AuditLog>> = _auditLogs.asStateFlow()

    private fun addAuditLog(action: String, target: String, severity: AuditSeverity) {
        val timeNow = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date())
        val log = AuditLog(
            id = "aud_" + UUID.randomUUID().toString().take(6),
            timestamp = timeNow,
            actorName = _activeEmployee.value.name,
            actorRole = _activeEmployee.value.role.title,
            action = action,
            target = target,
            ipAddress = "10.0.4.12 (Secure Session)",
            severity = severity
        )
        _auditLogs.value = listOf(log) + _auditLogs.value
    }

    // Notifications
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem("notif_1", "New Task Assigned", "You were assigned to 'Optimize App Check Attestation'.", "15m ago", false, "TASK"),
            NotificationItem("notif_2", "Meeting Starting Soon", "Sprint 42 Architecture Review starts in 15 mins.", "25m ago", false, "MEETING"),
            NotificationItem("notif_3", "Executive Announcement", "Daniel Illy broadcasted Q4 Milestone update.", "1h ago", true, "BROADCAST"),
            NotificationItem("notif_4", "Expense Claim Approved", "Your JetBrains software license was approved.", "Yesterday", true, "EXPENSE")
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun markNotificationsAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }
}
