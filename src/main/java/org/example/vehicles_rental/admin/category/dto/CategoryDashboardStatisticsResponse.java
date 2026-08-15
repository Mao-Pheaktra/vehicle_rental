package org.example.vehicles_rental.admin.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDashboardStatisticsResponse {

    private long totalCategories;
    private long activeCategories;
    private long vehicles;
}