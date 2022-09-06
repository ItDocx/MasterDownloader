package com.brainmedia.masterdownloader.Utils

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.brainmedia.masterdownloader.R

class NetworkChangeListener : BroadcastReceiver() {

    val objcommon:Commons = Commons()

    override fun onReceive(context: Context, intent: Intent) {
        if (!objcommon.isconnectedToInternet(context) ) {
            val builder = AlertDialog.Builder(context)
            val layout_Inflater: View =
                LayoutInflater.from(context).inflate(R.layout.check_internet, null)
            builder.setView(layout_Inflater)
            val btn_Retry = layout_Inflater.findViewById<Button>(R.id.btn_retry)
            val dialog = builder.create()
            dialog.show()
            dialog.setCancelable(false)
            dialog.window!!.setGravity(Gravity.CENTER)
            btn_Retry.setOnClickListener {
                dialog.dismiss()
                onReceive(context, intent)
            }
        }
    }
}
