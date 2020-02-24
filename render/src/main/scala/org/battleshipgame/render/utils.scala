package org.battleshipgame.render

import scala.language.postfixOps

object ColorUtils {
    def alpha(color: Long): Int = {
        return (color >> 24) toInt
    }
    
    def red(color: Long): Int = {
        return ((color >> 16) & 0xFF) toInt
    }
    
    def green(color: Long): Int = {
        return ((color >> 8) & 0xFF) toInt
    }
    
    def blue(color: Long): Int = {
        return (color & 0xFF) toInt
    }
    
    def alpha(color: Long, alpha: Int): Long = {
        return argb(alpha, red(color), green(color), blue(color))
    }
    
    def red(color: Long, red: Int): Long = {
        return argb(alpha(color), red, green(color), blue(color))
    }
    
    def green(color: Long, green: Int): Long = {
        return argb(alpha(color), red(color), green, blue(color))
    }
    
    def blue(color: Long, blue: Int): Long = {
        return argb(alpha(color), red(color), green(color), blue)
    }
        
    def argb(alpha: Int, red: Int, green: Int, blue: Int): Long = {
        return (alpha << 24) | (red << 16) | (green << 8) | blue
    }
}