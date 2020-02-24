package org.battleshipgame.render

/**
 * Вьюха, представление, абстрактное что-то на экране
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait View {
    /**
     * Прямоугольничек, в который вписано это что-то
     */
    def rectangle(): Rectangle
}

/**
 * Вьюха с текстом: надпись или текстовое поле
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait TextView extends View {
    def text(): String
    def text(text: String): Unit
    def textSize(): Double
    
    /**
     * КЛИКНИ МЕНЯ!
     */
    def onClick(): Unit = {}
}

/**
 * Кнопка
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait Button extends TextView {
    /**
     * Состояние кнопки
     * 
     * Ой, девачки, мне плоха (с)
     */
    var state: ButtonState = null
}

/**
 * Вьюха с картинкой
 */
trait ImageView extends View {
    /**
     * Картинка
     */
    def image(): Image
    
    /**
     * КЛИКНИ МЕНЯ! (по умолчанию выпускает ядерную боеголовку КНДР)
     */
    def onClick(): Unit = {}
}

/**
 * МэпГридВью - сетка игрового поля
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait MapGridView extends View {
    /**
     * Размер ячеечки
     */
    def cellSize(): Int
    
    /**
     * По нам стреляют, ааааа, спасайся кто может
     */
    def onShot(i: Int, j: Int): Unit
}