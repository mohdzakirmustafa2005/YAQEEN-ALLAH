package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.service.AiCareerService
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.StatusWarning

@Composable
fun PostJobDialog(
    onDismiss: () -> Unit,
    onSubmit: (
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
        requiresFee: Boolean
    ) -> Unit
) {
    var title by remember { mutableStateOf("Junior Web Developer") }
    var category by remember { mutableStateOf("SOFTWARE_DEV") }
    var city by remember { mutableStateOf("Mahbubnagar") }
    var location by remember { mutableStateOf("IT Tower, Mahbubnagar") }
    var jobType by remember { mutableStateOf("Full Time") }
    var minSalText by remember { mutableStateOf("25000") }
    var maxSalText by remember { mutableStateOf("35000") }
    var isInternship by remember { mutableStateOf(false) }
    var isPaid by remember { mutableStateOf(true) }
    var stipendDetails by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("HTML/CSS, JavaScript, React, Git") }
    var experience by remember { mutableStateOf("Fresher to 1 Year") }
    var education by remember { mutableStateOf("B.Tech / MCA / B.Sc Computers") }
    var description by remember { mutableStateOf("We are hiring passionate web developers to build responsive web applications for Telangana rural business clients.") }
    var responsibilities by remember { mutableStateOf("Develop responsive frontends, connect with REST APIs, debug browser issues, participate in daily team sprints.") }
    var requiresFee by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Post a New Vacancy",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Job / Internship Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City (e.g. Mahbubnagar)") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Detailed Location / Office") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = minSalText,
                        onValueChange = { minSalText = it },
                        label = { Text("Min Salary (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = maxSalText,
                        onValueChange = { maxSalText = it },
                        label = { Text("Max Salary (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Internship Checkbox & Disclosures
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isInternship = !isInternship }
                ) {
                    Checkbox(checked = isInternship, onCheckedChange = { isInternship = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("This is an Internship Opportunity (B.Tech / MCA)", style = MaterialTheme.typography.bodySmall)
                }

                if (isInternship) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                            .clickable { isPaid = !isPaid }
                    ) {
                        Checkbox(checked = isPaid, onCheckedChange = { isPaid = it })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isPaid) "Paid Internship (Monthly stipend required)" else "Unpaid Academic Internship (Certificate provided)", style = MaterialTheme.typography.bodySmall)
                    }

                    OutlinedTextField(
                        value = stipendDetails,
                        onValueChange = { stipendDetails = it },
                        label = { Text("Internship Disclosures & Stipend Details") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. ₹12,000/month stipend + pre-placement offer") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text("Skills Required (comma-separated)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = experience,
                        onValueChange = { experience = it },
                        label = { Text("Experience") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = education,
                        onValueChange = { education = it },
                        label = { Text("Education Eligibility") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // AI JD Generator Shortcut
                OutlinedButton(
                    onClick = {
                        val aiJd = AiCareerService.generateJobDescription(title, city, experience, skills)
                        description = aiJd.overview
                        responsibilities = aiJd.responsibilities
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Generate Job Description with AI")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Job Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = responsibilities,
                    onValueChange = { responsibilities = it },
                    label = { Text("Key Responsibilities") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Zero-Scam Disclosure Checkbox
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Anti-Fraud Rule: Employers must NEVER charge registration, security deposit, or training fees.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val minSal = minSalText.toIntOrNull() ?: 20000
                        val maxSal = maxSalText.toIntOrNull() ?: 35000
                        onSubmit(
                            title, category, city, location, jobType, minSal, maxSal,
                            isInternship, isPaid, stipendDetails, skills, experience,
                            education, description, responsibilities, requiresFee
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_post_job_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Publish Vacancy", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
