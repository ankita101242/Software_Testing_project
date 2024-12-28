package com.example.ProsePetal.Services;

import com.example.ProsePetal.Entity.ActivityLog;
import com.example.ProsePetal.Payloads.ActivityLogDTO;
import com.example.ProsePetal.Repositories.ActivityLogRepository;
import com.example.ProsePetal.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepo userRepository;

    // Save a single log
    public void saveLog(ActivityLogDTO logDTO) {
        ActivityLog activityLog = mapToEntity(logDTO);
        activityLogRepository.save(activityLog);
    }

    // Save multiple logs
    public void saveAllLogs(List<ActivityLogDTO> logDTOs) {
        List<ActivityLog> activityLogs = logDTOs.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        activityLogRepository.saveAll(activityLogs);
    }

    // Get logs by userId
    public List<ActivityLogDTO> getLogsByUserId(Integer userId) {
        List<ActivityLog> logs = activityLogRepository.findByUserId_UserId(userId);
        return logs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Helper method: Convert DTO to Entity
    private ActivityLog mapToEntity(ActivityLogDTO logDTO) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setSessionId(logDTO.getSessionId());
        activityLog.setUserId(userRepository.findById(logDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID")));
        activityLog.setActivity(logDTO.getActivity());
        activityLog.setDetails(logDTO.getDetails());
        activityLog.setTimestamp(LocalDateTime.now());
        return activityLog;
    }

    // Helper method: Convert Entity to DTO
    private ActivityLogDTO mapToDTO(ActivityLog activityLog) {
        ActivityLogDTO dto = new ActivityLogDTO();
        dto.setSessionId(activityLog.getSessionId());
        dto.setUserId(activityLog.getUserId().getUserId());
        dto.setActivity(activityLog.getActivity());
        dto.setDetails(activityLog.getDetails());
        dto.setTimestamp(activityLog.getTimestamp());
        return dto;
    }
}
