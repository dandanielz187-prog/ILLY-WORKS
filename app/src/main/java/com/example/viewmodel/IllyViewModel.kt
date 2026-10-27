package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.IllyAiService
import com.example.data.IllyRepository
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppScreen(val label: String) {
    DASHBOARD("Dashboard"),
    PROJECTS("Projects"),
    CHAT("Team Chat"),
    ATTENDANCE("Attendance"),
    OPERATIONS("Operations"),
    ACADEMY("Academy"),
    AI_ASSISTANT("ILLY AI"),
    AUDIT_LOGS("Audit Trail"),
    PROFILE("My Profile")
}

data class AiChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String
)

class IllyViewModel(
    val repository: IllyRepository = IllyRepository(),
    private val aiService: IllyAiService = IllyAiService()
) : ViewModel() {

    // Current Screen
    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Live Working Hours Clock Timer (Running when punched in)
    private val _liveWorkingSeconds = MutableStateFlow(28800L + 1920L) // seeded ~8h 32m
    val liveWorkingSeconds: StateFlow<Long> = _liveWorkingSeconds.asStateFlow()

    private var clockJob: Job? = null

    init {
        startLiveClock()
    }

    private fun startLiveClock() {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                if (repository.isPunchedIn.value) {
                    _liveWorkingSeconds.value += 1
                }
            }
        }
    }

    // Role Switcher
    fun switchRole(employeeId: String) {
        repository.switchActiveEmployee(employeeId)
    }

    // AI Assistant State
    private val _aiMessages = MutableStateFlow(
        listOf(
            AiChatMessage(
                id = "ai_welcome",
                isUser = false,
                text = "Greetings! I am the **ILLY AI Assistant**, your corporate intelligence partner.\n\nHow can I help you today? I can summarize sprint health, clarify ILLY workplace policies, draft team announcements, or explain company engineering standards.",
                timestamp = "Online"
            )
        )
    )
    val aiMessages: StateFlow<List<AiChatMessage>> = _aiMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    fun sendAiPrompt(promptText: String) {
        if (promptText.isBlank()) return
        val userMsg = AiChatMessage(
            id = "u_" + System.currentTimeMillis(),
            isUser = true,
            text = promptText,
            timestamp = "Just now"
        )
        _aiMessages.value = _aiMessages.value + userMsg

        viewModelScope.launch {
            _isAiLoading.value = true
            val role = repository.activeEmployee.value.role.title
            val responseText = aiService.queryAssistant(promptText, role)
            val aiMsg = AiChatMessage(
                id = "ai_" + System.currentTimeMillis(),
                isUser = false,
                text = responseText,
                timestamp = "Just now"
            )
            _aiMessages.value = _aiMessages.value + aiMsg
            _isAiLoading.value = false
        }
    }

    fun clearAiChat() {
        _aiMessages.value = listOf(
            AiChatMessage(
                id = "ai_welcome",
                isUser = false,
                text = "Chat cleared. Ready for your next query!",
                timestamp = "Online"
            )
        )
    }

    // Dialog & Sheet States
    val showCreateTaskDialog = MutableStateFlow(false)
    val showBroadcastDialog = MutableStateFlow(false)
    val showLeaveDialog = MutableStateFlow(false)
    val showExpenseDialog = MutableStateFlow(false)
    val showRoleDialog = MutableStateFlow(false)
    val showNotificationsSheet = MutableStateFlow(false)
    val selectedPaySlip = MutableStateFlow<PayrollSlip?>(null)
    val selectedProjectFilter = MutableStateFlow<String>("ALL")
}
