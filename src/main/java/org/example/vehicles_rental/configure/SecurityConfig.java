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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))
                .authorizeHttpRequests(auth -> auth

                        // AUTH - PUBLIC
                        .requestMatchers("/api/auth/**").permitAll()

                        // USERS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")

                        // BRANDS - USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/brands/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/brands/**").hasRole("ADMIN")

                        // CATEGORIES - USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")

                        // VEHICLES - USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/vehicle/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle/**").hasRole("ADMIN")

                        // VEHICLE IMAGES - USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/vehicle_image/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/vehicle_image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle_image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle_image/**").hasRole("ADMIN")

                        // BOOKINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN")

                        // BAKONG PAYMENT - USER + ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/payments/bakong/create").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/bakong/*/status").authenticated()

                        // PAYMENTS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/payments/**").hasRole("ADMIN")

                        // PAYMENT METHODS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/paymentMethods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/paymentMethods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/paymentMethods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/paymentMethods/**").hasRole("ADMIN")

                        // ADMIN DASHBOARD - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/dashboard/**").hasRole("ADMIN")

                        // ADMIN USER - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/user/**").hasRole("ADMIN")

                        // ADMIN VEHICLE - ADMIN
                        .requestMatchers("/api/admin/vehicle").hasRole("ADMIN")

                        // ADMIN CATEGORY - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/category").hasRole("ADMIN")

                        // ADMIN PAYMENT - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/payment").hasRole("ADMIN")

                        // RENTAL HISTORY - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/rental_history").hasRole("ADMIN")

                        // REPORT - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/report").hasRole("ADMIN")

                        // GENERAL SETTINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/general").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/general").hasRole("ADMIN")

                        // NOTIFICATION SETTINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/notification").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/notification").hasRole("ADMIN")

                        // SECURITY SETTINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/security").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/security/**").hasRole("ADMIN")

                        // PAYMENT SETTINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/payment").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/payment").hasRole("ADMIN")

                        // NOTIFICATION - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/notification/**").hasRole("ADMIN")

                        // CUSTOMIZER - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/customizer").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/customizer").hasRole("ADMIN")

                        // CHANGE PASSWORD - USER + ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/request_pwd/change").authenticated()

                        // ADMIN REQUEST - ADMIN
                        .requestMatchers("/api/admin/request/**").hasRole("ADMIN")

                        // ERROR - PUBLIC
                        .requestMatchers("/error/**").permitAll()

                        // OTHER API - USER + ADMIN
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}