package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs ORDER BY id DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE status = 'ACTIVE' ORDER BY id DESC")
    fun getActiveJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE isInternship = 1 AND status = 'ACTIVE' ORDER BY id DESC")
    fun getInternships(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE employerId = :employerId ORDER BY id DESC")
    fun getJobsByEmployer(employerId: Long): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :id LIMIT 1")
    suspend fun getJobById(id: Long): JobEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jobs: List<JobEntity>)

    @Update
    suspend fun updateJob(job: JobEntity)

    @Query("UPDATE jobs SET status = :status WHERE id = :id")
    suspend fun updateJobStatus(id: Long, status: String)

    @Query("DELETE FROM jobs WHERE id = :id")
    suspend fun deleteJob(id: Long)
}

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications ORDER BY id DESC")
    fun getAllApplications(): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE candidateId = :candidateId ORDER BY id DESC")
    fun getApplicationsByCandidate(candidateId: Long): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE jobId = :jobId ORDER BY id DESC")
    fun getApplicationsByJob(jobId: Long): Flow<List<ApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: ApplicationEntity): Long

    @Update
    suspend fun updateApplication(application: ApplicationEntity)

    @Query("UPDATE applications SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("UPDATE applications SET status = 'INTERVIEW_SCHEDULED', interviewDate = :date, interviewTime = :time, interviewMeetingLink = :link, interviewerNotes = :notes WHERE id = :id")
    suspend fun scheduleInterview(id: Long, date: String, time: String, link: String, notes: String)
}

@Dao
interface SavedJobDao {
    @Query("SELECT * FROM saved_jobs WHERE candidateId = :candidateId")
    fun getSavedJobs(candidateId: Long): Flow<List<SavedJobEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_jobs WHERE candidateId = :candidateId AND jobId = :jobId)")
    suspend fun isJobSaved(candidateId: Long, jobId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(savedJob: SavedJobEntity)

    @Query("DELETE FROM saved_jobs WHERE candidateId = :candidateId AND jobId = :jobId")
    suspend fun unsaveJob(candidateId: Long, jobId: Long)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE role = :role LIMIT 1")
    suspend fun getUserByRole(role: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isVerified = :isVerified WHERE id = :id")
    suspend fun updateUserVerification(id: Long, isVerified: Boolean)

    @Query("UPDATE users SET subscriptionTier = :tier WHERE id = :id")
    suspend fun updateSubscriptionTier(id: Long, tier: String)

    @Query("UPDATE users SET dailyAppliesCount = :count, lastApplyDate = :date WHERE id = :id")
    suspend fun updateDailyApplies(id: Long, count: Int, date: String)
}

@Dao
interface FraudReportDao {
    @Query("SELECT * FROM fraud_reports ORDER BY id DESC")
    fun getAllReports(): Flow<List<FraudReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: FraudReportEntity): Long

    @Query("UPDATE fraud_reports SET status = :status WHERE id = :id")
    suspend fun updateReportStatus(id: Long, status: String)
}

@Dao
interface SubscriptionPlanDao {
    @Query("SELECT * FROM subscription_plans")
    fun getAllPlans(): Flow<List<SubscriptionPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plans: List<SubscriptionPlanEntity>)

    @Query("UPDATE subscription_plans SET priceInr = :price WHERE id = :id")
    suspend fun updatePrice(id: String, price: Int)
}

@Dao
interface AuditLogDao {
    @Query("SELECT * FROM audit_logs ORDER BY id DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AuditLogEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}
