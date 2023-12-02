package com.example.testing

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView

fun showPopUpDialog(context: Context, indexView: Int) {
    val dialog = Dialog(context)
    dialog.setCancelable(true)
    if (indexView === 0){
        dialog.setContentView(R.layout.detail_konsul)
    } else if (indexView === 1){
        dialog.setContentView(R.layout.detail_konsul1)
    } else if (indexView === 2){
        dialog.setContentView(R.layout.detail_konsul2)
    }else if (indexView === 3){
        dialog.setContentView(R.layout.detail_konsul3)
    }

    val bPopUpback = dialog.findViewById(R.id.txtclose) as TextView

    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    bPopUpback.setOnClickListener { dialog.dismiss() }

    dialog.show()
}
