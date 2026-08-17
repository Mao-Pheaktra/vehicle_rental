package org.example.vehicles_rental.admin.setting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneralSettingsResponse {
    private Long id;

    private String businessName;

    private String contactEmail;

    private String phoneNumber;

    private String address;

    private String currency;

    private String timezone;
}
