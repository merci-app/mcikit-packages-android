package com.mcikit.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mcikit.otpauth.OTPAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OTPAuth("otpauth://totp/Merci:5ca00106-2859-4e7c-a4ba-c95401982d29?algorithm=SHA1&digits=6&issuer=Merci&period=30&secret=TB5XCI7Q3QCW2KQENJD2LKDFLSRB3R6L")
            .handlerTimerRemaining {
                Log.i(
                    "MainActivity",
                    "Time Remaining -> ${it.remainingSeconds} - Token -> ${it.token}"
                )
            }
    }
}
