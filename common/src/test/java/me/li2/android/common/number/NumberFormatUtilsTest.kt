/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
package me.li2.android.common.number

import me.li2.android.common.number.NumberFormatUtils.formatCurrency
import me.li2.android.common.number.NumberFormatUtils.formatNumber
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class NumberFormatUtilsTest {

    @Test
    fun testIntegerNumber() {
        assertEquals("12 34 56", formatNumber(123456.toDouble(), "##,##,##", ' '))
    }

    @Test
    fun testDecimalNumber() {
        val pattern = "###,###.##"
        assertEquals("123,456,789.12", formatNumber(123456789.12, pattern))
    }

    @Test
    fun testRoundingUp() {
        assertEquals("123.45", formatNumber(123.451, "###,###.##"))
        assertEquals("123.46", formatNumber(123.459, "###,###.##"))
    }

    @Test
    fun testGroupingSeparator() {
        val pattern = "###,###.##"
        assertEquals("123-456-789.12", formatNumber(123456789.12, pattern, '-'))
    }

    @Test
    fun testLeadingAndTrailingZeros() {
        assertEquals("000123.780", formatNumber(123.78, "000000.000"))
    }

    @Test
    fun testDollarSign() {
        assertEquals("$12,345.67", formatNumber(12345.67, "$###,###.###"))
    }

    @Test
    fun testCurrency() {
        assertEquals("$9,876,543.21",
                formatCurrency(9876543.21, "USD", Locale("en", "US")))
        assertEquals("USD9,876,543.21",
                formatCurrency(9876543.21, "USD", Locale("en", "NZ")))
        assertEquals("NZD9,876,543.21",
                formatCurrency(9876543.21, "NZD", Locale("en", "US")))
        assertEquals("$9,876,543.21",
                formatCurrency(9876543.21, "NZD", Locale("en", "NZ")))
    }
}