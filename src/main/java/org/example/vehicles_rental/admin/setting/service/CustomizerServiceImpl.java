package org.example.vehicles_rental.admin.setting.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.CustomizerRequest;
import org.example.vehicles_rental.admin.setting.dto.response.CustomizerResponse;
import org.example.vehicles_rental.admin.setting.entity.Customizer;
import org.example.vehicles_rental.admin.setting.repository.CustomizerRepository;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.repository.UserRepository;
import org.example.vehicles_rental.service.CloudinaryService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomizerServiceImpl implements CustomizerService {

    private final CustomizerRepository repository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public CustomizerResponse getSettings() {
        Customizer customizer = repository.findById(1L)
                .orElseGet(() -> Customizer.builder()
                        .id(1L)
                        .title("Precision Auto")
                        .description("Default description")
                        .buttonText("Explore Fleet")
                        .buttonLink("/vehicles")
                        .build()
                );

        return mapToResponse(customizer);
    }

    @Override
    @Transactional
    public CustomizerResponse updateSettings(CustomizerRequest customizerRequest, MultipartFile logoFile, MultipartFile heroImageFile) {

        Customizer customizer = repository.findById(1L)
                .orElseGet(() -> Customizer.builder().id(1L).build());

        // 1. Resolve Admin User safely
        Long adminId = getCurrentAdminId();

        try {
            // 2. Handle Logo Upload & Update
            if (logoFile != null && !logoFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadCustomizerLogo(logoFile);
                customizer.setLogo(imageUrl);
            } else if (customizerRequest != null) {
                // Allows clearing or updating to string URL
                customizer.setLogo(customizerRequest.getLogo());
            }

            // 3. Handle Hero Image Upload & Update
            if (heroImageFile != null && !heroImageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadCustomizerHeroImage(heroImageFile);
                customizer.setHeroImage(imageUrl);
            } else if (customizerRequest != null) {
                // Allows clearing or updating to string URL
                customizer.setHeroImage(customizerRequest.getHeroImage());
            }

            // 4. Update Text Fields
            if (customizerRequest != null) {
                customizer.setTitle(customizerRequest.getTitle());
                customizer.setDescription(customizerRequest.getDescription());
                customizer.setButtonText(customizerRequest.getButtonText());
                customizer.setButtonLink(customizerRequest.getButtonLink());
            }

            // 5. Audit Metadata
            if (adminId != null) {
                customizer.setUpdatedBy(adminId);
            }
            customizer.setUpdatedAt(LocalDateTime.now());

            Customizer saved = repository.save(customizer);
            return mapToResponse(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary: " + e.getMessage(), e);
        }
    }

    private Long getCurrentAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String principalName = authentication.getName();
            return userRepository.findByEmail(principalName)
                    .map(User::getId)
                    .orElse(null);
        }
        return null;
    }

    private CustomizerResponse mapToResponse(Customizer customizer) {
        String updatedByName = null;

        if (customizer.getUpdatedBy() != null) {
            updatedByName = userRepository.findById(customizer.getUpdatedBy())
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