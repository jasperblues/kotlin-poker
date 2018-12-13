package poker

data class Card(val weight: Weight, val suit: Suit) {

    override fun toString() = "$weight:$suit"

}