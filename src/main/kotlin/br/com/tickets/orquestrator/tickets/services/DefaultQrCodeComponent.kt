package br.com.tickets.orquestrator.tickets.services

import br.com.tickets.orquestrator.tickets.domain.QRCodeData
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class DefaultQrCodeComponent {

    fun generateQRCodeImage(data: QRCodeData, width: Int, height: Int): ByteArray {
        val mapper = jacksonObjectMapper()
        val jsonData = mapper.writeValueAsString(data)

        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(jsonData, BarcodeFormat.QR_CODE, width, height)

        ByteArrayOutputStream().use { bas ->
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bas)
            return bas.toByteArray()
        }
    }
}