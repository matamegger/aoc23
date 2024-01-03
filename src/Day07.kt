private const val DAY = 7

data class Hand(
    val cards: List<Int>,
    val bid: Int,
    val type: HandType
)

enum class HandType {
    Five,
    Four,
    Full,
    Three,
    TwoPair,
    Pair,
    High
}

val List<Int>.handType: HandType
    get() {
        val cards = fold(mutableMapOf<Int, Int>()) { acc, i ->
//        acc.getOrDefault(i, 0)
            acc.merge(i, 1) { old, new -> old + new }
            acc
        }
        val cardCount = cards.values.sortedDescending()

        return when {
            cardCount.first() == 5 -> HandType.Five
            cardCount.first() == 4 -> HandType.Four
            cardCount.first() == 3 && cardCount.last() == 2 -> HandType.Full
            cardCount.first() == 3 -> HandType.Three
            cardCount.first() == 2 && cardCount.getOrNull(1) == 2 -> HandType.TwoPair
            cardCount.first() == 2 -> HandType.Pair
            else -> HandType.High
        }
    }

val List<Int>.handType2: HandType
    get() {
        val cards = fold(mutableMapOf<Int, Int>()) { acc, i ->
//        acc.getOrDefault(i, 0)
            acc.merge(i, 1) { old, new -> old + new }
            acc
        }
        val joker = cards.getOrDefault(1, 0)
        cards.remove(1)
        val cardCount = cards.values.sortedDescending()
        if(cards.isEmpty()) return HandType.Five

        return when {
            cardCount.first() == 5 -> HandType.Five
            cardCount.first() == 4 && joker == 1 -> HandType.Five
            cardCount.first() == 4 -> HandType.Four
            cardCount.first() == 3 && joker == 2 -> HandType.Five
            cardCount.first() == 3 && joker == 1 -> HandType.Four
            cardCount.first() == 3 && cardCount.last() == 2 -> HandType.Full
            cardCount.first() == 3 -> HandType.Three
            cardCount.first() == 2 && joker == 3 -> HandType.Five
            cardCount.first() == 2 && joker == 2 -> HandType.Four
            cardCount.first() == 2 && joker == 1 && cardCount.getOrNull(1) == 2 -> HandType.Full
            cardCount.first() == 2 && joker == 1 -> HandType.Three
            cardCount.first() == 2 && cardCount.getOrNull(1) == 2 -> HandType.TwoPair
            cardCount.first() == 2 -> HandType.Pair
            cardCount.first() == 1 && joker == 4 -> HandType.Five
            cardCount.first() == 1 && joker == 3 -> HandType.Four
            cardCount.first() == 1 && joker == 2 && cardCount.getOrNull(1) == 2 -> HandType.Full
            cardCount.first() == 1 && joker == 2 -> HandType.Three
            cardCount.first() == 1 && joker == 1 -> HandType.Pair
            else -> HandType.High
        }
    }

fun main() {

    fun part1(input: List<String>): Int {
        return input.map {
            val (cards, bid) = it.split(" ")
            val cardsAsNumbers = cards.map { card ->
                when (card) {
                    'A' -> 14
                    'K' -> 13
                    'Q' -> 12
                    'J' -> 11
                    'T' -> 10
                    else -> card.digitToInt()
                }
            }
            Hand(
                cardsAsNumbers,
                bid.toInt(),
                cardsAsNumbers.handType
            )
        }
            .sortedWith(
                compareBy({it.type},
                {-it.cards.first()},
                {-it.cards.get(1)},
                {-it.cards.get(2)},
                {-it.cards.get(3)},
                {-it.cards.get(4)},
            )
        ).reversed()
            .mapIndexed { index, it ->
            (index+1) * it.bid
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val (cards, bid) = it.split(" ")
            val cardsAsNumbers = cards.map { card ->
                when (card) {
                    'A' -> 14
                    'K' -> 13
                    'Q' -> 12
                    'J' -> 1
                    'T' -> 10
                    else -> card.digitToInt()
                }
            }
            Hand(
                cardsAsNumbers,
                bid.toInt(),
                cardsAsNumbers.handType2
            )
        }
            .sortedWith(
                compareBy({it.type},
                    {-it.cards.first()},
                    {-it.cards.get(1)},
                    {-it.cards.get(2)},
                    {-it.cards.get(3)},
                    {-it.cards.get(4)},
                )
            ).reversed()
            .mapIndexed { index, it ->
                (index+1) * it.bid
            }.sum()
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}