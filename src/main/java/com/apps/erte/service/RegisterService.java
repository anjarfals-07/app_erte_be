package com.apps.erte.service;

import com.apps.erte.dto.request.user.UserRegisterRequest;
import com.apps.erte.dto.response.user.UserRegisterResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.user.User;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegisterService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    PendudukRepository pendudukRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;

    public UserRegisterResponse registerUser(UserRegisterRequest requestDTO) {
        // Cek apakah user berdasarkan pendudukId sudah terdaftar
        if (userRepository.findByPendudukId(requestDTO.getPendudukId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.IM_USED, "User with specified pendudukId is already registered");
        }

        // Cek apakah username sudah digunakan
        if (userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        Penduduk penduduk = pendudukRepository.findById(requestDTO.getPendudukId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penduduk not found"));

        User registerUser = new User();
        registerUser.setUsername(requestDTO.getUsername());
        registerUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        registerUser.setRole("ROLE_USER"); // role default
        registerUser.setPenduduk(penduduk);
        registerUser = userRepository.save(registerUser);
        UserRegisterResponse responseDTO = new UserRegisterResponse();
        responseDTO.setId(registerUser.getId());
        responseDTO.setUsername(registerUser.getUsername());
        responseDTO.setRole(registerUser.getRole());
        responseDTO.setPenduduk(penduduk);
        return responseDTO;
    }
}
