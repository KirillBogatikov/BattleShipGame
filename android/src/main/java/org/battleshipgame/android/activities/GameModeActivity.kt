package org.battleshipgame.android.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.battleshipgame.android.R

class GameModeActivity : StandardActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mode)
    }

    fun singlePlayer(view: View) {
        val intent = Intent(this, GameMapActivity::class.java)
        startActivity(intent)
    }

    fun multiPlayer(view: View) {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        dialogBuilder.setTitle("Мультиплеера нема")
        dialogBuilder.setMessage("Шо, недоволен? А ты возьми и помоги проекту. Модуль мультиплеера есть, но его надобно отладить и баги пофиксить. Кароч, репозиторий найдешь, удачи.")
        dialogBuilder.setNeutralButton("Наорать на разработчика") { _, _ ->
            run {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","kirill15112000@gmail.com", null))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Хочу мультиплеер")
                intent.putExtra(Intent.EXTRA_TEXT, "Сделай быстрее мультиплеер, хочу взрывать кораблики своего соседа.")
                startActivity(intent)
            }
        }
        dialogBuilder.create().show();
    }
}