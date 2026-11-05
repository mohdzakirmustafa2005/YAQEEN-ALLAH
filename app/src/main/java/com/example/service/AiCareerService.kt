package com.example.service

import com.example.data.model.InterviewFeedback
import com.example.data.model.Language
import com.example.data.model.MockInterviewQuestion

object AiCareerService {

    // 1. AI Resume Builder
    fun generateProfessionalResume(
        name: String,
        targetRole: String,
        education: String,
        skillsInput: String,
        projects: String
    ): GeneratedResume {
        val skillsList = skillsInput.split(",", ";").map { it.trim() }.filter { it.isNotEmpty() }
        val summary = "Proactive and detail-oriented $targetRole with a strong academic foundation in $education. Demonstrates practical competence in ${skillsList.take(4).joinToString(", ")}. Eager to contribute to dynamic engineering teams and deliver impactful solutions in the Indian technology sector."

        val bulletPoints = listOf(
            "Engineered modern responsive applications applying industry-standard architecture and clean code patterns.",
            "Integrated secure REST APIs, optimized local persistent caching, and handled asynchronous data streams.",
            "Collaborated on agile projects ($projects), conducting code reviews and ensuring 99.8% crash-free sessions.",
            "Leveraged ${skillsList.firstOrNull() ?: "core technologies"} to optimize performance and reduce latency by 25%."
        )

        return GeneratedResume(
            headline = "$name | $targetRole",
            summary = summary,
            skills = skillsList,
            achievements = bulletPoints,
            targetRole = targetRole
        )
    }

    // 2. Resume-to-Job Matching & Skill Gap Analysis
    fun analyzeSkillMatch(candidateSkills: String, jobRequiredSkills: String): SkillMatchReport {
        val candidateSet = candidateSkills.lowercase().split(",", ";", " ").map { it.trim() }.filter { it.length > 1 }.toSet()
        val jobSet = jobRequiredSkills.lowercase().split(",", ";").map { it.trim() }.filter { it.length > 1 }

        val matched = mutableListOf<String>()
        val missing = mutableListOf<String>()

        for (skill in jobSet) {
            val words = skill.lowercase().split(" ")
            if (words.any { candidateSet.contains(it) } || candidateSet.contains(skill.lowercase())) {
                matched.add(skill)
            } else {
                missing.add(skill)
            }
        }

        val total = (matched.size + missing.size).coerceAtLeast(1)
        val score = ((matched.size.toDouble() / total) * 100).toInt().coerceIn(20, 98)

        val recommendations = missing.map { skill ->
            "Complete a 3-day accelerated project module in $skill to boost interview readiness."
        }

        return SkillMatchReport(
            matchScore = score,
            matchedSkills = matched,
            missingSkills = missing,
            learningRoadmap = recommendations
        )
    }

    // 3. Multilingual AI Mock Interview Engine
    fun getMockInterviewQuestions(category: String): List<MockInterviewQuestion> {
        return listOf(
            MockInterviewQuestion(
                id = 1,
                questionEn = "Can you introduce yourself and tell us why you are interested in this role in Telangana?",
                questionTe = "మీ గురించి పరిచయం చేసుకోండి మరియు తెలంగాణలో ఈ ఉద్యోగం పట్ల మీకు ఎందుకు ఆసక్తి ఉందో చెప్పండి?",
                questionHi = "कृपया अपना परिचय दें और बताएं कि आप तेलंगाना में इस पद के लिए क्यों रुचि रखते हैं?",
                questionUr = "براہ کرم اپنا تعارف کروائیں اور بتائیں کہ آپ تلنگانہ میں اس عہدے میں کیوں دلچسپی رکھتے ہیں؟",
                category = "General / Cultural Fit",
                expectedKeyPoints = listOf("Education background", "Technical interests", "Long-term career vision in Telangana", "Passion for problem solving")
            ),
            MockInterviewQuestion(
                id = 2,
                questionEn = "How do you handle asynchronous operations and state management in your software projects?",
                questionTe = "మీ ప్రాజెక్ట్‌లలో అసమకాలిక కార్యకలాపాలు (Asynchronous) మరియు స్టేట్ మేనేజ్‌మెంట్‌ను మీరు ఎలా నిర్వహిస్తారు?",
                questionHi = "आप अपने सॉफ्टवेयर प्रोजेक्ट्स में एसिंक्रोनस ऑपरेशंस और स्टेट मैनेजमेंट को कैसे संभालते हैं?",
                questionUr = "آپ اپنے سوفٹ ویئر پراجیکٹس میں غیر ہم وقتی کاموں اور اسٹیٹ مینجمنٹ کو کس طرح ہینڈل کرتے ہیں؟",
                category = "Technical Architecture",
                expectedKeyPoints = listOf("Coroutines/Promises/Async-Await", "StateFlow/Redux/Live state", "Thread safety", "Error handling")
            ),
            MockInterviewQuestion(
                id = 3,
                questionEn = "Describe a challenging bug or problem you solved recently and the steps you took.",
                questionTe = "ఇటీవల మీరు పరిష్కరించిన ఒక క్లిష్టమైన సమస్య లేదా బగ్‌ను వివరించండి?",
                questionHi = "हाल ही में आपके द्वारा हल किए गए किसी चुनौतीपूर्ण बग या समस्या और उसके कदमों का वर्णन करें।",
                questionUr = "حال ہی میں حل کیے گئے کسی مشکل مسئلے کی وضاحت کریں اور آپ نے کیا اقدامات کیے؟",
                category = "Problem Solving",
                expectedKeyPoints = listOf("Root cause analysis", "Debugging tools used", "Fix implementation", "Testing & prevention")
            ),
            MockInterviewQuestion(
                id = 4,
                questionEn = "Where do you see yourself in the next 2-3 years, and how do you continuously upgrade your skills?",
                questionTe = "రాబోయే 2-3 సంవత్సరాల్లో మిమ్మల్ని మీరు ఎక్కడ చూడాలనుకుంటున్నారు, మీ నైపుణ్యాలను ఎలా మెరుగుపరుచుకుంటారు?",
                questionHi = "अगले 2-3 वर्षों में आप खुद को कहाँ देखते हैं, और आप अपने कौशल को कैसे अपग्रेड करते हैं?",
                questionUr = "اگلے 2-3 سالوں میں آپ خود کو کہاں دیکھتے ہیں، اور آپ اپنی صلاحیتوں کو کیسے اپ گریڈ کرتے ہیں؟",
                category = "Career Growth",
                expectedKeyPoints = listOf("Technical leadership", "Mentorship", "Continuous learning & certifications", "Company value creation")
            )
        )
    }

    fun evaluateInterviewAnswer(
        question: MockInterviewQuestion,
        candidateAnswer: String,
        language: Language
    ): InterviewFeedback {
        val wordCount = candidateAnswer.trim().split("\\s+".toRegex()).size
        val hasTechnicalTerms = candidateAnswer.contains("project", ignoreCase = true) ||
                candidateAnswer.contains("code", ignoreCase = true) ||
                candidateAnswer.contains("team", ignoreCase = true) ||
                candidateAnswer.contains("problem", ignoreCase = true) ||
                candidateAnswer.contains("learn", ignoreCase = true)

        val score = when {
            wordCount < 10 -> 4
            wordCount < 30 -> 6
            hasTechnicalTerms && wordCount >= 50 -> 9
            hasTechnicalTerms -> 8
            else -> 7
        }

        val strengths = when (language) {
            Language.TELUGU -> "మీ సమాధానంలో అంశంపై స్పష్టత మరియు ఆత్మవిశ్వాసం కనిపించాయి. ముఖ్య ఉద్దేశ్యాన్ని స్పష్టంగా వ్యక్తం చేశారు."
            Language.HINDI -> "आपके उत्तर में स्पष्टता और आत्मविश्वास दिखा। आपने मुख्य बिंदु को अच्छी तरह प्रस्तुत किया।"
            Language.URDU -> "آپ کے جواب میں واضح سوچ اور خود اعتمادی نظر آئی۔ آپ نے بنیادی نکتہ اچھے انداز میں پیش کیا۔"
            Language.ENGLISH -> "Strong articulation of personal experience and clear professional tone with relevant practical examples."
        }

        val improvements = when (language) {
            Language.TELUGU -> "సాధ్యమైనంత వరకు నిర్దిష్ట కొలమానాలు (Metrics), టెక్నాలజీ పేర్లు మరియు ప్రాజెక్ట్ ఫలితాలను ప్రస్తావించండి."
            Language.HINDI -> "प्रयास करें कि अपने उत्तर में ठोस परिणाम (Metrics) और तकनीकी उपकरणों के नाम शामिल करें।"
            Language.URDU -> "کوشش کریں کہ اپنے جواب میں ٹھوس نتائج اور مخصوص تکنیکی اوزاروں کا ذکر کریں۔"
            Language.ENGLISH -> "Quantify your achievements (e.g. improved performance by 30%) and explicitly name frameworks and debugging tools used."
        }

        val sampleAnswer = "A strong STAR (Situation, Task, Action, Result) response: State the specific business scenario, describe your exact individual contribution, mention the key technical tools, and finish with measurable business or customer impact."

        return InterviewFeedback(
            scoreOutOf10 = score,
            strengths = strengths,
            improvements = improvements,
            sampleIdealAnswer = sampleAnswer
        )
    }

    // 4. AI Job Description Generator for Employers
    fun generateJobDescription(
        roleTitle: String,
        city: String,
        experience: String,
        skills: String
    ): GeneratedJobDescription {
        val cleanSkills = skills.split(",", ";").map { it.trim() }.filter { it.isNotEmpty() }
        val overview = "We are seeking a talented $roleTitle based in $city. In this role, you will collaborate with cross-functional product and engineering teams to build high-scale, resilient digital platforms."
        val responsibilities = listOf(
            "Design, implement, and maintain scalable software modules adhering to best engineering practices.",
            "Write clean, performant, and thoroughly tested code using ${cleanSkills.take(3).joinToString(", ")}.",
            "Collaborate actively in daily standups, code reviews, and product architecture discussions.",
            "Optimize application latency, resource utilization, and user-centric workflows."
        )
        val requirements = listOf(
            "Relevant educational degree (B.Tech / MCA / B.Sc / Diploma in Computers or equivalent).",
            "Demonstrated experience ($experience) with solid fundamentals in $skills.",
            "Strong analytical thinking and practical debugging capabilities.",
            "Excellent team communication skills and eagerness to learn emerging technologies."
        )
        return GeneratedJobDescription(
            overview = overview,
            responsibilities = responsibilities.joinToString("\n• "),
            requirements = requirements.joinToString("\n• ")
        )
    }

    // 5. Job Fraud & Scam Detector
    fun detectJobFraud(
        title: String,
        description: String,
        contactEmail: String,
        salaryMax: Int,
        requiresFee: Boolean
    ): FraudRiskAnalysis {
        val scamKeywords = listOf(
            "registration fee", "security deposit", "pay money", "transfer money",
            "guaranteed daily income", "telegram only", "no interview direct join",
            "part time earn 50000 daily", "training charge", "laptop deposit", "processing fee"
        )

        var riskScore = 0
        val riskFlags = mutableListOf<String>()

        if (requiresFee) {
            riskScore += 60
            riskFlags.add("Critical: Demands upfront monetary charge / deposit from job seeker.")
        }

        val combinedText = "$title $description".lowercase()
        for (kw in scamKeywords) {
            if (combinedText.contains(kw)) {
                riskScore += 25
                riskFlags.add("Suspicious keyword found: '$kw'")
            }
        }

        if (contactEmail.endsWith("@gmail.com") || contactEmail.endsWith("@yahoo.com")) {
            riskScore += 10
            riskFlags.add("Public free email domain used instead of verified company domain.")
        }

        if (salaryMax > 200000 && (combinedText.contains("data entry") || combinedText.contains("no experience"))) {
            riskScore += 30
            riskFlags.add("Unrealistic salary promised for entry-level / data entry role.")
        }

        val finalScore = riskScore.coerceIn(0, 100)
        val isFlagged = finalScore >= 40

        return FraudRiskAnalysis(
            riskScore = finalScore,
            isSuspicious = isFlagged,
            riskFactors = riskFlags,
            verdict = when {
                finalScore >= 70 -> "HIGH SCAM RISK - Block Immediately"
                finalScore >= 40 -> "MODERATE RISK - Manual Review Required"
                else -> "SAFE & VERIFIED"
            }
        )
    }
}

data class GeneratedResume(
    val headline: String,
    val summary: String,
    val skills: List<String>,
    val achievements: List<String>,
    val targetRole: String
)

data class SkillMatchReport(
    val matchScore: Int,
    val matchedSkills: List<String>,
    val missingSkills: List<String>,
    val learningRoadmap: List<String>
)

data class GeneratedJobDescription(
    val overview: String,
    val responsibilities: String,
    val requirements: String
)

data class FraudRiskAnalysis(
    val riskScore: Int,
    val isSuspicious: Boolean,
    val riskFactors: List<String>,
    val verdict: String
)
