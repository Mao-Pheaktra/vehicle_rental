package org.example.vehicles_rental.admin.setting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentSettingsResponse {

    private Long id;

    private boolean cashEnabled;

    private boolean abaKhqrEnabled;

    private boolean cardEnabled;

    private boolean bakongEnabled;

}