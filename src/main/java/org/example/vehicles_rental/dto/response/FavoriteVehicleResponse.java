package org.example.vehicles_rental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FavoriteVehicleResponse {

    private Long favoriteId;

    private Long vehicleId;

    private String name;

    private String mainImage;

    private String model;

    private Integer year;

    private String transmission;

    private String fuelType;

    private Integer seat;

    private BigDecimal pricePerDay;

    private String status;

    private String brandName;

    private String categoryName;
}