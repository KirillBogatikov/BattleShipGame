package org.battleshipgame.render

/**
 * События окна: ввод символов, передвижение мыши, нажатия и др
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait InputListener {
    def onKeyPress(key: Int): Unit
    def onMouseMove(x: Int, y: Int): Unit
    def onMouseUp(x: Int, y: Int): Unit
    def onMouseDown(x: Int, y: Int): Unit
    def onClick(x: Int, y: Int): Unit
}