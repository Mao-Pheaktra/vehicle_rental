package org.example.vehicles_rental.admin.user.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.user.repository.UserDashboardRepository;
import org.springframework.stereotype.Service;
import org.example.vehicles_rental.admin.user.dto.UserStatisticResponse;
import org.example.vehicles_rental.enums.Role;
import org.example.vehicles_rental.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDashboardServeiceImple implements UserDashboardService {
    private final UserDashboardRepository userDashboardRepository;

    @Override
    public UserStatisticResponse getUserStatistics() {

        long totalUsers = userDashboardRepository.count();

        long activeUsers = userDashboardRepository.countActiveUsers();

        long suspendedUsers = userDashboardRepository.countSuspendedUsers();

        long adminUsers = userDashboardRepository.countUsersByRole(Role.ADMIN);

        return UserStatisticResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .suspendedUsers(suspendedUsers)
                .adminUsers(adminUsers)
                .build();
    }
}
