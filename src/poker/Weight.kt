package poker

import java.util.HashMap

enum class Weight constructor(private val code: String) : Iterator<Weight?> {

    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("T"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");


    override operator fun next(): Weight? = if (hasNext()) ordinalLookup[this.ordinal + 1] else null

    override fun hasNext(): Boolean = this.ordinal < ordinalLookup.size - 1

    companion object {

        fun forCode(code: Char) = forCode(code.toString())

        fun forCode(code: String): Weight = codeLookup[code] ?: throw RuntimeException("No Value for code: $code")

        private val codeLookup = HashMap<String, Weight>()
        private val ordinalLookup = HashMap<Int, Weight>()

        init {
            for (value in Weight.values()) {
                codeLookup[value.code] = value
                ordinalLookup[value.ordinal] = value
            }
        }
    }

}