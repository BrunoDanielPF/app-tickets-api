package br.com.tickets.orquestrator.tickets.configuration.security

import br.com.tickets.orquestrator.tickets.configuration.security.handler.CustomAccessDeniedHandler
import br.com.tickets.orquestrator.tickets.configuration.security.handler.CustomAuthenticationHandlerEntryPoint
import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

//    @Bean
    fun customAuthenticationHandlerEntrypoint(): AuthenticationEntryPoint {
        return CustomAuthenticationHandlerEntryPoint()
    }

//    @Bean
    fun customAccessDeniedHandler(): CustomAccessDeniedHandler {
        return CustomAccessDeniedHandler()
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        http: HttpSecurity,
        customBasicAuthenticationFilter: CustomBasicAuthenticationFilter
    ): SecurityFilterChain {
        http.csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auths ->
                auths
                    .requestMatchers("/users/**", "/h2-console/**").permitAll()
                    .requestMatchers("/swagger-ui.html", "/v2/api-docs", "/v3/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**").permitAll()
                    .requestMatchers(HttpMethod.DELETE).hasRole("USER_ADMIN")
                    .requestMatchers(HttpMethod.POST).hasRole("USER_ADMIN")
                    .requestMatchers(HttpMethod.PUT).hasRole("USER_ADMIN")
                    .requestMatchers(HttpMethod.GET).permitAll()
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
            }
            .headers { headersConfigures -> headersConfigures.frameOptions { it.sameOrigin() } }
            .exceptionHandling {
                it.authenticationEntryPoint(customAuthenticationHandlerEntrypoint())
                it.accessDeniedHandler(customAccessDeniedHandler())
            }
            .addFilterBefore(customBasicAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
