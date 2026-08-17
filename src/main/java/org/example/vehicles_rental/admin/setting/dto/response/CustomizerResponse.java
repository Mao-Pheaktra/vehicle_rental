package org.example.vehicles_rental.admin.setting.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomizerResponse {
    private Long id;

    private String logo;

    private String heroImage;

    private String title;

    private String description;

    private String buttonText;

    private String buttonLink;

    private Long updatedBy;

    private String updatedByName;

    private LocalDateTime updatedAt;
}
