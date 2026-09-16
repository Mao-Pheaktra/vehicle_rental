package org.example.vehicles_rental.configure;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.entity.SecuritySettings;
import org.example.vehicles_rental.admin.setting.repository.SecuritySettingsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final SecuritySettingsRepository securitySettingsRepository;

    @Bean
    CommandLineRunner initSecuritySettings() {
        return args -> {

            if (securitySettingsRepository.count() == 0) {

                SecuritySettings settings = new SecuritySettings();

                settings.setTwoFactorAuthentication(false);
                settings.setSessionTimeoutMinutes(30);

                securitySettingsRepository.save(settings);
            }
        };
    }
}