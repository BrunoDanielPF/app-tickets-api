package br.com.tickets.orquestrator.tickets.configuration.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationHandlerEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response?.setContentType("application/json");
        response?.getWriter()?.write("{\"error\": \"Unauthorized\", \"message\": \"" + authException?.message + "\"}");
    }
}