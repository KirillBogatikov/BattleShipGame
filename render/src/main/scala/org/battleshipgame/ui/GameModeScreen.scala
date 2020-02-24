package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.{Button, ImageView, Screen, TextView}

trait GameModeScreen extends Screen {
    def buttons(): Array[Button] 
    def images(): Array[ImageView]
    def labels(): Array[TextView]
}