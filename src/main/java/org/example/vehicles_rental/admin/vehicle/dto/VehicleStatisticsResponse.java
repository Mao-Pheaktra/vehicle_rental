package org.example.vehicles_rental.admin.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleStatisticsResponse {

    private long totalVehicles;
    private long availableVehicles;
    private long rentedVehicles;
    private long maintenanceVehicles;
}