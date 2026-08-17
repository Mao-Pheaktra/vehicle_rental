package org.example.vehicles_rental.admin.setting.repository;

import org.example.vehicles_rental.admin.setting.entity.PaymentSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentSettingsRepository
        extends JpaRepository<PaymentSettings, Long> {
}