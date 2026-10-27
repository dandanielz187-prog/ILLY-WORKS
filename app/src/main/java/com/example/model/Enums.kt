package com.example.model

enum class RoleEnum(val title: String, val level: Int) {
    CEO("Chief Executive Officer", 100),
    EXECUTIVE("Executive / VP", 80),
    MANAGER("Engineering Manager", 60),
    TEAM_LEAD("Technical Team Lead", 50),
    DEVELOPER("Senior / Staff Developer", 40),
    DESIGNER("Product / UI Designer", 40),
    INTERN("Software Engineering Intern", 20);

    val isLeadership: Boolean get() = level >= 60
    val isCeo: Boolean get() = this == CEO
    val canApproveRequests: Boolean get() = level >= 50
    val canManageProjects: Boolean get() = level >= 50
    val canBroadcastAnnouncement: Boolean get() = level >= 60
    val canAccessAuditLogs: Boolean get() = level >= 80
    val canAccessCommandCenter: Boolean get() = level >= 60
    val canViewCompanyFinances: Boolean get() = level >= 80
}

enum class DepartmentEnum(val displayName: String, val code: String) {
    EXECUTIVE_BOARD("Executive Board", "EXEC"),
    CORE_ENGINEERING("Core Engineering", "ENG"),
    MOBILE_AND_CLOUD("Mobile & Cloud Systems", "MCLD"),
    UI_UX_DESIGN("UI/UX & Product Design", "DSGN"),
    QA_AND_SECURITY("QA & Cyber Security", "SECA"),
    PRODUCT_OPERATIONS("Product Operations", "POPS"),
    ILLY_ACADEMY("ILLY Academy & Research", "ACAD")
}

enum class WorkMode(val label: String) {
    IN_OFFICE("In-Office (HQ Tower)"),
    REMOTE("Remote Secure"),
    HYBRID("Hybrid Mode")
}

enum class TaskPriority(val label: String) {
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

enum class TaskStatus(val label: String) {
    BACKLOG("Backlog"),
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    DONE("Completed")
}

enum class ApprovalStatus(val label: String) {
    PENDING("Pending Review"),
    APPROVED("Approved"),
    REJECTED("Rejected")
}

enum class LeaveType(val label: String) {
    ANNUAL("Annual Vacation"),
    SICK("Sick Leave"),
    CASUAL("Casual Leave"),
    REMOTE_DAY("Remote Work Day"),
    TRAINING("Academy Training")
}

enum class ExpenseCategory(val label: String) {
    SOFTWARE("Software & Cloud Tools"),
    HARDWARE("Hardware & Equipment"),
    TEAM_MEAL("Team Working Meal"),
    TRAVEL("Travel & Client Meeting"),
    CONFERENCE("Tech Conference / Cert")
}

enum class AuditSeverity(val label: String) {
    INFO("Info"),
    WARNING("Warning"),
    CRITICAL("Security Alert")
}
