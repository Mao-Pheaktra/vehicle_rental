package org.example.vehicles_rental.configure;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(csrf->csrf.disable())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/users/**").hasRole("ADMIN")

//                        .requestMatchers("/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/brands/**").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/brands/**").hasRole("ADMIN")


//                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/categories/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/categories/**").hasRole("ADMIN")

//                                .requestMatchers("/api/vehicle/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/vehicle/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/vehicle/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/vehicle/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/vehicle/**").hasRole("ADMIN")

//                                .requestMatchers("/api/vehicle_image/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/vehicle_image/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/vehicle_image/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/vehicle_image/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/vehicle_image/**").hasRole("ADMIN")
//
                                .requestMatchers(HttpMethod.GET, "/api/bookings/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/bookings/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/bookings/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/bookings/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/payments/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/payments/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/payments/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/payments/**").hasRole("ADMIN")
                        
                                .requestMatchers(HttpMethod.GET, "/api/paymentMethods/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/paymentMethods/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/paymentMethods/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/paymentMethods/**").hasRole("ADMIN")
//                                Dashboard
                                .requestMatchers(HttpMethod.GET, "/api/admin/dashboard/**").hasRole("ADMIN")
                        
                                .requestMatchers("/error/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
