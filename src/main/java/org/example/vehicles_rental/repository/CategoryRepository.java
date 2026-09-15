package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Categories,Long> {
    @Query("SELECT c FROM Categories c WHERE LOWER(c.category_name) = LOWER(:categoryName)")
    Optional<Categories> findByCategoryNameIgnoreCase(@Param("categoryName") String categoryName);
}
