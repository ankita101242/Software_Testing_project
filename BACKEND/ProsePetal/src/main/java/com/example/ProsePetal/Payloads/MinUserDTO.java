package com.example.ProsePetal.Payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinUserDTO {
    private Integer userId;
    private String email;
    private String name;

}