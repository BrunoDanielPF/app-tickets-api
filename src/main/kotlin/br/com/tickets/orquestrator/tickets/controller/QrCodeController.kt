package br.com.tickets.orquestrator.tickets.controller

import br.com.tickets.orquestrator.tickets.domain.QRCodeData
import br.com.tickets.orquestrator.tickets.service.DefaultQrCodeComponent
import com.google.zxing.WriterException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@RestController
class QrCodeController(
    val qrCodeComponent: DefaultQrCodeComponent
) {

    val logger: Logger = LoggerFactory.getLogger(QrCodeController::class.java)
    @GetMapping("/generate-qr-code", produces = [MediaType.IMAGE_PNG_VALUE])
    fun generateQRCode(
        @RequestParam userName: String,
        @RequestParam eventName: String,
        @RequestParam enabled: Boolean,
    ): ResponseEntity<ByteArray> {
        return try {
            val qrData = QRCodeData(
                userName = userName,
                eventName = eventName,
                enabled = enabled,
                couponCode = UUID.randomUUID().toString(),
                dateValid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/mm/yyyy"))
            )
            val qrCodeImage = qrCodeComponent.generateQRCodeImage(qrData, 250, 250)
            val headers = HttpHeaders()
            headers.contentType = MediaType.IMAGE_PNG
            logger.atInfo().setMessage("Criando QR code").log()
            ResponseEntity(qrCodeImage, headers, HttpStatus.OK)
        } catch (e: WriterException) {
            logger.atWarn().setMessage("Erro ao criar qrcode").log()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }
}