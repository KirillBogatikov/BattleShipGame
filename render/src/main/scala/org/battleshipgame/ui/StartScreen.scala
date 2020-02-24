package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.{Button, Screen}

trait StartScreen extends Screen {
    def buttons(): Array[Button]
}