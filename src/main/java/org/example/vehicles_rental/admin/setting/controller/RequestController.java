package org.example.vehicles_rental.admin.setting.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.PasswordChangeRequestUser;
import org.example.vehicles_rental.admin.setting.entity.Request;
import org.example.vehicles_rental.admin.setting.service.RequestService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/request_pwd")
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/change")
    public ResponseEntity<?> createRequest(
            @Valid @RequestBody PasswordChangeRequestUser requestUser) {

        requestService.createRequest(requestUser);

        return ResponseEntity.ok(
                Map.of("message", "Password change request submitted")
        );
    }

}

