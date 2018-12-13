package poker

import org.junit.Assert.assertEquals
import org.junit.Test

class CardTests {

    @Test
    fun toStringMethod() {
        assertEquals("ACE:SPADES", Card(Weight.ACE, Suit.SPADES).toString())
    }

    @Test
    fun equality() {
        assertEquals(Card(Weight.ACE, Suit.SPADES), Card(Weight.ACE, Suit.SPADES))
    }

}