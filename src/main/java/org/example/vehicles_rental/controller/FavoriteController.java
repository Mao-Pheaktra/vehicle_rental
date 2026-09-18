package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.response.FavoriteVehicleResponse;
import org.example.vehicles_rental.entity.Favorite;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.service.FavoriteService;
import org.example.vehicles_rental.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final ProfileService profileService;

    @PostMapping("/{vehicleId}")
    public ResponseEntity<Favorite> addFavorite(
            Authentication authentication,
            @PathVariable Long vehicleId
    ) {
        User user = profileService.findByEmail(authentication.getName());

        Favorite favorite = favoriteService.addFavorite(
                user.getId(),
                vehicleId
        );

        return ResponseEntity.ok(favorite);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<String> removeFavorite(
            Authentication authentication,
            @PathVariable Long vehicleId
    ) {
        User user = profileService.findByEmail(authentication.getName());

        favoriteService.removeFavorite(
                user.getId(),
                vehicleId
        );

        return ResponseEntity.ok("Vehicle removed from favorites");
    }

    @GetMapping("/check/{vehicleId}")
    public ResponseEntity<Boolean> checkFavorite(
            Authentication authentication,
            @PathVariable Long vehicleId
    ) {
        User user = profileService.findByEmail(authentication.getName());

        boolean favorite = favoriteService.isFavorite(
                user.getId(),
                vehicleId
        );

        return ResponseEntity.ok(favorite);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteVehicleResponse>> getUserFavorites(
            Authentication authentication
    ) {
        User user = profileService.findByEmail(authentication.getName());

        return ResponseEntity.ok(
                favoriteService.getUserFavorites(user.getId())
        );
    }
}