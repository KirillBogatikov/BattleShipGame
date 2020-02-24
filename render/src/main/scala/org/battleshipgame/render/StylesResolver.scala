package org.battleshipgame.render

import org.battleshipgame.ui.ShipSize

trait StylesResolver {
    /**
     * Фоновое изображение для каждого экрана
     */
    def background(): Image
        
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
     * Цвет текста
     */
    def textColor(): Long
}