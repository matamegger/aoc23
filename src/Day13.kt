private const val DAY = 13


fun main() {

    fun List<String>.transpose(): List<String> {
        val width = first().length
        assert(all { it.length == width })
        val strings = this
        return buildList<String> {
            for (w in 0..<width) {
                add(
                    buildString {
                        for (s in strings) append(s[w])
                    }
                )
            }

        }
    }

    fun isMirrorLine(pattern: List<String>, pair: Pair<Int, Int>): Boolean {
        val upper = pattern.take(pair.first)
        val lower = pattern.drop(pair.second + 1)

        return upper
            .reversed()
            .zip(lower)
            .all {
                it.first == it.second
            }
    }

    fun isSmugedMirrorLine(pattern: List<String>, pair: Pair<Int, Int>): Boolean {
        val upper = pattern.take(pair.first)
        val lower = pattern.drop(pair.second + 1)

        var smudged = false
        return upper
            .reversed()
            .zip(lower)
            .all {
                it.first.zip(it.second).all { (aChar, bChar) ->
                    when {
                        aChar != bChar && !smudged -> {
                            smudged = true
                            true
                        }

                        else -> aChar == bChar
                    }
                }
            } && smudged
    }

    fun findMirrorLine(pattern: List<String>): Pair<Int, Int>? {
        return pattern.withIndex()
            .zipWithNext()
            .filter { (a, b) -> a.value == b.value }
            .firstOrNull {
                isMirrorLine(pattern, it.first.index to it.second.index)
            }?.let {
                it.first.index to it.second.index
            }
    }

    fun findSmugedMirrorLine(pattern: List<String>): Pair<Int, Int>? {
        return pattern.withIndex()
            .zipWithNext()
            .filter { (a, b) ->
                var smudged = false
                a.value.zip(b.value).all { (aChar, bChar) ->
                    when {
                        aChar != bChar && !smudged -> {
                            smudged = true
                            true
                        }

                        else -> aChar == bChar
                    }
                }
            }
            .firstOrNull {
                val smuged = it.first.value != it.second.value
                if (smuged) {
                    isMirrorLine(pattern, it.first.index to it.second.index)
                } else {
                    isSmugedMirrorLine(pattern, it.first.index to it.second.index)
                }
            }?.let {
                it.first.index to it.second.index
            }
    }

    fun part1(input: List<String>): Long {
        return input.split { it.isEmpty() }
            .map {
                findMirrorLine(it.transpose())?.first?.plus(1)
                    ?: findMirrorLine(it)?.first?.plus(1)?.times(100) ?: error("No mirror line found")
            }.sumOf { it.toLong() }
    }

    fun part2(input: List<String>): Long {
        return input.split { it.isEmpty() }
            .map {
                findSmugedMirrorLine(it.transpose())?.first?.plus(1)
                    ?: findSmugedMirrorLine(it)?.first?.plus(1)?.times(100) ?: error("No mirror line found")
            }.sumOf { it.toLong() }
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 405L)
    check(part2(testInput) == 400L)

    val input = readInput(DAY)
    part1(input).println() //==7025
    part2(input).println()
}