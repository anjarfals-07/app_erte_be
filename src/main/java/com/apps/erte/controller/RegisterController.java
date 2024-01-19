    package com.apps.erte.controller;

    import com.apps.erte.dto.request.user.UserRegisterRequest;
    import com.apps.erte.dto.response.user.UserRegisterResponse;
    import com.apps.erte.entity.user.User;
    import com.apps.erte.repository.UserRepository;
    import com.apps.erte.service.RegisterService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Optional;

    @RestController
    @RequestMapping("app/register")
    @CrossOrigin
    public class RegisterController {
        @Autowired
        private RegisterService registerService;
        @Autowired
        private UserRepository userRepository;
        @PostMapping
        public UserRegisterResponse registerUser(@RequestBody UserRegisterRequest requestDTO) {
            return registerService.registerUser(requestDTO);
        }
        @GetMapping("/check-username/{username}")
        public ResponseEntity<Boolean> checkUsernameAvailability(@PathVariable String username) {
            Optional<User> existingUser = userRepository.findByUsername(username);
            return ResponseEntity.ok(existingUser.isEmpty());
        }

        @GetMapping("/check-penduduk/{pendudukId}")
        public ResponseEntity<Boolean> checkPendudukRegistered(@PathVariable Long pendudukId) {
            Optional<User> existingUser = userRepository.findByPendudukId(pendudukId);
            return ResponseEntity.ok(existingUser.isPresent());
        }
    }
