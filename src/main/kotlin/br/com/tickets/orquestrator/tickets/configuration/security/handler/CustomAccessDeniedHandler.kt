package br.com.tickets.orquestrator.tickets.configuration.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.access.AccessDeniedHandler

class CustomAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: org.springframework.security.access.AccessDeniedException?
    ) {
        response?.status = HttpServletResponse.SC_FORBIDDEN
        response?.contentType = "application/json"
        response?.writer?.write(("{\"error\": \"Access Denied\", \"message\": \"" + accessDeniedException?.message) + "\"}")
    }
}