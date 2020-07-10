package com.arudo.githubconsumer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arudo.githubconsumer.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val NOTIFICATION_SWITCH_PREFERENCES = "Notification Switch Preferences"
        const val NOTIFICATION_SWITCH_DATA = "Notification Switch Data"
    }

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences =
            getSharedPreferences(NOTIFICATION_SWITCH_PREFERENCES, Context.MODE_PRIVATE)
        notificationSwitch.isChecked = sharedPreferences.getBoolean(NOTIFICATION_SWITCH_DATA, false)
        alarmReceiver = AlarmReceiver()
        changeLocalization.setOnClickListener(this)
        notificationSwitch.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.changeLocalization -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.notificationSwitch -> {
                if (notificationSwitch.isChecked) {
                    showToastMessage("Notification Started")
                    alarmReceiver.startNotification(this)
                } else {
                    showToastMessage("Notification Canceled")
                    alarmReceiver.cancelNotification(this)
                }
                sharedPreferences =
                    getSharedPreferences(NOTIFICATION_SWITCH_PREFERENCES, Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putBoolean(NOTIFICATION_SWITCH_DATA, notificationSwitch.isChecked).apply()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}