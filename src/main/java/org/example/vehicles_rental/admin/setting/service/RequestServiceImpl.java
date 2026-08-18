package org.example.vehicles_rental.admin.setting.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.PasswordChangeRequestUser;
import org.example.vehicles_rental.admin.setting.entity.Request;
import org.example.vehicles_rental.admin.setting.entity.SecuritySettings;
import org.example.vehicles_rental.admin.setting.enums.RequestStatus;
import org.example.vehicles_rental.admin.setting.repository.RequestRepository;
import org.example.vehicles_rental.admin.setting.repository.SecuritySettingsRepository;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.exception.IncorrectPassword;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final SecuritySettingsRepository securitySettingsRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void createRequest(PasswordChangeRequestUser requestUser){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(requestUser.getCurrentPassword(), user.getPwd())){
            throw new IncorrectPassword("Current password is incorrect");
        }
        if (!requestUser.getNewPassword().equals(requestUser.getConfirmPassword())){
            throw new IncorrectPassword("New password and confirm password do not match");
        }
        Optional<Request> existing = requestRepository.findByUserIdAndStatus(user.getId(), RequestStatus.PENDING);

        if (existing.isPresent()){
            throw new RuntimeException("you already have a pending request");
        }
        SecuritySettings settings = securitySettingsRepository.findTopByOrderByIdDesc().orElseThrow(()-> new NotFoundException("Security settings not found"));
        Request request = new Request();
        request.setUser(user);
        request.setStatus(RequestStatus.PENDING);
        request.setRequestAt(LocalDateTime.now());

        String newPasswordHash = passwordEncoder.encode(requestUser.getNewPassword());
        if(settings.isAutoApprovePasswordChange()){
            request.setStatus(RequestStatus.APPROVED);
            request.setAutoApproved(true);
            user.setPwd(newPasswordHash);
            userRepository.save(user);

            request.setRequestedPassword(null);
        }else {
            request.setStatus(RequestStatus.PENDING);
            request.setAutoApproved(false);

            request.setRequestedPassword(newPasswordHash);
        }
        requestRepository.save(request);
    }
    @Override
    public List<Request> getPendingRequest(){
        return requestRepository.findByStatus(RequestStatus.PENDING);
    }

    @Override
    public void approveRequest(Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Request request =
                requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException(
                    "Request has already been reviewed"
            );
        }

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        User user = request.getUser();
        user.setPwd(request.getRequestedPassword());
        userRepository.save(user);

        request.setStatus(RequestStatus.APPROVED);
        request.setAutoApproved(false);
        request.setReviewedAt(LocalDateTime.now());
        request.setReviewedBy(admin);

        request.setRequestedPassword(null);

        requestRepository.save(request);
    }
    @Override
    public void rejectRequest(Long requestId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Request request =
                requestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Request not found"
                                ));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException(
                    "Request has already been reviewed"
            );
        }

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Admin not found"
                        ));

        request.setStatus(RequestStatus.REJECTED);
        request.setAutoApproved(false);
        request.setReviewedAt(LocalDateTime.now());
        request.setReviewedBy(admin);

        request.setRequestedPassword(null);

        requestRepository.save(request);
    }

}
