package com.example.data.model

enum class UserPersona(val labelMr: String, val labelEn: String, val subtitleMr: String) {
    KISAN("शेतकरी (Kisan)", "Farmer", "हवामान, हमीभाव, पीक सल्ला व खत अनुदाने"),
    AAJI("आजी-आजोबा (Senior)", "Senior Citizen", "मोठे अक्षर, पेन्शन, आरोग्य योजना व एक-टॅप ऑडिओ"),
    YUVA("युवा / विद्यार्थी (Yuva)", "Youth / Student", "स्कॉलरशिप, कागदपत्रे, मिंग्लिश व थेट इंग्रजी भाषांतर")
}

enum class MarathiDialect(
    val code: String,
    val nameMr: String,
    val region: String,
    val exampleGreeting: String
) {
    PRAMAAN("mr-PRAMAAN", "प्रमाण मराठी", "राज्यव्यापी (पुणे/मुंबई)", "नमस्कार, मी तुम्हाला कशी मदत करू?"),
    VARHADI("mr-VARHADI", "वऱ्हाडी", "विदर्भ (अकोला, अमरावती, नागपूर)", "राम राम बापू! काय काम पडलं?"),
    AHIRANI("mr-AHIRANI", "अहिराणी", "खान्देश (जळगाव, धुळे)", "राम राम भौ! काय मदत व्हई?"),
    MALVANI("mr-MALVANI", "मालवणी", "कोकण (सिंधुदुर्ग, रत्नागिरी)", "नमस्कार रे, काय खबर आसा?"),
    MARATHWADI("mr-MARATHWADI", "मराठवाडी", "मराठवाडा (लातूर, नांदेड, संभाजीनगर)", "राम राम जी, काय माहिती पायजे?"),
    ZADI_BOLI("mr-ZADIBOLI", "झाडीबोली", "पूर्व विदर्भ (चंद्रपूर, गडचिरोली)", "जोहार दादा! काय चाललं?"),
    MINGLISH("mr-MINGLISH", "Minglish", "शहरी / विद्यार्थी (रोमन मराठी)", "Hello! Kahi help havi ahe ka?")
}

enum class MicInteractionMode(val titleMr: String, val titleEn: String, val descriptionMr: String) {
    TAP_TO_SPEAK(
        "टॅप करून बोला (Tap-to-Speak)",
        "Tap to Speak",
        "अॅप नेव्हिगेशन, योजना चौकशी व स्क्रीन वाचण्यासाठी एकदा टॅप करा."
    ),
    HOLD_TO_TALK(
        "दाबून ठेवा (Hold-to-Talk)",
        "Hold to Talk (Noisy)",
        "आवाज असलेल्या मंडई किंवा ट्रॅक्टरजवळ सतत दाबून ठेवून बोला."
    ),
    LIVE_TRANSLATE(
        "थेट दुभाषी (Live Bhashantar)",
        "Live Speech Translator",
        "मराठी आणि इंग्रजी/हिंदीमध्ये समोरासमोर थेट आवाज संभाषण."
    )
}

data class SchemeItem(
    val id: String,
    val titleMr: String,
    val titleEn: String,
    val department: String,
    val benefitHighlightMr: String,
    val eligibilitySummaryMr: String,
    val requiredDocuments: List<String>,
    val officialHelpline: String = "1800-120-8040",
    val audioSummaryMr: String
)

data class AgriMarketItem(
    val id: String,
    val cropNameMr: String,
    val cropNameEn: String,
    val mandiName: String,
    val district: String,
    val minPrice: Int,
    val maxPrice: Int,
    val modalPrice: Int,
    val priceTrend: String, // "वाढ", "स्थिर", "घट"
    val arrivalsText: String,
    val pestAlert: String? = null,
    val pestRemedyMr: String? = null,
    val audioSummaryMr: String
)

data class DocumentSample(
    val id: String,
    val titleMr: String,
    val titleEn: String,
    val sampleType: String,
    val keyFields: Map<String, String>,
    val verifiedGovtPortal: String,
    val simpleAudioExplanationMr: String
)

data class TranslationTurn(
    val id: Long = System.currentTimeMillis(),
    val isUserMarathi: Boolean,
    val speakerName: String,
    val sourceText: String,
    val sourceDialectOrLang: String,
    val normalizedMarathi: String? = null,
    val translatedText: String,
    val targetLang: String
)

enum class UiActionType {
    NONE,
    NAVIGATE_SCHEMES,
    NAVIGATE_AGRI,
    NAVIGATE_TRANSLATE,
    NAVIGATE_DOC_SCANNER,
    READ_SCREEN,
    ADJUST_VOICE_SPEED,
    START_SLOT_FILLING
}

data class ConciergeResponse(
    val replyMr: String,
    val normalizedQueryMr: String? = null,
    val englishTranslation: String? = null,
    val actionType: UiActionType = UiActionType.NONE,
    val actionPayload: String? = null,
    val audioReady: Boolean = true
)
