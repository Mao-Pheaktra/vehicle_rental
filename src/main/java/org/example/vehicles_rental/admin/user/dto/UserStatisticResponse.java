package org.example.vehicles_rental.admin.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticResponse {

    private long totalUsers;
    private long activeUsers;
    private long suspendedUsers;
    private long adminUsers;
}