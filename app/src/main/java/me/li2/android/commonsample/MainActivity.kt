package me.li2.android.commonsample

import android.annotation.SuppressLint
import android.content.DialogInterface.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.li2.android.common.rx.buttonClicks

class MainActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MaterialAlertDialogBuilder(this)
                .setTitle("title")
                .setMessage("message")
                .setPositiveButton("ok", null)
                .setNegativeButton("cancel", null)
                .setNeutralButton("neutral", null)
                .show()
                .buttonClicks()
                .subscribe { which ->
                    when (which) {
                        BUTTON_POSITIVE -> Toast.makeText(this, "Ok clicked", Toast.LENGTH_SHORT).show()
                        BUTTON_NEGATIVE -> Toast.makeText(this, "Cancel clicked", Toast.LENGTH_SHORT).show()
                        BUTTON_NEUTRAL ->  Toast.makeText(this, "neutral clicked", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
