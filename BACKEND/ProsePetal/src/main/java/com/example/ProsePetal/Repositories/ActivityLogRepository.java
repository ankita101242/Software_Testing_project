package com.example.ProsePetal.Repositories;

import com.example.ProsePetal.Entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {
    // Query to find logs by userId
    List<ActivityLog> findByUserId_UserId(Integer userId);
}
