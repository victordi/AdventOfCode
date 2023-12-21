package utils

import kotlin.math.max

class Grid<T>(private val grid: List<List<T>>) {
  init {
    require(grid.all { it.size == (grid.firstOrNull()?.size ?: 0) }) { "Invalid grid format." }
  }

  companion object {
    fun <R> fromGrid(grid: List<List<R>>): Grid<R> = Grid(grid)

    fun <R> from(input: List<String>, mapper: (String) -> List<R>): Grid<R> = input
      .map { mapper(it) }
      .let { Grid(it) }

    fun from(input: List<String>): Grid<Char> = from(input) { it.toList() }

    fun <R> fromInput(mapper: (String) -> List<R>): Grid<R> = Input.readInput()
      .map { mapper(it) }
      .let { Grid(it) }

    fun fromInput(): Grid<Char> = from(Input.readInput())
  }

  val width: Int = grid.firstOrNull()?.size ?: 0

  val xRange: IntRange = 0 until width

  val height: Int = grid.size

  val yRange: IntRange = 0 until height

  val indexes: List<Point> = yRange.flatMap { i -> xRange.map { j -> i to j } }

  val elements: Map<Point, T> = indexes.associateWith { (i, j) -> grid[i][j] }

  val columns: List<List<T>> = xRange.map { i -> yRange.map { j -> grid[j][i] } }

  val lines: List<List<T>> = grid

  operator fun get(point: Point): T = grid[point.first][point.second]

  fun get(x: Int, y: Int): T = get(x to y)

  fun getOrNull(point: Point): T? = grid.getOrNull(point.first)?.getOrNull(point.second)

  fun getOrNull(x: Int, y: Int): T? = getOrNull(x to y)

  fun getAdjacent(x: Int, y: Int): List<T> = listOf(x - 1 to y, x to y + 1, x + 1 to y, x to y - 1)
    .filter { indexes.contains(it) }
    .map { get(it) }

  fun getAdjacentPoints(x: Int, y: Int): List<Pair<Point, T>> = listOf(x - 1 to y, x to y + 1, x + 1 to y, x to y - 1)
    .filter { indexes.contains(it) }
    .map { it to get(it) }

  fun getAdjacentPoints(point: Point): List<Pair<Point,T>> = getAdjacentPoints(point.first, point.second)

  fun getAdjacent(point: Point): List<T> = getAdjacent(point.first, point.second)

  fun getAdjacentWithCorners(x: Int, y: Int): List<T> = getAdjacentWithCorners(x to y)

  fun getAdjacentWithCorners(point: Point): List<T> = Direction.values()
    .map { point.move(it) }
    .filter { indexes.contains(it) }
    .map { get(it) }

  override fun toString(): String {
    val maxElementSize = fold(0) { acc, element -> max(acc, element.toString().length) }
    return yRange.fold("") { acc, i ->
      val line = xRange.fold("") { acc2, j ->
        acc2 + grid[i][j].toString().padStart(maxElementSize, ' ') + " "
      }
      acc + line + System.lineSeparator()
    }
  }

  fun <R> mapIndexed(mapper: (Point, T) -> R): Grid<R> = grid
    .mapIndexed { i, line ->
      line.mapIndexed { j, element ->
        mapper(i to j, element)
      }
    }
    .let { Grid(it) }

  fun <R> map(mapper: (T) -> R): Grid<R> = mapIndexed(liftAsIndexed(mapper))

  fun filterIndexed(condition: (Point, T) -> Boolean): Grid<T> = grid
    .mapIndexed { i, line ->
      line.filterIndexed { j, element ->
        condition(i to j, element)
      }
    }
    .let { Grid(it) }

  fun filter(condition: (T) -> Boolean): Grid<T> = filterIndexed(liftAsIndexed(condition))

  fun <R> foldIndexed(initial: R, f: (Point, R, T) -> R): R = grid
    .foldIndexed(initial) { i, acc, line ->
      line.foldIndexed(acc) { j, acc2, element ->
        f(i to j, acc2, element)
      }
    }

  fun <R> fold(initial: R, f: (R, T) -> R): R = grid
    .fold(initial) { acc, line ->
      line.fold(acc) { acc2, element ->
        f(acc2, element)
      }
    }

  fun <R> foldLinesIndexed(initial: R, f: (Int, R, List<T>) -> R): R = grid.foldIndexed(initial, f)

  fun <R> foldLines(initial: R, f: (R, List<T>) -> R): R = grid.fold(initial, f)

  fun <R> foldColumnsIndexed(initial: R, f: (Int, R, List<T>) -> R): R = columns.foldIndexed(initial, f)

  fun <R> foldColumns(initial: R, f: (R, List<T>) -> R): R = columns.fold(initial, f)

  private fun <A, B> liftAsIndexed(f: (A) -> B): (Point, A) -> B = { _: Point, el: A -> f(el) }
}