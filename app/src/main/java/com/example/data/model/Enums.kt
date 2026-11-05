package com.example.data.model

enum class UserRole(val displayName: String) {
    CANDIDATE("Job Seeker"),
    EMPLOYER("Employer"),
    ADMIN("Admin")
}

enum class Language(val code: String, val label: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    TELUGU("te", "Telugu", "తెలుగు"),
    HINDI("hi", "Hindi", "हिंदी"),
    URDU("ur", "Urdu", "اردو")
}

enum class JobCategory(val displayName: String, val iconName: String) {
    SOFTWARE_DEV("Software Developer", "code"),
    IT_SUPPORT("IT Support", "support"),
    DATA_ENTRY("Data Entry", "keyboard"),
    DATA_ANALYST("Data Analyst", "analytics"),
    AI_ML("AI & Machine Learning", "psychology"),
    CYBERSECURITY("Cybersecurity", "security"),
    CLOUD_COMPUTING("Cloud Computing", "cloud"),
    WEB_DEV("Web Development", "web"),
    DIGITAL_MARKETING("Digital Marketing", "campaign"),
    HR_RECRUITMENT("HR & Recruitment", "badge"),
    WAREHOUSE("Warehouse Assistant", "inventory"),
    CUSTOMER_SUPPORT("Customer Support", "headset"),
    REMOTE_JOBS("Remote Jobs", "home_work"),
    INTERNSHIPS("Internships", "school"),
    WORK_FROM_HOME("Work From Home", "laptop_mac")
}

enum class JobType(val label: String) {
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    INTERNSHIP("Internship"),
    CONTRACT("Contract"),
    REMOTE("Remote / WFH")
}

enum class ApplicationStatus(val label: String, val stepIndex: Int) {
    APPLIED("Applied", 0),
    UNDER_REVIEW("Under Review", 1),
    SHORTLISTED("Shortlisted", 2),
    INTERVIEW_SCHEDULED("Interview Scheduled", 3),
    OFFER("Offer Extended", 4),
    REJECTED("Not Selected", -1)
}

enum class SubscriptionTier(val label: String) {
    FREE("Free"),
    PRO("Pro Job Seeker"),
    ELITE("Career Elite"),
    GROWTH("Growth Recruiter"),
    ENTERPRISE("Enterprise Bulk")
}

data class MockInterviewQuestion(
    val id: Int,
    val questionEn: String,
    val questionTe: String,
    val questionHi: String,
    val questionUr: String,
    val category: String,
    val expectedKeyPoints: List<String>
)

data class InterviewFeedback(
    val scoreOutOf10: Int,
    val strengths: String,
    val improvements: String,
    val sampleIdealAnswer: String
)
