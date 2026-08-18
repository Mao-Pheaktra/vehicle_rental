package org.example.vehicles_rental.admin.setting.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.entity.Request;
import org.example.vehicles_rental.admin.setting.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/request")
public class AdminController {
    private final RequestService requestService;
    @GetMapping
    public List<Request> getPendingRequest() {

        return requestService.getPendingRequest();
    }
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {

        requestService.approveRequest(id);
        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password change request approved"
                )
        );
    }
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(
            @PathVariable Long id) {

        requestService.rejectRequest(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password change request rejected"
                )
        );
    }
}


