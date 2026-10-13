package com.bassant.squarerepos.core.designsystem

import androidx.compose.ui.graphics.Color
import com.bassant.squarerepos.core.designsystem.theme.DarkColors
import com.bassant.squarerepos.core.designsystem.theme.LanguageColors
import com.bassant.squarerepos.core.designsystem.theme.LightColors
import com.bassant.squarerepos.core.designsystem.util.contrastRatio
import com.bassant.squarerepos.core.designsystem.util.withMinContrast
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorContrastTest {

    @Test
    fun `black on white is the maximum contrast`() {
        assertEquals(21f, contrastRatio(Color.Black, Color.White), 0.01f)
    }

    @Test
    fun `colors that are already visible are kept as they are`() {
        val kotlinPurple = LanguageColors.getValue("kotlin")

        assertEquals(kotlinPurple, kotlinPurple.withMinContrast(DarkColors.surfaceContainerLow))
    }

    @Test
    fun `every language dot is visible on both themes`() {
        val cards = listOf(LightColors.surfaceContainerLow, DarkColors.surfaceContainerLow, DarkColors.surfaceContainer)
        for ((language, color) in LanguageColors) {
            for (card in cards) {
                val ratio = contrastRatio(color.withMinContrast(card), card)
                assertTrue("$language has contrast $ratio on $card", ratio >= 3f)
            }
        }
    }
}
