package com.example

import com.example.service.AiCareerService
import com.example.service.PaymentService
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun `test AI resume builder generates structured resume`() {
    val resume = AiCareerService.generateProfessionalResume(
      name = "Mohammed Imran Ali",
      targetRole = "Android Developer",
      education = "B.Tech Computer Science",
      skillsInput = "Kotlin, Jetpack Compose, Room, Coroutines",
      projects = "Job Marketplace App"
    )
    assertNotNull(resume)
    assertTrue(resume.headline.contains("Android Developer"))
    assertTrue(resume.summary.contains("B.Tech Computer Science"))
    assertTrue(resume.achievements.isNotEmpty())
  }

  @Test
  fun `test AI skill gap analysis computes matching score`() {
    val report = AiCareerService.analyzeSkillMatch(
      candidateSkills = "Kotlin, Jetpack Compose, Coroutines, Room, Git",
      jobRequiredSkills = "Kotlin, Jetpack Compose, Coroutines, CI/CD, Ktor"
    )
    assertTrue(report.matchScore in 20..100)
    assertTrue(report.matchedSkills.any { it.contains("Kotlin", ignoreCase = true) })
    assertTrue(report.missingSkills.any { it.contains("CI/CD", ignoreCase = true) })
  }

  @Test
  fun `test job fraud detector flags upfront fees`() {
    val fraudReport = AiCareerService.detectJobFraud(
      title = "Part Time Data Entry",
      description = "Pay registration fee of Rs 1000 for job kit",
      contactEmail = "scam@gmail.com",
      salaryMax = 250000,
      requiresFee = true
    )
    assertTrue(fraudReport.isSuspicious)
    assertTrue(fraudReport.riskScore >= 60)
  }

  @Test
  fun `test payment service simulates order and verification`() {
    val order = PaymentService.createOrder(
      planId = "candidate_pro",
      planName = "Pro Job Seeker",
      amountInr = 199,
      customerName = "Mohammed Imran",
      customerEmail = "candidate@yaqeenallah.in"
    )
    assertEquals(199, order.amountInr)
    val receipt = PaymentService.verifyAndCompletePayment(order, "UPI (Google Pay)")
    assertTrue(receipt.paymentId.startsWith("pay_rzp_"))
    assertEquals(199, receipt.amountInr)
  }
}
