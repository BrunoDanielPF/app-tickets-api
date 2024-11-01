package br.com.tickets.orquestrator.tickets.services

import br.com.tickets.orquestrator.tickets.controller.authentication.AuthenticationDTO
import br.com.tickets.orquestrator.tickets.controller.authentication.UserRequest
import br.com.tickets.orquestrator.tickets.domain.entity.Image
import br.com.tickets.orquestrator.tickets.domain.entity.Role
import br.com.tickets.orquestrator.tickets.domain.entity.User
import br.com.tickets.orquestrator.tickets.exceptions.NotFoundUserException
import br.com.tickets.orquestrator.tickets.exceptions.PasswordFormatException
import br.com.tickets.orquestrator.tickets.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.regex.Pattern

@Service
class UserService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

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

    fun register(userRequest: UserRequest): String {
        if (userRequest.roles.isNullOrEmpty()) {
            userRequest.roles = listOf(Role(1, "USER"))
        }
        if (userRepository.existsByEmail(userRequest.email)) {
            return "Email in use!"
        }
        userRequest.password = passwordEncoder.encode(validatePassword(userRequest.password))
        try {
//            emailValidation.sendEmail(userRequest.email, userRequest.name)
        } catch (ioException: IOException) {
            throw RuntimeException("Error validating email for registration, please provide a valid email")
        }
        userRepository.save(
            User(
                email = userRequest.email,
                name = userRequest.name,
                password = userRequest.password,
                roles = userRequest.roles!!,
            )
        )
        return "User registered successfully!"
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

    fun validatePassword(password: String): String {
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", password)) {
            throw PasswordFormatException()
        }
        return password
    }
}