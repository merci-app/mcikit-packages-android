package com.mcikit.qrimageview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.view_qr_layout.view.*

class QRImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    var code = ""
    set(value) {
        field = value
        updateQR()
    }

    init {
        inflate(context, R.layout.view_qr_layout, this)
    }

    private fun updateQR() {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 200*2, 200*2,
            hashMapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
        )
        qrImage.setImageBitmap(bitmap)
    }

}