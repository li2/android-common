package me.li2.android.commonsample

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.rx.buttonClicks
import me.li2.android.common.rx.checkAndRequestLocationPermission
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(android.R.id.content).setOnClickListener {
            compositeDisposable += checkAndRequestLocationPermission(locationPermissionPrompt(this)).subscribe {
                toast(it.toString())
            }
        }
    }

    private fun locationPermissionPrompt(context: Context): AlertDialog {
        return MaterialAlertDialogBuilder(context)
            .setTitle("\"Demo App\" Would Like to Access the Location")
            .setMessage("This will let you search place, get your current location")
            .setPositiveButton("Yep!", null)
            .setNegativeButton("Nope!", null)
            .create()
    }

    @SuppressLint("CheckResult")
    private fun demoRxDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("title")
            .setMessage("message")
            .setPositiveButton("ok", null)
            .setNegativeButton("cancel", null)
            .setNeutralButton("neutral", null)
            .show()
            .buttonClicks()
            .throttleFirstShort()
            .subscribe { which ->
                when (which) {
                    BUTTON_POSITIVE -> toast("Ok clicked")
                    BUTTON_NEGATIVE -> toast("Cancel clicked")
                    BUTTON_NEUTRAL ->  toast("neutral clicked")
                }
            }
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, "$message at ${System.currentTimeMillis()}", Toast.LENGTH_SHORT).show()
    }

    private fun <T> Observable<T>.throttleFirstShort() = this.throttleFirst(500L, TimeUnit.MILLISECONDS)!!
}
