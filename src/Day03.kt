private val numberMatcher = Regex("[0-9]+")

private const val DAY = 3

private typealias Schematic = List<NumberedPart>

fun main() {
    fun part1(input: List<String>): Int {
        return Schematic(input).sumOf { it.numbers.sumOf(PartNumber::number) }
    }

    fun part2(input: List<String>): Int {
        return Schematic(input).sumOf { it.gearRatio ?: 0 }
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}

private fun List<String>.getPartNumbers() = flatMapIndexed { y, line ->
    numberMatcher.findAll(line).map {
        PartNumber(it.value.toInt(), it.range, y)
    }.toList()
}

private fun List<String>.getParts() = flatMapIndexed { y, line ->
    line.foldIndexed(emptyList<NumberlessPart>()) { x, parts, char ->
        if (char.isDigit() || char == '.') {
            parts
        } else {
            parts + NumberlessPart(char, x, y)
        }
    }
}


private data class PartNumber(
    val number: Int,
    val x: IntRange,
    val y: Int
)

private fun Schematic(input: List<String>): Schematic {
    val partNumbers = input.getPartNumbers()
    return input.getParts().map { part ->
        NumberedPart(
            part,
            partNumbers.filter {
                part.adjacentX overlapsWith it.x && it.y in part.adjacentY
            }
        )
    }
}

private fun NumberedPart(numberlessPart: NumberlessPart, partNumbers: List<PartNumber>) = NumberedPart(
    numberlessPart.part,
    numberlessPart.x,
    numberlessPart.y,
    partNumbers
)

private sealed interface Part {
    val part: Char
    val x: Int
    val y: Int
}

private data class NumberlessPart(
    override val part: Char,
    override val x: Int,
    override val y: Int,
) : Part

private data class NumberedPart(
    override val part: Char,
    override val x: Int,
    override val y: Int,
    val numbers: List<PartNumber>
) : Part

private val Part.adjacentX: IntRange get() = x - 1..x + 1
private val Part.adjacentY: IntRange get() = y - 1..y + 1

private infix fun IntRange.overlapsWith(other: IntRange): Boolean = first <= other.last && other.first <= last

private val Part.gearRatio: Int? get() = (this as? NumberedPart)?.gearRatio
private val NumberedPart.gearRatio: Int?
    get() = if (part == '*' && numbers.size == 2) {
        numbers.map(
            PartNumber::number
        ).reduce { acc, number -> acc * number }
    } else {
        null
    }
