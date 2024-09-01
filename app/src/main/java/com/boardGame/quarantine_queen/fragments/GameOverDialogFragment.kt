package com.boardGame.quarantine_queen.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boardGame.quarantine_queen.R

class GameOverDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = it.layoutInflater

            val builder = AlertDialog.Builder(it)
            builder.setView(inflater.inflate(R.layout.game_over_view,null))
            /*builder.setMessage("Wow you Found it")
                .setPositiveButton(
                    "ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss();
                    })*/
            builder.create()
        } ?: throw  IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.let { it.onBackPressed() }
    }
}