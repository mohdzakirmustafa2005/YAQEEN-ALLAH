package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ApplicationEntity
import com.example.data.local.JobEntity
import com.example.data.model.UserRole
import com.example.ui.components.JobCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechGold
import com.example.ui.viewmodel.MainViewModel

@Composable
fun DashboardsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentRole by viewModel.currentRole.collectAsState()

    when (currentRole) {
        UserRole.CANDIDATE -> CandidateDashboard(viewModel, modifier)
        UserRole.EMPLOYER -> EmployerDashboard(viewModel, modifier)
        UserRole.ADMIN -> AdminDashboard(viewModel, modifier)
    }
}

// ==========================================
// 1. CANDIDATE DASHBOARD
// ==========================================
@Composable
private fun CandidateDashboard(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val activeUser by viewModel.activeUser.collectAsState()
    val allApplications by viewModel.allApplications.collectAsState()
    val activeJobs by viewModel.activeJobs.collectAsState()
    val savedJobIds by viewModel.savedJobIds.collectAsState()

    var candidateTab by remember { mutableStateOf(0) }
    val tabs = listOf("Applied Jobs (${allApplications.size})", "Interviews", "Saved Jobs (${savedJobIds.size})")

    val scheduledInterviews = allApplications.filter { it.status == "INTERVIEW_SCHEDULED" }
    val savedJobsList = activeJobs.filter { savedJobIds.contains(it.id) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Candidate Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activeUser?.name?.take(1) ?: "U",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp,
                                    color = EmeraldPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = activeUser?.name ?: "Job Seeker",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    if (activeUser?.subscriptionTier != "FREE") {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = TechGold
                                        ) {
                                            Text(
                                                text = "PRO",
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${activeUser?.education} • ${activeUser?.city}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { viewModel.navigateTo("AI_STUDIO") },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("AI Resume", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Skills: ${activeUser?.skills}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Sub-tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = candidateTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                contentColor = EmeraldPrimary,
                divider = {}
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = candidateTab == idx,
                        onClick = { candidateTab = idx },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (candidateTab == idx) FontWeight.Bold else FontWeight.Normal,
                                color = if (candidateTab == idx) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }

        // TAB 0: APPLICATIONS TIMELINE TRACKER
        if (candidateTab == 0) {
            if (allApplications.isEmpty()) {
                item {
                    Text(
                        text = "You haven't submitted any job applications yet. Browse vacancies and apply!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            } else {
                items(allApplications) { app ->
                    ApplicationTrackerCard(
                        app = app,
                        onSimulateProgress = { viewModel.simulateRecruiterProgress(app) }
                    )
                }
            }
        }

        // TAB 1: SCHEDULED INTERVIEWS
        if (candidateTab == 1) {
            if (scheduledInterviews.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("No interviews scheduled yet.", fontWeight = FontWeight.Bold)
                            Text(
                                "When an employer shortlists your profile and schedules an interview, Google Meet links and details will appear here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(scheduledInterviews) { app ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = BorderStroke(1.dp, TechCyan.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(app.jobTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Surface(shape = RoundedCornerShape(6.dp), color = TechCyan.copy(alpha = 0.2f)) {
                                    Text(
                                        text = "Interview Confirmed",
                                        color = TechCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Text(app.companyName, style = MaterialTheme.typography.bodySmall, color = EmeraldPrimary)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Event, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("${app.interviewDate} at ${app.interviewTime}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }

                            if (app.interviewMeetingLink.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.VideoCall, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = app.interviewMeetingLink,
                                        style = MaterialTheme.typography.bodySmall.copy(color = TechCyan)
                                    )
                                }
                            }

                            if (app.interviewerNotes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Interviewer Notes: ${app.interviewerNotes}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: SAVED JOBS
        if (candidateTab == 2) {
            if (savedJobsList.isEmpty()) {
                item {
                    Text(
                        text = "No saved jobs yet. Tap the bookmark icon on any job card to save it for later.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            } else {
                items(savedJobsList) { job ->
                    JobCard(
                        job = job,
                        isSaved = true,
                        onSaveClick = { viewModel.toggleSaveJob(job.id) },
                        onDetailsClick = { viewModel.selectedJobForDetails.value = job },
                        onApplyClick = { viewModel.selectedJobForApply.value = job }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun ApplicationTrackerCard(
    app: ApplicationEntity,
    onSimulateProgress: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(app.jobTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(app.companyName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (app.status) {
                        "OFFER" -> StatusSuccess.copy(alpha = 0.2f)
                        "INTERVIEW_SCHEDULED" -> TechCyan.copy(alpha = 0.2f)
                        "SHORTLISTED" -> EmeraldPrimary.copy(alpha = 0.2f)
                        "REJECTED" -> StatusError.copy(alpha = 0.2f)
                        else -> TechGold.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = app.status.replace("_", " "),
                        color = when (app.status) {
                            "OFFER" -> StatusSuccess
                            "INTERVIEW_SCHEDULED" -> TechCyan
                            "SHORTLISTED" -> EmeraldPrimary
                            "REJECTED" -> StatusError
                            else -> TechGold
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5-Step Visual Timeline
            val steps = listOf("Applied", "Review", "Shortlisted", "Interview", "Offer")
            val currentStepIndex = when (app.status) {
                "APPLIED" -> 0
                "UNDER_REVIEW" -> 1
                "SHORTLISTED" -> 2
                "INTERVIEW_SCHEDULED" -> 3
                "OFFER" -> 4
                else -> 0
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { i, s ->
                    val isDone = i <= currentStepIndex
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(if (isDone) EmeraldPrimary else MaterialTheme.colorScheme.outline),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = s,
                            fontSize = 9.sp,
                            fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                            color = if (isDone) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (i < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .weight(0.6f)
                                .height(2.dp)
                                .background(if (i < currentStepIndex) EmeraldPrimary else MaterialTheme.colorScheme.outline)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom bar with Date and Live Simulation Trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Applied on ${app.appliedDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (app.status != "OFFER") {
                    OutlinedButton(
                        onClick = onSimulateProgress,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("⚡ Simulate Recruiter Action", fontSize = 10.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. EMPLOYER DASHBOARD
// ==========================================
@Composable
private fun EmployerDashboard(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val activeUser by viewModel.activeUser.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val allApplications by viewModel.allApplications.collectAsState()

    val myJobs = allJobs.filter { it.employerId == (activeUser?.id ?: 2) || it.companyName == activeUser?.companyName }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Employer Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TechCyan.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Business, contentDescription = null, tint = TechCyan, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = activeUser?.companyName ?: "Telangana Tech Innovations",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = "Verified Employer", tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = "Recruiter: ${activeUser?.name} • ${activeUser?.city}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.isPostJobOpen.value = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black),
                            modifier = Modifier.testTag("employer_post_job_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Post Job", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Job Postings & Candidates (${myJobs.size})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        if (myJobs.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No active job postings yet.")
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = { viewModel.isPostJobOpen.value = true }) {
                            Text("Post Your First Vacancy")
                        }
                    }
                }
            }
        } else {
            items(myJobs) { job ->
                val jobApps = allApplications.filter { it.jobId == job.id }
                EmployerJobItem(
                    job = job,
                    applications = jobApps,
                    onStatusUpdate = { appId, st -> viewModel.updateApplicationStatus(appId, st) },
                    onScheduleInterview = { app -> viewModel.isScheduleInterviewOpen.value = app }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun EmployerJobItem(
    job: JobEntity,
    applications: List<ApplicationEntity>,
    onStatusUpdate: (Long, String) -> Unit,
    onScheduleInterview: (ApplicationEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(job.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("${job.location} • Posted ${job.postedDate}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldPrimary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "${applications.size} Applicants",
                        color = EmeraldPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expand/Collapse Applicants
            OutlinedButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (expanded) "Hide Applicants" else "Review Applicants (${applications.size})")
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(10.dp))
                if (applications.isEmpty()) {
                    Text(
                        text = "No candidate applications yet for this role.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    applications.forEach { app ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(app.candidateName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("${app.candidateEmail} • ${app.candidatePhone}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Surface(shape = RoundedCornerShape(4.dp), color = EmeraldPrimary.copy(alpha = 0.15f)) {
                                        Text(app.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary, modifier = Modifier.padding(4.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Skills: ${app.candidateSkills}", style = MaterialTheme.typography.bodySmall)
                                if (app.coverNote.isNotBlank()) {
                                    Text("Pitch: \"${app.coverNote}\"", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    OutlinedButton(
                                        onClick = { onStatusUpdate(app.id, "SHORTLISTED") },
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text("Shortlist", fontSize = 11.sp)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Button(
                                        onClick = { onScheduleInterview(app) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color.Black),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text("Schedule Interview", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. ADMIN DASHBOARD
// ==========================================
@Composable
private fun AdminDashboard(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val allJobs by viewModel.allJobs.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val allApplications by viewModel.allApplications.collectAsState()
    val fraudReports by viewModel.fraudReports.collectAsState()
    val subscriptionPlans by viewModel.subscriptionPlans.collectAsState()

    var adminTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Job Moderation (${allJobs.size})", "Scam Reports (${fraudReports.size})", "Pricing Config")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Admin Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = TechGold.copy(alpha = 0.2f)
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = TechGold, modifier = Modifier.padding(8.dp).size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Admin Trust & Platform Control",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Telangana Job Safety & Fraud Detection Radar",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item {
            ScrollableTabRow(
                selectedTabIndex = adminTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                contentColor = TechGold,
                divider = {}
            ) {
                tabs.forEachIndexed { i, title ->
                    Tab(
                        selected = adminTab == i,
                        onClick = { adminTab = i },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (adminTab == i) FontWeight.Bold else FontWeight.Normal,
                                color = if (adminTab == i) TechGold else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }

        // TAB 0: ANALYTICS & OVERVIEW
        if (adminTab == 0) {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AdminKpiCard("Total Jobs", allJobs.size.toString(), EmeraldPrimary, Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    AdminKpiCard("Applications", allApplications.size.toString(), TechCyan, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    AdminKpiCard("Users", allUsers.size.toString(), Color(0xFF818CF8), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    AdminKpiCard("Scam Reports", fraudReports.size.toString(), StatusWarning, Modifier.weight(1f))
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Active Employers in Telangana", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        allUsers.filter { it.role == "EMPLOYER" }.forEach { emp ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(emp.companyName.ifBlank { emp.name }, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text(emp.email, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.verifyEmployer(emp.id, !emp.isVerified) },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(if (emp.isVerified) "Verified ✓" else "Verify Employer", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: MODERATION & FRAUD RADAR
        if (adminTab == 1) {
            items(allJobs) { job ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(
                        1.dp,
                        if (job.scamRiskScore > 30) StatusWarning else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(job.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${job.companyName} • ${job.location}", style = MaterialTheme.typography.labelSmall)
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (job.scamRiskScore > 30) StatusWarning.copy(alpha = 0.2f) else EmeraldPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Risk: ${job.scamRiskScore}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (job.scamRiskScore > 30) StatusWarning else EmeraldPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Status: ${job.status}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            if (job.status != "ACTIVE") {
                                Button(
                                    onClick = { viewModel.moderateJob(job.id, "ACTIVE") },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Approve", fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            if (job.status != "SUSPENDED") {
                                OutlinedButton(
                                    onClick = { viewModel.moderateJob(job.id, "SUSPENDED") },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Suspend Post", fontSize = 11.sp, color = StatusError)
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: FRAUD COMPLAINTS
        if (adminTab == 2) {
            if (fraudReports.isEmpty()) {
                item {
                    Text(
                        "No candidate fraud complaints reported. Platform is safe and secure.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(fraudReports) { rep ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Job #${rep.jobId}: ${rep.jobTitle}", fontWeight = FontWeight.Bold)
                                Surface(shape = RoundedCornerShape(4.dp), color = StatusError.copy(alpha = 0.2f)) {
                                    Text(rep.status, color = StatusError, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
                                }
                            }
                            Text("Reported by: ${rep.reporterName} on ${rep.reportedDate}", style = MaterialTheme.typography.labelSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Reason: ${rep.reason}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = StatusWarning)
                            if (rep.details.isNotBlank()) {
                                Text("Details: ${rep.details}", style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Button(
                                    onClick = { viewModel.resolveFraudReport(rep.id, "RESOLVED") },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Mark Resolved", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 3: SUBSCRIPTION PRICING CONFIG
        if (adminTab == 3) {
            item {
                Text(
                    text = "Configure Live Subscription Pricing (INR)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            items(subscriptionPlans) { plan ->
                var priceText by remember { mutableStateOf(plan.priceInr.toString()) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(plan.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(plan.billingCycle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("₹") },
                                modifier = Modifier.width(90.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val newP = priceText.toIntOrNull() ?: plan.priceInr
                                    viewModel.updatePlanPrice(plan.id, newP)
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(42.dp)
                            ) {
                                Text("Save", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun AdminKpiCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Black, fontSize = 24.sp, color = color)
        }
    }
}
