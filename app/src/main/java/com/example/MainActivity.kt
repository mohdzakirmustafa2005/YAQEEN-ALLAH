package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.components.ApplyJobDialog
import com.example.ui.components.FraudReportDialog
import com.example.ui.components.JobDetailDialog
import com.example.ui.components.PostJobDialog
import com.example.ui.components.RazorpayPaymentDialog
import com.example.ui.components.ScheduleInterviewDialog
import com.example.ui.screens.AiCareerStudioScreen
import com.example.ui.screens.DashboardsScreen
import com.example.ui.screens.FindJobsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InternshipsScreen
import com.example.ui.screens.PricingScreen
import com.example.ui.screens.SafetyGuideScreen
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark by viewModel.isDarkTheme.collectAsState()
            MyApplicationTheme(darkTheme = isDark) {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeUser by viewModel.activeUser.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    // Dialog states
    val detailJob by viewModel.selectedJobForDetails.collectAsState()
    val applyJob by viewModel.selectedJobForApply.collectAsState()
    val reportJob by viewModel.selectedJobForReport.collectAsState()
    val checkoutPlan by viewModel.selectedPlanForCheckout.collectAsState()
    val interviewApp by viewModel.isScheduleInterviewOpen.collectAsState()
    val isPostJobOpen by viewModel.isPostJobOpen.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppHeader(viewModel = viewModel)
        },
        bottomBar = {
            AppBottomNavigation(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) },
                getString = { viewModel.getString(it) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentScreen) {
                "HOME" -> HomeScreen(viewModel = viewModel)
                "FIND_JOBS" -> FindJobsScreen(viewModel = viewModel)
                "INTERNSHIPS" -> InternshipsScreen(viewModel = viewModel)
                "AI_STUDIO" -> AiCareerStudioScreen(viewModel = viewModel)
                "DASHBOARD" -> DashboardsScreen(viewModel = viewModel)
                "PRICING" -> PricingScreen(viewModel = viewModel)
                "SAFETY_GUIDE" -> SafetyGuideScreen(viewModel = viewModel)
                else -> HomeScreen(viewModel = viewModel)
            }
        }

        // Job Details Dialog
        detailJob?.let { job ->
            JobDetailDialog(
                job = job,
                candidate = activeUser,
                onDismiss = { viewModel.selectedJobForDetails.value = null },
                onApply = {
                    viewModel.selectedJobForDetails.value = null
                    viewModel.selectedJobForApply.value = job
                },
                onCheckSkillGap = {
                    val userSkills = activeUser?.skills ?: "Kotlin, Compose, Room"
                    viewModel.selectedJobForDetails.value = null
                    viewModel.runSkillMatchAnalysis(userSkills, job)
                    viewModel.navigateTo("AI_STUDIO")
                },
                onReportFraud = {
                    viewModel.selectedJobForDetails.value = null
                    viewModel.selectedJobForReport.value = job
                }
            )
        }

        // Apply Job Dialog
        applyJob?.let { job ->
            ApplyJobDialog(
                job = job,
                candidate = activeUser,
                onDismiss = { viewModel.selectedJobForApply.value = null },
                onSubmit = { coverNote ->
                    viewModel.applyForJob(job, coverNote)
                }
            )
        }

        // Fraud Report Dialog
        reportJob?.let { job ->
            FraudReportDialog(
                job = job,
                onDismiss = { viewModel.selectedJobForReport.value = null },
                onSubmit = { reason, details ->
                    viewModel.submitFraudReport(job, reason, details)
                }
            )
        }

        // Razorpay Payment Dialog
        checkoutPlan?.let { plan ->
            RazorpayPaymentDialog(
                plan = plan,
                onDismiss = { viewModel.selectedPlanForCheckout.value = null },
                onPaymentSuccess = { method ->
                    viewModel.processRazorpayPayment(plan, method)
                }
            )
        }

        // Schedule Interview Dialog
        interviewApp?.let { app ->
            ScheduleInterviewDialog(
                application = app,
                onDismiss = { viewModel.isScheduleInterviewOpen.value = null },
                onSchedule = { date, time, link, notes ->
                    viewModel.scheduleInterview(app.id, date, time, link, notes)
                }
            )
        }

        // Post Job Dialog
        if (isPostJobOpen) {
            PostJobDialog(
                onDismiss = { viewModel.isPostJobOpen.value = false },
                onSubmit = { title, cat, city, loc, type, minS, maxS, isIntern, isPaid, stip, sk, exp, edu, desc, resp, fee ->
                    viewModel.postJob(
                        title, cat, city, loc, type, minS, maxS, isIntern, isPaid,
                        stip, sk, exp, edu, desc, resp, fee
                    )
                }
            )
        }

        // Real-Time Live Notification Dialog
        val isNotifOpen by viewModel.isNotificationSheetOpen.collectAsState()
        if (isNotifOpen) {
            com.example.ui.components.NotificationDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.isNotificationSheetOpen.value = false }
            )
        }
    }
}

@Composable
fun AppBottomNavigation(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    getString: (String) -> String
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .testTag("bottom_navigation_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        val navItems = listOf(
            Triple("HOME", getString("nav_home"), Icons.Default.Home),
            Triple("FIND_JOBS", getString("nav_jobs"), Icons.Default.Search),
            Triple("INTERNSHIPS", getString("nav_internships"), Icons.Default.School),
            Triple("AI_STUDIO", getString("nav_ai_studio"), Icons.Default.AutoAwesome),
            Triple("DASHBOARD", getString("nav_dashboard"), Icons.Default.Dashboard),
            Triple("PRICING", getString("nav_pricing"), Icons.Default.Payment)
        )

        navItems.forEach { (route, label, icon) ->
            val isSelected = currentScreen == route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EmeraldPrimary,
                    selectedTextColor = EmeraldPrimary,
                    indicatorColor = EmeraldPrimary.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_$route")
            )
        }
    }
}
