package com.mcikit.otpauth.exception

data class OTPAuthException(
    override val message: String
) : Exception(message)