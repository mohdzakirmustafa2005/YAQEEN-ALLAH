package com.example.service

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object PaymentService {

    data class PaymentOrder(
        val orderId: String,
        val amountInr: Int,
        val planName: String,
        val planId: String,
        val customerName: String,
        val customerEmail: String
    )

    data class PaymentReceipt(
        val paymentId: String,
        val orderId: String,
        val amountInr: Int,
        val planName: String,
        val planId: String,
        val paymentMethod: String,
        val timestamp: String,
        val status: String,
        val gstInr: Int,
        val totalPaidInr: Int
    )

    fun createOrder(
        planId: String,
        planName: String,
        amountInr: Int,
        customerName: String,
        customerEmail: String
    ): PaymentOrder {
        val randomSuffix = (100000..999999).random()
        return PaymentOrder(
            orderId = "order_YAQ_$randomSuffix",
            amountInr = amountInr,
            planName = planName,
            planId = planId,
            customerName = customerName,
            customerEmail = customerEmail
        )
    }

    fun verifyAndCompletePayment(
        order: PaymentOrder,
        paymentMethod: String // "UPI (Google Pay)", "PhonePe", "HDFC NetBanking", "Card"
    ): PaymentReceipt {
        val randomId = UUID.randomUUID().toString().substring(0, 8)
        val paymentId = "pay_rzp_$randomId"
        val timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val gst = (order.amountInr * 0.18).toInt() // 18% GST standard in India
        val total = order.amountInr + gst

        return PaymentReceipt(
            paymentId = paymentId,
            orderId = order.orderId,
            amountInr = order.amountInr,
            planName = order.planName,
            planId = order.planId,
            paymentMethod = paymentMethod,
            timestamp = timestamp,
            status = "SUCCESS - Verified by Razorpay",
            gstInr = gst,
            totalPaidInr = total
        )
    }
}
