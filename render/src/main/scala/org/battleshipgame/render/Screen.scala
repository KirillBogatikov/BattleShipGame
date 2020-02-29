package org.battleshipgame.render

import scala.language.postfixOps

import org.battleshipgame.render.ButtonState.{DEFAULT, HOVERED, PRESSED}
import org.battleshipgame.render.ColorUtils.alpha

/**
 * Экран
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait Screen extends InputListener {
    /**
     * Платформа, дай рисовальщик
     */
    def renderer(): Renderer
    /**
     * Платформа, дай стили
     */
    def styles(): StylesResolver
    /**
     * Платформа, дай размер
     */
    def size(): Size
    
    /**
     * Массив кнопочек, для изи-рисовки
     */
    def buttons(): Array[Button] = Array()
    /**
     * Массив картинок, для изи-рисовки
     */
    def images(): Array[ImageView] = Array()
    /**
     * Массив надписей, для изи-рисовки
     */
    def labels(): Array[TextView] = Array()
    /**
     * Массив полей ввода, тоже для изи-рисовки
     */
    def inputs(): Array[TextView] = Array()
    
    /**
     * А нарисуй-ка мне все, шо есть в наличии
     */
    def render(): Unit = {
        renderer begin()
        
        background()
        buttons foreach(button)
        images foreach(view => renderer image(view rectangle, view image))
        labels foreach(label)
        inputs foreach(input)
        
        renderer end()
    }
        
    /**
     * Нарисовать кнопку
     */
    protected def button(button: Button): Unit = {
        renderer fill(true)
        renderer stroke(false)
        
        val color = button state match {
            case HOVERED => styles buttonHovered
            case PRESSED => styles buttonPressed
            case DEFAULT => styles buttonDefault
        }
                
        renderer fill(color)
        renderer text(styles textColor)
                
        renderer shadow(button rectangle)
        renderer rectangle(button rectangle)
        renderer text(button rectangle, button text, button textSize)
    }
    
    /**
     * Нарисовать поле ввода
     */
    protected def input(input: TextView): Unit = {
        renderer fill(true)
        renderer stroke(true)
        renderer stroke(styles strokeSize)
        
        renderer fill(styles inputBackground)
        renderer stroke(styles linesColor)
        renderer text(styles textColor)
        
        renderer rectangle(input rectangle)
        renderer text(input rectangle, input text, input textSize)
    }
    
    /**
     * Нарисовать сетку игрового поля
     */
    protected def grid(grid: MapGridView): Unit = {
        val rect = grid rectangle
        
        renderer fill(true)
        renderer stroke(true)
        
        renderer fill(styles inputBackground)
        renderer stroke(styles buttonDefault)
        renderer stroke(styles strokeSize)
        
        renderer rectangle(rect)
        
        for(i <- 1 until 10) {            
            var x = rect.x + i * grid.cellSize
            renderer line(x, rect.y, x, rect.y + rect.height)
            
            var y = rect.y + i * grid.cellSize
            renderer line(rect.x, y, rect.x + rect.height, y)
        }
    }
      
    /**
     * Нарисовать надпись
     */
    protected def label(view: TextView): Unit = {
        if (view dark) 
            renderer text(styles darkTextColor)
        else
            renderer text(styles textColor)
        renderer text(view rectangle, view text, view textSize)
    }
    
    /**
     * Нарисовать фон
     */
    protected def background(): Unit = {
        val rect = new Rectangle(0, 0, size width, size height)
        renderer image(rect, styles background)
    }
    
    override def onKeyPress(key: Int): Boolean = false
    
    /**
     * Если юзер нажал клавишу мыши, проходим все кнопки и ищем ту, на которой находится курсор
     * Т.к. клавишу еще зажата, меняем состояние кнопки на НАЖАТАЯ и (возможно) перерисовываем ее
     */
    override def onMouseDown(x: Int, y: Int): Boolean = {
        val point = new Point(x, y)
        val option = buttons find(view => view.rectangle contains(point))
        
        if (!option.isEmpty) {
            option.get state = PRESSED
            return true
        }
        return false
    }
    
    /**
     * Если юзер двинул мышь, проходим все кнопки и ищем ту, на которой находится курсор
     * Ну а потом меняем состояние кнопки на ХОВЕРЕД (не придумал короткого перевода) и (возможно) перерисовываем ее
     */
    override def onMouseMove(x: Int, y: Int): Boolean = {
        val point = new Point(x, y)
        val prevent = buttons find(view => view.state == HOVERED)
        val current = buttons find(view => view.rectangle contains(point)) 
              
        if (!prevent.isEmpty && !current.isEmpty && prevent.get == current.get) {
            return false
        }
        
        if (!prevent.isEmpty) {
            prevent.get state = DEFAULT
            return true
        }
        if (!current.isEmpty) {
            current.get state = HOVERED
            return true
        }
        return false
    }
    
    /**
     * Если юзер отпустил клавишу мыши, проходим все кнопки и ищем ту, на которой находится курсор
     * Ну а потом меняем состояние кнопки на ОБЫЧНАЯ и (возможно) перерисовываем ее + вызываем ее onClick
     * (по сути юзер же кликнул)
     */
    override def onMouseUp(x: Int, y: Int): Boolean = {
        val point = new Point(x, y)
        val option = buttons find(view => view.state == PRESSED)
        
        if (!option.isEmpty) {
            val btn = option.get
            if (btn.rectangle contains(point))
                btn state = HOVERED
            else
                btn state = DEFAULT
                
            return true
        }
        return false
    }
    
    /**
     * Если юзер кликнул (или пальцем ткнул, невежа) то ищем кнопку, на которую он кликнул
     * и вызываем ее onClick
     */
    override def onClick(x: Int, y: Int): Boolean = {
        val point = new Point(x, y)
        val btn = buttons find(view => view.rectangle contains(point))
                
        if (!btn.isEmpty) {
            btn.get.listener onClick()
            return true
        }
        
        val img = images find(view => view.rectangle contains(point))
        if (!img.isEmpty) {
            img.get.listener onClick()
            return true
        }
        
        val txt = labels find(view => view.rectangle contains(point))
        if (!txt.isEmpty) {
            txt.get.listener onClick()
            return true
        }
        
        return false
    }
}