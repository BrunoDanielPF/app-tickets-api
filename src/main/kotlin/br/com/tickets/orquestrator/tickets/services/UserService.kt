package br.com.tickets.orquestrator.tickets.services

import br.com.tickets.orquestrator.tickets.controller.authentication.AuthenticationDTO
import br.com.tickets.orquestrator.tickets.controller.authentication.dto.UserRequest
import br.com.tickets.orquestrator.tickets.controller.authentication.dto.UserValidatedRequest
import br.com.tickets.orquestrator.tickets.domain.entity.Image
import br.com.tickets.orquestrator.tickets.domain.entity.Role
import br.com.tickets.orquestrator.tickets.domain.entity.User
import br.com.tickets.orquestrator.tickets.exceptions.*
import br.com.tickets.orquestrator.tickets.repository.UserRepository
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import java.time.LocalDateTime
import java.util.regex.Pattern


@Service
class UserService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    val logger: Logger = LoggerFactory.getLogger(UserService::class.simpleName)

    fun getUser(id: Long?, name: String?, email: String?): User {
        email?.let {
            return userRepository.findByEmail(it).orElseThrow { UsernameNotFoundException("User Not found!") }
        }
        id?.let {
            return userRepository.findById(it).orElseThrow { UsernameNotFoundException("User Not found!") }
        }
        name?.let {
            return userRepository.findByName(it).orElseThrow { UsernameNotFoundException("User Not found!") }
        }
        throw RuntimeException("You must specify email, id or name")
    }

    fun insertImage(email: String, file: MultipartFile): String {
        val user = userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException("User not found!") }
        val image = Image.createImage(file)
//        imageRepository.save(image)
        user.image = image
        userRepository.save(user)
        return "Image uploaded successfully!"
    }

    fun register(userRequest: UserRequest): ResponseEntity<Nothing> {
        if (userRequest.roles.isNullOrEmpty()) {
            userRequest.roles = listOf(Role(1, "USER"))
        }
        if (userRepository.existsByEmail(userRequest.email)) {
            throw EmailInUseException("E-mail already exists")
        }
        userRequest.password = passwordEncoder.encode(validatePassword(userRequest.password))

        val user = User(
            email = userRequest.email,
            name = userRequest.name,
            password = userRequest.password,
            roles = userRequest.roles!!,
            emailCode = generateEmailCode(),
            emailValidated = false,
            emailCodeExpiration = generateTimeExpirationCode()
        )
        userRepository.save(
            user
        )

        logger.atInfo().addKeyValue("usuario", user).log("usuario cadastrado")

        try {
            sendEmailToConfirmAccount(user.email, user.name, user.emailCode!!)
        } catch (ioException: IOException) {
            throw RuntimeException("Error validating email for registration, please provide a valid email")
        }
        return ResponseEntity(null, HttpStatus.CREATED)
    }

    fun changePassword(authenticationDTO: AuthenticationDTO): String {
        val user = userRepository.findByEmail(authenticationDTO.email)
            .orElseThrow { NotFoundUserException("User email not found") }
        user.password = passwordEncoder.encode(validatePassword(authenticationDTO.password))
        userRepository.save(user)
        return "Your password has been successfully changed."
    }

    fun authenticateUser(authenticationDTO: AuthenticationDTO): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authenticationDTO.email, authenticationDTO.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
        return "User signed in successfully!"
    }

    fun validatedEmailFromUser(userValidatedRequest: UserValidatedRequest) {
        val user = getUser(email = userValidatedRequest.email, name = null, id = null)
        if (user.emailCode == userValidatedRequest.code && user.emailCodeExpiration!!.isAfter(LocalDateTime.now())) {
            user.emailValidated = true
            user.emailCode = null
            user.emailCodeExpiration = null
            userRepository.save(user)
        } else {
            throw InvalidCodeException("Invalid code")
        }
    }

    fun resendEmailToConfirmAccount(to: String) {
        val user = getUser(id = null, email = to, name = null)
        user.emailCode = generateEmailCode()
        user.emailCodeExpiration = generateTimeExpirationCode()
        sendEmailToConfirmAccount(
            to = user.email,
            name = user.name,
            code = user.emailCode!!
        )
        userRepository.save(user)
    }

    private fun sendEmailToConfirmAccount(to: String, name: String, code: Int) {

        val NAME_DYNAMIC_DATA = "nome";
        val CODE_DYNAMIC_DATA = "codigo";

        val from = Email(System.getenv("ORGANIZATION_EMAIL"))

        val subject = "Confirme o e-mail para concluir o cadastro !"

        val emailToSendConfirmation = Email(to)

        val content = Content("text/html", "descricao")

        val mail = Mail(from, subject, emailToSendConfirmation, content)


        val personalization = Personalization()
        personalization.addTo(emailToSendConfirmation)

        personalization.addDynamicTemplateData(NAME_DYNAMIC_DATA, name)
        personalization.addDynamicTemplateData(CODE_DYNAMIC_DATA, code)


        mail.addPersonalization(personalization)
        mail.setTemplateId(System.getenv("TEMPLATE_ID"));
        val sg = SendGrid(System.getenv("API_KEY_SEND_GRID_EMAIL"))

        val request = Request()

        logger.atInfo().addKeyValue("personalizacao", personalization).log("personalizacao do email para: $to")
        try {
            request.setMethod(Method.POST)
            request.setEndpoint("mail/send")
            request.setBody(mail.build())
            val response = sg.api(request)
            logger.atInfo().addKeyValue("status code", response.statusCode).addKeyValue("body", response.body).log("resposta do email do email")
        } catch (ex: IOException) {
            logger.atError().setMessage(ex.message).log()
        }
    }
    private fun validatePassword(password: String): String {
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$", password)) {
            throw PasswordFormatException()
        }
        return password
    }
    private fun generateEmailCode(): Int {
        return (1000..9999).random()
    }
    private fun generateTimeExpirationCode(): LocalDateTime? {
        return LocalDateTime.now().plusMinutes(15)
    }
}