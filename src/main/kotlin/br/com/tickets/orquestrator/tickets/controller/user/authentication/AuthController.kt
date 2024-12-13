package br.com.tickets.orquestrator.tickets.controller.user.authentication

import br.com.tickets.orquestrator.tickets.controller.user.authentication.dto.UserRequest
import br.com.tickets.orquestrator.tickets.controller.user.authentication.dto.UserValidatedRequest
import br.com.tickets.orquestrator.tickets.domain.entity.user.User
import br.com.tickets.orquestrator.tickets.services.UserService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@RestController
@RequestMapping("/users/auth")
@Validated
class AuthController(private val userService: UserService) {

    @PostMapping("/signin")
    @Operation(summary = "Loga usuário")
    fun authenticateUser(@Valid @RequestBody authenticationDTO: AuthenticationDTO): ResponseEntity<String> {
        return ResponseEntity(userService.authenticateUser(authenticationDTO), HttpStatus.OK)
    }

    @PostMapping("/recover")
    @Operation(summary = "Altera senha de usuário")
    fun changePassword(@RequestBody loginDto: AuthenticationDTO): ResponseEntity<String> {
        return ResponseEntity(userService.changePassword(loginDto), HttpStatus.OK)
    }

    @RequestMapping("/signup", method = [RequestMethod.POST], consumes = ["application/json"])
    @Operation(summary = "Cadastra usuário")
    fun registerUser(@RequestBody userRequest: UserRequest): ResponseEntity<Nothing> {
        return userService.register(userRequest)
    }

    @PostMapping(value = ["{email}"], consumes = ["multipart/form-data"])
    @Operation(summary = "Insere uma imagem em um usuário")
    @Throws(IOException::class)
    fun insertImageOnUser(
        @NotNull @RequestParam(name = "file") file: MultipartFile,
        @PathVariable(name = "email") email: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok(userService.insertImage(email, file))
    }

    @PostMapping("/validated")
    @Operation(summary = "Valida e-mail de usuario")
    fun validatedUserEmail(
        @RequestBody userValidatedRequest: UserValidatedRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.validatedEmailFromUser(userValidatedRequest))
    }

    @PostMapping("/resend/{email}")
    @Operation(summary = "Reenvia e-mail com codigo")
    fun resendEmail(@PathVariable(name = "email") to: String): ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.resendEmailToConfirmAccount(to))
    }
}
