package com.example.ProsePetal.Impl;

import com.example.ProsePetal.Payloads.UserDTO;
import com.example.ProsePetal.Repositories.UserRepo;
import com.example.ProsePetal.Services.UserService;
import com.example.ProsePetal.Entity.User;
import com.example.ProsePetal.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDTO getProfileDetails(Integer userId) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return convertToDTO(user);
    }

    @Override
    public void deleteUser(Integer userId) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        userRepo.delete(user);
    }

    @Override
    public UserDTO updateUser(Integer userId, UserDTO userDTO) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAbout(userDTO.getAbout());

        User updatedUser = userRepo.save(user);
        return convertToDTO(updatedUser);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAbout(user.getAbout());
        return userDTO;
    }
}
