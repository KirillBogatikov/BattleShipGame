package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.{Button, Screen}

/**
 * Стартовый экран
 * 
 * Минималистично, однако (с)
 */
trait StartScreen extends Screen {
    /**
     * Чисто напоминалка, что этот метод надо реализовать
     */
    def buttons(): Array[Button]
}