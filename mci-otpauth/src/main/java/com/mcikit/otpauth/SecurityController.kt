package com.mcikit.otpauth

import com.mcikit.otpauth.extensions.getLong
import com.mcikit.otpauth.extensions.getUint
import com.mcikit.otpauth.extensions.setLong
import com.mcikit.otpauth.util.Base32String
import unsigned.Ulong
import unsigned.toUbyte
import unsigned.toUint
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object SecurityController {

    class SecurityInvalidAlgorithmException: Exception("Invalid algorithm!")
    class SecurityInvalidSecretException: Exception("Invalid Secret!")
    class SecurityGenericException(message: String?) : Exception(message)

    enum class SecurityAlgorithm (val algName: String) {
        SHA_1("sha1"),
        SHA_256 ("sha256"),
        SHA_512 ("sha512");

        fun toHMacAlgName(): String {

            if (this == SHA_1) {
                return "HmacSHA1"
            }
            else if (this == SHA_256) {
                return "HmacSHA256"
            }
            else if (this == SHA_512) {
                return "HmacSHA512"
            }
            else {
                return ""
            }
        }

        companion object {
            fun createFromString(desiredAlgName: String?): SecurityAlgorithm? {

                val allValues = enumValues<SecurityAlgorithm>()
                for (v in allValues) {
                    if (v.algName.toUpperCase(Locale.getDefault()) == desiredAlgName?.toUpperCase(Locale.getDefault())) {
                        return v
                    }
                }

                return null
            }
        }
    }

    class SecurityFactor {
        var period: Double? = 0.0

        constructor(thePeriod: Double) {
            period = thePeriod
        }

        fun getCounter(time: Date): Ulong {
            val timeSinceEpoch = time.time / 1000
            return Ulong(timeSinceEpoch / period!!)
        }
    }


    //calculates a hash based on the provided parameters
    fun hash(time: Date, factor: SecurityFactor, algorithm: SecurityAlgorithm, digits: Int, secret: String?) : String {

        //val time2 = Date(1536672591201)

        if (secret == null) {
            throw SecurityInvalidAlgorithmException()
        }

        val counter = factor.getCounter(time).toLong()

        if (factor.period == null) {
            throw SecurityGenericException("Factor value is null")
        }

        val counterByteArray =  ByteArray(8)
        counterByteArray.setLong(0, counter, false)

        val counterBigEndian = counterByteArray.getLong(0, true)

        val hMacAlg = algorithm.toHMacAlgName()
        if (hMacAlg.isBlank()) {
            throw SecurityGenericException("Invalid hMAC agorithm name")
        }

        val secretDecodedBase32 = Base32String.decode(secret)

        //counter big endian as byte array
        val dataByteArray = ByteArray(8)
        dataByteArray.setLong(0, counterBigEndian, false)

        //here we config our hash generator
        val keySpec = SecretKeySpec(secretDecodedBase32, hMacAlg)
        val mac = Mac.getInstance(hMacAlg)
        mac.init(keySpec)

        //generates the hash
        val hmac = mac.doFinal(dataByteArray)

        //computations to return the final code
        val offset = hmac.last().toUbyte() and 0x0f

        var truncatedHash = hmac.getUint(offset.toInt(), true)
        truncatedHash = truncatedHash and 0x7fffffff
        truncatedHash = (truncatedHash.toInt() % Math.pow(10.0, digits.toDouble()).toInt()).toUint()
        val ret = String.format("%0" + digits + "d", truncatedHash.toInt())
        return ret
    }

}