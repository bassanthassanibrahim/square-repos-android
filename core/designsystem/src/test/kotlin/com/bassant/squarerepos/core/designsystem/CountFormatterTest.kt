package com.bassant.squarerepos.core.designsystem

import com.bassant.squarerepos.core.designsystem.util.formatCount
import org.junit.Assert.assertEquals
import org.junit.Test

class CountFormatterTest {

    @Test
    fun `small numbers are shown as they are`() {
        assertEquals("0", formatCount(0))
        assertEquals("999", formatCount(999))
    }

    @Test
    fun `thousands use k with one decimal`() {
        assertEquals("1k", formatCount(1_000))
        assertEquals("1.2k", formatCount(1_234))
        assertEquals("46.1k", formatCount(46_123))
        assertEquals("999.9k", formatCount(999_999))
    }

    @Test
    fun `millions use M`() {
        assertEquals("1M", formatCount(1_000_000))
        assertEquals("2.5M", formatCount(2_540_000))
    }
}
