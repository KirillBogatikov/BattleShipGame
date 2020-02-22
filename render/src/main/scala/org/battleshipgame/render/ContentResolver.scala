package org.battleshipgame.render

trait ContentResolver {
    def background(): Image
    def ships(): Array[Image]
    def primaryColor(): Long
    def textColor(): Long
}