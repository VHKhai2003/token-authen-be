package com.example.demoweb.dto.response;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private Date createdAt;
}
