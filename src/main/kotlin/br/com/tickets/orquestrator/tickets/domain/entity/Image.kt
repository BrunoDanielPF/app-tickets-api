package br.com.tickets.orquestrator.tickets.domain.entity

import br.com.tickets.orquestrator.tickets.exceptions.ImageNotCompatibilityException
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.Serializable
import java.time.Instant
import javax.imageio.ImageIO
import java.util.Base64

@Entity
@Table(name = "image")
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JsonProperty("nome")
    var name: String,

    @JsonProperty("altura")
    var height: Int = 0,

    @JsonProperty("largura")
    var width: Int = 0,

    @JsonProperty("dados")
    var data: String,

    @JsonProperty("extensão")
    var extension: String,

    @CreationTimestamp
    @JsonProperty("criado_em")
    val createdOn: Instant? = null,

    @UpdateTimestamp
    @JsonProperty("atualizado_em")
    val lastUpdatedOn: Instant? = null
) : Serializable {

    companion object {
        fun createImage(file: MultipartFile): Image {
            val imageBuffered: BufferedImage
            val imageBase64: String
            try {
                imageBuffered = ImageIO.read(file.inputStream) ?: throw ImageNotCompatibilityException("Erro ao ler dados de imagem, incompativel")
                imageBase64 = Base64.getEncoder().encodeToString(file.bytes)
            } catch (err: Exception) {
                throw ImageNotCompatibilityException("Erro ao ler dados de imagem, incompativel")
            }

            val fileName = file.originalFilename ?: ""
            val name = fileName.substringBeforeLast(".")
            val extension = fileName.substringAfterLast(".", "")

            return Image(
                name = name,
                height = imageBuffered.height,
                width = imageBuffered.width,
                data = imageBase64,
                extension = extension
            )
        }
    }
}
