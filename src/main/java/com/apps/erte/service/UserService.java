package com.apps.erte.service;

import com.apps.erte.entity.user.User;
import com.apps.erte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser(){
        return  userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByNoKtp(String noKtp) {
        return userRepository.findByPenduduk_NoKtp(noKtp);
    }
}
