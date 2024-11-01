package br.com.tickets.orquestrator.tickets.repository

import br.com.tickets.orquestrator.tickets.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    fun findByEmailFetchRoles(@Param("email") email: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
    fun findByName(name: String): Optional<User>
    fun existsByEmail(email: String): Boolean
}