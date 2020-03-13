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
 * </ul>
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
case class Rectangle(var x: Int, var y: Int, 
                var width: Int, var height: Int) {
    
    /**
     * Прямоугольник с закруглением углов на основе {@link Point точки} и {@link Size размера}
     */
    def this(point: Point, size: Size) =
        this(point.x, point.y, size.width, size.height)
   
    def start(): Point = new Point(x, y)
    def end(): Point = new Point(x + width, y + height)
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
    def width(): Int
    def height(): Int
    def size(): Size = new Size(width, height)
}