package com.fwhyn.app.deandro.feature.presentation.login

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlusTest {

    private lateinit var plus: Plus

    @Before
    fun setUp() {
        plus = Plus()
    }

    @Test
    fun testPlus() {
        val result = plus(2.0, 3.0)
        assertEquals(5.0, result, 0.0)
    }

    @Test
    fun testPlusWithZero() {
        val result = plus(5.0, 0.0)
        assertEquals(5.0, result, 0.0)
    }

    @Test
    fun testPlusWithNegativeNumbers() {
        val result = plus(-3.0, -2.0)
        assertEquals(-5.0, result, 0.0)
    }

    @Test
    fun testPlusWithPositiveAndNegative() {
        val result = plus(10.0, -4.0)
        assertEquals(6.0, result, 0.0)
    }

    @Test
    fun testPlusWithLargeNumbers() {
        val result = plus(1000000.0, 2000000.0)
        assertEquals(3000000.0, result, 0.0)
    }

    @Test
    fun testPlusWithZeroAndZero() {
        val result = plus(0.0, 0.0)
        assertEquals(0.0, result, 0.0)
    }
}