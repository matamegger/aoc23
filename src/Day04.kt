private val numberMatcher = Regex("[0-9]+")

private const val DAY = 4


fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line ->
            Card(line).points
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { line ->
            Card(line)
        }.sortedBy { it.id }

        // cards.map { it.id }.forEachIndexed { index, i -> check(index + 1 == i) }

        return cards.getWonCards(cards, emptyList()).size
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}

private fun Card(card: String) = Card(
    card.substringBefore(":").dropWhile { !it.isDigit() }.toInt(),
    card.extractNumberLists().let { (winningNumbers, cardNumbers) ->
        cardNumbers.count { it in winningNumbers }
    }
)

private data class Card(
    val id: Int,
    val matchingNumbers: Int,
)

private val Card.points get() = if (matchingNumbers == 0) 0 else 1.shl(matchingNumbers - 1)
private val Card.wonCardIds get() = id + 1..id + matchingNumbers

private fun String.extractNumberLists() = substringAfter(":")
    .split("|")
    .takeIf { it.size == 2 }
    ?.toIntLists()
    ?.let {
        it.first() to it.last()
    } ?: error("Unexpected amount of lists in line")

private fun List<String>.toIntLists() = map { list ->
    list
        .trim()
        .split(" ")
        .filter(String::isNotBlank)
        .map { it.toInt() }
}

// after all those years I could force a tailrec into the code
private tailrec fun List<Card>.getWonCards(allCards: List<Card>, alreadyWon: List<Card>): List<Card> {
    if (isEmpty()) return alreadyWon
    return flatMap {
        val cards = it.wonCardIds.map { id -> allCards[id - 1] }
        cards
    }.getWonCards(allCards, alreadyWon + this)
}

