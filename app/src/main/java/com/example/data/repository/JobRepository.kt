package com.example.data.repository

import com.example.data.local.ApplicationDao
import com.example.data.local.ApplicationEntity
import com.example.data.local.AuditLogDao
import com.example.data.local.AuditLogEntity
import com.example.data.local.FraudReportDao
import com.example.data.local.FraudReportEntity
import com.example.data.local.JobDao
import com.example.data.local.JobEntity
import com.example.data.local.NotificationDao
import com.example.data.local.NotificationEntity
import com.example.data.local.SavedJobDao
import com.example.data.local.SavedJobEntity
import com.example.data.local.SubscriptionPlanDao
import com.example.data.local.SubscriptionPlanEntity
import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JobRepository(
    private val jobDao: JobDao,
    private val applicationDao: ApplicationDao,
    private val savedJobDao: SavedJobDao,
    private val userDao: UserDao,
    private val fraudReportDao: FraudReportDao,
    private val subscriptionPlanDao: SubscriptionPlanDao,
    private val auditLogDao: AuditLogDao,
    private val notificationDao: NotificationDao
) {
    val allJobs: Flow<List<JobEntity>> = jobDao.getAllJobs()
    val activeJobs: Flow<List<JobEntity>> = jobDao.getActiveJobs()
    val internships: Flow<List<JobEntity>> = jobDao.getInternships()
    val allApplications: Flow<List<ApplicationEntity>> = applicationDao.getAllApplications()
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()
    val fraudReports: Flow<List<FraudReportEntity>> = fraudReportDao.getAllReports()
    val subscriptionPlans: Flow<List<SubscriptionPlanEntity>> = subscriptionPlanDao.getAllPlans()
    val auditLogs: Flow<List<AuditLogEntity>> = auditLogDao.getRecentLogs()
    val notifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    val unreadNotificationCount: Flow<Int> = notificationDao.getUnreadCount()

    fun getCandidateApplications(candidateId: Long): Flow<List<ApplicationEntity>> =
        applicationDao.getApplicationsByCandidate(candidateId)

    fun getCandidateSavedJobs(candidateId: Long): Flow<List<SavedJobEntity>> =
        savedJobDao.getSavedJobs(candidateId)

    fun getEmployerJobs(employerId: Long): Flow<List<JobEntity>> =
        jobDao.getJobsByEmployer(employerId)

    fun getJobApplications(jobId: Long): Flow<List<ApplicationEntity>> =
        applicationDao.getApplicationsByJob(jobId)

    suspend fun getJobById(id: Long): JobEntity? = jobDao.getJobById(id)

    suspend fun getUserById(id: Long): UserEntity? = userDao.getUserById(id)

    suspend fun isJobSaved(candidateId: Long, jobId: Long): Boolean =
        savedJobDao.isJobSaved(candidateId, jobId)

    suspend fun toggleSaveJob(candidateId: Long, jobId: Long) {
        if (savedJobDao.isJobSaved(candidateId, jobId)) {
            savedJobDao.unsaveJob(candidateId, jobId)
        } else {
            savedJobDao.saveJob(SavedJobEntity(jobId = jobId, candidateId = candidateId))
        }
    }

    suspend fun applyForJob(
        job: JobEntity,
        candidate: UserEntity,
        coverNote: String
    ): Result<Long> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentApplies = if (candidate.lastApplyDate == today) candidate.dailyAppliesCount else 0

        // If on free tier, limit is 3 per day
        if (candidate.subscriptionTier == "FREE" && currentApplies >= 3) {
            return Result.failure(
                Exception("Daily free application limit (3/day) reached. Upgrade to Pro for unlimited applies!")
            )
        }

        val app = ApplicationEntity(
            jobId = job.id,
            jobTitle = job.title,
            companyName = job.companyName,
            candidateId = candidate.id,
            candidateName = candidate.name,
            candidateEmail = candidate.email,
            candidatePhone = candidate.phone,
            candidateSkills = candidate.skills,
            resumeSummary = candidate.resumeBio,
            coverNote = coverNote,
            status = "APPLIED",
            appliedDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
        )
        val id = applicationDao.insertApplication(app)

        // Increment count
        userDao.updateDailyApplies(candidate.id, currentApplies + 1, today)
        return Result.success(id)
    }

    suspend fun postJob(job: JobEntity): Long {
        return jobDao.insertJob(job)
    }

    suspend fun updateJobStatus(jobId: Long, status: String, adminName: String = "Admin") {
        jobDao.updateJobStatus(jobId, status)
        auditLogDao.insertLog(
            AuditLogEntity(
                action = "JOB_STATUS_CHANGED",
                targetType = "JOB",
                targetId = jobId.toString(),
                details = "Job #$jobId status set to $status",
                timestamp = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(Date()),
                performedBy = adminName
            )
        )
    }

    suspend fun updateApplicationStatus(appId: Long, status: String) {
        applicationDao.updateStatus(appId, status)
    }

    suspend fun scheduleInterview(
        appId: Long,
        date: String,
        time: String,
        link: String,
        notes: String
    ) {
        applicationDao.scheduleInterview(appId, date, time, link, notes)
    }

    suspend fun submitFraudReport(
        jobId: Long,
        jobTitle: String,
        companyName: String,
        reporterName: String,
        reason: String,
        details: String
    ): Long {
        val report = FraudReportEntity(
            jobId = jobId,
            jobTitle = jobTitle,
            companyName = companyName,
            reporterName = reporterName,
            reason = reason,
            details = details,
            status = "PENDING",
            reportedDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
        )
        val id = fraudReportDao.insertReport(report)
        // Auto-increase scam risk score
        val job = jobDao.getJobById(jobId)
        if (job != null) {
            val updated = job.copy(scamRiskScore = (job.scamRiskScore + 35).coerceAtMost(100))
            jobDao.updateJob(updated)
        }
        return id
    }

    suspend fun updateFraudReportStatus(reportId: Long, status: String) {
        fraudReportDao.updateReportStatus(reportId, status)
    }

    suspend fun updatePlanPrice(planId: String, newPrice: Int) {
        subscriptionPlanDao.updatePrice(planId, newPrice)
    }

    suspend fun upgradeUserSubscription(userId: Long, tier: String) {
        userDao.updateSubscriptionTier(userId, tier)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    suspend fun verifyEmployer(userId: Long, isVerified: Boolean) {
        userDao.updateUserVerification(userId, isVerified)
    }

    suspend fun markNotificationAsRead(id: Long) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        notificationDao.markAllAsRead()
    }

    suspend fun clearAllNotifications() {
        notificationDao.clearAll()
    }

    suspend fun simulateRecruiterProgress(app: ApplicationEntity): String {
        val nextStatus = when (app.status) {
            "APPLIED" -> "UNDER_REVIEW"
            "UNDER_REVIEW" -> "SHORTLISTED"
            "SHORTLISTED" -> "INTERVIEW_SCHEDULED"
            "INTERVIEW_SCHEDULED" -> "OFFER"
            else -> "OFFER"
        }

        if (nextStatus == "INTERVIEW_SCHEDULED") {
            applicationDao.scheduleInterview(
                id = app.id,
                date = "Nov 12, 2026",
                time = "02:00 PM IST",
                link = "https://meet.google.com/yaq-live-eval",
                notes = "Real-time Recruiter update: Candidate shortlisted for live technical evaluation."
            )
        } else {
            applicationDao.updateStatus(app.id, nextStatus)
        }

        notificationDao.insertNotification(
            NotificationEntity(
                title = "Application Update: ${app.jobTitle}",
                message = "${app.companyName} updated your status to '$nextStatus'.",
                timestamp = "Just now",
                isRead = false,
                type = if (nextStatus == "INTERVIEW_SCHEDULED") "INTERVIEW" else "APPLICATION",
                relatedId = app.id
            )
        )
        return nextStatus
    }
}
