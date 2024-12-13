package br.com.tickets.orquestrator.tickets.services.feign.interceptor

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class RequestFeignInterceptor(val environment: Environment): RequestInterceptor {

    override fun apply(p0: RequestTemplate?) {
        if (p0 != null) {
            val accessToken = environment.getProperty("ACCESS_TOKEN_ASAAS")
            p0.header("access_token", accessToken)
        }
    }
}