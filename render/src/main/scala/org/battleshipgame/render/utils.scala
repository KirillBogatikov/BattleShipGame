package org.battleshipgame.render

import scala.language.postfixOps

/**
 * Утилита работы с цветами
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
object ColorUtils {
    /**
     * Подайте мне прозрачность, сударь
     */
    def alpha(color: Long): Int = {
        return (color >> 24) toInt
    }
    
    /**
     * Возвращает компонент красного цвета
     */
    def red(color: Long): Int = {
        return ((color >> 16) & 0xFF) toInt
    }
    
    /**
     * Возвращает компонент зеленого цвета
     */
    def green(color: Long): Int = {
        return ((color >> 8) & 0xFF) toInt
    }
    
    /**
     * Компонент синего цвета
     */
    def blue(color: Long): Int = {
        return (color & 0xFF) toInt
    }
    
    /**
     * Поменять прозрачность цвета
     * Возвращает новый цвет
     */
    def alpha(color: Long, alpha: Int): Long = {
        return argb(alpha, red(color), green(color), blue(color))
    }
    
    /**
     * Поменять содержание красного компонента цвета
     * Возвращает новый цвет
     */
    def red(color: Long, red: Int): Long = {
        return argb(alpha(color), red, green(color), blue(color))
    }
    
    /**
     * Поменять содержание зеленого компонента цвета
     * Возвращает новый цвет
     */
    def green(color: Long, green: Int): Long = {
        return argb(alpha(color), red(color), green, blue(color))
    }
    
    /**
     * Поменять содержание синего компонента цвета
     * Возвращает новый цвет
     */
    def blue(color: Long, blue: Int): Long = {
        return argb(alpha(color), red(color), green(color), blue)
    }
        
    /**
     * Упаковать компоненты в числовое значение
     */
    def argb(alpha: Int, red: Int, green: Int, blue: Int): Long = {
        return (alpha << 24) | (red << 16) | (green << 8) | blue
    }
}