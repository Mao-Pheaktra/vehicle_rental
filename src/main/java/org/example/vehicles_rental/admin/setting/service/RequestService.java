package org.example.vehicles_rental.admin.setting.service;

import org.example.vehicles_rental.admin.setting.dto.request.PasswordChangeRequestUser;
import org.example.vehicles_rental.admin.setting.entity.Request;
import org.example.vehicles_rental.dto.response.ApiResponse;

import java.util.List;

public interface RequestService {
    void createRequest(PasswordChangeRequestUser requestUser);
    List<Request> getPendingRequest();
    void approveRequest(Long requestId);
    void rejectRequest(Long requestId);


}
