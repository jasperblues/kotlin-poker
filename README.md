# kotlin-poker

Here is a 'Texas Hold em' poker rules engine, implemented in Kotlin. 


We have cards, each with a `Weight` and a `Suit`: 

```kotlin
data class Card(val weight: Weight, val suit: Suit)
```

A `PokerHand` consists of five cards and is instantiated with an immutable `List<Card>` of cards. There is a string factory method for convienience, example `PokerHand.fromString("KS AS TS QS JS")`. 

Upon instantiation, we have methods to evaluate traits. For efficiency, they are cached: 

```kotlin
val isStraightFlush; get() = isStraight && isFlush

val isFourOfAKind = cards.groupBy { it.weight }.map { it.value }.any { it.size == 4 }

val isFullHouse = cards.groupBy { it.weight }.map { it.value }.size == 2

val isFlush = cards.groupBy { it.suit }.map { it.value }.size == 1

val isStraight = cards.map { it.weight.ordinal } ==
        (cards[0].weight.ordinal..cards[0].weight.ordinal + 4).toList()

val isThreeOfAKind = cards.groupBy { it.weight }.map { it.value }.any { it.size == 3 }

val isTwoPair = cards.groupBy { it.weight }.map { it.value }.filter { it.size == 2 }.count() == 2

val isPair = cards.groupBy { it.weight }.map { it.value }.any { it.size == 2 }
```

## Rules 

It is now simple to implement `Comparable<PokerHand>` against another hand, in a top-down approach, for example a straight flush beats four of a kind, which beats a full house, and so forth. 

```kotlin
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
```

## Tests

```kotlin
@Test
fun compareTo() {
    test(LOSS,  "2H 3H 4H 5H 6H", "KS AS TS QS JS", "The highest straight flush wins")
    test(WIN,   "2H 3H 4H 5H 6H", "AS AD AC AH JD", "A straight flush wins over 4 of a kind")
    test(WIN,   "AS AH 2H AD AC", "JS JD JC JH 3D", "The highest 4 of a kind wins")
    test(LOSS,  "2S AH 2H AS AC", "JS JD JC JH AD", "4 Of a kind wins over a full house")
    test(WIN,   "2S AH 2H AS AC", "2H 3H 5H 6H 7H", "A full house wins over a flush")
    test(WIN,   "AS 3S 4S 8S 2S", "2H 3H 5H 6H 7H", "The highest flush wins")
    test(WIN,   "2H 3H 5H 6H 7H", "2S 3H 4H 5S 6C", "A flush wins over a straight")
    test(TIE,   "2S 3H 4H 5S 6C", "3D 4C 5H 6H 2S", "Equal straight is tie")
    test(WIN,   "2S 3H 4H 5S 6C", "AH AC 5H 6H AS", "A straight wins over three of a kind")
    test(LOSS,  "2S 2H 4H 5S 4C", "AH AC 5H 6H AS", "3 Of a kind wins over a two pair")
    test(WIN,   "2S 2H 4H 5S 4C", "AH AC 5H 6H 7S", "A 2 pair wins over a pair")
    test(LOSS,  "6S AD 7H 4S AS", "AH AC 5H 6H 7S", "The highest pair wins")
    test(LOSS,  "2S AH 4H 5S KC", "AH AC 5H 6H 7S", "A pair wins over a shit hand")
    test(LOSS,  "8C 4S KH JS 4D", "KC 4H KS 2H 8D", "Both sides with pairs, the high card side wins")
    test(WIN,   "KD 6S 9D TH AD", "JH 8S TH AH QH", "A shit hands beats another shit hand, by high card")
    test(TIE,   "2S AH 4H 5S 6C", "AD 4C 5H 6H 2C", "Equal shit cards tie")
}
```
