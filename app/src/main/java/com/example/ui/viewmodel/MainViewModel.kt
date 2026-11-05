package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ApplicationEntity
import com.example.data.local.FraudReportEntity
import com.example.data.local.JobEntity
import com.example.data.local.SubscriptionPlanEntity
import com.example.data.local.UserEntity
import com.example.data.local.YaqeenDatabase
import com.example.data.model.InterviewFeedback
import com.example.data.model.JobCategory
import com.example.data.model.Language
import com.example.data.model.MockInterviewQuestion
import com.example.data.model.UserRole
import com.example.data.repository.JobRepository
import com.example.service.AiCareerService
import com.example.service.FraudRiskAnalysis
import com.example.service.GeneratedJobDescription
import com.example.service.GeneratedResume
import com.example.service.LocalizationHelper
import com.example.service.PaymentService
import com.example.service.SkillMatchReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = YaqeenDatabase.getDatabase(application, viewModelScope)
    private val repository = JobRepository(
        jobDao = database.jobDao(),
        applicationDao = database.applicationDao(),
        savedJobDao = database.savedJobDao(),
        userDao = database.userDao(),
        fraudReportDao = database.fraudReportDao(),
        subscriptionPlanDao = database.subscriptionPlanDao(),
        auditLogDao = database.auditLogDao(),
        notificationDao = database.notificationDao()
    )

    // Current State
    private val _currentRole = MutableStateFlow(UserRole.CANDIDATE)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _currentScreen = MutableStateFlow("HOME") // HOME, FIND_JOBS, INTERNSHIPS, AI_STUDIO, DASHBOARD, PRICING, SAFETY_GUIDE
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Users & Profile
    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeUser = MutableStateFlow<UserEntity?>(null)
    val activeUser: StateFlow<UserEntity?> = _activeUser.asStateFlow()

    // Notifications
    val allNotifications = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val unreadNotificationCount = repository.unreadNotificationCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val isNotificationSheetOpen = MutableStateFlow(false)

    // Jobs & Applications
    val allJobs: StateFlow<List<JobEntity>> = repository.allJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeJobs: StateFlow<List<JobEntity>> = repository.activeJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val internships: StateFlow<List<JobEntity>> = repository.internships
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allApplications: StateFlow<List<ApplicationEntity>> = repository.allApplications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val fraudReports: StateFlow<List<FraudReportEntity>> = repository.fraudReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subscriptionPlans: StateFlow<List<SubscriptionPlanEntity>> = repository.subscriptionPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and Filters
    val searchQuery = MutableStateFlow("")
    val selectedCity = MutableStateFlow("All")
    val selectedCategory = MutableStateFlow("All")
    val selectedJobType = MutableStateFlow("All")
    val minSalaryFilter = MutableStateFlow(0)

    // Saved Jobs set
    private val _savedJobIds = MutableStateFlow<Set<Long>>(emptySet())
    val savedJobIds: StateFlow<Set<Long>> = _savedJobIds.asStateFlow()

    private data class JobFilterCriteria(
        val query: String,
        val city: String,
        val category: String,
        val jobType: String,
        val minSalary: Int
    )

    private val filterCriteria = combine(
        searchQuery,
        selectedCity,
        selectedCategory,
        selectedJobType,
        minSalaryFilter
    ) { q, city, cat, type, minSal ->
        JobFilterCriteria(q, city, cat, type, minSal)
    }

    // Filtered Jobs State
    val filteredJobs: StateFlow<List<JobEntity>> = combine(
        activeJobs,
        filterCriteria
    ) { jobs, criteria ->
        jobs.filter { job ->
            val matchesQuery = criteria.query.isBlank() ||
                    job.title.contains(criteria.query, ignoreCase = true) ||
                    job.companyName.contains(criteria.query, ignoreCase = true) ||
                    job.skillsRequired.contains(criteria.query, ignoreCase = true) ||
                    job.city.contains(criteria.query, ignoreCase = true)

            val matchesCity = criteria.city == "All" ||
                    (criteria.city == "Remote" && job.isRemote) ||
                    job.city.equals(criteria.city, ignoreCase = true)

            val matchesCategory = criteria.category == "All" || job.category.equals(criteria.category, ignoreCase = true)
            val matchesType = criteria.jobType == "All" || job.jobType.equals(criteria.jobType, ignoreCase = true)
            val matchesSalary = criteria.minSalary == 0 || job.maxSalary >= criteria.minSalary

            matchesQuery && matchesCity && matchesCategory && matchesType && matchesSalary
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Modals & Dialog selections
    val selectedJobForDetails = MutableStateFlow<JobEntity?>(null)
    val selectedJobForApply = MutableStateFlow<JobEntity?>(null)
    val selectedJobForReport = MutableStateFlow<JobEntity?>(null)
    val selectedPlanForCheckout = MutableStateFlow<SubscriptionPlanEntity?>(null)
    val isScheduleInterviewOpen = MutableStateFlow<ApplicationEntity?>(null)
    val isPostJobOpen = MutableStateFlow(false)

    // AI Career Outputs
    val generatedResume = MutableStateFlow<GeneratedResume?>(null)
    val skillMatchReport = MutableStateFlow<SkillMatchReport?>(null)
    val interviewQuestions = MutableStateFlow(AiCareerService.getMockInterviewQuestions("General"))
    val currentInterviewQuestionIndex = MutableStateFlow(0)
    val candidateAnswerText = MutableStateFlow("")
    val interviewFeedback = MutableStateFlow<InterviewFeedback?>(null)
    val generatedJobDescription = MutableStateFlow<GeneratedJobDescription?>(null)
    val fraudRiskAnalysis = MutableStateFlow<FraudRiskAnalysis?>(null)

    // UI Feedback
    val snackbarMessage = MutableStateFlow<String?>(null)
    val paymentReceipt = MutableStateFlow<PaymentService.PaymentReceipt?>(null)

    init {
        // Load initial user
        viewModelScope.launch {
            allUsers.collect { users ->
                if (users.isNotEmpty() && _activeUser.value == null) {
                    val roleStr = _currentRole.value.name
                    _activeUser.value = users.find { it.role == roleStr } ?: users.first()
                    refreshSavedJobs()
                }
            }
        }
    }

    private fun refreshSavedJobs() {
        val user = _activeUser.value ?: return
        viewModelScope.launch {
            repository.getCandidateSavedJobs(user.id).collect { list ->
                _savedJobIds.value = list.map { it.jobId }.toSet()
            }
        }
    }

    fun switchRole(role: UserRole) {
        _currentRole.value = role
        val users = allUsers.value
        val matchedUser = users.find { it.role == role.name }
        if (matchedUser != null) {
            _activeUser.value = matchedUser
        }
        refreshSavedJobs()
        // Switch to appropriate landing tab
        _currentScreen.value = when (role) {
            UserRole.CANDIDATE -> "HOME"
            UserRole.EMPLOYER -> "DASHBOARD"
            UserRole.ADMIN -> "DASHBOARD"
        }
    }

    fun setLanguage(lang: Language) {
        _currentLanguage.value = lang
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun toggleSaveJob(jobId: Long) {
        val user = _activeUser.value ?: return
        viewModelScope.launch {
            repository.toggleSaveJob(user.id, jobId)
            refreshSavedJobs()
            snackbarMessage.value = if (_savedJobIds.value.contains(jobId)) {
                "Job removed from saved list"
            } else {
                "Job saved to your bookmarks"
            }
        }
    }

    fun applyForJob(job: JobEntity, coverNote: String) {
        val user = _activeUser.value ?: return
        viewModelScope.launch {
            val result = repository.applyForJob(job, user, coverNote)
            result.onSuccess {
                snackbarMessage.value = "Application submitted successfully! Track it in Dashboard."
                selectedJobForApply.value = null
                // Refresh active user to get updated application count
                _activeUser.value = repository.getUserById(user.id)
            }.onFailure { err ->
                snackbarMessage.value = err.message ?: "Failed to apply."
            }
        }
    }

    fun submitFraudReport(job: JobEntity, reason: String, details: String) {
        val user = _activeUser.value ?: return
        viewModelScope.launch {
            repository.submitFraudReport(
                jobId = job.id,
                jobTitle = job.title,
                companyName = job.companyName,
                reporterName = user.name,
                reason = reason,
                details = details
            )
            selectedJobForReport.value = null
            snackbarMessage.value = "Report submitted to Trust & Safety team. Thank you for protecting the community."
        }
    }

    // Employer actions
    fun postJob(
        title: String,
        category: String,
        city: String,
        location: String,
        jobType: String,
        minSal: Int,
        maxSal: Int,
        isInternship: Boolean,
        isPaid: Boolean,
        stipendDetails: String,
        skills: String,
        experience: String,
        education: String,
        description: String,
        responsibilities: String,
        requiresUpfrontFee: Boolean
    ) {
        val user = _activeUser.value ?: return
        val fraudCheck = AiCareerService.detectJobFraud(
            title = title,
            description = description,
            contactEmail = user.email,
            salaryMax = maxSal,
            requiresFee = requiresUpfrontFee
        )

        val newJob = JobEntity(
            title = title,
            companyName = user.companyName.ifBlank { "Registered Employer" },
            location = location,
            city = city,
            category = category,
            jobType = jobType,
            minSalary = minSal,
            maxSalary = maxSal,
            isSalaryMonthly = true,
            isInternship = isInternship,
            isPaid = isPaid,
            stipendDetails = stipendDetails,
            skillsRequired = skills,
            experienceRequired = experience,
            educationRequirement = education,
            description = description,
            responsibilities = responsibilities,
            isVerifiedCompany = user.isVerified,
            isRemote = city.equals("Remote", ignoreCase = true) || jobType.contains("Remote"),
            postedDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date()),
            status = if (fraudCheck.isSuspicious) "PENDING_MODERATION" else "ACTIVE",
            scamRiskScore = fraudCheck.riskScore,
            contactEmail = user.email,
            employerId = user.id
        )

        viewModelScope.launch {
            repository.postJob(newJob)
            isPostJobOpen.value = false
            snackbarMessage.value = if (fraudCheck.isSuspicious) {
                "Job submitted and under Trust & Safety moderation check."
            } else {
                "Job published live on YAQEEN ALLAH marketplace!"
            }
        }
    }

    fun updateApplicationStatus(appId: Long, status: String) {
        viewModelScope.launch {
            repository.updateApplicationStatus(appId, status)
            snackbarMessage.value = "Candidate application status updated to $status."
        }
    }

    fun scheduleInterview(appId: Long, date: String, time: String, link: String, notes: String) {
        viewModelScope.launch {
            repository.scheduleInterview(appId, date, time, link, notes)
            isScheduleInterviewOpen.value = null
            snackbarMessage.value = "Interview scheduled and invitation sent to candidate!"
        }
    }

    // Admin Actions
    fun moderateJob(jobId: Long, status: String) {
        viewModelScope.launch {
            repository.updateJobStatus(jobId, status)
            snackbarMessage.value = "Job #$jobId marked as $status."
        }
    }

    fun verifyEmployer(userId: Long, isVerified: Boolean) {
        viewModelScope.launch {
            repository.verifyEmployer(userId, isVerified)
            snackbarMessage.value = "Employer verification status updated."
        }
    }

    fun resolveFraudReport(reportId: Long, status: String) {
        viewModelScope.launch {
            repository.updateFraudReportStatus(reportId, status)
            snackbarMessage.value = "Report #$reportId marked as $status."
        }
    }

    fun updatePlanPrice(planId: String, newPrice: Int) {
        viewModelScope.launch {
            repository.updatePlanPrice(planId, newPrice)
            snackbarMessage.value = "Subscription plan price updated to ₹$newPrice."
        }
    }

    // AI Career Studio methods
    fun runAiResumeBuilder(name: String, targetRole: String, education: String, skills: String, projects: String) {
        val result = AiCareerService.generateProfessionalResume(
            name = name,
            targetRole = targetRole,
            education = education,
            skillsInput = skills,
            projects = projects
        )
        generatedResume.value = result
        snackbarMessage.value = "AI Resume generated successfully!"
    }

    fun runSkillMatchAnalysis(candidateSkills: String, targetJob: JobEntity) {
        val report = AiCareerService.analyzeSkillMatch(candidateSkills, targetJob.skillsRequired)
        skillMatchReport.value = report
        snackbarMessage.value = "Match Score: ${report.matchScore}%"
    }

    fun submitMockInterviewAnswer(answer: String) {
        val question = interviewQuestions.value.getOrNull(currentInterviewQuestionIndex.value) ?: return
        val feedback = AiCareerService.evaluateInterviewAnswer(
            question = question,
            candidateAnswer = answer,
            language = _currentLanguage.value
        )
        interviewFeedback.value = feedback
    }

    fun nextInterviewQuestion() {
        if (currentInterviewQuestionIndex.value < interviewQuestions.value.size - 1) {
            currentInterviewQuestionIndex.value += 1
            candidateAnswerText.value = ""
            interviewFeedback.value = null
        }
    }

    fun previousInterviewQuestion() {
        if (currentInterviewQuestionIndex.value > 0) {
            currentInterviewQuestionIndex.value -= 1
            candidateAnswerText.value = ""
            interviewFeedback.value = null
        }
    }

    fun generateJobDescription(roleTitle: String, city: String, experience: String, skills: String) {
        val jd = AiCareerService.generateJobDescription(roleTitle, city, experience, skills)
        generatedJobDescription.value = jd
    }

    // Payment & Razorpay Simulation
    fun processRazorpayPayment(plan: SubscriptionPlanEntity, paymentMethod: String) {
        val user = _activeUser.value ?: return
        val order = PaymentService.createOrder(
            planId = plan.id,
            planName = plan.name,
            amountInr = plan.priceInr,
            customerName = user.name,
            customerEmail = user.email
        )
        val receipt = PaymentService.verifyAndCompletePayment(order, paymentMethod)
        paymentReceipt.value = receipt

        // Map plan id to user tier
        val newTier = when {
            plan.id.contains("pro") -> "PRO"
            plan.id.contains("elite") -> "ELITE"
            plan.id.contains("growth") -> "GROWTH"
            plan.id.contains("enterprise") -> "ENTERPRISE"
            else -> "FREE"
        }

        viewModelScope.launch {
            repository.upgradeUserSubscription(user.id, newTier)
            _activeUser.value = repository.getUserById(user.id)
            selectedPlanForCheckout.value = null
            snackbarMessage.value = "Payment Verified! Upgraded to ${plan.name}."
        }
    }

    // Comfortable 1-Tap Quick Apply
    fun quickApply(job: JobEntity) {
        val user = _activeUser.value ?: return
        val defaultCover = "I am excited to apply for ${job.title} at ${job.companyName}. With my foundation in ${user.skills}, I am ready to join immediately and contribute to your team."
        applyForJob(job, defaultCover)
    }

    // Real-Time Recruiter Progression Simulator
    fun simulateRecruiterProgress(app: ApplicationEntity) {
        viewModelScope.launch {
            val newStatus = repository.simulateRecruiterProgress(app)
            snackbarMessage.value = "⚡ Real-time Update: ${app.companyName} updated application to '$newStatus'!"
        }
    }

    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
            snackbarMessage.value = "All notifications marked as read."
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            repository.clearAllNotifications()
            snackbarMessage.value = "Notification inbox cleared."
        }
    }

    fun clearSnackbar() {
        snackbarMessage.value = null
    }

    fun getString(key: String): String {
        return LocalizationHelper.getString(key, _currentLanguage.value)
    }
}
