package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.response.FavoriteVehicleResponse;
import org.example.vehicles_rental.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    Favorite addFavorite(Long userId, Long vehicleId);

    void removeFavorite(Long userId, Long vehicleId);

    boolean isFavorite(Long userId, Long vehicleId);

    List<FavoriteVehicleResponse> getUserFavorites(Long userId);
}