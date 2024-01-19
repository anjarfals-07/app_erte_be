package com.apps.erte.dto.request.user;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private Long pendudukId;
    private String username;
    private String password;
}
