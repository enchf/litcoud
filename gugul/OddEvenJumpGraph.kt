package gugul

import java.util.*
import kotlin.collections.HashSet

data class Vertex(val value: Int, var index: Int,
                  val odds: MutableSet<Vertex> = HashSet(),
                  val evens: MutableSet<Vertex> = HashSet())

data class Jump(val vertex: Vertex, val odd: Boolean)

fun Vertex.printSimple() = "[$value at $index]"
fun Vertex.print() =
    { set: Set<Vertex> -> set.map { it.value }.joinToString(",") }
    .let { "\n${printSimple()} - Odds: ${it(odds)}, Evens: ${it(evens)}" }

class OddEvenJumpGraph(input: List<Int>) {
    val graph : List<Vertex>
    val oddTree = TreeSet<Vertex> { a, b -> if (a.value == b.value) a.index - b.index else a.value - b.value }
    val evenTree = TreeSet<Vertex> { a, b -> if (a.value == b.value) a.index - b.index else b.value - a.value }

    init {
        graph = input.withIndex().map { (i, v) -> Vertex(v, i) }
        oddTree.addAll(graph)
        evenTree.addAll(graph)
    }

    fun build(): Int {
        graph.forEach {
            val old = it.index
            it.index = -1

            oddTree.remove(it)
            evenTree.remove(it)

            oddTree.ceiling(it)?.also { next -> next.odds.add(it) }
            evenTree.ceiling(it)?.also { next -> next.evens.add(it) }

            it.index = old
        }

        graph.map(Vertex::print).let(::println)

        return 1
    }
}

fun main() =
        listOf(listOf(10,13,12,14,15),listOf(2,3,1,1,4),listOf(5,1,3,4,2))
          .zip(listOf(2,3,3))
          .map { (input, expect) -> OddEvenJumpGraph(input).build().let {
              """
                  * Input: $input
                  * Output: $it
                  * Expected: $expect
                  * Result: ${if (it == expect) "PASS" else "FAIL"}
                  **************
              """.trimIndent() } }
          .forEach(::println)
