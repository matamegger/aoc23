import kotlin.math.abs

private const val DAY = 11

data class Galaxy(
    val x: Int,
    val y: Int,
)

infix fun Galaxy.distanceTo(other: Galaxy) = abs(x - other.x) + abs(y - other.y)

fun main() {

    fun List<Galaxy>.expandSpaceAlongCoordinate(
        coordinate: Galaxy.() -> Int,
        expansion: Int = 1,
        recreate: Galaxy.(offset: Int) -> Galaxy
    ) = groupBy(coordinate)
        .toList()
        .sortedBy { it.first }
        .fold(0 to emptyList<Galaxy>()) { state, current ->
            val offset = state.first +
                    expansion * (state.first + current.first - 1 - (state.second.lastOrNull()?.coordinate() ?: -1))
            offset to state.second + current.second.map { it.recreate(offset) }
        }
        .second

    fun part1(input: List<String>): Long {
        val galaxies = input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
                if (char == '#') Galaxy(x, y) else null
            }
        }
            .expandSpaceAlongCoordinate({ x }) { offset -> copy(x + offset) }
            .expandSpaceAlongCoordinate({ y }) { offset -> copy(y = y + offset) }

        return galaxies.flatMapIndexed { index: Int, galaxy: Galaxy ->
            galaxies.drop(index + 1).map {
                galaxy.distanceTo(it).toLong()
            }
        }.sum()
    }

    fun part2(input: List<String>, expansion: Int = 1000000): Long {
        val galaxies = input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
                if (char == '#') Galaxy(x, y) else null
            }
        }
            .expandSpaceAlongCoordinate({ x }, expansion - 1) { offset -> copy(x + offset) }
            .expandSpaceAlongCoordinate({ y }, expansion - 1) { offset -> copy(y = y + offset) }


        return galaxies.flatMapIndexed { index: Int, galaxy: Galaxy ->
            galaxies.drop(index + 1).map {
                galaxy.distanceTo(it).toLong()
            }
        }.sum()
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 374L)
    check(part2(testInput, 10) == 1030L)
    check(part2(testInput, 100) == 8410L)

    val input = readInput(DAY)
    check(part1(input)==9545480L).println()
    check(part2(input)==406725732046L).println()
}