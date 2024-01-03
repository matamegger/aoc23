import kotlin.math.absoluteValue

private const val DAY = 10

enum class Pipe {
    Vertical,
    Horizontal,
    BottomLeft,
    BottomRight,
    TopLeft,
    TopRight,
    Start,
    Empty
}

data class Position(
    val row: Int,
    val column: Int
)

fun main() {
    fun part1(input: List<String>): Long {
        lateinit var start: Position
        val maze = input.mapIndexed { rowIndex, line ->
            line.mapIndexed { columnIndex, pipeSymbol ->
                when (pipeSymbol) {
                    '.' -> Pipe.Empty
                    '|' -> Pipe.Vertical
                    '-' -> Pipe.Horizontal
                    '7' -> Pipe.BottomLeft
                    'J' -> Pipe.TopLeft
                    'F' -> Pipe.BottomRight
                    'L' -> Pipe.TopRight
                    'S' -> Pipe.Start.also { start = Position(rowIndex, columnIndex) }
                    else -> error("Invalid input $pipeSymbol")
                }
            }
        }

        var previous = listOf(start)
        var positions = maze.getNeighbours(start).filter { start in maze.getNeighbours(it) }
        var distance = 1L
        while (positions.toSet().size == 2) {
            positions = positions.map { pos ->
                maze.getNeighbours(pos)
                    .first { it !in previous }
            }.also { previous = positions }
            distance++
        }


        return distance
    }

    fun part2(input: List<String>): Int {
        lateinit var start: Position
        val maze = input.mapIndexed { rowIndex, line ->
            line.mapIndexed { columnIndex, pipeSymbol ->
                when (pipeSymbol) {
                    '.' -> Pipe.Empty
                    '|' -> Pipe.Vertical
                    '-' -> Pipe.Horizontal
                    '7' -> Pipe.BottomLeft
                    'J' -> Pipe.TopLeft
                    'F' -> Pipe.BottomRight
                    'L' -> Pipe.TopRight
                    'S' -> Pipe.Start.also { start = Position(rowIndex, columnIndex) }
                    else -> error("Invalid input $pipeSymbol")
                }
            }
        }

        var pipeSystem = mutableSetOf(start)
        var previous = start
        var position = maze.getNeighbours(start)
            .filterNot { it.row < 0 || it.column < 0 }
            .first { start in maze.getNeighbours(it) }
        pipeSystem.add(position)
        while (position != start) {
            position = maze.getNeighbours(position)
                .first { it != previous }
                .also { previous = position }
            pipeSystem.add(position)
        }

        val vertices = pipeSystem
            .filter { maze[it] !in listOf(Pipe.Horizontal, Pipe.Vertical) }

        val polyArea = vertices
            .zipWithNext()
            .let {
                it + (vertices.last() to vertices.first())
            }
            .fold(0) { sum, it ->
                sum + (it.first.row * it.second.column - it.second.row * it.first.column)
            }.absoluteValue - pipeSystem.size


        return polyArea / 2 + 1
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 4L)
    val testInput2 = readTestInput(DAY, 2)
    check(part2(testInput2) == 4)
    val testInput3 = readTestInput(DAY, 3)
    check(part2(testInput3) == 8)
    val testInput4 = readTestInput(DAY, 4)
    check(part2(testInput4) == 10)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}

private val Position.neighbors: List<Position>
    get() = listOf(
        Position(row - 1, column),
        Position(row + 1, column),
        Position(row, column - 1),
        Position(row, column + 1),
    )

private fun Pipe.getNeighbours(position: Position) = when (this) {
    Pipe.Vertical -> position.neighbors.slice(0..1)
    Pipe.Horizontal -> position.neighbors.slice(2..3)
    Pipe.BottomLeft -> position.neighbors.slice(1..2)
    Pipe.BottomRight -> position.neighbors.slice(listOf(1, 3))
    Pipe.TopLeft -> position.neighbors.slice(listOf(0, 2))
    Pipe.TopRight -> position.neighbors.slice(listOf(0, 3))
    Pipe.Start -> position.neighbors
    Pipe.Empty -> emptyList()
}

private fun List<List<Pipe>>.getNeighbours(position: Position): List<Position> = get(position).getNeighbours(position)

private operator fun List<List<Pipe>>.get(position: Position) = get(position.row)[position.column]

