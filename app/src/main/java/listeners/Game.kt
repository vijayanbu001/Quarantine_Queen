package listeners

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class Game : ViewModel() {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>();
    var availableQueens = MutableLiveData<Int>();
    var grid = MutableLiveData<Array<Array<String>>>();
    var countGridLiveData = MutableLiveData<ArrayList<String>>();
    var conflictLiveMap = MutableLiveData<HashMap<String, ArrayList<String>>>()
    var boardSize = 8;
    private var selectedRow = -1;
    private var selectedCol = -1;
    private var countArray = ArrayList<String>(boardSize)

    init {
        countArray.addAll(Array(boardSize){"Q"})
        println("${countArray[3]} ====>")
        countGridLiveData.postValue(countArray)
        grid.postValue(Array(boardSize) { Array(boardSize) { c -> c.toString() } })
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        availableQueens.postValue(8)
        conflictLiveMap.postValue(HashMap<String, ArrayList<String>>())

    }

    fun updateSelectedCell(row: Int, col: Int) {
        selectedCellLiveData.postValue(Pair(row, col));
    }

    fun updateAvailableQueenCount(value: Int) {
        var countGrid = countGridLiveData.value!!
        if (countGrid.size > 0) {
            if (value == 1) {
                countGrid.remove("Q")
            }
        }
        if (countGrid.size <= boardSize) {
            if (value == -1) {
                countGrid.add("Q")
            }
        }
        countGridLiveData.postValue(countGrid)
        availableQueens.postValue(countGrid.size)

    }

    fun updateGrid(grid: Array<Array<String>>) {
        this.grid.postValue(grid);
    }

    fun updateConflictMap(row: Int, column: Int, operation: Int) {
        val conflictedMap = conflictLiveMap.value!!
        val key = selectedCellLiveData.value!!.first.toString().plus(selectedCellLiveData.value!!.second)
        val value = row.toString().plus(column)
        if (operation == 1) {
            if (conflictedMap.containsKey(key)) {
                conflictedMap[key]!!.add(value)
            } else {
                val valueList = ArrayList<String>()
                valueList.add(value)
                conflictedMap[key] = valueList
            }

            if (conflictedMap.containsKey(value)) {
                conflictedMap[value]!!.add(key)
            } else {
                val valueList = ArrayList<String>()
                valueList.add(key)
                conflictedMap[value] = valueList
            }
        } else {
            val valueList = conflictedMap[key]
            valueList?.forEach { element -> conflictedMap[element]?.remove(key) }
            conflictedMap.remove(key)
        }

        conflictLiveMap.postValue(conflictedMap)

    }

}
