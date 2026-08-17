package org.example.vehicles_rental.admin.setting.dto.request;

import lombok.Data;

@Data
public class SecuritySettingsRequest {

    private boolean twoFactorAuthentication;

    private Integer sessionTimeoutMinutes;
}