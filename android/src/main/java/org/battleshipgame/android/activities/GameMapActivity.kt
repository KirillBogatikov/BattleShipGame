package org.battleshipgame.android.activities

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.DragEvent
import android.view.Gravity
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.View.*
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_game_map.*
import org.battleshipgame.android.GameInstance
import org.battleshipgame.android.R
import org.battleshipgame.core.Ship
import org.battleshipgame.core.ShipOrientation
import org.battleshipgame.core.ShipSize
import org.battleshipgame.geometry.Point
import kotlin.math.floor
import kotlin.math.roundToInt

class GameMapActivity : StandardActivity() {
    private var draggedShip: ShipSize? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_map)

        val h = Handler()
        val dragListener = OnDragListener { _, e ->
            if (e.action == DragEvent.ACTION_DRAG_ENDED && draggedShip != null) {
                val ey = e.y - getStatusBarHeight()
                if (e.x < map.left || e.x > map.left + map.width ||
                    e.y < map.top || ey > map.top + map.height) {
                    return@OnDragListener true
                }

                val views = when(draggedShip) {
                    ShipSize.WARSHIP -> arrayOf(warship, warship_counter)
                    ShipSize.CRUISER -> arrayOf(cruiser, cruiser_counter)
                    ShipSize.DESTROYER -> arrayOf(destroyer, destroyer_counter)
                    ShipSize.TORPEDO -> arrayOf(torpedo, torpedo_counter)
                    else -> arrayOf()
                }

                val shipView = views[0] as ImageView
                val counterView = views[1] as TextView

                val step = map.width / 10
                var xf = (e.x -map.left) / step
                var yf = (ey - map.top) / step

                if(vertical) {
                    yf -= draggedShip!!.size / 2
                } else {
                    xf -= draggedShip!!.size / 2
                    //yf--
                }

                val x = floor(xf).roundToInt()
                val y = floor(yf).roundToInt()

                val vx = x * step + map.left
                val vy = y * step + map.top

                val ship = Ship(draggedShip, if (vertical) ShipOrientation.Y_AXIS else ShipOrientation.X_AXIS, Point(x, y));
                val rect = GameInstance.getUser().placeShip(ship)
                if(rect == null) {
                    draggedShip = null
                    val staticShipView = ImageView(this@GameMapActivity)
                    staticShipView.setImageDrawable(shipView.drawable)
                    staticShipView.setOnClickListener { v ->
                        shipView.visibility = VISIBLE
                        val count = (counterView.text[1] - '0') + 1
                        counterView.text = "x$count"
                        counterView.visibility = VISIBLE
                        container.removeView(staticShipView)
                        GameInstance.getUser().ships().remove(ship)
                    }

                    val params = RelativeLayout.LayoutParams(shipView.layoutParams)
                    params.leftMargin = vx
                    params.topMargin = vy
                    container.addView(staticShipView, params)

                    val count = (counterView.text[1] - '0') - 1
                    counterView.text = "x$count"

                    if (count == 0) {
                        shipView.visibility = GONE
                        counterView.visibility = GONE
                    } else {
                        shipView.visibility = VISIBLE
                    }

                    if (GameInstance.getUser().ships().size == 10) {
                        start.visibility = VISIBLE
                    }
                } else {
                    val highlighter = ImageView(this@GameMapActivity)
                    highlighter.setImageResource(R.drawable.highlight)
                    val params = RelativeLayout.LayoutParams(rect.width * step, rect.height * step)
                    params.leftMargin = map.left + rect.start.x * step
                    params.topMargin = map.top + rect.start.y * step

                    Log.d("DND", "" + rect.width + ", " + rect.height)
                    container.addView(highlighter, params);

                    h.postDelayed(Runnable {
                        container.removeView(highlighter)
                    }, 3000)
                }

                return@OnDragListener false
            }
            true
        }

        val touchListener = OnTouchListener { v, e ->
            if (e.action == ACTION_DOWN) {
                val data = ClipData.newPlainText("123","hello dolly")
                val dsb = DragShadowBuilder(v)
                ViewCompat.startDragAndDrop(v, data, dsb, v, 0)

                draggedShip = when(v.id) {
                    R.id.warship -> ShipSize.WARSHIP
                    R.id.cruiser -> ShipSize.CRUISER
                    R.id.destroyer -> ShipSize.DESTROYER
                    R.id.torpedo -> ShipSize.TORPEDO
                    else -> null
                }

                return@OnTouchListener true
            } else if (e.action == ACTION_UP) {
                ViewCompat.cancelDragAndDrop(v)
                return@OnTouchListener true
            }
            false
        }

        for(view in arrayOf(warship, cruiser, destroyer, torpedo)) {
            view.setOnTouchListener(touchListener)
            view.setOnDragListener(dragListener)
        }
    }

    private var vertical = true

    fun rotateShips(view: View) {
        if (vertical) {
            setGridColumns(2)

            warship.setImageResource(R.drawable.warship)
            cruiser.setImageResource(R.drawable.cruiser)
            destroyer.setImageResource(R.drawable.destroyer)
            torpedo.setImageResource(R.drawable.torpedo)
        } else {
            setGridColumns(8)

            warship.setImageResource(R.drawable.warship_v)
            cruiser.setImageResource(R.drawable.cruiser_v)
            destroyer.setImageResource(R.drawable.destroyer_v)
            torpedo.setImageResource(R.drawable.torpedo_v)
        }

        for(v in arrayOf(warship, cruiser, destroyer, torpedo)) {
            val params = v.layoutParams
            params.width = v.height
            params.height = v.width
            v.layoutParams = params
        }

        vertical = !vertical
    }

    private fun setGridColumns(columns: Int) {
        var col = 0
        var row = 0
        if (columns > ships.columnCount) {
            ships.columnCount = columns
        }

        for (child in ships.children) {
            val params = child.layoutParams as GridLayout.LayoutParams
            params.columnSpec = GridLayout.spec(col, 1)
            params.rowSpec = GridLayout.spec(row, 1)
            params.setGravity(Gravity.CENTER)
            if (col + 1 == columns) {
                col = 0
                row++
            } else {
                col++
            }
            child.layoutParams = params
        }
        ships.requestLayout()

        if (columns < ships.columnCount) {
            ships.columnCount = columns + 1
        }
    }

    fun startGame(view: View) {
        startActivity(Intent(this, GameActivity::class.java))
    }
}