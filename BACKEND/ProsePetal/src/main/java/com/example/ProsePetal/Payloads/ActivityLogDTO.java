package com.example.ProsePetal.Payloads;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActivityLogDTO {
    private String sessionId;
    private Integer userId;
    private String activity;
    private String details;
    private LocalDateTime timestamp;
}
