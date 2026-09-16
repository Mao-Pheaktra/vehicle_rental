package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryDashboardRepository extends JpaRepository<Categories, Long> {

    @Query("""
            SELECT COUNT(DISTINCT v.category)
            FROM Vehicle v
            WHERE v.category IS NOT NULL
            """)
    long countActiveCategories();

    @Query("""
            SELECT COUNT(v)
            FROM Vehicle v
            WHERE v.category IS NOT NULL
            """)
    long countVehicles();

}