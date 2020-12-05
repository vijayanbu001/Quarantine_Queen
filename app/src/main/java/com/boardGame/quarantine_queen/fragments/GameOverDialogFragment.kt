package com.boardGame.quarantine_queen.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class GameOverDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Wow you Found it")
                .setPositiveButton(
                    "ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        // FIRE ZE MISSILES!
                        it?.onBackPressed()
                    })
            builder.create()
        } ?: throw  IllegalStateException("Activity cannot be null")
    }
}