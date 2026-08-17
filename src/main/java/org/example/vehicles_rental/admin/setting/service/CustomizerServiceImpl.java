package org.example.vehicles_rental.admin.setting.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.CustomizerRequest;
import org.example.vehicles_rental.admin.setting.dto.response.CustomizerResponse;
import org.example.vehicles_rental.admin.setting.entity.Customizer;
import org.example.vehicles_rental.admin.setting.repository.CustomizerRepository;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomizerServiceImpl implements CustomizerService {

    private final CustomizerRepository repository;
    private final UserRepository userRepository;

    @Override
    public CustomizerResponse getSettings() {

        Customizer customizer = repository.findById(1L)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Customizer settings not found"
                        )
                );

        return mapToResponse(customizer);
    }

    @Override
    public CustomizerResponse updateSettings(
            CustomizerRequest request) {

        Customizer customizer = repository.findById(1L)
                .orElseGet(Customizer::new);

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Admin not found"
                        )
                );

        customizer.setLogo(request.getLogo());
        customizer.setHeroImage(request.getHeroImage());
        customizer.setTitle(request.getTitle());
        customizer.setDescription(request.getDescription());
        customizer.setButtonText(request.getButtonText());
        customizer.setButtonLink(request.getButtonLink());

        customizer.setUpdatedBy(admin.getId());
        customizer.setUpdatedAt(LocalDateTime.now());

        Customizer saved = repository.save(customizer);

        return mapToResponse(saved);
    }

    private CustomizerResponse mapToResponse(
            Customizer customizer) {

        String updatedByName = null;

        if (customizer.getUpdatedBy() != null) {

            updatedByName = userRepository
                    .findById(customizer.getUpdatedBy())
                    .map(User::getName)
                    .orElse(null);
        }

        return CustomizerResponse.builder()
                .id(customizer.getId())
                .logo(customizer.getLogo())
                .heroImage(customizer.getHeroImage())
                .title(customizer.getTitle())
                .description(customizer.getDescription())
                .buttonText(customizer.getButtonText())
                .buttonLink(customizer.getButtonLink())
                .updatedBy(customizer.getUpdatedBy())
                .updatedByName(updatedByName)
                .updatedAt(customizer.getUpdatedAt())
                .build();
    }
}