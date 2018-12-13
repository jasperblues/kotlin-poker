package poker

import java.util.*


class PokerHand private constructor(val cards: List<Card>) : Comparable<PokerHand> {

    companion object {
        const val WIN = 1
        const val TIE = 0
        const val LOSS = -1

        fun fromList(cards : List<Card>): PokerHand {
            return PokerHand(cards.sortedBy { it.weight.ordinal })
        }

        fun fromString(hand: String) : PokerHand {
            val cards = ArrayList<Card>()
            hand.split(" ").forEach {
                when (it.length != 2) {
                    true -> throw RuntimeException("A card code must be two characters")
                    else -> cards += Card(Weight.forCode(it[0]), Suit.forCode(it[1]))
                }
            }
            if (cards.size != 5) {
                throw RuntimeException("There must be five cards in a hand")
            }
            return fromList(cards)
        }

    }

    val isStraightFlush; get() = isStraight && isFlush

    val isFourOfAKind = cards.groupBy { it.weight }.map { it.value }.any { it.size == 4 }

    val isFullHouse = cards.groupBy { it.weight }.map { it.value }.size == 2

    val isFlush = cards.groupBy { it.suit }.map { it.value }.size == 1

    val isStraight = cards.map { it.weight.ordinal } ==
            (cards[0].weight.ordinal..cards[0].weight.ordinal + 4).toList()

    val isThreeOfAKind = cards.groupBy { it.weight }.map { it.value }.any { it.size == 3 }

    val isTwoPair = cards.groupBy { it.weight }.map { it.value }.filter { it.size == 2 }.count() == 2

    val isPair = cards.groupBy { it.weight }.map { it.value }.any { it.size == 2 }

    override fun compareTo(other: PokerHand): Int = when {
        (this.isStraightFlush || other.isStraightFlush) ->
            if (this.isStraightFlush) if (!other.isStraightFlush) WIN else compareByHighCard(other) else LOSS
        (this.isFourOfAKind || other.isFourOfAKind) ->
            if (this.isFourOfAKind) if (!other.isFourOfAKind) WIN else compareByHighCard(other) else LOSS
        (this.isFullHouse || other.isFullHouse) ->
            if (this.isFullHouse) if (!other.isFullHouse) WIN else compareByHighCard(other) else LOSS
        (this.isFlush || other.isFlush) ->
            if (this.isFlush) if (!other.isFlush) WIN else compareByHighCard(other) else LOSS
        (this.isStraight || other.isStraight) ->
            if (this.isStraight) if (!other.isStraight) WIN else compareByHighCard(other) else LOSS
        (this.isThreeOfAKind || other.isThreeOfAKind) ->
            if (this.isThreeOfAKind) if (!other.isThreeOfAKind) WIN else compareByHighCard(other) else LOSS
        (this.isTwoPair || other.isTwoPair) ->
            if (this.isTwoPair) if (!other.isTwoPair) WIN else compareByHighCard(other) else LOSS
        (this.isPair || other.isPair) ->
            if (this.isPair) if (!other.isPair) WIN else compareByHighCard(other) else LOSS
        else -> compareByHighCard(other)
    }

    private fun compareByHighCard(other: PokerHand, index: Int = 4): Int = when {
        (index < 0) -> TIE
        cards[index].weight === other.cards[index].weight -> compareByHighCard(other, index - 1)
        cards[index].weight.ordinal > other.cards[index].weight.ordinal -> WIN
        else -> LOSS
    }

}