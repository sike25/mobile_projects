package hu.ait.minimine.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import hu.ait.minimine.MainActivity
import hu.ait.minimine.R
import hu.ait.minimine.model.Field
import hu.ait.minimine.model.MineFieldModel

class MineFieldView (context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paintBackground: Paint = Paint()
    private var paintLine: Paint
    private var enabled = true

    private var blankCell  = BitmapFactory.decodeResource(resources, R.drawable.blank)
    private var oneCell    = BitmapFactory.decodeResource(resources, R.drawable.one)
    private var twoCell    = BitmapFactory.decodeResource(resources, R.drawable.two)
    private var threeCell  = BitmapFactory.decodeResource(resources, R.drawable.three)
    private var flagCell  = BitmapFactory.decodeResource(resources, R.drawable.flag)
    private var bombCell  = BitmapFactory.decodeResource(resources, R.drawable.bomb)
    private var hiddenCell = BitmapFactory.decodeResource(resources, R.drawable.hidden)

    private var gameDimension = MineFieldModel.GAME_DIMENSION


    init {
        paintBackground.setColor(Color.BLACK)
        paintBackground.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.setColor(Color.BLACK)
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 30f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blankCell = Bitmap.createScaledBitmap(blankCell, width/gameDimension, height/gameDimension, false)
        oneCell = Bitmap.createScaledBitmap(oneCell, width/gameDimension, height/gameDimension, false)
        twoCell = Bitmap.createScaledBitmap(twoCell, width/gameDimension, height/gameDimension, false)
        threeCell = Bitmap.createScaledBitmap(threeCell, width/gameDimension, height/gameDimension, false)
        flagCell = Bitmap.createScaledBitmap(flagCell, width/gameDimension, height/gameDimension, false)
        bombCell = Bitmap.createScaledBitmap(bombCell, width/gameDimension, height/gameDimension, false)
        hiddenCell = Bitmap.createScaledBitmap(hiddenCell, width/gameDimension, height/gameDimension, false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGameArea(canvas)
    }

    private fun drawGameArea(canvas: Canvas) {
        drawLines(canvas)
        drawCells(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
    }

    private fun drawLines(canvas: Canvas) {
        for (i in 1..gameDimension) {
            canvas.drawLine(0f, (i * height / gameDimension).toFloat(), width.toFloat(), (i * height / gameDimension).toFloat(), paintLine)
            canvas.drawLine((i * width / gameDimension).toFloat(), 0f, (i * width / gameDimension).toFloat(), height.toFloat(), paintLine)
        }
    }

    private fun drawCells(canvas: Canvas) {
        val fieldMatrix = MineFieldModel.getFieldMatrix()
        for (i in 0..<gameDimension) {
            for (j in 0..<gameDimension) {
                val cellDisplay = fieldMatrix[i][j].display

                when (cellDisplay) {
                    MineFieldModel.BLANK -> {
                        canvas.drawBitmap(blankCell, (i * width/gameDimension).toFloat(), (j * height/gameDimension).toFloat(), null)
                    }
                    MineFieldModel.ONE -> {
                        canvas.drawBitmap(oneCell, (i * width/gameDimension).toFloat(), (j * height/gameDimension).toFloat(), null)
                    }
                    MineFieldModel.TWO -> {
                        canvas.drawBitmap(twoCell, (i * width/gameDimension).toFloat(), (j * height/gameDimension).toFloat(), null)
                    }
                    MineFieldModel.THREE -> {
                        canvas.drawBitmap(threeCell, (i * width/gameDimension).toFloat(), (j * height/gameDimension).toFloat(), null)
                    }
                    MineFieldModel.FLAG -> {
                        canvas.drawBitmap(flagCell, (i * width/gameDimension).toFloat(), (j * height/gameDimension).toFloat(), null)
                    }
                    MineFieldModel.BOMB -> {
                        canvas.drawBitmap(bombCell, (i * width/gameDimension).toFloat(), (j * height/gameDimension).toFloat(), null)
                    }
                    MineFieldModel.HIDDEN -> {
                        canvas.drawBitmap(hiddenCell, (i * width/gameDimension).toFloat(), (j * height/gameDimension).toFloat(), null)
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && enabled) {
            val tX = event.x.toInt() / (width / gameDimension)
            val tY = event.y.toInt() / (height / gameDimension)
            if (tX < gameDimension && tY < gameDimension) {
                val field: Field? = MineFieldModel.getField(tX, tY)
                if ((field != null) && (!field.isClicked)) {
                    field.isClicked = true
                    if ((context as MainActivity).isFlagModeOn()) {
                        touchEventFlagMode(tX, tY, field)
                    } else {
                        touchEventTryMode(tX, tY, field)
                    }
                    winCheck()
                    invalidate()
                }
            }
        }
        return true
    }

    private fun touchEventFlagMode(x: Int, y:Int, field: Field) {
        if (field.mine == 0) {
            loseGame(context.getString(R.string.snack_not_mine))
            revealSafeCell(field)
            freezeGame()
        } else {
            field.isFlagged = true
            field.display = MineFieldModel.FLAG
        }
        MineFieldModel.setField(x, y, field)
    }

    private fun touchEventTryMode(x: Int, y:Int, field: Field) {
        if (field.mine == 1) {
            loseGame(context.getString(R.string.snack_mine))
            field.display = MineFieldModel.BOMB
            freezeGame()
        } else {
           revealSafeCells(x, y, field)
        }
        MineFieldModel.setField(x, y, field)
    }

    private fun revealSafeCells(x: Int, y:Int, field: Field){
        val queue = ArrayDeque<Field>()
        val visited = HashSet<Field>()
        queue.add(field)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            visited.add(current)
            if (current.mine == 0) {
                revealSafeCell(current)
            }
            for (neighbour in MineFieldModel.getSafeNeighbours(x, y)) {
                if (!visited.contains(neighbour)) {
                    queue.add(neighbour)
                }
            }
        }

    }

    private fun revealSafeCell(field: Field) {
        when (field.minesAround) {
            1 -> {
                field.display = MineFieldModel.ONE
            }
            2 -> {
                field.display = MineFieldModel.TWO
            }
            3 -> {
                field.display = MineFieldModel.THREE
            }
            0 -> {
                field.display = MineFieldModel.BLANK
            }
        }

    }

    fun resetGame() {
        MineFieldModel.resetModel()
        enabled = true
        invalidate()
    }

    private fun freezeGame() {
        enabled = false
    }

    private fun winCheck() {
        if (MineFieldModel.isWon()) {
            winGame()
        }
    }

    private fun loseGame(message: String) {
        (context as MainActivity).showMessage(context.getString(R.string.snack_lose, message))
        freezeGame()
    }

    private fun winGame() {
        (context as MainActivity).showMessage(context.getString(R.string.snack_win))
        freezeGame()
    }

}
