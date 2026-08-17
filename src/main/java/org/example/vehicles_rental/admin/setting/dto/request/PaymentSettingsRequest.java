package org.example.vehicles_rental.admin.setting.dto.request;

import lombok.Data;
import org.example.vehicles_rental.admin.setting.entity.PaymentSettings;

import java.math.BigDecimal;

@Data
public class PaymentSettingsRequest {

    private boolean cashEnabled;

    private boolean abaKhqrEnabled;

    private boolean cardEnabled;

    private boolean bakongEnabled;
    
}