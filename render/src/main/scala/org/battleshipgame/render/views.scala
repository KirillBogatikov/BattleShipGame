package org.battleshipgame.render

import scala.language.postfixOps
import org.battleshipgame.render.ButtonState.{ DEFAULT, PRESSED, HOVERED }

/**
 * Вьюха, представление, абстрактное что-то на экране
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
class View(var bounds: Rectangle) { 
    def x(): Int = bounds x
    def y(): Int = bounds y
    def width(): Int = bounds width
    def height(): Int = bounds height
}

trait ClickListener {
    def onClick(): Unit
}

/**
 * Вьюха с текстом: надпись или текстовое поле
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
class TextView(bounds: Rectangle, var text: String, val textSize: Double, val textColor: Long, val corner: Double, var listener: ClickListener) extends View(bounds) {
    def this(x: Int, y: Int, w: Int, h: Int, text: String, textSize: Double, textColor: Long, corner: Double) =
        this(new Rectangle(x, y, w, h), text, textSize, textColor, corner, () => {})
        
    def this(x: Int, y: Int, w: Int, h: Int, text: String, textSize: Double, textColor: Long, corner: Double, listener: ClickListener) =
        this(new Rectangle(x, y, w, h), text, textSize, textColor, corner, listener)
        
    def this(x: Int, y: Int, w: Int, h: Int, text: String, textSize: Double, corner: Double, listener: ClickListener) =
        this(new Rectangle(x, y, w, h), text, textSize, Long.MaxValue, corner, listener)
}

/**
 * Кнопка
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
class Button(bounds: Rectangle, text: String, textSize: Double, textColor: Long, 
             val default: Long, val pressed: Long, val hovered: Long, 
             val corner: Double, var state: ButtonState, 
             listener: ClickListener) 
    extends TextView(bounds, text, textSize, textColor, corner, listener) {
        
    def this(x: Int, y: Int, w: Int, h: Int, 
             text: String, textSize: Double, textColor: Long, 
             default: Long, pressed: Long, hovered: Long, 
             radius: Double) =
        this(new Rectangle(x, y, w, h), text, textSize, textColor, default, pressed, hovered, radius, DEFAULT, () => {})
        
    def this(x: Int, y: Int, w: Int, h: Int, text: String, textSize: Double) =
        this(x, y, w, h, text, textSize, Long.MaxValue, Long.MaxValue, Long.MaxValue, Long.MaxValue, 0.0)
        
    def background(): Long = {
        return state match {
            case DEFAULT => default
            case PRESSED => pressed
            case HOVERED => hovered
        }
    }
}

/**
 * Вьюха с картинкой
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
class ImageView(rectangle: Rectangle, var image: Image, var listener: ClickListener) extends View(rectangle) {
    def this(x: Int, y: Int, w: Int, h: Int, image: Image) = 
        this(new Rectangle(x, y, w, h), image, () => {})
}

/**
 * ГридВью - сетка игрового поля
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
class GridView(bounds: Rectangle, var cellSize: Int) extends View(bounds) {
    def this(x: Int, y: Int, width: Int, height: Int, styles: StylesResolver) = 
        this(new Rectangle(x, y, width, height), width / 10)
        
    def toGridCoords(pixel: Point): Point = {
        val ox = pixel.x - bounds.x
        val oy = pixel.y - bounds.y
        
        val i = ox / cellSize
        var j = oy / cellSize
        
        return new Point(i, j)
    }
    
    def toPixelCoords(grid: Point): Point = {
        val x = grid.x * cellSize
        var y = grid.y * cellSize
        
        return new Point(x, y)
    }
    
    def toGridSize(pixel: Size): Size = {
        val i = pixel.width / cellSize
        var j = pixel.height / cellSize
        
        return new Size(i, j)
    }
    
    def toPixelSize(grid: Size): Size = {
        val w = grid.width * cellSize
        var h = grid.height * cellSize 
        
        return new Size(w, h)
    }
}