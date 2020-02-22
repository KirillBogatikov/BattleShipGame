package org.battleshipgame.ui

import org.battleshipgame.render.Image

trait ContentResolver {
    def background(): Image
    def ships(): Array[Image]
    def primaryColor(): Long
    def textColor(): Long
}