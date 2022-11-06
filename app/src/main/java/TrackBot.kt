import com.boardGame.quarantine_queen.Status
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import com.boardGame.quarantine_queen.utils.toStringList

class TrackBot {

    private var size: Int = 4
    private var value = "Q"
    private var gridSolutionMap = HashMap<Int, ArrayList<Array<Array<String>>>>()
    private var gridSolutionIndexMap = HashMap<Int, ArrayList<Array<String>>>()
    val gridDetails = ArrayList<GridDetail>()
        get() = field
    val gridSolutionDetails = ArrayList<GridSolutionDetail>()
        get() = field
    val progressDetails = ArrayList<ProgressDetail>()
        get() = field

    fun trackBot(grid: Array<Array<String>>) {
        println("TrackBot running with grid size $size")
        var row = 0
        var column = 0
        while (row < size) {
            var queenAdded = false
            while (column < size) {
                // check for the valid position and add the queen
                if (checkValidPosition(row, column, grid)) {
                    queenAdded = addQueen(row, column, grid)
                    break
                }

                // increment the column till find the valid position and add queen
                column++
            }
            if (!queenAdded) {
                row = getPreviousRow(row)
                column = getExistingQueenColumn(row, grid)
                if (column < 0) {
                    break
                }
                removeQueen(row, column, grid)
                column++
            } else {
                if (row == (size - 1)) {

                    storeSolutions(grid)
                    storeSolutionIndexes(grid)
//                    println(++count);
                    removeQueen(row, column, grid)
                    column++
                } else {
                    row++
                    column = 0
                }
            }
        }
    }

    private fun getPreviousRow(row: Int): Int {
        return when (row > 0) {
            true -> row - 1
            else -> row
        }
    }

    private fun addQueen(
        selectedRow: Int,
        selectedColumn: Int,
        grid: Array<Array<String>>
    ): Boolean {
        grid[selectedRow][selectedColumn] = value
        return true
    }

    private fun removeQueen(selectedRow: Int, selectedColumn: Int, grid: Array<Array<String>>) {
        grid[selectedRow][selectedColumn] = selectedColumn.toString()
    }

    private fun getExistingQueenColumn(selectedRow: Int, grid: Array<Array<String>>): Int {
        return grid[selectedRow].indexOf(value)
    }

    private fun checkValidPosition(
        selectedRow: Int,
        selectedColumn: Int,
        grid: Array<Array<String>>
    ): Boolean {
        var status = true
        for (row in 0 until size) {
            for (column in 0 until size) {
                if (this.isConflictCell(
                        selectedRow,
                        selectedColumn,
                        row,
                        column
                    ) && grid[row][column] == value
                ) {
                    status = false
                    break
                }
                if (!status) {
                    break
                }

            }
        }
        return status
    }

    private fun storeSolutions(grid: Array<Array<String>>) {
        var solutionList = ArrayList<Array<Array<String>>>()
        if (gridSolutionMap.containsKey(size)) solutionList = gridSolutionMap[size]!!
        solutionList.add(grid.copy())
        this.gridSolutionMap[size] = solutionList
    }

    private fun storeSolutionIndexes(grid: Array<Array<String>>) {
        var solutionList = ArrayList<Array<String>>()
        if (gridSolutionIndexMap.containsKey(size)) solutionList = gridSolutionIndexMap[size]!!
        val solutionArray: Array<String> = grid.copy().mapIndexedTo()
        solutionList.add(solutionArray)
        this.gridSolutionIndexMap[size] = solutionList

    }

    fun getSolutions(): HashMap<Int, ArrayList<Array<Array<String>>>> {
        return gridSolutionMap
    }

    fun getSolutionIndex(): HashMap<Int, ArrayList<Array<String>>> {
        return gridSolutionIndexMap
    }

    fun generateGrid(): Array<Array<String>> {
        return Array(size) { Array(size) { it.toString() } }
    }

    private fun Array<Array<String>>.copy() = Array(size) { get(it).clone() }
    private fun Array<Array<String>>.mapIndexedTo(): Array<String> =
        Array(size) { it.toString().plus("_").plus(get(it).indexOf(value)) }

    fun setSize(size: Int) {
        this.size = size
    }

    private fun isConflictCell(
        selectedRow: Int,
        selectedColumn: Int,
        row: Int,
        column: Int
    ): Boolean {
        return (selectedColumn == column || selectedRow == row) || (selectedColumn + selectedRow == column + row) || (selectedColumn - selectedRow) == (column - row)
    }

    fun start() {
        val minSize = 4
        val maxSize = 10
        for (size in minSize..maxSize) {
            this.setSize(size)
            val grid = this.generateGrid()
            this.trackBot(grid)
        }
        val gridSolutionIndexMap = this.getSolutionIndex()

        for ((size, solutionList) in gridSolutionIndexMap) {
            val gridDetail = GridDetail(size, "$size Queen", solutionList.size)
            solutionList.forEachIndexed { solutionIndex, solution ->
                val gridSolutionDetail = GridSolutionDetail()
                val progressDetail = ProgressDetail("${size}_${solutionIndex + 1}")
                gridSolutionDetail.size = size
                gridSolutionDetail.solutionList = solution.toStringList() as ArrayList<String>
                progressDetail.userSolutionList = getDefaultList(size)
                if (solutionIndex == 0) {
                    progressDetail.status = Status.PROGRESS.value
                    gridSolutionDetail.status = Status.PROGRESS.value
                } else {
                    progressDetail.status = Status.START.value
                    gridSolutionDetail.status = Status.START.value
                }
                gridSolutionDetails.add(gridSolutionDetail)

                progressDetail.size = size

                progressDetails.add(progressDetail)

            }
            gridDetails.add(gridDetail)
        }

    }


}

private fun getDefaultList(size: Int): ArrayList<String> {
    val emptyList = ArrayList<String>(size)
    for (i in 0 until size) {
        emptyList.add(("-1").plus("_").plus("-1"))
    }
    return emptyList
}

fun main() {
    println("TrackBot starting")
    val trackBotInstance = TrackBot()
    val minSize = 4

    val maxSize = 6
    for (size in minSize..maxSize) {
        trackBotInstance.setSize(size)
        val grid = trackBotInstance.generateGrid()
//        gridPrinter(grid)
        trackBotInstance.trackBot(grid)
        val gridSolutionMap = trackBotInstance.getSolutions()
        gridSolutionMap[size]?.forEach {
            gridPrinter(it)
        }
        val gridSolutionIndexMap = trackBotInstance.getSolutionIndex()

        if (size == maxSize) {

            for ((key, value) in gridSolutionIndexMap) {
                println(
                    "Size => $key , solution count ${
                        value.forEach { it ->
                            it.forEach {
                                print(
                                    it
                                )
                            }
                            println()
                        }
                    }"
                )
            }
            gridSolutionMap.forEach { (key, value) ->
                println("Size => $key , solution count ${value.size}")
            }


        }

    }
//        gridSolutionList[size]?.size
    println("TrackBot completed")

}

fun gridPrinter(grid: Array<Array<String>>) {
    grid.forEachIndexed { subGridIndex: Int, subGrid: Array<String> ->
        subGrid.forEach { _ ->
            print(
                "+-------"
            )
        }
        print(
            "+"
        )
        println()

        subGrid.forEach {
            if (it == "Q") {
                print(
                    "|   $it   "
                )
            } else {
                print(
                    "|       "
                )
            }
        }
        print(
            "|"
        )
        println()
        if (subGridIndex == grid.size - 1) {
            subGrid.forEach { _ ->
                print(
                    "+-------"
                )
            }
            print(
                "+"
            )
            println()
        }
    }
}
