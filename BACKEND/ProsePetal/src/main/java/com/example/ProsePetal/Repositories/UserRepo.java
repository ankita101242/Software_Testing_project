package com.example.ProsePetal.Repositories;

import com.example.ProsePetal.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    @Override
    Optional<User> findById(Integer userId);
    Optional<User> findByEmail(String email);
    @Query("SELECT u.userId FROM User u WHERE u.email = :userEmail")
    Integer findUserIdByEmail(String userEmail);
}