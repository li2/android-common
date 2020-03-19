/*
 * Created by Weiyi Li on 19/03/20.
 * https://github.com/li2
 */
package me.li2.android.common.collections

import junit.framework.Assert.*
import org.junit.Test

class CollectionsExtKtTest {

    @Test
    fun `copy() should return a new list with updated item`() {
        val originalSample = listOf("Android", "JetPack", "Java", "Rx")
        val updatedSample = originalSample.copy { this[2] = "Kotlin" }
        assertTrue(originalSample !== updatedSample)
        assertEquals("Kotlin", updatedSample[2])
    }

    @Test
    fun `containsIgnoreCase() should return true if list contains the item even case is not the same`() {
        val sample = listOf("Android", "JetPack", "Kotlin", "Rx")
        assertTrue(sample.containsIgnoreCase("android"))
    }

    @Test
    fun `containsIgnoreCase() should return false if list doesn't contains the item`() {
        val sample = listOf("Android", "JetPack", "Kotlin", "Rx")
        assertFalse(sample.containsIgnoreCase("adroid"))
    }

    @Test
    fun `swap() should return a new list with items swapped`() {
        val sample = listOf("A", "B")
        val updatedSample = sample.swap(0, 1)
        assertTrue("B" == updatedSample[0] && "A" == updatedSample[1])
    }

    @Test
    fun `swap() should return the original list when item is out of index`() {
        val sample = listOf("A", "B", "C")
        val updatedSample = sample.swap(2, 3)
        assertTrue(sample === updatedSample)
    }
}
