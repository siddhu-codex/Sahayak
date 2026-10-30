package com.example

import com.example.data.model.MarathiDialect
import com.example.data.model.UserPersona
import com.example.data.repository.SahayakRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testSchemesRepository_containsCoreMaharashtraSchemes() {
        val schemes = SahayakRepository.schemes
        assertTrue(schemes.isNotEmpty())
        val ladkiBahin = schemes.find { it.id == "ladki_bahin" }
        assertNotNull(ladkiBahin)
        assertTrue(ladkiBahin!!.benefitHighlightMr.contains("१,५००"))

        val namoShetkari = schemes.find { it.id == "namo_shetkari" }
        assertNotNull(namoShetkari)
    }

    @Test
    fun testAgriMarkets_containsSoybeanAndPestAlert() {
        val markets = SahayakRepository.agriMarkets
        val akolaSoybean = markets.find { it.id == "mandi_soybean_akola" }
        assertNotNull(akolaSoybean)
        assertEquals("अकोला बाजार समिती", akolaSoybean!!.mandiName)
        assertEquals("पिवळा मोझॅक (Yellow Mosaic)", akolaSoybean.pestAlert)
    }

    @Test
    fun testDialectNormalization_varhadiToPramaan() {
        val (norm, eng) = SahayakRepository.normalizeDialect(
            "माझ्या सोयाबीनच्या पिकावर पिवळा मोझॅक आलाय, काय करू?",
            MarathiDialect.VARHADI
        )
        assertTrue(norm.contains("पिवळा मोझॅक रोग आला आहे"))
        assertTrue(eng.contains("Yellow Mosaic"))
    }

    @Test
    fun testPersonas_haveValidTitles() {
        val personas = UserPersona.values()
        assertEquals(3, personas.size)
        assertTrue(personas.any { it.name == "KISAN" })
        assertTrue(personas.any { it.name == "AAJI" })
        assertTrue(personas.any { it.name == "YUVA" })
    }
}
