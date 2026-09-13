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
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))
                .authorizeHttpRequests(auth -> auth

                        // AUTH - PUBLIC and OAtuh2 with /google/callback
                        .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/telegram/**").permitAll()

                        // USERS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")

                        // BRANDS - USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/brands/**").hasRole("ADMIN")

                        // CATEGORIES - USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")

                        // VEHICLES - USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/vehicle/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle/**").hasRole("ADMIN")

                        // VEHICLE IMAGES - USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/vehicle_image/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/vehicle_image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle_image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle_image/**").hasRole("ADMIN")

                        // BOOKINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN")

                        // BAKONG PAYMENT - USER + ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/payments/bakong/create").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/bakong/*/status").authenticated()


                        // PAYMENTS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/payments/**").hasRole("ADMIN")
                        //Admin
                        .requestMatchers("/api/admin/**").permitAll()
        //              Category
                        .requestMatchers(HttpMethod.GET,"/api/admin/category").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/admin/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/admin/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/admin/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/admin/bookings").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/admin/bookings").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/admin/bookings").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/admin/bookings").hasRole("ADMIN")
                        // PAYMENT METHODS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/paymentMethods/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/paymentMethods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/paymentMethods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/paymentMethods/**").hasRole("ADMIN")

                        // ADMIN DASHBOARD - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/dashboard/**").permitAll()

                        // ADMIN USER - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/user/**").hasRole("ADMIN")

                        // ADMIN VEHICLE - ADMIN
                        .requestMatchers("/api/admin/vehicle").hasRole("ADMIN")

                        // ADMIN CATEGORY - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/category").hasRole("ADMIN")

                        // ADMIN PAYMENT - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/payment").permitAll()

                        // RENTAL HISTORY - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/rental_history").permitAll()

                        // REPORT - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/report").permitAll()

                        // GENERAL SETTINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/general").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/general").permitAll()

                        // NOTIFICATION SETTINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/notification").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/notification").permitAll()

                        // SECURITY SETTINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/security").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/security/**").permitAll()

                        // PAYMENT SETTINGS - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/payment").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/payment").permitAll()

                        // NOTIFICATION - ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/admin/notification/**").permitAll()

                        // CUSTOMIZER - ADMIN
// CUSTOMIZER - ADMIN
                                .requestMatchers(HttpMethod.GET, "/api/admin/setting/customizer", "/api/admin/setting/customizer/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/admin/setting/customizer", "/api/admin/setting/customizer/**").permitAll()

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