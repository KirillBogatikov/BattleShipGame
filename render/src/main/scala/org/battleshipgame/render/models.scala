package org.battleshipgame.render

/**
 * Точка с координатами
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
case class Point(var x: Int, var y: Int) {}

/**
 * Размер объекта (ширина и высота)
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
case class Size(var width: Int, var height: Int) {}

/**
 * Прямоугольник
 * <ul>
 * 	<li>координаты левого верхнего угла: x, y, point</li>
 *  <li>размер: width, height, size</li>
 *  <li>радиус скругления углов: cornerRadius, по умолчанию - 0</li>
 * </ul>
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
class Rectangle(var x: Int, var y: Int, 
                var width: Int, var height: Int, 
                var cornerRadius: Double) {
    
    /**
     * Прямоугольник без закругления углов
     */
    def this(x: Int, y: Int, width: Int, height: Int) = 
        this(x, y, width, height, 0.0)
        
    /**
     * Прямоугольник с закруглением углов на основе {@link Point точки} и {@link Size размера}
     */
    def this(point: Point, size: Size, cornerRadius: Double) =
        this(point.x, point.y, size.width, size.height, cornerRadius)
        
    /**
     * Прямоугольник без закругления углов на основе {@link Point точки} и {@link Size размера}
     */
    def this(point: Point, size: Size) =
        this(point, size, 0.0)

    def point(): Point = new Point(x, y)
    def size(): Size = new Size(width, height)
    
    /**
     * Лежит ли точка внутри или на ребре прямоугольника
     * 
     * @return true, если точка point лежит внутри или на ребре прямоугольника, false во всех иных случаях
     */
    def contains(point: Point): Boolean = {
        return point.x >= x && point.y >= y &&
               point.x < x + width && point.y < y + height
    }
}

/**
 * Абстрактное представление картинки
 * Требует платформенно-зависимой реализации
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait Image {
    def size(): Size
}