package com.mcikit.otpauth.model

data class OTPToken(
    val token: String,
    val remainingSeconds: Int
)