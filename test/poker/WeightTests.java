package poker;

import static org.junit.Assert.*;

import org.junit.Test;

public class WeightTests {

	@Test
	public void forCode() {
		assertEquals(Weight.TWO, Weight.Companion.forCode("2"));
		assertEquals(Weight.THREE, Weight.Companion.forCode("3"));
		assertEquals(Weight.FOUR, Weight.Companion.forCode("4"));
		assertEquals(Weight.FIVE, Weight.Companion.forCode("5"));
		assertEquals(Weight.SIX, Weight.Companion.forCode("6"));
		assertEquals(Weight.SEVEN, Weight.Companion.forCode("7"));
		assertEquals(Weight.EIGHT, Weight.Companion.forCode("8"));
		assertEquals(Weight.NINE, Weight.Companion.forCode("9"));
		assertEquals(Weight.TEN, Weight.Companion.forCode("T"));
		assertEquals(Weight.ACE, Weight.Companion.forCode("A"));
		assertEquals(Weight.JACK, Weight.Companion.forCode("J"));
		assertEquals(Weight.QUEEN, Weight.Companion.forCode("Q"));
		assertEquals(Weight.KING, Weight.Companion.forCode("K"));
		try {
			Weight.Companion.forCode("Anything else");
			fail("Should have thrown");
		}
		catch (RuntimeException e) {
			assertEquals("No Value for code: Anything else", e.getMessage());
		}
	}

	@Test
	public void ordinal() {
		assertTrue(Weight.ACE.ordinal() > Weight.KING.ordinal());
	}

	@Test
	public void next() {
		assertNull(Weight.ACE.next());
		assertEquals(Weight.THREE, Weight.TWO.next());
	}

}
