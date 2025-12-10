package es.dsw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.dsw.jwtservices.JwtAuthFilter;

/**************************************************************************
 *                 CONFIGURACIÓN DEL MÓDULO DE SEGURIDAD                  *
 * ************************************************************************
 * Define los filtros a los mapeos de las apis (control de acceso) pero   * 
 * delega la verificación del acceso a cada mapeo al token JWT.           *                              *
 * ************************************************************************/
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                		//El único mapeo con acceso a todos los clientes para obtener el token.
                        .requestMatchers("/auth/login").permitAll() 
                        .anyRequest().authenticated()
                )
                //Se delega en jwtAuthFilter la verificación de la autenticación de las peticiones.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) 
                .build();
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}