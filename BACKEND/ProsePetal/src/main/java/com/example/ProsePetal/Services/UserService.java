package com.example.ProsePetal.Services;

import com.example.ProsePetal.Payloads.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDTO getProfileDetails(Integer userId) throws Exception;

    void deleteUser(Integer userId) throws Exception;
    UserDTO updateUser(Integer userId, UserDTO userDTO) throws Exception;

}
