package hu.ait.minimine.model

object MineFieldModel {
    var BLANK = 0
    var ONE = 1
    var TWO = 2
    var THREE = 3
    ////// FOUR TO EIGHT = 4 to 8
    var FLAG = 9
    var BOMB = 10
    var HIDDEN = 11

    var GAME_DIMENSION = 5


    private var fieldMatrix: Array<Array<Field>> = arrayOf(
        arrayOf(
            Field(0, 0, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 0, isFlagged = false, isClicked = false, HIDDEN)),

        arrayOf(
            Field(0, 0, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(1, 0, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 2, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN)),

        arrayOf(
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 2, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 2, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 2, isFlagged = false, isClicked = false, HIDDEN),
            Field(1, 0, isFlagged = false, isClicked = false, HIDDEN)),

        arrayOf(
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(1, 0, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN)),

        arrayOf(
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 0, isFlagged = false, isClicked = false, HIDDEN),
            Field(0, 0, isFlagged = false, isClicked = false, HIDDEN)),
    )

    fun getFieldMatrix(): Array<Array<Field>> {
        return fieldMatrix
    }

    fun getField(x:Int, y:Int): Field? {
        if (x >= GAME_DIMENSION || y >= GAME_DIMENSION || y < 0 || x < 0) {
            return null
        }
        return fieldMatrix[x][y]
    }

    fun setField(x:Int, y:Int, field: Field) {
        fieldMatrix[x][y] = field
    }

    fun getSafeNeighbours(x:Int, y:Int): ArrayList<Field> {
        val neighbours = ArrayList<Field>()

        val directions = listOf(-1, 0, 1)
        for (dx in directions) {
            for (dy in directions) {
                if (dx == 0 && dy == 0) continue

                val neighborX = x + dx
                val neighborY = y + dy
                val neighborField = getField(neighborX, neighborY)

                neighborField?.let {
                    if (it.mine == 0) {
                        neighbours.add(it)
                    }
                }
            }
        }
        return neighbours
    }

    fun isWon(): Boolean {
        var stillGoing = false
        for (i in 0..<GAME_DIMENSION) {
            for (j in 0..<GAME_DIMENSION) {
                if (fieldMatrix[i][j].display == HIDDEN) {
                    stillGoing = true
                }
            }
        }
        return !stillGoing
    }


    fun resetModel() {
        fieldMatrix = arrayOf(
            arrayOf(
                Field(0, 0, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 0, isFlagged = false, isClicked = false, HIDDEN)),

            arrayOf(
                Field(0, 0, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(1, 0, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 2, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN)),

            arrayOf(
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 2, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 2, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 2, isFlagged = false, isClicked = false, HIDDEN),
                Field(1, 0, isFlagged = false, isClicked = false, HIDDEN)),

            arrayOf(
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(1, 0, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN)),

            arrayOf(
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 1, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 0, isFlagged = false, isClicked = false, HIDDEN),
                Field(0, 0, isFlagged = false, isClicked = false, HIDDEN)),
        )

    }
}

data class Field (
    var mine: Int,
    var minesAround: Int,
    var isFlagged: Boolean,
    var isClicked: Boolean,
    var display: Int
)