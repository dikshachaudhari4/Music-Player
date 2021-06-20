package com.musicplayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.*
import kotlin.system.exitProcess

class DialogFragment:DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView:View=inflater.inflate(R.layout.fragment_custom_dialog, container, false);

        rootView.cancel.setOnClickListener {
            dismiss();
        }
        rootView.exit.setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        return rootView;

    }
}