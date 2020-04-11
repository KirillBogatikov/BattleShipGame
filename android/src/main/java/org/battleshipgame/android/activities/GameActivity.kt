package org.battleshipgame.android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_game.*
import org.battleshipgame.android.GameInstance
import org.battleshipgame.android.R
import org.battleshipgame.android.views.ShotListener
import org.battleshipgame.core.InfoChangeListener
import org.battleshipgame.core.ShipOrientation
import org.battleshipgame.core.ShipSize
import org.battleshipgame.geometry.Point
import org.battleshipgame.player.AI

class GameActivity : StandardActivity() {
    private val fadeInAnimation = AlphaAnimation(0.0f, 1.0f)
    private val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        prepareAnimations()

        val game = GameInstance.get()
        friend.active = true;
        friend.shotListener = object : ShotListener {
            override fun onShot(x: Int, y: Int) {
                game.user.setShot(Point(x, y))
                locker.startAnimation(fadeInAnimation)
            }
        }

        game.userInfo.addListener(object : InfoChangeListener {
            override fun onMiss(point: Point) {
                drawCellImage(point.x, point.y, user, R.drawable.miss)
                locker.startAnimation(fadeOutAnimation)
            }

            override fun onFlame(point: Point) {
                drawCellImage(point.x, point.y, user, R.drawable.flame)
                println("F")
            }

            override fun onWreck(point: Point) {
                drawCellImage(point.x, point.y, user, R.drawable.wreck)
                println("W")

                if(GameInstance.getFriend().wrecks().size == 20) {
                    val intent = Intent(this@GameActivity, ResultActivity::class.java)
                    intent.putExtra("win", false)
                    startActivity(intent)
                }
            }
        })

        game.friendInfo.addListener(object : InfoChangeListener {
            override fun onMiss(point: Point) {
                drawCellImage(point.x, point.y, friend, R.drawable.miss)
            }

            override fun onFlame(point: Point) {
                drawCellImage(point.x, point.y, friend, R.drawable.flame)
                println("F")
            }

            override fun onWreck(point: Point) {
                drawCellImage(point.x, point.y, friend, R.drawable.wreck)
                locker.startAnimation(fadeOutAnimation)
                println("W")

                if(GameInstance.getFriend().wrecks().size == 1) {
                    val intent = Intent(this@GameActivity, ResultActivity::class.java)
                    intent.putExtra("win", true)
                    startActivity(intent)
                }
            }

            override fun onTryAgain(point: Point) {
                locker.startAnimation(fadeOutAnimation)
            }
        })

        getHandler().postDelayed(this::showUserShips, 500)

        game.setFriend(AI());
        game.start()
    }

    private fun addView(view: View, params: RelativeLayout.LayoutParams?) {
        val index = container.indexOfChild(locker)

        if (params == null) {
            runOnUiThread { container.addView(view, index - 1) }
        } else {
            runOnUiThread { container.addView(view, index - 1, params) }
        }
    }

    private fun showUserShips() {
        for (ship in GameInstance.getUser().ships()) {
            val img = ImageView(this)
            val resId = if (ship.orientation == ShipOrientation.X_AXIS) {
                when(ship.size!!) {
                    ShipSize.WARSHIP -> R.drawable.warship
                    ShipSize.CRUISER -> R.drawable.cruiser
                    ShipSize.DESTROYER -> R.drawable.destroyer
                    ShipSize.TORPEDO -> R.drawable.torpedo
                }
            } else {
                when(ship.size!!) {
                    ShipSize.WARSHIP -> R.drawable.warship_v
                    ShipSize.CRUISER -> R.drawable.cruiser_v
                    ShipSize.DESTROYER -> R.drawable.destroyer_v
                    ShipSize.TORPEDO -> R.drawable.torpedo_v
                }
            }

            img.setImageResource(resId)
            val step = user.width / 10
            val rect = ship.rect
            val params = RelativeLayout.LayoutParams(rect.width * step, rect.height * step)
            params.leftMargin = rect.start.x * step + user.left
            params.topMargin = rect.start.y * step + user.top
            addView(img, params)
        }
        locker.startAnimation(fadeOutAnimation)
    }

    private fun prepareAnimations() {
        fadeInAnimation.duration = 400
        fadeOutAnimation.duration = 400

        val listener = object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (animation == fadeInAnimation) {
                    locker.visibility = VISIBLE
                }
            }

            override fun onAnimationEnd(animation: Animation) {
                if (animation == fadeOutAnimation) {
                    locker.visibility = GONE
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        }

        fadeInAnimation.setAnimationListener(listener)
        fadeOutAnimation.setAnimationListener(listener)
    }

    private fun drawCellImage(x: Int, y: Int, view: View, resId: Int) {
        val img = ImageView(this)
        img.setImageResource(resId)
        val step = view.width / 10
        val params = RelativeLayout.LayoutParams(step, step)
        params.leftMargin = view.left + x * step
        params.topMargin = view.top + y * step
        addView(img, params)
    }
}