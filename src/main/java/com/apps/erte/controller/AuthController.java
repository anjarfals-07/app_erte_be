    package com.apps.erte.controller;

    import com.apps.erte.dto.request.user.UserLoginRequest;
    import com.apps.erte.dto.response.user.UserLoginResponse;
    import com.apps.erte.security.jwt.JwtUtils;
    import com.apps.erte.security.services.UserDetailsImpl;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;

    import java.util.Date;

    @RestController
    @RequestMapping("/app/auth")
    @CrossOrigin
    public class AuthController {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtUtils jwtUtils;

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@RequestBody UserLoginRequest loginRequest) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                String role = userDetails.getAuthorities().iterator().next().getAuthority();
                Long pendudukId = role.equals("ROLE_USER") ? userDetails.getPenduduk().getId() : null;
                String fullName =  role.equals("ROLE_USER")  ? userDetails.getPenduduk().getNamaLengkap() : "Administration";
                String telepon = role.equals("ROLE_USER") ? userDetails.getPenduduk().getTelepon() : null;
                String email =  role.equals("ROLE_USER") ? userDetails.getPenduduk().getEmail():null;
                String foto =  role.equals("ROLE_USER") ? userDetails.getPenduduk().getFotoUrl():null;

                Date expirationDate = jwtUtils.getExpirationDateFromJwtToken(jwt);
                long expiresInMs = expirationDate.getTime() - System.currentTimeMillis();

                return ResponseEntity.ok(new UserLoginResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        role,
                        jwt,
                        expiresInMs,  // Menyertakan waktu kadaluwarsa token
                        pendudukId,
                        fullName,
                        telepon,
                        email,
                        foto

                ));
            }
            catch (BadCredentialsException e) {
                if (e.getMessage().contains("Bad credentials")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid username or password");
                } else if (e.getMessage().contains("Username not found")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Username not found");
                } else if (e.getMessage().contains("Bad credentials")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid password");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Authentication failed");
                }
            }
        }


    }
