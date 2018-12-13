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

## Rules Engine 

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

