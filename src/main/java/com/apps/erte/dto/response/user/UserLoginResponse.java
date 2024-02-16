package com.apps.erte.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
    private Long id;
    private String username;
    private String role;
    private String token;
    private long expiresInMs;
    private Long pendudukId;
    private String namaLengkap;
    private String telepon;
    private String email;
    private String fotoUrl;
}
