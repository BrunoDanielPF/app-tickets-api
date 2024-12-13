package br.com.tickets.orquestrator.tickets.configuration.security

import br.com.tickets.orquestrator.tickets.configuration.security.principal.UserPrincipal
import br.com.tickets.orquestrator.tickets.domain.entity.user.User
import br.com.tickets.orquestrator.tickets.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.Base64

@Component
class CustomBasicAuthenticationFilter(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : OncePerRequestFilter() {

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BASIC = "Basic "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isBasicAuthentication(request)) {
            val credentials = decodeBase64(getHeader(request)?.replace(BASIC, "") ?: "").split(":")
            val username = credentials[0]
            val password = credentials[1]

            val userOptional = userRepository.findByEmailFetchRoles(username)

            if (userOptional.isEmpty) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("User does not exist!")
                return
            }

            val user = userOptional.get()
            val valid = checkPassword(user.password, password)

            if (!valid) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Password not match!")
                return
            }

            setAuthentication(user)
        }
        filterChain.doFilter(request, response)
    }

    private fun setAuthentication(user: User) {
        val authentication = createAuthenticationToken(user)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun createAuthenticationToken(user: User): Authentication {
        val userPrincipal = UserPrincipal.create(user)
        return UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.authorities)
    }

    private fun checkPassword(userPassword: String, loginPassword: String): Boolean {
        return passwordEncoder.matches(loginPassword, userPassword)
    }

    private fun decodeBase64(base64: String): String {
        val decodeBytes = Base64.getDecoder().decode(base64)
        return String(decodeBytes)
    }

    private fun isBasicAuthentication(request: HttpServletRequest): Boolean {
        val header = getHeader(request)
        return header != null && header.startsWith(BASIC)
    }

    private fun getHeader(request: HttpServletRequest): String? {
        return request.getHeader(AUTHORIZATION)
    }
}
