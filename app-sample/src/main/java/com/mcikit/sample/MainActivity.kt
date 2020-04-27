package com.mcikit.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mcikit.otpauth.OTPAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OTPAuth("otpauth://totp/XPTO:FOO?issuer=XPTO&algorithm=SHA1&digits=6&period=30&secret=N4SYQORWRZ2TIML5")
            .handlerTimerRemaining {
                Log.i(
                    "MainActivity",
                    "Time Remaining -> ${it.remainingSeconds} - Token -> ${it.token}"
                )
            }
    }
}
