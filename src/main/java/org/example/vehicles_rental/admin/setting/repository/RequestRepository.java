package org.example.vehicles_rental.admin.setting.repository;

import org.example.vehicles_rental.admin.setting.entity.Request;
import org.example.vehicles_rental.admin.setting.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByStatus(RequestStatus status);

    Optional<Request> findByUserIdAndStatus(Long userId, RequestStatus status);
}
