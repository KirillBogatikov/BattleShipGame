package org.battleshipgame.render

import scala.language.postfixOps

/**
 * Интерфейс рисовальщика (полотна, канвы, как Вам удобнее)
 * 
 * @author Кирилл Испольнов
 * @version 1.1
 * @since 2.0.0
 */
trait Renderer {    
    /**
     * Сохранить текущую конфигурацию и подготовиться к рисованию
     * 
     * Достаем мольберты, так сказать (с)
     */
    def begin(): Unit
    /**
     * Восстановить прошлую конфигурацию, высвободить ресурсы
     */
    def end(): Unit
    
    /**
     * Устанавливает необходимость отрисовки линий/границ фигур
     */
    def stroke(paint: Boolean): Unit
    /**
     * Устанавливает необходимость заливки фигур цветом
     */
    def fill(paint: Boolean): Unit

    /**
     * Устанавливает цвет линий
     */
    def stroke(color: Long): Unit
    /**
     * Устанавливает цвет заливки
     */
    def fill(color: Long): Unit
    /**
     * Устанавливает цвет текста
     */
    def text(color: Long): Unit
    
    /**
     * Устанавливает толщину линий
     */
    def stroke(size: Int): Unit

    /**
     * Отрисовать прямоугольник
     */
    def rectangle(rectangle: Rectangle): Unit
    /**
     * Отрисовать прямоугольник (для ленивых)
     */
    def rectangle(vertex: Point, size: Size, cornerRadius: Double = 0.0): Rectangle = {
        val rect = new Rectangle(vertex x, vertex y, size width, size height, cornerRadius)
        rectangle(rect)
        return rect
    }
    
    /**
     * Отрисовать тень прямоугольника
     * 
     * Материал дизайн, что-ли?! (с)
     */
    def shadow(rectangle: Rectangle): Unit
    
    /**
     * Отрисовать линию
     */
    def line(start: Point, end: Point): Unit
    /**
     * Отрисовать линию (опять для ленивых, да я такой)
     */
    def line(sx: Int, sy: Int, ex: Int, ey: Int): Unit

    /**
     * Отрисовать изображение
     */
    def image(rectangle: Rectangle, image: Image): Unit

    /**
     * Отрисовать текст заданного размера, вписав его в прямоугольник
     * Размер текста - в попугаях, каких-то абстрактных платформенно-независимых единицах
     */
    def text(rectangle: Rectangle, text: String, size: Double): Unit
}