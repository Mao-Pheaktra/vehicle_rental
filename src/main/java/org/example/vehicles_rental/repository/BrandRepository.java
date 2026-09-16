package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    @Query("SELECT b FROM Brand b WHERE LOWER(b.brand_name) = LOWER(:brandName)")
    Optional<Brand> findByBrandNameIgnoreCase(@Param("brandName") String brandName);
}
