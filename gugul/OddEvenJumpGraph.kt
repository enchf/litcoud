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
fun Vertex.addAll(odd: Boolean, target: MutableSet<Jump>) =
    (if (odd) odds else evens).forEach { target.add(Jump(it, odd)) }

class OddEvenJumpGraph(input: List<Int>) {
    private val graph : List<Vertex>
    private val oddTree = TreeSet<Vertex> { a, b -> if (a.value == b.value) a.index - b.index else a.value - b.value }
    private val evenTree = TreeSet<Vertex> { a, b -> if (a.value == b.value) a.index - b.index else b.value - a.value }

    init {
        graph = input.withIndex().map { (i, v) -> Vertex(v, i) }
        oddTree.addAll(graph)
        evenTree.addAll(graph)

        // build graph
        graph.forEach {
            val old = it.index
            it.index = -1

            oddTree.remove(it)
            evenTree.remove(it)

            oddTree.ceiling(it)?.also { next -> next.odds.add(it) }
            evenTree.ceiling(it)?.also { next -> next.evens.add(it) }

            it.index = old
        }
    }

    fun build(): Int {
        val visited = BooleanArray(graph.size) { false }
        val paths = mutableSetOf<Jump>()
        val last = graph.last()

        visited[last.index] = true
        last.addAll(true, paths)
        last.addAll(false, paths)

        while (paths.isNotEmpty()) {
            val next = paths.first()
            paths.remove(next)

            if (next.odd && !visited[next.vertex.index]) {
                visited[next.vertex.index] = true
                next.vertex.addAll(false, paths)
            } else {
                next.vertex.addAll(true, paths)
            }
        }

        return visited.count { it }
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
