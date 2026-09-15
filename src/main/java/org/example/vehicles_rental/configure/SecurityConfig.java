package org.example.vehicles_rental.configure;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
        httpSecurity.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/brand/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/brands/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/vehicle/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/vehicle/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/vehicle_image/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/vehicle_image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle_image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle_image/**").hasRole("ADMIN")
                        //
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/payments/bakong/test-qr").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/payments/bakong/test-status").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payments/bakong/scan-qr").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/payments/bakong/scan-status").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payments/bakong/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/bakong/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/booking/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/payments").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/payments/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/paymentMethods/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/paymentMethods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/paymentMethods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/paymentMethods/**").hasRole("ADMIN")
                        // Dashboard
                        .requestMatchers(HttpMethod.GET, "/api/admin/dashboard/**").hasRole("ADMIN")
                        // User
                        .requestMatchers(HttpMethod.GET, "/api/admin/user/**").hasRole("ADMIN")
                        // Vehicle
                        .requestMatchers("/api/admin/vehicle").hasRole("ADMIN")

                        // Category
                        .requestMatchers(HttpMethod.GET, "/api/admin/category").hasRole("ADMIN")

                        // Payment
                        .requestMatchers(HttpMethod.GET, "/api/admin/payment").hasRole("ADMIN")
                        // Rental History
                        .requestMatchers(HttpMethod.GET, "/api/admin/rental_history").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/report").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/general").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/general").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/notification").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/notification").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/security").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/security/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/payment").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/payment").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/admin/notification/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/admin/setting/customizer").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/setting/customizer").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/request_pwd/change").authenticated()

                        .requestMatchers("/api/admin/request/**").hasRole("ADMIN")
                        .requestMatchers("/error/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
