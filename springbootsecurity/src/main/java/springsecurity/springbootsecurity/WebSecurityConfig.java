package springsecurity.springbootsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.POST, "/login").permitAll()

                // ADMIN tem acesso a tudo
                .requestMatchers("/admin", "/managers", "/user").hasRole("ADMIN")

                // MANAGERS acessa /managers e /user
                .requestMatchers("/managers").hasRole("MANAGERS")
                .requestMatchers("/user").hasAnyRole("USER", "MANAGERS")

                .anyRequest().authenticated()
        ).formLogin(form -> form
                .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login?logout")
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.builder()
                .username("user")
                .password("{noop}user123")
                .roles("USER")
                .build();

        var manager = User.builder()
                .username("manager")
                .password("{noop}manager123")
                .roles("MANAGERS")
                .build();

        var admin = User.builder()
                .username("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, manager, admin);
    }
}