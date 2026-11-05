package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val companyName: String,
    val location: String, // e.g. "Mahbubnagar, Telangana", "HITEC City, Hyderabad"
    val city: String,     // "Mahbubnagar", "Hyderabad", "Remote", "Bengaluru"
    val category: String, // from JobCategory.name
    val jobType: String,  // "Full Time", "Internship", "Remote"
    val minSalary: Int,   // INR / month or year
    val maxSalary: Int,
    val isSalaryMonthly: Boolean = true,
    val isInternship: Boolean = false,
    val isPaid: Boolean = true,
    val stipendDetails: String = "", // e.g., "₹15,000 / month with PPO potential"
    val skillsRequired: String,      // comma-separated: "Kotlin, Compose, Room"
    val experienceRequired: String,  // e.g. "Fresher / 0-1 yr", "1-3 yrs"
    val educationRequirement: String, // "B.Tech / MCA / Degree"
    val description: String,
    val responsibilities: String = "",
    val isVerifiedCompany: Boolean = true,
    val isRemote: Boolean = false,
    val postedDate: String,          // e.g., "Nov 4, 2026"
    val status: String = "ACTIVE",   // "ACTIVE", "PENDING_MODERATION", "SUSPENDED"
    val scamRiskScore: Int = 0,      // 0 - 100
    val contactEmail: String = "hr@company.com",
    val employerId: Long = 1
)

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val jobTitle: String,
    val companyName: String,
    val candidateId: Long,
    val candidateName: String,
    val candidateEmail: String,
    val candidatePhone: String,
    val candidateSkills: String,
    val resumeSummary: String,
    val coverNote: String,
    val status: String = "APPLIED", // APPLIED, UNDER_REVIEW, SHORTLISTED, INTERVIEW_SCHEDULED, OFFER, REJECTED
    val appliedDate: String,
    val interviewDate: String = "",
    val interviewTime: String = "",
    val interviewMeetingLink: String = "",
    val interviewerNotes: String = ""
)

@Entity(tableName = "saved_jobs")
data class SavedJobEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val candidateId: Long,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val name: String,
    val phone: String,
    val role: String, // CANDIDATE, EMPLOYER, ADMIN
    val city: String,
    val education: String = "B.Tech (Computer Science)",
    val experienceYears: String = "Fresher (2026 Batch)",
    val skills: String = "Java, Kotlin, Jetpack Compose, SQL, Git",
    val resumeBio: String = "Aspiring software engineer eager to build modern mobile and backend applications in Telangana.",
    val companyName: String = "",
    val companyWebsite: String = "",
    val isVerified: Boolean = false,
    val dailyAppliesCount: Int = 0,
    val lastApplyDate: String = "",
    val subscriptionTier: String = "FREE" // FREE, PRO, ELITE, GROWTH, ENTERPRISE
)

@Entity(tableName = "fraud_reports")
data class FraudReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val jobTitle: String,
    val companyName: String,
    val reporterName: String,
    val reason: String, // "Demanding upfront fee", "Fake company", "Misleading info", "Phishing link"
    val details: String,
    val status: String = "PENDING", // PENDING, RESOLVED, DISMISSED
    val reportedDate: String
)

@Entity(tableName = "subscription_plans")
data class SubscriptionPlanEntity(
    @PrimaryKey val id: String, // "candidate_pro", "employer_growth", etc.
    val targetRole: String,     // "CANDIDATE", "EMPLOYER"
    val name: String,
    val priceInr: Int,
    val billingCycle: String,   // "/ month"
    val features: String,       // newline or pipe separated
    val isPopular: Boolean = false
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val action: String,
    val targetType: String,
    val targetId: String,
    val details: String,
    val timestamp: String,
    val performedBy: String
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: String = "APPLICATION", // "APPLICATION", "INTERVIEW", "JOB_ALERT", "SECURITY"
    val relatedId: Long = 0
)
