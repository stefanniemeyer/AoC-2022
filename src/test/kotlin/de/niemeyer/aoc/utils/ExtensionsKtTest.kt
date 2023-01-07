package de.niemeyer.aoc.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

class ExtensionsKtTest {

    @Test
    @DisplayName("next")
    fun testNext() {
        assertThat("abcdefghij".iterator().next(5)).isEqualTo("abcde")
    }

    @Test
    @DisplayName("nextInt")
    fun testNextInt() {
        assertThat("10011111".iterator().nextInt(4)).isEqualTo(9)
    }

    @Test
    @DisplayName("nextUntilFirst")
    fun testNextUntilFirst() {
        val res = "abcdefghij".iterator().nextUntilFirst(3) { it == "def" }
        assertThat(res).isEqualTo(listOf("abc", "def"))
    }

    @Test
    @DisplayName("executeUntilEmpty")
    fun testExecuteUntilEmpty() {
        val res = "abcdef".iterator().executeUntilEmpty { it.next() < 'c' }
        assertThat(res).isEqualTo(listOf(true, true, false, false, false, false))
    }

    @Test
    @DisplayName("takeUntil")
    fun testTakeUntil() {
        val res = "abcdefghij".asIterable().takeUntil { it == 'c' }
        assertThat(res).isEqualTo(listOf('a', 'b', 'c'))
    }

    @Test
    @DisplayName("pairs")
    fun testPairs() {
        val res = listOf(1, 2, 3).pairs()
        assertThat(res).isEqualTo(listOf(Pair(1, 1), Pair(1, 2), Pair(1, 3), Pair(2, 2), Pair(2, 3), Pair(3, 3)))
    }

    @Test
    @DisplayName("Int.product")
    fun testIntProduct() {
        assertThat(listOf(1, 2, 3).product()).isEqualTo(6)
        assertThat(listOf(2, -3, 5).product()).isEqualTo(-30)
        assertThat(listOf(2, 0, 5).product()).isEqualTo(0)
    }

    @Test
    @DisplayName("Long.product")
    fun testLongProduct() {
        assertThat(listOf(1L, 2L, 3L).product()).isEqualTo(6L)
        assertThat(listOf(2L, -3L, 5L).product()).isEqualTo(-30L)
        assertThat(listOf(2L, 0L, 5L).product()).isEqualTo(0L)
    }

    @Test
    @DisplayName("asLong")
    fun testAsLong() {
        assertThat('0'.asLong()).isEqualTo(0L)
        assertThat('1'.asLong()).isEqualTo(1L)
        assertThat('2'.asLong()).isEqualTo(2L)
        assertThat('9'.asLong()).isEqualTo(9L)
    }

    @Test
    @DisplayName("peer")
    fun testPeer() {
        val a = arrayOf(
            "abcde".toCharArray(),
            "12345".toCharArray(),
            "ABCDE".toCharArray(),
        )
        assertThat(a.peer(1, 1, 2, 2)).isEqualTo(0 to 3)
        assertThat(a.peer(2, 4, 11, 13)).isEqualTo(1 to 2)
        assertThat(a.peer(1, 2, 1, 2)).isEqualTo(2 to 4)
    }

    @Test
    @DisplayName("intersects")
    fun testIntersects() {
        assert(0..10 intersects 5..15)
        assert(0..10 intersects 10..15)
        assert(10..15 intersects 5..10)
        assertThat(0..9 intersects 10..15).isEqualTo(false)
        assert(5..15 intersects 0..10)
        assertThat(10..15 intersects 0..9).isEqualTo(false)
        assertThat(-5..-1 intersects 0..5).isEqualTo(false)
    }

    @Test
    @DisplayName("intersect")
    fun testIntersect() {
        assertThat(0..10 intersect 5..15).isEqualTo(5..10)
        assertThat(0..10 intersect 10..15).isEqualTo(10..10)
        assertThat(10..15 intersect 5..10).isEqualTo(10..10)
        assertThat(-5..-1 intersect 0..5).isEqualTo(IntRange.EMPTY)
    }

    @Test
    @DisplayName("union")
    fun testUnion() {
        assertThat(0..10 union 5..15).isEqualTo(0..15)
        assertThat(10..15 union -5..-3).isEqualTo(-5..15)
    }

    @Test
    @DisplayName("size")
    fun testSize() {
        assertThat((0..10).size()).isEqualTo(11)
        assertThat((-5..-3).size()).isEqualTo(3)
        assertThat((99..99).size()).isEqualTo(1)
        assertThat((IntRange.EMPTY).size()).isEqualTo(0)
    }
}
