package org.example.vehicles_rental.admin.setting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomizerRequest {
    private String logo;

    private String websiteName;

    private String heroImage;

    private String title;

    private String description;

    private String buttonText;

    private String buttonLink;
}
