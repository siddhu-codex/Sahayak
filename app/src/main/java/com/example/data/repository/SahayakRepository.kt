package com.example.data.repository

import com.example.data.model.AgriMarketItem
import com.example.data.model.DocumentSample
import com.example.data.model.MarathiDialect
import com.example.data.model.SchemeItem

object SahayakRepository {

    val schemes = listOf(
        SchemeItem(
            id = "ladki_bahin",
            titleMr = "मुख्यमंत्री माझी लाडकी बहीण योजना",
            titleEn = "Mukhyamantri Majhi Ladki Bahin Yojana",
            department = "महिला व बालविकास विभाग, महाराष्ट्र शासन",
            benefitHighlightMr = "दरमहा ₹१,५०० थेट बँक खात्यात (वार्षिक ₹१८,०००)",
            eligibilitySummaryMr = "वय २१ ते ६५ वर्षे, कुटुंबाचे वार्षिक उत्पन्न ₹२.५० लाखांपेक्षा कमी असावे.",
            requiredDocuments = listOf("आधार कार्ड", "रेशन कार्ड", "अधिवास दाखला / जन्म दाखला", "आधार लिंक बँक पासबुक"),
            officialHelpline = "181 / 022-22027050",
            audioSummaryMr = "मुख्यमंत्री माझी लाडकी बहीण योजनेअंतर्गत महाराष्ट्रातील २१ ते ६५ वयोगटातील महिलांना दरमहा दीड हजार रुपये थेट बँक खात्यात दिले जातात. यासाठी आधार कार्ड, रेशन कार्ड आणि बँक खाते लागते."
        ),
        SchemeItem(
            id = "namo_shetkari",
            titleMr = "नमो शेतकरी महासन्मान निधी योजना",
            titleEn = "Namo Shetkari Mahasanman Nidhi",
            department = "कृषी विभाग, महाराष्ट्र शासन",
            benefitHighlightMr = "दरवर्षी ₹६,००० (केंद्राच्या ₹६,००० सोबत एकूण ₹१२,०००)",
            eligibilitySummaryMr = "पी एम किसान योजनेसाठी पात्र असलेले सर्व शेतकरी कुटुंब.",
            requiredDocuments = listOf("७/१२ उतारा व ८-अ", "आधार कार्ड", "बँक खाते डीबीटी लिंक", "ई-केवायसी"),
            officialHelpline = "1800-115-526",
            audioSummaryMr = "नमो शेतकरी योजनेमध्ये पीएम किसानच्या पात्र शेतकऱ्यांना महाराष्ट्र शासनाकडून वर्षाला ६ हजार रुपये अतिरिक्त मिळतात. यासाठी बँक खात्याचे ई-केवायसी पूर्ण असणे आवश्यक आहे."
        ),
        SchemeItem(
            id = "mjpjay_health",
            titleMr = "महात्मा ज्योतिराव फुले जन आरोग्य योजना (MJPJAY)",
            titleEn = "Mahatma Jyotirao Phule Jan Arogya Yojana",
            department = "सार्वजनिक आरोग्य विभाग",
            benefitHighlightMr = "कुटुंबासाठी प्रतिवर्षी ₹५ लाखांपर्यंत मोफत कॅशलेस उपचार",
            eligibilitySummaryMr = "महाराष्ट्रातील पिवळे, केशरी, पांढरे रेशन कार्डधारक व सर्व अधिवासी नागरिक.",
            requiredDocuments = listOf("रेशन कार्ड", "आधार कार्ड किंवा मतदार ओळखपत्र"),
            officialHelpline = "155388 / 1800-120-8040",
            audioSummaryMr = "महात्मा ज्योतिराव फुले जन आरोग्य योजनेत महाराष्ट्रातील नागरिकांना वर्षाला ५ लाख रुपयांपर्यंत मोफत हॉस्पिटल उपचार मिळतात. यासाठी रेशन कार्ड आणि आधार कार्ड घेऊन जवळच्या सरकारी किंवा नोंदणीकृत खाजगी रुग्णालयात जावे."
        ),
        SchemeItem(
            id = "shravanbal_pension",
            titleMr = "श्रावणबाळ सेवा राज्य निवृत्तीवेतन योजना",
            titleEn = "Shravanbal Seva State Pension Scheme",
            department = "सामाजिक न्याय व विशेष सहाय्य विभाग",
            benefitHighlightMr = "दरमहा ₹१,५०० वृद्धापकाळ निवृत्तीवेतन",
            eligibilitySummaryMr = "वय ६५ वर्षे किंवा त्याहून अधिक, कुटुंबाचे वार्षिक उत्पन्न ₹२१,००० पेक्षा कमी (गट अ) किंवा बीपीएल यादीतील ज्येष्ठ (गट ब).",
            requiredDocuments = listOf("वयाचा पुरावा", "तहसीलदारांचा उत्पन्नाचा दाखला", "आधार कार्ड", "बँक पासबुक"),
            officialHelpline = "1800-120-8040",
            audioSummaryMr = "श्रावणबाळ पेन्शन योजनेमध्ये ६५ वर्षांवरील आजी-आजोबांना दरमहा दीड हजार रुपये पेन्शन मिळते. यासाठी उत्पन्नाचा दाखला आणि वयाचा पुरावा तहसील कार्यालयात सादर करावा लागतो."
        ),
        SchemeItem(
            id = "mahadbt_scholarship",
            titleMr = "महाडीबीटी पोस्ट मॅट्रिक शिष्यवृत्ती",
            titleEn = "MahaDBT Post-Matric Scholarship",
            department = "उच्च व तंत्र शिक्षण / समाजकल्याण विभाग",
            benefitHighlightMr = "१००% पर्यंत शिक्षण शुल्क माफी व निर्वाह भत्ता",
            eligibilitySummaryMr = "१०वी नंतरच्या मान्यताप्राप्त पदवी/डिप्लोमा अभ्यासक्रमात शिकणारे विद्यार्थी.",
            requiredDocuments = listOf("१०वी/१२वी गुणपत्रिका", "जात प्रमाणपत्र व वैधता", "उत्पन्न प्रमाणपत्र", "डोमिसाईल"),
            officialHelpline = "022-49150800",
            audioSummaryMr = "महाडीबीटी शिष्यवृत्तीद्वारे १०वीनंतरच्या पदवी व डिप्लोमा विद्यार्थ्यांना पूर्ण फी माफी आणि खात्यात निर्वाह भत्ता मिळतो. महाडीबीटी पोर्टलवर दरवर्षी अर्ज करणे आवश्यक आहे."
        )
    )

    val agriMarkets = listOf(
        AgriMarketItem(
            id = "mandi_soybean_akola",
            cropNameMr = "सोयाबीन (Soybean)",
            cropNameEn = "Soybean",
            mandiName = "अकोला बाजार समिती",
            district = "अकोला (Akola)",
            minPrice = 4600,
            maxPrice = 5080,
            modalPrice = 4850,
            priceTrend = "वाढ (+₹७५)",
            arrivalsText = "४,२५० क्विंटल आवक",
            pestAlert = "पिवळा मोझॅक (Yellow Mosaic)",
            pestRemedyMr = "पांढऱ्या माशीच्या नियंत्रणासाठी एकरी ५ पिवळे चिकट सापळे लावा आणि थायामेथोक्सम २५% डब्ल्यूजी ५ ग्रॅम प्रति १० लिटर पाण्यात फवारा.",
            audioSummaryMr = "अकोला बाजार समितीत सोयाबीनला सरासरी भाव ४,८५० रुपये प्रति क्विंटल आहे. पिकावर पिवळा मोझॅक रोगाचा धोका असून पिवळे चिकट सापळे लावण्याचा सल्ला कृषी तज्ज्ञांनी दिला आहे."
        ),
        AgriMarketItem(
            id = "mandi_cotton_yavatmal",
            cropNameMr = "कापूस (Cotton)",
            cropNameEn = "Cotton",
            mandiName = "यवतमाळ बाजार समिती",
            district = "यवतमाळ (Yavatmal)",
            minPrice = 7100,
            maxPrice = 7720,
            modalPrice = 7450,
            priceTrend = "स्थिर",
            arrivalsText = "२,८०० क्विंटल आवक",
            pestAlert = "गुलाबी बोंडअळी (Pink Bollworm)",
            pestRemedyMr = "कामगंध सापळे (फेरोमोन ट्रॅप) एकरी ५ लावावेत आणि निंबोळी अर्क ५% ची फवारणी करावी.",
            audioSummaryMr = "यवतमाळ बाजारात कापसाला ७,४५० रुपये सरासरी भाव सुरू आहे. गुलाबी बोंडअळीच्या नियंत्रणासाठी एकरी पाच कामगंध सापळे लावावेत."
        ),
        AgriMarketItem(
            id = "mandi_onion_lasalgaon",
            cropNameMr = "कांदा (लाल कांदा)",
            cropNameEn = "Red Onion",
            mandiName = "लासलगाव बाजार समिती",
            district = "नाशिक (Nashik)",
            minPrice = 1450,
            maxPrice = 2450,
            modalPrice = 2100,
            priceTrend = "वाढ (+₹१२०)",
            arrivalsText = "१२,००० क्विंटल आवक",
            pestAlert = "करपा व फुलकिडे (Thrips)",
            pestRemedyMr = "प्रोफेनोफॉस + सायपरमेथ्रीन २० मिली प्रति १० लिटर पाणी फवारावे.",
            audioSummaryMr = "लासलगाव बाजार समितीत लाल कांद्याला सरासरी २,१०० रुपये प्रति क्विंटल दर आहे. कालच्या तुलनेत भावात १२० रुपयांची सुधारणा झाली आहे."
        ),
        AgriMarketItem(
            id = "mandi_tur_latur",
            cropNameMr = "तूर / अरहर (Tur)",
            cropNameEn = "Pigeon Pea",
            mandiName = "लातूर बाजार समिती",
            district = "लातूर (Latur)",
            minPrice = 9600,
            maxPrice = 10450,
            modalPrice = 10100,
            priceTrend = "वाढ (+₹१५०)",
            arrivalsText = "१,९५० क्विंटल आवक",
            pestAlert = "शेंगा पोखरणारी अळी (Helicoverpa)",
            pestRemedyMr = "इमामेक्टिन बेन्झोएट ५ एसजी ४ ग्रॅम प्रति १० लिटर पाण्यात फवारा.",
            audioSummaryMr = "लातूर बाजारात पांढऱ्या व लाल तुरीला १०,१०० रुपये प्रति क्विंटल उच्चांकी भाव मिळत आहे."
        )
    )

    val documentSamples = listOf(
        DocumentSample(
            id = "doc_satbara",
            titleMr = "डिजिटल स्वाक्षरीत ७/१२ उतारा (Satbara)",
            titleEn = "7/12 Land Record Extract",
            sampleType = "महसूल दस्तऐवज (Land Revenue)",
            keyFields = mapOf(
                "गाव / तालुका" to "चांदूर, जि. अमरावती",
                "गट क्रमांक / सर्व्हे" to "७८ / २-अ",
                "एकूण क्षेत्र" to "१ हेक्टर ४२ आर (सुमारे ३.५ एकर)",
                "खातेदार नाव" to "तुकाराम विठ्ठल पाटील",
                "बोजा / कर्ज नोंद" to "जिल्हा मध्यवर्ती बँक - ₹८०,००० पीक कर्ज (चालू)"
            ),
            verifiedGovtPortal = "mahabhumi.gov.in (महाभूमी)",
            simpleAudioExplanationMr = "हा तुमचा डिजिटल ७/१२ उतारा आहे. यामध्ये गट क्रमांक ७८ मधील १ हेक्टर ४२ आर शेतजमीन असून त्यावर तुकाराम पाटील यांचे नाव आहे. ८० हजार रुपयांचे जिल्हा बँक पीक कर्ज नोंदवलेले आहे."
        ),
        DocumentSample(
            id = "doc_ration_card",
            titleMr = "केशरी रेशन कार्ड (National Food Security)",
            titleEn = "Orange Ration Card",
            sampleType = "अन्न व नागरी पुरवठा",
            keyFields = mapOf(
                "कार्ड प्रकार" to "केशरी (Orange - NPHS)",
                "कुटुंब प्रमुख" to "सुलोचना महादेव गायकवाड",
                "एकूण सदस्य" to "४ व्यक्ती",
                "रास्त भाव दुकान क्र." to "FPS-412 (वडगाव)",
                "धान्य पात्रता" to "गहू ₹२/किलो (१० किलो), तांदूळ ₹३/किलो (१० किलो)"
            ),
            verifiedGovtPortal = "rcms.mahafood.gov.in",
            simpleAudioExplanationMr = "हे तुमचे अधिकृत केशरी रेशन कार्ड आहे. यामध्ये कुटुंब प्रमुख सुलोचना गायकवाड यांच्यासह ४ सदस्यांची नावे आहेत. या कार्डावर दरमहा १० किलो गहू आणि १० किलो तांदूळ सवलतीच्या दरात मिळण्यास तुम्ही पात्र आहात."
        ),
        DocumentSample(
            id = "doc_pension_order",
            titleMr = "श्रावणबाळ पेन्शन मंजुरी आदेश",
            titleEn = "Pension Sanction Approval Order",
            sampleType = "तहसील कार्यालय आदेश",
            keyFields = mapOf(
                "मंजुरी क्र." to "TEH/PEN/2026/894",
                "लाभार्थी नाव" to "पार्वतीबाई गणपत साळुंखे (वय ६९ वर्षे)",
                "मासिक रक्कम" to "₹१,५०० थेट डीबीटी बँक खात्यात",
                "बँक खाते" to "बँक ऑफ महाराष्ट्र (शेवटचे ४ अंक: ३०२१)",
                "स्थिती" to "मंजूर व सक्रिय (Approved)"
            ),
            verifiedGovtPortal = "aaplesarkar.mahaonline.gov.in",
            simpleAudioExplanationMr = "हा श्रावणबाळ पेन्शन योजनेचा मंजुरी आदेश आहे. पार्वतीबाई साळुंखे यांच्या नावे दरमहा दीड हजार रुपये पेन्शन मंजूर झाली असून रक्कम बँक ऑफ महाराष्ट्र खात्यात थेट जमा होईल."
        )
    )

    // Normalizes dialect and colloquial phrases to formal Pramaan Marathi
    fun normalizeDialect(input: String, dialect: MarathiDialect): Pair<String, String> {
        val lower = input.trim().lowercase()

        // Check for specific dialect mappings
        when {
            // Varhadi
            lower.contains("सोयाबीन") && (lower.contains("पिवळा") || lower.contains("आलाय") || lower.contains("काय करू")) -> {
                return Pair(
                    "माझ्या सोयाबीन पिकावर पिवळा मोझॅक रोग आला आहे, मी यावर काय उपाय करू शकतो?",
                    "My soybean crop has Yellow Mosaic disease; what remedy should I apply?"
                )
            }
            lower.contains("पेन्शन") || lower.contains("पेन्शनची माहिती") -> {
                return Pair(
                    "या ॲपमध्ये ज्येष्ठ नागरिकांच्या पेन्शनची माहिती कुठे उपलब्ध आहे?",
                    "Where can I find senior citizen pension details in this app?"
                )
            }
            lower.contains("लाडकी बहीण") || lower.contains("ladki") -> {
                return Pair(
                    "मुख्यमंत्री माझी लाडकी बहीण योजनेसाठी काय पात्रता व कागदपत्रे लागतात?",
                    "What are the eligibility criteria and documents required for Ladki Bahin Yojana?"
                )
            }
            lower.contains("बाजारभाव") || lower.contains("भाव") || lower.contains("मंडी") -> {
                return Pair(
                    "महाराष्ट्रातील बाजार समित्यांमधील आजचे सोयाबीन, कापूस व कांदा बाजारभाव दाखवा.",
                    "Show today's market rates for soybean, cotton, and onion in Maharashtra mandis."
                )
            }
            lower.contains("कागद") || lower.contains("वाचून") || lower.contains("उतारा") -> {
                return Pair(
                    "माझ्या हातातील हा कागद व ७/१२ उतारा वाचून साध्या भाषेत समजावून सांगा.",
                    "Please read this physical document/7-12 extract and explain it in simple Marathi."
                )
            }
            lower.contains("सुरक्षित") || lower.contains("डेटा") -> {
                return Pair(
                    "माझा वैयक्तिक डेटा आणि आवाज या ॲपमध्ये पूर्णपणे सुरक्षित आहे का?",
                    "Is my personal data and voice fully secure in this app?"
                )
            }
            lower.contains("आवाज") && (lower.contains("वेग") || lower.contains("बदलायचा")) -> {
                return Pair(
                    "ॲपचा बोलण्याचा आवाज आणि वेग मी कसा बदलू शकतो?",
                    "How can I change the speaking speed and voice settings of the app?"
                )
            }
            // Romanized Marathi (Minglish)
            lower.contains("form bharaycha") || lower.contains("scholarship") -> {
                return Pair(
                    "मला महाडीबीटी शिष्यवृत्तीचा अर्ज भरायचा आहे, कोणती कागदपत्रे लागतील?",
                    "I want to apply for MahaDBT scholarship, what documents are required?"
                )
            }
            lower.contains("kase kadhave") || lower.contains("pan card") -> {
                return Pair(
                    "नवीन पॅन कार्ड किंवा दाखला कसा काढावा याची माहिती द्या.",
                    "Give details on how to apply for PAN card or official certificate."
                )
            }
            else -> {
                return Pair(input, "Query: $input")
            }
        }
    }
}
