package org.battleshipgame.android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_result.*
import org.battleshipgame.android.GameInstance
import org.battleshipgame.android.R


class ResultActivity : StandardActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GameInstance.get().stop();

        if (intent == null) {
            gotoMainMenu(null)
        }

        setContentView(R.layout.activity_result)

        val win = intent.getBooleanExtra("win", false)
        result.text = if (win) "ВЫ ПОБЕДИЛИ!" else "ВЫ ПРОИГРАЛИ :{"
    }

    fun gotoMainMenu(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}