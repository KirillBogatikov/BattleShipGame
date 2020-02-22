package org.battleshipgame.render

import scala.language.postfixOps

object ColorUtils {
    def alpha(color: Long): Byte = {
        return (color >> 24) toByte
    }
    
    def red(color: Long): Byte = {
        return ((color >> 16) & 0xFF) toByte
    }
    
    def green(color: Long): Byte = {
        return ((color >> 8) & 0xFF) toByte
    }
    
    def blue(color: Long): Byte = {
        return (color & 0xFF) toByte
    }
    
    def alpha(color: Long, alpha: Byte): Long = {
        return argb(alpha, red(color), green(color), blue(color))
    }
    
    def red(color: Long, red: Byte): Long = {
        return argb(alpha(color), red, green(color), blue(color))
    }
    
    def green(color: Long, green: Byte): Long = {
        return argb(alpha(color), red(color), green, blue(color))
    }
    
    def blue(color: Long, blue: Byte): Long = {
        return argb(alpha(color), red(color), green(color), blue)
    }
        
    def argb(alpha: Byte, red: Byte, green: Byte, blue: Byte): Long = {
        return (alpha << 24) | (red << 16) | (green << 8) | blue
    }
}