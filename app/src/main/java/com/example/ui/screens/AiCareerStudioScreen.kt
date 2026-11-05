package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechGold
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiCareerStudioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("AI Resume Builder", "Skill Gap Analyzer", "Multilingual Mock Interview")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Studio Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(EmeraldPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "AI Career Studio",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Resume builder, job skill matching & multilingual interview simulator",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = EmeraldPrimary,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (selectedTab == index) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (selectedTab) {
            0 -> AiResumeBuilderTab(viewModel)
            1 -> AiSkillGapMatcherTab(viewModel)
            2 -> AiMockInterviewTab(viewModel)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AiResumeBuilderTab(viewModel: MainViewModel) {
    val activeUser by viewModel.activeUser.collectAsState()
    val generatedResume by viewModel.generatedResume.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    var name by remember { mutableStateOf(activeUser?.name ?: "Mohammed Imran Ali") }
    var targetRole by remember { mutableStateOf("Android Developer (Kotlin & Compose)") }
    var education by remember { mutableStateOf(activeUser?.education ?: "B.Tech Computer Science") }
    var skills by remember { mutableStateOf(activeUser?.skills ?: "Kotlin, Jetpack Compose, Coroutines, Room DB, Git") }
    var projects by remember { mutableStateOf("YAQEEN ALLAH Job Marketplace App, Weather Radar App") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Generate Tailored Indian Tech Resume",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = targetRole,
                        onValueChange = { targetRole = it },
                        label = { Text("Target Role (e.g. Android Developer)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = education,
                        onValueChange = { education = it },
                        label = { Text("Education / Degree") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = skills,
                        onValueChange = { skills = it },
                        label = { Text("Key Technical Skills (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = projects,
                        onValueChange = { projects = it },
                        label = { Text("Key Projects / Work Done") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.runAiResumeBuilder(name, targetRole, education, skills, projects)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_resume_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate ATS-Optimized Resume", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (generatedResume != null) {
            val r = generatedResume!!
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AI Generated Resume Preview",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldPrimary
                            )
                            OutlinedButton(
                                onClick = {
                                    val fullResumeText = "${r.headline}\n\nSUMMARY:\n${r.summary}\n\nKEY SKILLS:\n${r.skills.joinToString(", ")}\n\nACHIEVEMENTS:\n${r.achievements.joinToString("\n• ")}"
                                    clipboardManager.setText(AnnotatedString(fullResumeText))
                                    viewModel.snackbarMessage.value = "Full Resume copied to clipboard!"
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Copy Text", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(r.headline, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        Text("Professional Summary", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(r.summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Core Competencies", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            r.skills.forEach { sk ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(sk, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Key Achievements & Project Deliverables", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        r.achievements.forEach { pt ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Text("• ", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                                Text(pt, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
private fun AiSkillGapMatcherTab(viewModel: MainViewModel) {
    val activeJobs by viewModel.activeJobs.collectAsState()
    val activeUser by viewModel.activeUser.collectAsState()
    val skillMatchReport by viewModel.skillMatchReport.collectAsState()

    var candidateSkillsInput by remember {
        mutableStateOf(activeUser?.skills ?: "Kotlin, Jetpack Compose, Android, Java, SQLite, Git, REST APIs")
    }
    var selectedJobIndex by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Match Your Skills Against Target Job",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = candidateSkillsInput,
                        onValueChange = { candidateSkillsInput = it },
                        label = { Text("Your Current Skills") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Select Target Job Opening:", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(6.dp))

                    activeJobs.take(4).forEachIndexed { index, job ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedJobIndex = index }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedJobIndex == index) EmeraldPrimary else Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(job.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("${job.companyName} • ${job.location}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val targetJob = activeJobs.getOrNull(selectedJobIndex) ?: activeJobs.first()
                            viewModel.runSkillMatchAnalysis(candidateSkillsInput, targetJob)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("analyze_skill_match_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Calculate Match % & Skill Roadmap", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (skillMatchReport != null) {
            val report = skillMatchReport!!
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Match Readiness", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${report.matchScore}% Compatibility", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = EmeraldPrimary)
                            }
                            CircularProgressIndicator(
                                progress = { report.matchScore / 100f },
                                color = EmeraldPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        Text("Matched Skills (Strengths):", fontWeight = FontWeight.Bold, color = StatusSuccess, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (report.matchedSkills.isEmpty()) "None matched directly" else report.matchedSkills.joinToString(", "),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Missing Skills (Skill Gaps):", fontWeight = FontWeight.Bold, color = StatusWarning, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (report.missingSkills.isEmpty()) "No missing skills!" else report.missingSkills.joinToString(", "),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Recommended 7-Day Upskilling Plan:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        report.learningRoadmap.forEach { step ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(step, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
private fun AiMockInterviewTab(viewModel: MainViewModel) {
    val questions by viewModel.interviewQuestions.collectAsState()
    val currentIndex by viewModel.currentInterviewQuestionIndex.collectAsState()
    val answerText by viewModel.candidateAnswerText.collectAsState()
    val feedback by viewModel.interviewFeedback.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    val currentQ = questions.getOrNull(currentIndex) ?: return

    val questionText = when (currentLang) {
        Language.TELUGU -> currentQ.questionTe
        Language.HINDI -> currentQ.questionHi
        Language.URDU -> currentQ.questionUr
        Language.ENGLISH -> currentQ.questionEn
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Language selector info chip
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = TechCyan.copy(alpha = 0.12f)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Current Practice Language: ${currentLang.nativeName} (${currentLang.label}). You can answer in English, Telugu, Hindi, or Urdu.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Question Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = RoundedCornerShape(6.dp), color = EmeraldPrimary.copy(alpha = 0.2f)) {
                            Text(
                                text = "Question ${currentIndex + 1} of ${questions.size} • ${currentQ.category}",
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Row {
                            IconButton(
                                onClick = { viewModel.previousInterviewQuestion() },
                                enabled = currentIndex > 0
                            ) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous")
                            }
                            IconButton(
                                onClick = { viewModel.nextInterviewQuestion() },
                                enabled = currentIndex < questions.size - 1
                            ) {
                                Icon(Icons.Default.ChevronRight, contentDescription = "Next")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = questionText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Key areas interviewer is assessing: ${currentQ.expectedKeyPoints.joinToString(" • ")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = answerText,
                        onValueChange = { viewModel.candidateAnswerText.value = it },
                        label = { Text("Type or paste your answer here...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("interview_answer_input"),
                        minLines = 4,
                        maxLines = 7
                    )

                    // Simulated Voice Dictation & Audio Wave Helper
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                val simulatedSpeech = when (currentLang) {
                                    Language.TELUGU -> "నేను కంప్యూటర్ సైన్స్ గ్రాడ్యుయేట్‌ని. నేను ఆండ్రాయిడ్ అప్లికేషన్ల అభివృద్ధిలో ప్రాజెక్టులు పూర్తి చేశాను. కష్టపడి పనిచేసే స్వభావం, బృందంతో కలిసి పనిచేసే నైపుణ్యం నాకు ఉన్నాయి."
                                    Language.HINDI -> "नमस्ते, मैं कंप्यूटर साइंस का छात्र हूँ। मैंने कोटलिन और आधुनिक एंड्रॉइड डेवलपमेंट में प्रोजेक्ट्स बनाए हैं और नई चुनौतियों को सीखने के लिए हमेशा तत्पर रहता हूँ।"
                                    Language.URDU -> "میرا تعلق تلنگانہ سے ہے اور میں نے کمپیوٹر سائنس میں تعلیم مکمل کی ہے۔ مجھے سوفٹ ویئر ڈویلپمنٹ اور اینڈرائیڈ پراجیکٹس پر کام کرنے کا گہرا شوق ہے۔"
                                    Language.ENGLISH -> "Hello, I am a passionate software engineer with hands-on experience building Kotlin and Jetpack Compose applications. In my recent projects, I implemented clean MVVM architecture, coroutines flow, and optimized local SQLite database operations."
                                }
                                viewModel.candidateAnswerText.value = simulatedSpeech
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("🎤 Fill Sample Spoken Answer", fontSize = 11.sp, color = TechCyan)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp, 14.dp).background(EmeraldPrimary, CircleShape))
                            Spacer(modifier = Modifier.width(3.dp))
                            Box(modifier = Modifier.size(6.dp, 22.dp).background(TechCyan, CircleShape))
                            Spacer(modifier = Modifier.width(3.dp))
                            Box(modifier = Modifier.size(6.dp, 10.dp).background(TechGold, CircleShape))
                            Spacer(modifier = Modifier.width(3.dp))
                            Box(modifier = Modifier.size(6.dp, 18.dp).background(EmeraldPrimary, CircleShape))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.submitMockInterviewAnswer(answerText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("submit_interview_answer_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black),
                        enabled = answerText.trim().isNotEmpty()
                    ) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Answer for AI Evaluation", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Feedback Card
        if (feedback != null) {
            val fb = feedback!!
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AI Evaluation & Score",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldPrimary
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (fb.scoreOutOf10 >= 7) StatusSuccess.copy(alpha = 0.2f) else StatusWarning.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${fb.scoreOutOf10} / 10",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = if (fb.scoreOutOf10 >= 7) StatusSuccess else StatusWarning,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 10.dp))

                        Text("Strengths Observed:", fontWeight = FontWeight.Bold, color = StatusSuccess, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(fb.strengths, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Areas for Improvement:", fontWeight = FontWeight.Bold, color = StatusWarning, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(fb.improvements, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Recommended High-Impact Answer Structure:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(fb.sampleIdealAnswer, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(12.dp))

                        if (currentIndex < questions.size - 1) {
                            Button(
                                onClick = { viewModel.nextInterviewQuestion() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Next Question (${currentIndex + 2}/${questions.size})")
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
