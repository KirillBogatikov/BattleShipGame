package org.battleshipgame.android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import org.battleshipgame.android.GameInstance
import org.battleshipgame.android.R

class MainActivity : StandardActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GameInstance.create()
    }

    fun startNewGame(view: View) {
        val intent = Intent(this, GameModeActivity::class.java)
        startActivity(intent)
    }

    fun exit(view: View?) {
        finish()
    }
}