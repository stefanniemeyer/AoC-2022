package de.niemeyer.aoc.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

class ExtensionsKtTest {

    @Test
    @DisplayName("next")
    fun testNext() {
        assertEquals("abcde", "abcdefghij".iterator().next(5))
    }

    @Test
    @DisplayName("nextInt")
    fun testNextInt() {
        assertEquals(9, "10011111".iterator().nextInt(4))
    }

    @Test
    @DisplayName("nextUntilFirst")
    fun testNextUntilFirst() {
        val res = "abcdefghij".iterator().nextUntilFirst(3) { it == "def" }
        assertEquals(listOf("abc", "def"), res)
    }

    @Test
    @DisplayName("executeUntilEmpty")
    fun testExecuteUntilEmpty() {
        val res = "abcdef".iterator().executeUntilEmpty { it.next() < 'c' }
        assertEquals(listOf(true, true, false, false, false, false), res)
    }

    @Test
    @DisplayName("takeUntil")
    fun testTakeUntil() {
        val res = "abcdefghij".asIterable().takeUntil { it == 'c' }
        assertEquals(listOf('a', 'b', 'c'), res)
    }

    @Test
    @DisplayName("pairs")
    fun testPairs() {
        val res = listOf(1, 2, 3).pairs()
        assertEquals(listOf(Pair(1, 1), Pair(1, 2), Pair(1, 3), Pair(2, 2), Pair(2, 3), Pair(3, 3)), res)
    }

    @Test
    @DisplayName("Int.product")
    fun testIntProduct() {
        assertEquals(6, listOf(1, 2, 3).product())
        assertEquals(-30, listOf(2, -3, 5).product())
        assertEquals(0, listOf(2, 0, 5).product())
    }

    @Test
    @DisplayName("Long.product")
    fun testLongProduct() {
        assertEquals(6L, listOf(1L, 2L, 3L).product())
        assertEquals(-30L, listOf(2L, -3L, 5L).product())
        assertEquals(0L, listOf(2L, 0L, 5L).product())
    }

    @Test
    @DisplayName("asLong")
    fun testAsLong() {
        assertEquals('0'.asLong(), 0L)
        assertEquals('1'.asLong(), 1L)
        assertEquals('2'.asLong(), 2L)
        assertEquals('9'.asLong(), 9L)
    }

    @Test
    @DisplayName("peer")
    fun testPeer() {
        val a = arrayOf(
            "abcde".toCharArray(),
            "12345".toCharArray(),
            "ABCDE".toCharArray(),
        )
        assertEquals(0 to 3, a.peer(1, 1, 2, 2))
        assertEquals(1 to 2, a.peer(2, 4, 11, 13))
        assertEquals(2 to 4, a.peer(1, 2, 1, 2))
    }

    @Test
    @DisplayName("intersects")
    fun testIntersects() {
        assert(0..10 intersects 5..15)
        assert(0..10 intersects 10..15)
        assert(10..15 intersects 5..10)
        assertEquals(false, 0..9 intersects 10..15)
        assert(5..15 intersects 0..10)
        assertEquals(false, 10..15 intersects 0..9)
        assertEquals(false, -5..-1 intersects 0..5)
    }

    @Test
    @DisplayName("intersect")
    fun testIntersect() {
        assertEquals(5..10, 0..10 intersect 5..15)
        assertEquals(10..10, 0..10 intersect 10..15)
        assertEquals(10..10, 10..15 intersect 5..10)
        assertEquals(IntRange.EMPTY, -5..-1 intersect 0..5)
    }

    @Test
    @DisplayName("union")
    fun testUnion() {
        assertEquals(0..15, 0..10 union 5..15)
        assertEquals(-5..10, 10..15 union -5..-3)
    }

    @Test
    @DisplayName("size")
    fun testSize() {
        assertEquals(11, (0..10).size())
        assertEquals(3, (-5..-3).size())
        assertEquals(1, (99..99).size())
        assertEquals(0, (IntRange.EMPTY).size())
    }
}
