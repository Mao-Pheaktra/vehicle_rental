package org.example.vehicles_rental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.vehicles_rental.entity.Vehicle;

@Data
@AllArgsConstructor
public class FavoriteResponse {

    private Long favoriteId;

    private Vehicle vehicle;
}