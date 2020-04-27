package com.mcikit.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mcikit.otpauth.OTPAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OTPAuth("<otp-to-test>")
            .handlerTimerRemaining {
                Log.i(
                    "MainActivity",
                    "Time Remaining -> ${it.remainingSeconds} - Token -> ${it.token}"
                )
            }
    }
}
