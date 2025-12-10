package es.dsw.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/loggin","/logginprocess", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/loggin")
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/loggin?logout"));

        return http.build();
    }
    
    @Bean
    InMemoryUserDetailsManager inMemoryUsers() {
        return new InMemoryUserDetailsManager();
    }
}