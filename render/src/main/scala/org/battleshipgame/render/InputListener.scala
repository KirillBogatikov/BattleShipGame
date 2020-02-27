package org.battleshipgame.render

/**
 * События окна: ввод символов, передвижение мыши, нажатия и др
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait InputListener {
    def onKeyPress(key: Int): Boolean
    def onMouseMove(x: Int, y: Int): Boolean
    def onMouseUp(x: Int, y: Int): Boolean
    def onMouseDown(x: Int, y: Int): Boolean
    def onClick(x: Int, y: Int): Boolean
}