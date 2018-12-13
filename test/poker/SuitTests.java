package poker;

import static org.junit.Assert.*;

import org.junit.Test;

public class SuitTests {

	@Test
	public void forCode() {
		assertEquals(Suit.SPADES, Suit.Companion.forCode("S"));
		assertEquals(Suit.HEARTS, Suit.Companion.forCode("H"));
		assertEquals(Suit.DIAMONDS, Suit.Companion.forCode("D"));
		assertEquals(Suit.CLUBS, Suit.Companion.forCode("C"));
		try {
			Suit.Companion.forCode("Anything else");
			fail("Should have thrown");
		}
		catch (RuntimeException e) {
			assertEquals("No Suit for code: Anything else", e.getMessage());
		}
	}


}
