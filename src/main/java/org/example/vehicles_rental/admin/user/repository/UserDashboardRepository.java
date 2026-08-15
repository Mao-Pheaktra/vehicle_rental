package org.example.vehicles_rental.admin.user.repository;

import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface UserDashboardRepository extends JpaRepository<User, Long> {

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = false")
    long countSuspendedUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countUsersByRole(@Param("role") Role role);
}