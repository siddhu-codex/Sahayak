package com.example.data.ai

import com.example.BuildConfig
import com.example.data.model.UiActionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

data class GuideChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val spokenAudioText: String? = null,
    val suggestedActionTitle: String? = null,
    val suggestedActionType: UiActionType? = null,
    val timestamp: Long = System.currentTimeMillis()
)

object AppGuideBotService {

    val initialWelcomeMessage = GuideChatMessage(
        isUser = false,
        text = "Hello! I am your Sahayak App Guide (ॲप मार्गदर्शक).\n\nI can explain everything about how this app works, from Voice Search and Welfare Schemes to Mandi Rates, Live Translation, and 7/12 Document Reading. Ask me any question below!",
        spokenAudioText = "नमस्कार! मी सहायक ॲप मार्गदर्शक आहे. हे ॲप कसे वापरावे, व्हॉईस सर्च, योजना आणि थेट भाषांतर कसे काम करते ते मला विचारा.",
        suggestedActionTitle = "Learn Voice Navigation",
        suggestedActionType = UiActionType.READ_SCREEN
    )

    val quickQuestions = listOf(
        "How do I use voice search?",
        "How to apply for Ladki Bahin Scheme?",
        "How does Live Translation work?",
        "How to read 7/12 Satbara documents?",
        "How to change Marathi dialects?",
        "What is MahaAgri & Mandi Rates?",
        "How to enable large text for seniors?"
    )

    suspend fun answerQuestion(userQuestion: String): GuideChatMessage = withContext(Dispatchers.IO) {
        val trimmed = userQuestion.trim()
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are the friendly official App Guide & Assistant for the "Sahayak" Android app.
                    The app is designed for Maharashtra citizens (farmers, seniors, women, students) providing:
                    1. Voice Concierge: Central Mic button for hands-free voice commands in 7 regional Marathi dialects (Varhadi, Ahirani, Malvani, etc.).
                    2. Welfare Schemes: Verified state schemes (Ladki Bahin Yojana, Namo Shetkari, MJPJAY Health, Shravanbal Pension) with voice-guided eligibility interviews.
                    3. MahaAgri & Mandi: Live APMC commodity prices (Akola, Latur, Nashik) and Crop Doctor pest advisory (Yellow mosaic, pink bollworm).
                    4. Live Speech Translation: Dual push-to-talk bidirectional translation between regional Marathi dialects and English/Hindi/Gujarati/Kannada.
                    5. Document OCR Reader: Reads official physical papers (7/12 Satbara, ration cards, pension sanction orders) out loud with audio summaries.
                    6. Senior Accessibility: Large font toggle ('TT' button) and screen reader ('Volume' button) in the top bar.

                    User's Question: "$trimmed"

                    Provide a friendly, structured, step-by-step answer explaining how to use the feature. Keep it concise (3-4 bullet points or short paragraphs). Include both English and a brief Marathi summary.
                """.trimIndent()

                val aiResult = GeminiAiService.callGeminiRestApiSimple(apiKey, prompt)
                if (!aiResult.isNullOrBlank()) {
                    val cleanText = aiResult.trim()
                    val (actionTitle, actionType) = detectSuggestedAction(trimmed)
                    return@withContext GuideChatMessage(
                        isUser = false,
                        text = cleanText,
                        spokenAudioText = cleanText.take(250),
                        suggestedActionTitle = actionTitle,
                        suggestedActionType = actionType
                    )
                }
            } catch (e: Exception) {
                // Fallback to offline guide
            }
        }

        return@withContext offlineGuideAnswer(trimmed)
    }

    private fun detectSuggestedAction(query: String): Pair<String?, UiActionType?> {
        val lower = query.lowercase()
        return when {
            lower.contains("scheme") || lower.contains("ladki") || lower.contains("योजना") || lower.contains("pension") -> 
                Pair("Open Schemes Section", UiActionType.NAVIGATE_SCHEMES)
            lower.contains("agri") || lower.contains("mandi") || lower.contains("crop") || lower.contains("बाजारभाव") || lower.contains("शेतकरी") -> 
                Pair("Open Mandi & Crops", UiActionType.NAVIGATE_AGRI)
            lower.contains("translat") || lower.contains("भाषांतर") || lower.contains("दुभाषी") || lower.contains("interpret") -> 
                Pair("Open Live Translation", UiActionType.NAVIGATE_TRANSLATE)
            lower.contains("doc") || lower.contains("satbara") || lower.contains("सातबारा") || lower.contains("scan") || lower.contains("ocr") -> 
                Pair("Open Document Reader", UiActionType.NAVIGATE_DOC_SCANNER)
            lower.contains("font") || lower.contains("size") || lower.contains("मोठा") || lower.contains("senior") -> 
                Pair("Toggle Large Font", UiActionType.ADJUST_VOICE_SPEED)
            else -> Pair(null, null)
        }
    }

    private fun offlineGuideAnswer(query: String): GuideChatMessage {
        val lower = query.lowercase()

        return when {
            // Voice Navigation / Mic
            lower.contains("voice") || lower.contains("mic") || lower.contains("बोलून") || lower.contains("मायक्रोफोन") || lower.contains("how do i use") || lower.contains("कसे वापरावे") || lower.contains("कसे वापरायचे") || lower.contains("चालवायचे") || lower.contains("मदत") || lower.contains("मार्गदर्शन") -> {
                GuideChatMessage(
                    isUser = false,
                    text = """
                        🎤 **How to use Voice Navigation:**
                        1. **Central Mic Button**: Tap the glowing blue microphone button in the center of the bottom navigation bar.
                        2. **Speak in your Dialect**: Speak naturally in standard Marathi, Varhadi, Ahirani, Malvani, or Minglish (e.g., "माझ्या पिकावर रोग आलाय" or "लाडकी बहीण योजनेची माहिती द्या").
                        3. **Instant Action**: The AI understands your dialect, answers via voice, and automatically navigates to the relevant screen!
                        4. **Mic Modes**: In the voice dialog, switch between 'Tap to Speak' or 'Push to Talk'.

                        मराठीत: खालील मधले निळे मायक्रोफोन बटण दाबा आणि तुमच्या स्थानिक बोलीत बोला. ॲप आपोआप उत्तर देऊन संबंधित पान उघडेल.
                    """.trimIndent(),
                    spokenAudioText = "व्हॉईस नेव्हिगेशन वापरण्यासाठी खालील मधले निळे मायक्रोफोन बटण दाबा आणि तुमच्या स्थानिक भाषेत बोला. हे ॲप तुम्हाला थेट मदत करेल.",
                    suggestedActionTitle = "Try Voice Assistant",
                    suggestedActionType = UiActionType.READ_SCREEN
                )
            }

            // Schemes & Voice Form Filling
            lower.contains("scheme") || lower.contains("ladki") || lower.contains("योजना") || lower.contains("form") || lower.contains("अर्ज") || lower.contains("बहीण") || lower.contains("श्रावणबाळ") || lower.contains("पेंशन") -> {
                GuideChatMessage(
                    isUser = false,
                    text = """
                        📋 **How Government Schemes & Voice Form Filling Work:**
                        1. **Browse Schemes**: Tap 'Schemes' in the bottom navigation. Filter by Farmers, Women, Seniors, or Healthcare.
                        2. **Detailed Benefits**: Tap any scheme card (e.g., Ladki Bahin Yojana ₹1,500/month or Namo Shetkari ₹12,000/year) to view eligibility and required documents.
                        3. **Voice Interview**: Tap 'Start Voice Form Filling'. The app will conduct a 3-step conversational interview in Marathi to check your eligibility without any tedious typing!
                        4. **Audio Summary**: Tap the speaker icon on any card to listen to the verified government summary.
                    """.trimIndent(),
                    spokenAudioText = "योजना विभागामध्ये जाऊन लाडकी बहीण किंवा शेतकरी योजनेवर टॅप करा. व्हॉईस फॉर्म फिलिंग सुरू करून बोलून अर्ज तपासणी करा.",
                    suggestedActionTitle = "Open Schemes Section",
                    suggestedActionType = UiActionType.NAVIGATE_SCHEMES
                )
            }

            // Live Translation
            lower.contains("translat") || lower.contains("भाषांतर") || lower.contains("दुभाषी") || lower.contains("interpret") || lower.contains("hindi") -> {
                GuideChatMessage(
                    isUser = false,
                    text = """
                        🌐 **How Live Speech Translation Works:**
                        1. **Navigate**: Tap 'Translate' in the bottom navigation bar.
                        2. **Two Speakers**:
                           - **Citizen Button (Blue)**: Citizen speaks in Marathi (or any dialect). It translates to English or Hindi and plays spoken audio.
                           - **Official Button (Navy)**: Government officer speaks English/Hindi. It translates back into warm Marathi!
                        3. **Custom Typing & Presets**: You can type your own text, select quick administrative phrases, or choose target languages (English, Hindi, Gujarati, Kannada).
                        4. **Audio Playback**: Tap the speaker icon beside any translation to hear it spoken aloud.
                    """.trimIndent(),
                    spokenAudioText = "थेट दुभाषी पानावर जाऊन मराठीत बोला आणि इंग्रजी किंवा हिंदीत भाषांतर ऐका. अधिकारी इंग्रजीत बोलतील त्याचे मराठीत भाषांतर होईल.",
                    suggestedActionTitle = "Open Live Translation",
                    suggestedActionType = UiActionType.NAVIGATE_TRANSLATE
                )
            }

            // Agri Mandi & Crop Doctor
            lower.contains("agri") || lower.contains("mandi") || lower.contains("भाव") || lower.contains("शेतकरी") || lower.contains("soybean") || lower.contains("कापूस") || lower.contains("crop") -> {
                GuideChatMessage(
                    isUser = false,
                    text = """
                        🌾 **How MahaAgri & Crop Doctor Works:**
                        1. **APMC Mandi Rates**: Tap 'Agri & Mandi' to see today's official APMC market rates in Akola, Latur, and Nashik for Soybean, Cotton, and Onion.
                        2. **MSP Indicators**: Compare current modal prices against government Minimum Support Price (MSP).
                        3. **Crop Doctor**: View verified pest diagnosis (e.g. Yellow Mosaic on soybean or pink bollworm on cotton) and recommended remedies.
                        4. **Audio Advisory**: Tap 'Listen Advisory' on any commodity to hear the market update in Marathi.
                    """.trimIndent(),
                    spokenAudioText = "कृषी बाजारभाव विभागात अकोला, लातूर, नाशिकचे हमीभाव आणि कीड नियंत्रणाचे उपाय दिलेले आहेत.",
                    suggestedActionTitle = "View Mandi Rates",
                    suggestedActionType = UiActionType.NAVIGATE_AGRI
                )
            }

            // Document OCR Reader / 7-12 Satbara
            lower.contains("doc") || lower.contains("satbara") || lower.contains("सातबारा") || lower.contains("ocr") || lower.contains("कागद") || lower.contains("paper") -> {
                GuideChatMessage(
                    isUser = false,
                    text = """
                        📄 **How Document OCR Reader Works:**
                        1. **Access**: Go to 'Translate' -> switch top tab to 'Document OCR Reader'.
                        2. **Select Document**: Choose from 7/12 Land Record (सातबारा), Kesari Ration Card, or Pension Sanction Order.
                        3. **Key Extraction**: The AI extracts landowner name, survey gat number, uncultivated land, and eligible benefits.
                        4. **Audio Explanation**: Tap 'Read This Document Aloud' to hear a plain-spoken Marathi explanation of the official document.
                    """.trimIndent(),
                    spokenAudioText = "कागदपत्र वाचक विभागात सातबारा किंवा रेशन कार्ड निवडून ऑडिओ सारांश ऐका.",
                    suggestedActionTitle = "Open Document Reader",
                    suggestedActionType = UiActionType.NAVIGATE_DOC_SCANNER
                )
            }

            // Dialects & Senior Accessibility
            lower.contains("dialect") || lower.contains("font") || lower.contains("size") || lower.contains("बोली") || lower.contains("मोठा") || lower.contains("senior") || lower.contains("aaji") -> {
                GuideChatMessage(
                    isUser = false,
                    text = """
                        ⚙️ **How to Change Dialects & Accessibility:**
                        1. **Dialect Picker**: In the top navigation bar, tap the dialect button (e.g., 'वऱ्हाडी' or 'प्रमाण'). Choose from Varhadi (Vidarbha), Ahirani (Khandesh), Malvani (Konkan), Marathwadi, Zadi Boli, or Minglish.
                        2. **Persona Modes**: Tap 'Farmer' in the top-left to switch to 'Senior' (auto large font & slower audio) or 'Youth'.
                        3. **Large Font ('TT' Button)**: Tap the 'TT' button in the top bar to instantly zoom text across the entire app.
                        4. **Screen Reader (Speaker Icon)**: Tap the speaker icon in the top bar to have the entire current screen read aloud in Marathi!
                    """.trimIndent(),
                    spokenAudioText = "वरच्या पट्टीमध्ये जाऊन बोलीभाषा बदला, टीटी बटण दाबून अक्षरे मोठी करा किंवा स्पीकर बटण दाबून स्क्रीन ऐका.",
                    suggestedActionTitle = "Adjust Font & Dialect",
                    suggestedActionType = UiActionType.ADJUST_VOICE_SPEED
                )
            }

            // General App Overview
            else -> {
                GuideChatMessage(
                    isUser = false,
                    text = """
                        🤖 **About Sahayak (सहायक):**
                        Sahayak is Maharashtra's Marathi-First AI and voice technology platform designed for inclusive digital access.
                        
                        **Key Sections:**
                        • **Home Dashboard**: Quick service metrics, voice status, and direct shortcuts.
                        • **Govt Welfare Schemes**: 100% verified DBT schemes with voice eligibility interviews.
                        • **MahaAgri Market**: Live APMC mandi rates, MSP benchmarks, and pest advisories.
                        • **Live Speech Interpreter**: Face-to-face bilingual conversation translator.
                        • **Document Reader**: Audio explanations of 7/12 land records and certificates.
                        
                        Try asking me: *"How to use voice search?"* or *"How does Ladki Bahin work?"*
                    """.trimIndent(),
                    spokenAudioText = "सहायक हे महाराष्ट्रातील नागरिकांसाठी व्हॉईस आणि एआय आधारित ॲप आहे. तुम्हाला कशाची मदत हवी आहे?",
                    suggestedActionTitle = "Explore Home",
                    suggestedActionType = UiActionType.READ_SCREEN
                )
            }
        }
    }
}
