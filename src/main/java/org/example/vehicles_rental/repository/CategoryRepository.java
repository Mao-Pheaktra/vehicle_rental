package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories,Long> {
}
