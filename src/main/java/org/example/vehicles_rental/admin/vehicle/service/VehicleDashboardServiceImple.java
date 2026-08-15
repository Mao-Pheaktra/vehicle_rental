package org.example.vehicles_rental.admin.vehicle.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.vehicle.dto.VehicleStatisticsResponse;
import org.example.vehicles_rental.admin.vehicle.repository.VehicleDashboardRepository;
import org.example.vehicles_rental.enums.Status;
import org.example.vehicles_rental.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleDashboardServiceImple implements VehicleDashboardService {

    private final VehicleDashboardRepository vehicleDashboardRepository;

    @Override
    public VehicleStatisticsResponse getVehicleStatistics() {

        long totalVehicles = vehicleDashboardRepository.count();

        long availableVehicles =
                vehicleDashboardRepository.countByStatus(Status.AVAILABLE);

        long rentedVehicles =
                vehicleDashboardRepository.countByStatus(Status.RENTED);

        long maintenanceVehicles =
                vehicleDashboardRepository.countByStatus(Status.MAINTENANCE);

        return VehicleStatisticsResponse.builder()
                .totalVehicles(totalVehicles)
                .availableVehicles(availableVehicles)
                .rentedVehicles(rentedVehicles)
                .maintenanceVehicles(maintenanceVehicles)
                .build();
    }
}