package com.hyperkani.reminder.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.hyperkani.reminder.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReminderPopupActivity : Activity() {


    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_reminder)

        val window = window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val params = window?.attributes
        params?.gravity = Gravity.TOP
        params?.y = 100
        window?.attributes = params

        scope.launch {
            delay(3000)
            finish()
        }

        val title = intent.getStringExtra("title") ?: "No Title"

        findViewById<TextView>(R.id.popupTitle).text = title
        findViewById<TextView>(R.id.popupMessage).text = getString(R.string.notification_text)

        val rootLayout = findViewById<ConstraintLayout>(R.id.notification_dialog)

        rootLayout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
