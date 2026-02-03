package no.stunor.origo.organiserapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
open class SecurityConfig {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    // Allow public access to actuator health endpoint
                    .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                    // Allow public access to test ping endpoint
                    .requestMatchers("/public/**").permitAll()
                    // Allow public access to API documentation
                    .requestMatchers("/api-docs", "/api-docs/**", "/documentation.html", "/swagger-ui/**").permitAll()
                    // All other endpoints require authentication
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { }
            }

        return http.build()
    }
}

