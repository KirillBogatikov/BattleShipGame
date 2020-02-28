package org.battleshipgame.render

import org.battleshipgame.ui.{ShipSize, ShipOrientation}

/**
 * Платформа, дай стили (цвета, размеры) и картинки
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait StylesResolver {
    /**
     * Фоновое изображение для каждого экрана
     */
    def background(): Image
    
    /**
     * Изображение огня
     */
    def flame(): Image
    
    /**
     * Изображение обломков
     */
    def wreck(): Image
    
    /**
     * Изображение промаха
     */
    def miss(): Image
        
    /**
     * Цвет кнопки по умолчанию
     */
    def buttonDefault(): Long
    
    /**
     * Цвет нажатой кнопки
     */
    def buttonPressed(): Long
    
    /**
     * Цвет кнопки с наведенным курсором
     */
    def buttonHovered(): Long
    
    /**
     * Цвет линий
     */
    def linesColor(): Long
    
    /**
     * Фон текстового поля
     */
    def inputBackground(): Long
    
    /**
     * Фон подсвеченной зоны
     */
    def highlightBackground(): Long
    
    /**
     * Цвет границы подсвеченной зоны
     */
    def highlightStroke(): Long
        
    /**
     * Цвет текста
     */
    def textColor(): Long
    
    /**
     * Цвет текста
     * 
     * темная тема темная тема темная тема
     */
    def darkTextColor(): Long
    
    /**
     * Толщина линии
     */
    def strokeSize(): Int
    
    /**
     * Дай кораблик по размеру и ориентации
     */
    def ship(size: ShipSize, orientation: ShipOrientation): Image
}