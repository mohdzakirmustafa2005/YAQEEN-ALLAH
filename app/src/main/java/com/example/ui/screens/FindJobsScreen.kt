package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobCategory
import com.example.ui.components.JobCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun FindJobsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val filteredJobs by viewModel.filteredJobs.collectAsState()
    val savedJobIds by viewModel.savedJobIds.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val selectedCat by viewModel.selectedCategory.collectAsState()
    val selectedType by viewModel.selectedJobType.collectAsState()

    val cities = listOf("All", "Mahbubnagar", "Hyderabad", "Remote", "Bengaluru")
    val types = listOf("All", "Full Time", "Internship", "Remote / WFH")
    val categories = listOf("All") + JobCategory.values().map { it.name }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.searchQuery.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("job_search_input"),
                placeholder = { Text("Search by title, skill (e.g. Kotlin), company...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = EmeraldPrimary)
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // City Filters
        item {
            Column {
                Text("City:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(cities) { city ->
                        val isSelected = selectedCity == city
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { viewModel.selectedCity.value = city }
                        ) {
                            Text(
                                text = city,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Category Filters
        item {
            Column {
                Text("Category:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCat == cat
                        val label = if (cat == "All") "All Categories" else JobCategory.valueOf(cat).displayName
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { viewModel.selectedCategory.value = cat }
                        ) {
                            Text(
                                text = label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Type Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(types) { t ->
                        val isSelected = selectedType == t
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) EmeraldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.clickable { viewModel.selectedJobType.value = t }
                        ) {
                            Text(
                                text = t,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                if (query.isNotEmpty() || selectedCity != "All" || selectedCat != "All" || selectedType != "All") {
                    Text(
                        text = "Reset",
                        color = EmeraldPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            viewModel.searchQuery.value = ""
                            viewModel.selectedCity.value = "All"
                            viewModel.selectedCategory.value = "All"
                            viewModel.selectedJobType.value = "All"
                        }
                    )
                }
            }
        }

        // Salary Range Slider
        item {
            val minSal by viewModel.minSalaryFilter.collectAsState()
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Min Salary Filter:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (minSal == 0) "Any Salary" else "₹${minSal / 1000}k+ / month",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        )
                    }
                    androidx.compose.material3.Slider(
                        value = minSal.toFloat(),
                        onValueChange = { viewModel.minSalaryFilter.value = it.toInt() },
                        valueRange = 0f..60000f,
                        steps = 5,
                        colors = androidx.compose.material3.SliderDefaults.colors(
                            thumbColor = EmeraldPrimary,
                            activeTrackColor = EmeraldPrimary
                        )
                    )
                }
            }
        }

        // Results count
        item {
            Text(
                text = "Found ${filteredJobs.size} Verified Openings",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Job Cards List
        if (filteredJobs.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No vacancies found matching your current filter.", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Try switching city to All or clearing your search keywords.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(onClick = {
                            viewModel.searchQuery.value = ""
                            viewModel.selectedCity.value = "All"
                            viewModel.selectedCategory.value = "All"
                            viewModel.selectedJobType.value = "All"
                            viewModel.minSalaryFilter.value = 0
                        }) {
                            Text("Clear Filters")
                        }
                    }
                }
            }
        } else {
            items(filteredJobs) { job ->
                JobCard(
                    job = job,
                    isSaved = savedJobIds.contains(job.id),
                    onSaveClick = { viewModel.toggleSaveJob(job.id) },
                    onDetailsClick = { viewModel.selectedJobForDetails.value = job },
                    onApplyClick = { viewModel.selectedJobForApply.value = job },
                    onQuickApplyClick = { viewModel.quickApply(job) },
                    onShareClick = { viewModel.snackbarMessage.value = "Link copied for ${job.title} at ${job.companyName}!" }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
