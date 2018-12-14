package poker

import org.junit.Assert.*
import org.junit.Test
import poker.PokerHand.Companion.LOSS
import poker.PokerHand.Companion.TIE
import poker.PokerHand.Companion.WIN

class PokerHandTests {

    @Test
    fun construction_fromListShouldSort() {
        val hand = PokerHand.fromList(listOf(
                Card(Weight.TEN, Suit.DIAMONDS),
                Card(Weight.TWO, Suit.HEARTS),
                Card(Weight.FIVE, Suit.CLUBS),
                Card(Weight.KING, Suit.SPADES),
                Card(Weight.JACK, Suit.DIAMONDS)
        ))
        assertEquals(listOf(
                Card(Weight.TWO, Suit.HEARTS),
                Card(Weight.FIVE, Suit.CLUBS),
                Card(Weight.TEN, Suit.DIAMONDS),
                Card(Weight.JACK, Suit.DIAMONDS),
                Card(Weight.KING, Suit.SPADES)
        ),
                hand.cards)
    }

    @Test
    fun construction_fromString() {
        val hand = PokerHand.fromString("KS 2H 5C JD TD")
        assertEquals(listOf(
                Card(Weight.TWO, Suit.HEARTS),
                Card(Weight.FIVE, Suit.CLUBS),
                Card(Weight.TEN, Suit.DIAMONDS),
                Card(Weight.JACK, Suit.DIAMONDS),
                Card(Weight.KING, Suit.SPADES)
        ),
                hand.cards)
    }


    @Test
    fun construction_fromStringShouldThrowIfIncorrectCardCount() {
        try {
            PokerHand.fromString("KS 2H 5C JD")
            fail("Should have thrown")
        } catch (e: RuntimeException) {
            assertEquals("There must be five cards in a hand", e.message)
        }

    }

    @Test
    fun isStraightFlush() {
        assertTrue(PokerHand.fromString("TS JS QS KS AS").isStraightFlush)
        assertFalse(PokerHand.fromString("TC JS QC KS AS").isStraightFlush)
    }

    @Test
    fun isFourOfAKind() {
        assertTrue(PokerHand.fromString("TS TC TD TH AS").isFourOfAKind)
        assertFalse(PokerHand.fromString("TC JS QC KS AS").isFourOfAKind)

        assertTrue(PokerHand.fromString("AS AH 2H AD AC").isFourOfAKind)
        assertTrue(PokerHand.fromString("JS JD JC JH 3D").isFourOfAKind)
    }

    @Test
    fun isFullHouse() {
        assertTrue(PokerHand.fromString("9S 9C JD JH JS").isFullHouse)
        assertFalse(PokerHand.fromString("TC JS QC KS AS").isFullHouse)

        assertTrue(PokerHand.fromString("9S 9H JH JD JC").isFullHouse)
        assertFalse(PokerHand.fromString("2S 2D 4C 5H 9D").isFullHouse)
    }

    @Test
    fun isFlush() {
        assertTrue(PokerHand.fromString("2S 3S 7S JS AS").isFlush)
        assertFalse(PokerHand.fromString("TC JS QC KS AS").isFlush)
    }

    @Test
    fun isStraight() {
        assertTrue(PokerHand.fromString("2S 3S 4D 5C 6C").isStraight)
        assertFalse(PokerHand.fromString("7C JS QC KS AS").isStraight)
    }

    @Test
    fun isThreeOfAKind() {
        assertTrue(PokerHand.fromString("2S 2D 2C 5C 6C").isThreeOfAKind)
        assertFalse(PokerHand.fromString("7C JS QC KS AS").isThreeOfAKind)
        assertFalse(PokerHand.fromString("2S 2D 4C 4S 6C").isThreeOfAKind)
    }

    @Test
    fun isTwoPair() {
        assertTrue(PokerHand.fromString("2S 2D 4C 4S 6C").isTwoPair)
        assertFalse(PokerHand.fromString("7C JS QC KS AS").isTwoPair)
        assertFalse(PokerHand.fromString("2S 2D 2C 5C 6C").isTwoPair)
    }

    @Test
    fun isPair() {
        assertTrue(PokerHand.fromString("2S 2D 4C 5S 6C").isPair)
        assertTrue(PokerHand.fromString("2S 2D 4C 4S 6C").isPair)
        assertFalse(PokerHand.fromString("7C JS QC KS AS").isPair)
        assertFalse(PokerHand.fromString("2S 2D 2C 5C 6C").isPair)
    }

    @Test
    fun compareTo() {
        test(LOSS, "2H 3H 4H 5H 6H", "KS AS TS QS JS", "The highest straight flush wins")
        test(WIN, "2H 3H 4H 5H 6H", "AS AD AC AH JD", "A straight flush wins over 4 of a kind")
        test(WIN, "AS AH 2H AD AC", "JS JD JC JH 3D", "The highest 4 of a kind wins")
        test(LOSS, "2S AH 2H AS AC", "JS JD JC JH AD", "4 Of a kind wins over a full house")
        test(WIN, "2S AH 2H AS AC", "2H 3H 5H 6H 7H", "A full house wins over a flush")
        test(WIN, "AS 3S 4S 8S 2S", "2H 3H 5H 6H 7H", "The highest flush wins")
        test(WIN, "2H 3H 5H 6H 7H", "2S 3H 4H 5S 6C", "A flush wins over a straight")
        test(TIE, "2S 3H 4H 5S 6C", "3D 4C 5H 6H 2S", "Equal straight is tie")
        test(WIN, "2S 3H 4H 5S 6C", "AH AC 5H 6H AS", "A straight wins over three of a kind")
        test(LOSS, "2S 2H 4H 5S 4C", "AH AC 5H 6H AS", "3 Of a kind wins over a two pair")
        test(WIN, "2S 2H 4H 5S 4C", "AH AC 5H 6H 7S", "A 2 pair wins over a pair")
        test(LOSS, "6S AD 7H 4S AS", "AH AC 5H 6H 7S", "The highest pair wins")
        test(LOSS, "2S AH 4H 5S KC", "AH AC 5H 6H 7S", "A pair wins over a shit hand")
        test(LOSS, "8C 4S KH JS 4D", "KC 4H KS 2H 8D", "Both sides with pairs, the high card side wins")
        test(WIN, "KD 6S 9D TH AD", "JH 8S TH AH QH", "A shit hands beats another shit hand, by high card")
        test(TIE, "2S AH 4H 5S 6C", "AD 4C 5H 6H 2C", "Equal shit cards tie")
    }

    private fun test(expected: Int, playerHand: String, opponentHand: String, description: String) {
        assertEquals("$description:", expected, PokerHand.fromString(playerHand)
                .compareTo(PokerHand.fromString(opponentHand)))
    }
}
