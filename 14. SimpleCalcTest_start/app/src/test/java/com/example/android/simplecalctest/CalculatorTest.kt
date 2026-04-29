package com.example.android.simplecalctest

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CalculatorTest {
    private var mCalculator : Calculator? = null

    @Before
    fun setUp() {
        mCalculator = Calculator()
    }

    // ESERCIZIO 1
    @Test // 1.0 + 1.0 = 2.0
    fun addTwoNumbers() {
        val result = mCalculator!!.add(1.0, 1.0) // !! - è l'operatore "not-null assertion"
        assertEquals(2.0, result, 0.0)
    }

    // ESERCIZIO 2
    @Test //  -1.0 + 2.0 = 1.0
    fun addTwoNumbers1() {
        val result = mCalculator!!.add(-1.0, 2.0)
        assertEquals(1.0, result, 0.0)
    }

    @Test // -1.0 + -17.0 = -18.0
    fun addTwoNumbers2() {
        val result = mCalculator!!.add(-1.0, -17.0)
        assertEquals(-18.0, result, 0.0)
    }

    @Test // 1.111 + 1.111 = 2.222
    fun addTwoNumbers3() {
        val result = mCalculator!!.add(1.111, 1.111)
        assertEquals(2.222, result, 0.0)
    }

    @Test // 123456781.0 + 111111111.0 = 234567892.0
    fun addTwoNumbers4() {
        val result = mCalculator!!.add(123456781.0, 111111111.0)
        assertEquals(234567892.0, result, 0.0)
    }

    @Test // 1.0 - 1.0 = 0.0
    fun subTwoNumbers() {
        val result = mCalculator!!.sub(1.0, 1.0)
        assertEquals(0.0, result, 0.0)
    }

    @Test // 1.0 - 17.0 = -16.0
    fun subTwoNumbers1() {
        val result = mCalculator!!.sub(1.0, 17.0)
        assertEquals(-16.0, result, 0.0)
    }

    @Test // 32.0 / 2.0 = 16.0
    fun divTwoNumbers() {
        val result = mCalculator!!.div(32.0, 2.0)
        assertEquals(16.0, result, 0.0)
    }

    @Test // 32.0 * 2.0 = 64.0
    fun mulTwoNumbers() {
        val result = mCalculator!!.mul(32.0, 2.0)
        assertEquals(64.0, result, 0.0)
    }

    // ESERCIZIO 3
    @Test // 1.0 / 0.0 = Infinity
    fun zeroDivision() {
        val result = mCalculator!!.div(1.0, 0.0)
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0)
    }

    @Test // 1.0 / 0.0 = Infinity
    fun zeroDivision1() {
        val result = mCalculator!!.div(1.0, 0.0)

    }
}