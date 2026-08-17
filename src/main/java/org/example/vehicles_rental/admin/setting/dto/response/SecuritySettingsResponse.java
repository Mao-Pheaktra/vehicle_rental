package org.example.vehicles_rental.admin.setting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecuritySettingsResponse {

    private Long id;

    private boolean twoFactorAuthentication;

    private Integer sessionTimeoutMinutes;
}