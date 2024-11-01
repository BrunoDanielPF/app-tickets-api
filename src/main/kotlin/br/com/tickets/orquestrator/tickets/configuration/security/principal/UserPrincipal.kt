package br.com.tickets.orquestrator.tickets.configuration.security.principal

import br.com.tickets.orquestrator.tickets.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val email: String,
    val password: String,
    val authorities: Collection<GrantedAuthority>
) {

    companion object {
        fun create(user: User): UserPrincipal {
            val authorities = user.roles.map { role ->
                SimpleGrantedAuthority("ROLE_${role.name}")
            }
            return UserPrincipal(
                email = user.email,
                password = user.password,
                authorities = authorities
            )
        }
    }
}
