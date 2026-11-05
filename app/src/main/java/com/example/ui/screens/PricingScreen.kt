package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SubscriptionPlanEntity
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TechGold
import com.example.ui.viewmodel.MainViewModel

@Composable
fun PricingScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val plans by viewModel.subscriptionPlans.collectAsState()
    val activeUser by viewModel.activeUser.collectAsState()
    val currentReceipt by viewModel.paymentReceipt.collectAsState()

    var planTab by remember { mutableStateOf(0) } // 0: Job Seekers, 1: Employers
    val tabs = listOf("Candidate Plans", "Employer & Recruiter Plans")

    val candidatePlans = plans.filter { it.targetRole == "CANDIDATE" }
    val employerPlans = plans.filter { it.targetRole == "EMPLOYER" }
    val displayedPlans = if (planTab == 0) candidatePlans else employerPlans

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Pricing Headline Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(8.dp), color = EmeraldPrimary.copy(alpha = 0.2f)) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.padding(8.dp).size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Transparent Indian Pricing",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "No hidden charges • Razorpay verified • GST compliant",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Every candidate gets 3 free job applications per day. Upgrade anytime for unlimited applications, AI Multilingual Mock Interviews, and priority recruiter visibility.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Paid Receipt Banner if recent payment
        if (currentReceipt != null) {
            val r = currentReceipt!!
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, EmeraldPrimary)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Payment Receipt", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            Surface(shape = RoundedCornerShape(4.dp), color = EmeraldPrimary.copy(alpha = 0.2f)) {
                                Text("VERIFIED", color = EmeraldPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Order ID: ${r.orderId} • Payment ID: ${r.paymentId}", style = MaterialTheme.typography.labelSmall)
                        Text("Plan: ${r.planName} • Method: ${r.paymentMethod}", style = MaterialTheme.typography.bodySmall)
                        Text("Base: ₹${r.amountInr} + GST: ₹${r.gstInr} = Total ₹${r.totalPaidInr}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(r.timestamp, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = planTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                contentColor = EmeraldPrimary,
                divider = {}
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = planTab == idx,
                        onClick = { planTab = idx },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (planTab == idx) FontWeight.Bold else FontWeight.Normal,
                                color = if (planTab == idx) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }

        items(displayedPlans) { plan ->
            val isCurrentPlan = activeUser?.subscriptionTier?.contains(plan.name.take(3), ignoreCase = true) == true ||
                    (plan.priceInr == 0 && (activeUser?.subscriptionTier == "FREE" || activeUser?.subscriptionTier.isNullOrBlank()))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(
                    1.dp,
                    if (plan.isPopular) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(plan.name, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text(plan.billingCycle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        if (plan.isPopular) {
                            Surface(shape = RoundedCornerShape(12.dp), color = EmeraldPrimary) {
                                Text(
                                    text = "MOST POPULAR",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = if (plan.priceInr == 0) "Free" else "₹${plan.priceInr}",
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp,
                            color = if (plan.priceInr == 0) MaterialTheme.colorScheme.onSurface else EmeraldPrimary
                        )
                        if (plan.priceInr > 0) {
                            Text(
                                text = " + 18% GST",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Features list
                    plan.features.split("\n").forEach { feature ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(feature, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (isCurrentPlan) {
                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Current Active Plan")
                        }
                    } else {
                        Button(
                            onClick = {
                                if (plan.priceInr == 0) {
                                    viewModel.processRazorpayPayment(plan, "Free Activation")
                                } else {
                                    viewModel.selectedPlanForCheckout.value = plan
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("select_plan_${plan.id}"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (plan.isPopular) EmeraldPrimary else MaterialTheme.colorScheme.primary,
                                contentColor = if (plan.isPopular) Color.Black else Color.White
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (plan.priceInr > 0) {
                                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = if (plan.priceInr == 0) "Get Started Free" else "Upgrade via Razorpay",
                                    fontWeight = FontWeight.Bold
                                )
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
