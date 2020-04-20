package org.battleshipgame.android.activities

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.battleshipgame.android.R
import org.battleshipgame.android.AndroidUtils
import org.battleshipgame.reporter.Report
import org.battleshipgame.reporter.Reporter
import org.cuba.log.Configurator
import org.cuba.log.Log

open class StandardActivity : AppCompatActivity(), DialogInterface.OnClickListener {
    companion object {
        var log: Log? = null
        var reporter: Reporter? = null
    }

    private var handler: Handler? = null
    private var reportDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()

        if (log == null) {
            log = Log(Configurator.system().build())
        }
        if (reporter == null) {
            reporter = Reporter(log, AndroidUtils.getSignature(this)?.toString())
        }
    }

    fun back(view: View) {
        finish()
    }

    fun showReportDialog(view: View) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle(R.string.report_title)
        builder.setView(R.layout.report_dialog)
        builder.setNegativeButton(R.string.report_cancel, this)
        builder.setPositiveButton(R.string.report_apply, this)
        reportDialog = builder.create();
        reportDialog!!.show();
    }

    open fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    open fun getHandler(): Handler {
        return handler!!
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()
        if (which == DialogInterface.BUTTON_POSITIVE) {
            val name = reportDialog?.findViewById<TextView>(R.id.userName)?.text
            val message = reportDialog?.findViewById<TextView>(R.id.message)?.text
            reporter?.report(Report(name?.toString(), AndroidUtils.getIP(log), message?.toString()));
        }
    }
}