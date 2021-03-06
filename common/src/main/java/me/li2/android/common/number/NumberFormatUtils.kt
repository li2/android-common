/*
 * Created by Weiyi Li on 2019-11-03.
 * https://github.com/li2
 */
@file:Suppress("unused")
package me.li2.android.common.number

import timber.log.Timber
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

object NumberFormatUtils {
    /**
     * Format number.
     *
     * @param pattern for example, "###,###.##", the sign (#) denotes a digit, the comma is a placeholder
     *  for the grouping separator.
     * @param groupingSeparator grouping separator
     * @return formatted string.
     *
     *  (1) 123456.789 ###,###.### =>> 123,456.789
     *
     *  (2) 123456.789 ###.## ==>> 123456.79 rounding up.
     *
     *  (3) 123.78 000000.000 ==>> 000123.780
     *
     *  (4) 12345.67 $###,###.### ==>> $12,345.67
     *
     *  (5) 123456 ##,##,## with ' ' ==>> 12 34 56
     *
     * @see <a href="https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html">Java: Customizing Formats</a>
     */
    fun formatNumber(number: Double, pattern: String, groupingSeparator: Char = ','): String =
            try {
                val dfs = DecimalFormatSymbols(Locale.getDefault()).apply {
                    this.groupingSeparator = groupingSeparator
                }
                DecimalFormat(pattern, dfs).format(number)
            } catch (exception: IllegalArgumentException) {
                Timber.e(exception, "Failed to format number: $number")
                number.toString()
            }

    /**
     * Return formatted currency amount for given local.
     *
     * @param amount
     * @param currencyCode see [ISO 4217 currency cod](https://en.wikipedia.org/wiki/ISO_4217)
     * @param locale [Locale] for given language, country, variant.
     * @see <a href="https://blog.stylingandroid.com/currency-curiosities/">Currency Curiosities</a>
     * @see <a href="https://docs.oracle.com/javase/tutorial/i18n/format/numberFormat.html">Java - Number Format</a>
     */
    fun formatCurrency(
            amount: Double,
            currencyCode: String,
            locale: Locale = Locale.getDefault()): String {
        // obtain a text formatter for a given currency whilst honouring the locale
        return NumberFormat.getCurrencyInstance(locale)
                .apply { currency = Currency.getInstance(currencyCode) }
                .format(amount)
    }
}
