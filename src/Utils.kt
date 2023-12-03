import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from an input file for the given day.
 */
fun readInput(day: Int) = Path("input/day%02d.txt".format(day)).readLines()

/**
 * Reads lines from a test input file for the given day.
 */
fun readTestInput(day: Int) =  Path("input/test/day%02d.txt".format(day)).readLines()

/**
 * Reads lines from a test input file for the given day and part.
 */
fun readTestInput(day: Int, part: Int) =  Path("input/test/day%02d-%d.txt".format(day, part)).readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
