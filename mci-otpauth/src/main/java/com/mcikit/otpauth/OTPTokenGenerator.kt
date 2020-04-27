package com.mcikit.otpauth

import android.util.Log
import com.mcikit.otpauth.exception.OTPAuthException
import com.mcikit.otpauth.model.OTPToken
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

class OTPTokenGenerator(private val url: String) {

    private var period: Double = 0.0

    private var algorithm: SecurityController.SecurityAlgorithm = SecurityController.SecurityAlgorithm.SHA_1

    private var factor: SecurityController.SecurityFactor? = null

    private var digits: Int = 0

    private var secret: String? = null

    private var lastToken: String = ""

    private var expirationDate: Date = Date()

    //returns the generated password
    fun getToken(): OTPToken {
        if (secret == null) {
            generate()
        }

        val correctedDate = getCorrectedDate()
        // Check if last password is still valid
        if (lastToken.isNotEmpty() && expirationDate.time - 1000 > correctedDate.time) {
            return OTPToken(lastToken, getRemainingSeconds())
        }

        // Calculates a new password
        val newToken =
            SecurityController.hash(correctedDate, factor!!, algorithm, digits, secret)

        if (newToken != lastToken) {
            lastToken = newToken

            // Re-calculates the expiration date
            calcExpirationDate(correctedDate)
        }

        return OTPToken(lastToken, getRemainingSeconds())
    }

    //calculates the expiration date
    private fun calcExpirationDate(correctedDate: Date) {
        if (secret == null) {
            generate()
        }

        var futureDate: Date? = null
        val otpToken = getToken()

        for (i in 0..period.toInt()) {
            val date = Date(correctedDate.time + i * 1000)
            try {
                val futureToken =
                    SecurityController.hash(date, factor!!, algorithm, digits, secret)
                if (futureToken != otpToken.token) {
                    futureDate = date
                    break
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString(), e)
            }
        }

        if (futureDate == null) {
            throw OTPAuthException("Invalid Expiration Date")
        }

        expirationDate = futureDate

    }

    private fun getCorrectedDate() = Date()

    fun getRemainingSeconds(): Int {
        val correctedDate = getCorrectedDate()
        return ((expirationDate.time - correctedDate.time) / 1000.0).toInt()
    }

    //generates the algorithms data
    //this first parses the url to get the hash algorithm parameters
    private fun generate() {

        //1. checks for valid url scheme
        if (!url.startsWith("otpauth", 0)) {
            throw OTPAuthException("Invalid URL Scheme - $url")
        }

        //2. now lets get the query parameters
        //android hack: we need to replace the otpauth protocol otherwise we get a MalformedURLException
        val theURL = URL(url.replace("otpauth", "http"))
        val queryParams = HashMap<String, String>()

        val queryPairs = theURL.query.split("&")
        for (pair in queryPairs) {
            val keyValuePair = pair.split("=")
            if (keyValuePair.size == 2) {
                queryParams[keyValuePair[0]] = keyValuePair[1]
            }
        }

        if (queryParams.isEmpty()) {
            throw OTPAuthException("Invalid URL Scheme - $url")
        }

        try {

            period = parseTimerPeriod(queryParams["period"]) ?: 30.0
            algorithm = parseAlgorithm(queryParams["algorithm"])
                ?: SecurityController.SecurityAlgorithm.SHA_1
            digits = parseDigits(queryParams["digits"]) ?: 6
            secret = parseSecret(queryParams["secret"])

        } catch (e: OTPAuthException) {
            throw OTPAuthException("Error parsing url: " + e.message)
        }

        try {
            validatePeriod(period)
            validateDigits(digits)
            factor = SecurityController.SecurityFactor(period)
        } catch (e: OTPAuthException) {
            throw OTPAuthException("Validation error: " + e.message)
        }

    }

    private fun parseTimerPeriod(rawValue: String?): Double? {
        return rawValue?.toDoubleOrNull()
    }

    private fun parseAlgorithm(rawValue: String?): SecurityController.SecurityAlgorithm? {
        val alg = SecurityController.SecurityAlgorithm.createFromString(rawValue)
        if (alg != null) {
            return alg
        } else {
            throw OTPAuthException("Invalid algorithm")
        }
    }

    private fun parseDigits(rawValue: String?): Int? {
        return rawValue?.toIntOrNull()
    }

    private fun parseSecret(rawValue: String?): String? {
        if (rawValue == null) {
            throw OTPAuthException("Invalid rawValue")
        }

        return rawValue
    }

    private fun validateDigits(digits: Int) {
        if (digits < 4 || digits > 8) {
            throw OTPAuthException("Invalid Digits")
        }
    }

    private fun validatePeriod(period: Double) {
        if (period <= 0) {
            throw  OTPAuthException("Invalid Period")
        }
    }

    companion object {
        const val TAG = "OTPTokenGenerator"
    }

}