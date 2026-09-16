package org.example.vehicles_rental.admin.category.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.category.dto.CategoryDashboardStatisticsResponse;
import org.example.vehicles_rental.repository.CategoryDashboardRepository;
import org.example.vehicles_rental.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryDashboardServiceImple implements CategoryDashboardService {

    private final CategoryDashboardRepository categoryDashboardRepository;

    @Override
    public CategoryDashboardStatisticsResponse getCategoryStatistics() {

        long totalCategories = categoryDashboardRepository.count();

        long activeCategories =
                categoryDashboardRepository.countActiveCategories();
        long vehicles =
                categoryDashboardRepository.countVehicles();

        return new CategoryDashboardStatisticsResponse(
                totalCategories,
                activeCategories,
                vehicles
        );
    }
}