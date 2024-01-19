package com.apps.erte.dto.response.user;

import com.apps.erte.entity.Penduduk;
import lombok.Data;

@Data
public class UserRegisterResponse {
    private Long id;
    private String username;
    private String role;
    private Penduduk penduduk;
}
