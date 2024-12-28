package com.example.ProsePetal.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String sessionId;
    @ManyToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "user_Id")
    @NotNull
    private User userId;
    private String activity;
    private String details;
    private LocalDateTime timestamp;
}
