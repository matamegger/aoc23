private val gameDescriptionMatcher = Regex("Game ([0-9]+): (.+)")

fun main() {
    val input = readInput(day = 2)

    val games = input.map(::parseGame)
    val gameConfig = mapOf(
        Color.Red to 12,
        Color.Green to 13,
        Color.Blue to 14
    )

    val sumOfPossibleGameIds = games.filter { it.isPossible2(gameConfig) }.sumOf { it.id }
    println("Sum of game ids that are possible: $sumOfPossibleGameIds")

    val gamePowers = games.sumOf { it.getPower() }
    println("Sum of game powers: $gamePowers")
}

private fun parseGame(gameLine: String): Game {
    val gameDescriptionParts = gameDescriptionMatcher.matchEntire(gameLine)
        ?.groupValues
        ?.drop(1)
        ?: error("Could not parse game: $gameLine")
    return Game(
        gameDescriptionParts.first().toInt(),
        parseGameSets(gameDescriptionParts.last())
    )
}

private fun parseGameSets(gameSets: String): List<GameSet> {
    return gameSets
        .split(";")
        .map(::parseGameSet)
}

private fun parseGameSet(gameSet: String): GameSet {
    val cubes = gameSet
        .split(",")
        .associate {
            val (amount, colorName) = it.trim().split(" ", limit = 2)
            colorName.toColor() to amount.toInt()
        }
    return GameSet(cubes)
}

private enum class Color {
    Red,
    Green,
    Blue;
}

private data class Game(
    val id: Int,
    val sets: List<GameSet>
)

private data class GameSet(
    val cubes: Map<Color, Int>
)

private fun Game.isPossible(totalCubes: Map<Color, Int>) = sets.all { set ->
    set.cubes.all {
        it.value <= totalCubes.getOrDefault(it.key, 0)
    }
}

private fun Game.getPower(): Int {
    return Color.entries.toTypedArray().fold(1) { power, color ->
        power * getCountSequence(color).max()
    }
}

private fun Game.isPossible2(totalCubes: Map<Color, Int>) = Color.entries.all { color ->
    getCountSequence(color).max() <= totalCubes.getOrDefault(color, 0)
}

private fun Game.getCountSequence(color: Color) = sets.map { it.getCount(color) }

private fun GameSet.getCount(color: Color) = cubes.getOrDefault(color, 0)

private fun String.toColor() = Color.entries.first { equals(it.name, true) }



