import java.math.BigInteger

private const val DAY = 8


fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input.first()
        val map = input.drop(2)
            .associate {
                val (node, directions) = it.split(" = ")
                val (left, right) = directions.removePrefix("(").removeSuffix(")").split(", ")
                node to (left to right)
            }
        var steps = 0
        var location = "AAA"
        while (true) {
            location = when (instructions[steps % instructions.length]) {
                'L' -> map[location]?.first ?: error("Location not mapped $location")
                'R' -> map[location]?.second ?: error("Location not mapped $location")
                else -> error("Invalid instruction")
            }
            steps++
            if (location == "ZZZ") break;
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first()
        val map = input.drop(2)
            .associate {
                val (node, directions) = it.split(" = ")
                val (left, right) = directions.removePrefix("(").removeSuffix(")").split(", ")
                node to (left to right)
            }

        val locations = map.keys.filter { it.endsWith("A") }
        val stepsUntilFinished = locations.map {
            var location = it
            var finalSteps = 0
            var z = 0L
            var zTries = 10
            var steps = 0L
            while (true) {
                location = when (instructions[(steps % instructions.length).toInt()]) {
                    'L' -> map[location]?.first ?: error("Location not mapped $location")
                    'R' -> map[location]?.second ?: error("Location not mapped $location")
                    else -> error("Invalid instruction")
                }
                steps++
                if (location.endsWith("Z")) {
                    zTries--
                    if (zTries <= 0) {
                        break;
                    } else {
                        if (z == 0L) {
                            z = steps
                        }
                        assert(z == steps)
                    }
                }
            }
            z
        }.also { it.println() }

        return stepsUntilFinished.lce().also { it.println() }!!.toLong()
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 2)
    val testInput2 = readTestInput(DAY, 2)
    check(part1(testInput2) == 6)
    val testInput3 = readTestInput(DAY, 3)
    check(part2(testInput3) == 6L)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println() //10554896561287680
}

fun List<Long>.lce(): BigInteger? {
    return map {
        BigInteger.valueOf(it)
    }.reduce { acc, i ->
        acc*i/acc.gcd(i)
    }
}