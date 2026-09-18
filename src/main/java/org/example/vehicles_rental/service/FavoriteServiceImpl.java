package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.response.FavoriteVehicleResponse;
import org.example.vehicles_rental.entity.Favorite;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.repository.FavoriteRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.example.vehicles_rental.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public Favorite addFavorite(Long userId, Long vehicleId) {

        if (favoriteRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new RuntimeException("Vehicle is already in favorites");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Favorite favorite = Favorite.builder()
                .user(user)
                .vehicle(vehicle)
                .build();

        return favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long vehicleId) {

        if (!favoriteRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new RuntimeException("Vehicle is not in favorites");
        }

        favoriteRepository.deleteByUserIdAndVehicleId(userId, vehicleId);
    }

    @Override
    public boolean isFavorite(Long userId, Long vehicleId) {
        return favoriteRepository.existsByUserIdAndVehicleId(userId, vehicleId);
    }


    @Override
    public List<FavoriteVehicleResponse> getUserFavorites(Long userId) {

        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(favorite -> {

                    Vehicle vehicle = favorite.getVehicle();

                    String brandName = vehicle.getBrand() != null
                            ? vehicle.getBrand().getBrandName()
                            : null;

                    String categoryName = vehicle.getCategory() != null
                            ? vehicle.getCategory().getCategoryName()
                            : null;

                    return new FavoriteVehicleResponse(
                            favorite.getId(),
                            vehicle.getId(),
                            vehicle.getName(),
                            vehicle.getMainImage(),
                            vehicle.getModel(),
                            vehicle.getYear(),
                            vehicle.getTransmission(),
                            vehicle.getFuel_type(),
                            vehicle.getSeat(),
                            vehicle.getPricePerDay(),
                            vehicle.getStatus() != null
                                    ? vehicle.getStatus().name()
                                    : null,
                            brandName,
                            categoryName
                    );
                })
                .toList();
    }
}