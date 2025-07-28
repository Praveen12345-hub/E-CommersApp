package com.ecom.config;

import com.ecom.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpSession;
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

   
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); // make sure UserService implements UserDetailsService
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() 
        .authenticationProvider(authenticationProvider())  
        .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/registration*", "/js/", "/css/", "/img/", "/webjars/*",
                    "/register/", "/verify", "/forgot-password", "/reset-password", "/login", "/","/register/seller", "/register/buyer"
                ).permitAll()
                .requestMatchers("/seller/").hasAuthority("SELLER")
                .requestMatchers("/buyer/").hasAuthority("BUYER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            HttpSession session = request.getSession();
            String email = authentication.getName();
            session.setAttribute("email", email);

            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SELLER"))) {
                response.sendRedirect("/seller/dashboard");
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("BUYER"))) {
                response.sendRedirect("/buyer/dashboard");
            } else {
                response.sendRedirect("/");
            }
        };
    }
   

}