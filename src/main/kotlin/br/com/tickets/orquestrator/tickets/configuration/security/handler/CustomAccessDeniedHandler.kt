package br.com.tickets.orquestrator.tickets.configuration.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAccessDeniedHandler: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        response?.writer?.print("Access Denied, verify you is authenticate for this request")
    }
}