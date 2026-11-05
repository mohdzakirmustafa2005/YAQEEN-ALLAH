package com.example.service

import com.example.data.model.Language

object LocalizationHelper {
    fun getString(key: String, language: Language): String {
        return translations[key]?.get(language) ?: translations[key]?.get(Language.ENGLISH) ?: key
    }

    private val translations = mapOf(
        "app_title" to mapOf(
            Language.ENGLISH to "YAQEEN ALLAH",
            Language.TELUGU to "యకీన్ అల్లాహ్",
            Language.HINDI to "यकीन अल्लाह",
            Language.URDU to "یقین اللہ"
        ),
        "tagline" to mapOf(
            Language.ENGLISH to "Your City. Your Skills. Your Next Job.",
            Language.TELUGU to "మీ నగరం. మీ నైపుణ్యాలు. మీ తదుపరి ఉద్యోగం.",
            Language.HINDI to "आपका शहर। आपका कौशल। आपकी अगली नौकरी।",
            Language.URDU to "آپ کا شہر۔ آپ کی مہارت۔ آپ کی اگلی ملازمت۔"
        ),
        "hero_heading" to mapOf(
            Language.ENGLISH to "Find Your Future. Build Your Career.",
            Language.TELUGU to "మీ భవిష్యత్తును కనుగొనండి. కెరీర్‌ను నిర్మించుకోండి.",
            Language.HINDI to "अपना भविष्य खोजें। अपना करियर बनाएं।",
            Language.URDU to "اپنا مستقبل تلاش کریں۔ اپنا کیریئر بنائیں۔"
        ),
        "hero_subheading" to mapOf(
            Language.ENGLISH to "Discover verified jobs, internships and career opportunities across Telangana and India.",
            Language.TELUGU to "తెలంగాణ మరియు భారతదేశం అంతటా ధృవీకరించబడిన ఉద్యోగాలు, ఇంటర్న్‌షిప్‌లను కనుగొనండి.",
            Language.HINDI to "तेलंगाना और भारत भर में सत्यापित नौकरियां, इंटर्नशिप और करियर अवसर खोजें।",
            Language.URDU to "تلنگانہ اور پورے ہندوستان میں تصدیق شدہ ملازمتیں اور انٹرن شپس تلاش کریں۔"
        ),
        "nav_home" to mapOf(
            Language.ENGLISH to "Home",
            Language.TELUGU to "హోమ్",
            Language.HINDI to "होम",
            Language.URDU to "ہوم"
        ),
        "nav_jobs" to mapOf(
            Language.ENGLISH to "Find Jobs",
            Language.TELUGU to "ఉద్యోగాలు",
            Language.HINDI to "नौकरियां",
            Language.URDU to "ملازمتیں"
        ),
        "nav_internships" to mapOf(
            Language.ENGLISH to "Internships",
            Language.TELUGU to "ఇంటర్న్‌షిప్స్",
            Language.HINDI to "इंटर्नशिप",
            Language.URDU to "انٹرن شپس"
        ),
        "nav_ai_studio" to mapOf(
            Language.ENGLISH to "AI Career",
            Language.TELUGU to "AI కెరీర్",
            Language.HINDI to "AI करियर",
            Language.URDU to "اے آئی کیریئر"
        ),
        "nav_dashboard" to mapOf(
            Language.ENGLISH to "Dashboard",
            Language.TELUGU to "డాష్‌బోర్డ్",
            Language.HINDI to "डैशबोर्ड",
            Language.URDU to "ڈیش بورڈ"
        ),
        "nav_pricing" to mapOf(
            Language.ENGLISH to "Pricing",
            Language.TELUGU to "ధరలు",
            Language.HINDI to "प्लान्स",
            Language.URDU to "قیمتیں"
        ),
        "btn_apply_now" to mapOf(
            Language.ENGLISH to "Apply Now",
            Language.TELUGU to "దరఖాస్తు చేయండి",
            Language.HINDI to "आवेदन करें",
            Language.URDU to "ابھی اپلائی کریں"
        ),
        "btn_post_job" to mapOf(
            Language.ENGLISH to "Post a Job",
            Language.TELUGU to "ఉద్యోగాన్ని పోస్ట్ చేయండి",
            Language.HINDI to "नौकरी पोस्ट करें",
            Language.URDU to "ملازمت پوسٹ کریں"
        ),
        "verified_badge" to mapOf(
            Language.ENGLISH to "Verified Employer",
            Language.TELUGU to "ధృవీకరించబడిన యజమాని",
            Language.HINDI to "सत्यापित नियोक्ता",
            Language.URDU to "تصدیق شدہ آجر"
        ),
        "scam_alert_title" to mapOf(
            Language.ENGLISH to "Trust & Safety: Zero Upfront Fees",
            Language.TELUGU to "విశ్వసనీయత: ముందస్తు రుసుములు లేవు",
            Language.HINDI to "विश्वास और सुरक्षा: शून्य अग्रिम शुल्क",
            Language.URDU to "اعتماد اور حفاظت: کوئی ایڈوانس فیس نہیں"
        ),
        "scam_alert_desc" to mapOf(
            Language.ENGLISH to "Legitimate employers never demand money, deposits, or registration charges for jobs or interview calls.",
            Language.TELUGU to "చట్టబద్ధమైన కంపెనీలు ఎప్పుడూ ఉద్యోగ ఇంటర్వ్యూల కోసం డబ్బు లేదా డిపాజిట్లు అడగవు.",
            Language.HINDI to "वास्तविक नियोक्ता कभी भी नौकरी या साक्षात्कार के लिए पैसे या पंजीकरण शुल्क नहीं मांगते हैं।",
            Language.URDU to "حقیقی آجر کبھی بھی نوکری یا انٹرویو کے لیے رقم یا رجسٹریشن چارجز کا مطالبہ نہیں کرتے ہیں۔"
        ),
        "daily_applies" to mapOf(
            Language.ENGLISH to "Daily Free Applications",
            Language.TELUGU to "రోజువారీ ఉచిత దరఖాస్తులు",
            Language.HINDI to "दैनिक निःशुल्क आवेदन",
            Language.URDU to "روزانہ مفت درخواستیں"
        )
    )
}
