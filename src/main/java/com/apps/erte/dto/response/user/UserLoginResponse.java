package com.apps.erte.dto.response.user;

import com.apps.erte.entity.Penduduk;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String namaLengkap;
    private String telepon;
    private String email;
    private String fotoUrl;
}
