package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.UserRequest;
import org.example.vehicles_rental.dto.response.UserResponse;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.enums.Role;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImple implements UserService{
    private final UserRepository userRepository;
    @Override
    public UserResponse create(UserRequest userRequest , MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        String fileUrl = UUID.randomUUID().toString()+"_"+fileName;
        Path path = Paths.get("profileImage");
        String imageUrl = "http://localhost:8080/profileImage/"+fileUrl;
        if (!Files.exists(path)){
            Files.createDirectories(path);
        }
        Files.copy(file.getInputStream(), path.resolve(fileUrl));
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .pwd(userRequest.getPwd())
                .gender(userRequest.getGender())
                .tell(userRequest.getTell())
                .role(Role.CLIENT)
                .isActive(false)
                .profileImage(imageUrl)
                .build();
        user=userRepository.save(user);
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .tell(user.getTell())
                .role(user.getRole())
                .isActive(user.isActive())
                .profileImage(imageUrl)
                .build();
    }
    @Override
    public List<UserResponse> readAll(){
        List<User> users = userRepository.findAll();
        List<UserResponse> responses = new ArrayList<>();
        for (User user:users){
            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .tell(user.getTell())
                    .role(user.getRole())
                    .isActive(user.isActive())
                    .profileImage(user.getProfileImage())
                    .build();
            responses.add(userResponse);
        }
        return responses;
    }
    @Override
    public UserResponse readById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .tell(user.getTell())
                .role(user.getRole())
                .isActive(user.isActive())
                .profileImage(user.getProfileImage())
                .build();
        return userResponse;
    }
    @Override
    public UserResponse delete(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .tell(user.getTell())
                .role(user.getRole())
                .isActive(user.isActive())
                .profileImage(user.getProfileImage())
                .build();
        userRepository.delete(user);
        return userResponse;
    }
    @Override
    public UserResponse update(Long id,UserRequest userRequest, MultipartFile file) throws  IOException{
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String fileUrl = UUID.randomUUID().toString() + "_" + fileName;
            Path path = Paths.get("profileImage");
            String imageUrl = "http://localhost:8080/profileImage/" + fileUrl;
            Files.copy(file.getInputStream(), path.resolve(fileUrl));
            user.setProfileImage(imageUrl);
        }
        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPwd() != null) {
            user.setPwd(userRequest.getPwd());
        }
        if (userRequest.getGender() != null) {
            user.setGender(userRequest.getGender());
        }
        if (userRequest.getTell() != null) {
            user.setTell(userRequest.getTell());
        }


        user=userRepository.save(user);

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .tell(user.getTell())
                .role(user.getRole())
                .isActive(user.isActive())
                .profileImage(user.getProfileImage())
                .build();
        return userResponse;
    }

}
