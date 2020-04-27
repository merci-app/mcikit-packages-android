package com.mcikit.otpauth

import android.os.Handler
import com.mcikit.otpauth.model.OTPToken

class OTPAuth(private val url: String) {

    private val generator: OTPTokenGenerator by lazy {
        OTPTokenGenerator(url)
    }

    private var timerCallback: ((token: OTPToken) -> Unit)? = null

    private val handler = Handler()
    private val timerRunnable = TimerRunnable()

    fun currentToken() = generator.getToken()

    fun remainingSeconds() = generator.getRemainingSeconds()

    fun handlerTimerRemaining(callback: (token: OTPToken) -> Unit) {
        this.timerCallback = callback
        handler.post(timerRunnable)
    }

    fun stopHandlerTimerRemaining() {
        this.timerCallback = null
        handler.removeCallbacks(timerRunnable)
    }

    inner class TimerRunnable : Runnable {
        override fun run() {
            timerCallback?.invoke(generator.getToken())
            handler.postDelayed(timerRunnable, 1000L)
        }
    }
}