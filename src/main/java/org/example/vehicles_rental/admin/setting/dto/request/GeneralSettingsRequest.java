package org.example.vehicles_rental.admin.setting.dto.request;

import lombok.Data;

@Data
public class GeneralSettingsRequest {
    private String businessName;

    private String contactEmail;

    private String phoneNumber;

    private String address;

    private String currency;

    private String timezone;
}
