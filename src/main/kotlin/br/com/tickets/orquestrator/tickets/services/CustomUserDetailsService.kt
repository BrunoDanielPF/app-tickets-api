package br.com.tickets.orquestrator.tickets.services

import br.com.tickets.orquestrator.tickets.exceptions.EmailNotVerifiedException
import br.com.tickets.orquestrator.tickets.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(usernameOrEmail: String): UserDetails {
        val user = userRepository.findByEmail(usernameOrEmail)
            .orElseThrow {
                UsernameNotFoundException("User not found with username or email: $usernameOrEmail")
            }

        val authorities: Set<GrantedAuthority> = user.roles.map { role ->
            SimpleGrantedAuthority(role.name)
        }.toSet()

        val accountNonExpired = true
        val credentialsNonExpired = true
        val accountNonLocked = true
        val enabled = user.emailValidated

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            enabled,
            accountNonExpired,
            credentialsNonExpired,
            accountNonLocked,
            authorities
        )
    }
}
