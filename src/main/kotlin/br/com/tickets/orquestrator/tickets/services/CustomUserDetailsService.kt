package br.com.tickets.orquestrator.tickets.services

import br.com.tickets.orquestrator.tickets.repository.UserRepository
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

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            authorities
        )
    }
}
