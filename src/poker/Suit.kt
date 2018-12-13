package poker

import java.util.HashMap

enum class Suit constructor(private val code: String) {

    SPADES("S"),
    HEARTS("H"),
    DIAMONDS("D"),
    CLUBS("C");

    companion object {

        fun forCode(code: Char) = forCode(code.toString())

        fun forCode(code: String): Suit = codeLookup[code] ?: throw RuntimeException("No Suit for code: $code")

        private val codeLookup = HashMap<String, Suit>()

        init {
            for (suit in Suit.values()) {
                codeLookup[suit.code] = suit
            }
        }
    }


}
