package br.com.tickets.orquestrator.tickets.controller.authentication

import br.com.tickets.orquestrator.tickets.domain.entity.User
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

    @GetMapping
    @Operation(summary = "Recupera usuario com parametro")
    fun getUserWithParam(
        @RequestParam(name = "email", required = false) email: String?,
        @RequestParam(name = "id", required = false) id: Long?,
        @RequestParam(name = "name", required = false) name: String?
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.getUser(id, name, email))
    }
}
