package com.example.model

data class LeaveBalance(
    val annualTotal: Int = 20,
    val annualUsed: Int = 6,
    val sickTotal: Int = 10,
    val sickUsed: Int = 2,
    val casualTotal: Int = 7,
    val casualUsed: Int = 1
) {
    val annualRemaining: Int get() = annualTotal - annualUsed
    val sickRemaining: Int get() = sickTotal - sickUsed
    val casualRemaining: Int get() = casualTotal - casualUsed
}

data class Employee(
    val id: String,
    val employeeCode: String,
    val name: String,
    val email: String,
    val role: RoleEnum,
    val department: DepartmentEnum,
    val title: String,
    val phone: String,
    val location: String,
    val joinedDate: String,
    val workMode: WorkMode,
    val avatarColorHex: Long,
    val skills: List<String>,
    val leaveBalance: LeaveBalance = LeaveBalance(),
    val directReportsCount: Int = 0,
    val isOnline: Boolean = true,
    val emergencyContact: String = "Jane Doe (+1-555-0199)"
)

data class ChecklistItem(
    val id: String,
    val title: String,
    val isDone: Boolean
)

data class TaskItem(
    val id: String,
    val projectId: String,
    val projectTitle: String,
    val title: String,
    val description: String,
    val assigneeId: String,
    val assigneeName: String,
    val priority: TaskPriority,
    val status: TaskStatus,
    val dueDate: String,
    val estimatedHours: Int,
    val loggedHours: Int,
    val checklist: List<ChecklistItem> = emptyList(),
    val department: DepartmentEnum
)

data class Project(
    val id: String,
    val title: String,
    val code: String,
    val description: String,
    val department: DepartmentEnum,
    val progress: Int, // 0 - 100
    val budget: String,
    val deadline: String,
    val leadName: String,
    val activeTasksCount: Int,
    val teamMembersCount: Int
)

data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val authorName: String,
    val authorRole: String,
    val date: String,
    val isCritical: Boolean = false,
    val acknowledgesCount: Int = 142,
    val isAcknowledgedByMe: Boolean = false,
    val category: String = "Company Update"
)

data class ChatMessage(
    val id: String,
    val channelId: String, // e.g. "general", "engineering", "dm_alex"
    val senderId: String,
    val senderName: String,
    val senderRole: String,
    val senderColorHex: Long,
    val text: String,
    val timestamp: String,
    val isMe: Boolean = false,
    val reactions: Map<String, Int> = emptyMap()
)

data class ChatChannel(
    val id: String,
    val name: String,
    val description: String,
    val isDirectMessage: Boolean = false,
    val unreadCount: Int = 0,
    val lastMessagePreview: String = ""
)

data class LeaveRequest(
    val id: String,
    val employeeId: String,
    val employeeName: String,
    val employeeRole: String,
    val type: LeaveType,
    val startDate: String,
    val endDate: String,
    val daysCount: Int,
    val reason: String,
    val status: ApprovalStatus,
    val reviewerComment: String? = null,
    val submittedAt: String
)

data class ExpenseClaim(
    val id: String,
    val employeeId: String,
    val employeeName: String,
    val title: String,
    val category: ExpenseCategory,
    val amount: Double,
    val currency: String = "USD",
    val receiptReference: String,
    val status: ApprovalStatus,
    val submittedDate: String
)

data class PayrollSlip(
    val id: String,
    val employeeId: String,
    val period: String, // e.g. "October 2026"
    val baseSalary: Double,
    val performanceBonus: Double,
    val techAllowance: Double,
    val taxDeduction: Double,
    val healthInsurance: Double,
    val netSalary: Double,
    val paymentStatus: String = "Paid",
    val payDate: String = "Oct 25, 2026"
)

data class AcademyCourse(
    val id: String,
    val title: String,
    val instructor: String,
    val category: String,
    val duration: String,
    val modulesCount: Int,
    val completedModules: Int,
    val isCertified: Boolean = false,
    val description: String
) {
    val progressPercent: Int get() = if (modulesCount == 0) 0 else ((completedModules.toFloat() / modulesCount) * 100).toInt()
}

data class DocumentItem(
    val id: String,
    val title: String,
    val category: String,
    val size: String,
    val lastUpdated: String,
    val accessLevel: String = "Company-Wide"
)

data class AuditLog(
    val id: String,
    val timestamp: String,
    val actorName: String,
    val actorRole: String,
    val action: String,
    val target: String,
    val ipAddress: String,
    val severity: AuditSeverity
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val actionType: String = "GENERAL"
)

data class MeetingItem(
    val id: String,
    val title: String,
    val host: String,
    val time: String,
    val durationMinutes: Int,
    val meetLink: String,
    val attendeesCount: Int,
    val isToday: Boolean = true
)
