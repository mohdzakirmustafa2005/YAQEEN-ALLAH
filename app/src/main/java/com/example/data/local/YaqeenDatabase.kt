package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        JobEntity::class,
        ApplicationEntity::class,
        SavedJobEntity::class,
        UserEntity::class,
        FraudReportEntity::class,
        SubscriptionPlanEntity::class,
        AuditLogEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class YaqeenDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun savedJobDao(): SavedJobDao
    abstract fun userDao(): UserDao
    abstract fun fraudReportDao(): FraudReportDao
    abstract fun subscriptionPlanDao(): SubscriptionPlanDao
    abstract fun auditLogDao(): AuditLogDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: YaqeenDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): YaqeenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YaqeenDatabase::class.java,
                    "yaqeen_allah_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database)
                }
            }
        }

        private suspend fun populateDatabase(db: YaqeenDatabase) {
            db.userDao().let { dao ->
                SampleData.getInitialUsers().forEach { dao.insertUser(it) }
            }
            db.jobDao().insertAll(SampleData.getInitialJobs())
            db.subscriptionPlanDao().insertPlans(SampleData.getInitialPlans())
            SampleData.getInitialApplications().forEach {
                db.applicationDao().insertApplication(it)
            }
            SampleData.getInitialNotifications().forEach {
                db.notificationDao().insertNotification(it)
            }
            db.auditLogDao().insertLog(
                AuditLogEntity(
                    action = "SYSTEM_INITIALIZED",
                    targetType = "PLATFORM",
                    targetId = "SYS-001",
                    details = "Platform initialized with Mahbubnagar & Hyderabad verified jobs.",
                    timestamp = "Nov 5, 2026 09:00 AM",
                    performedBy = "System"
                )
            )
        }
    }
}
