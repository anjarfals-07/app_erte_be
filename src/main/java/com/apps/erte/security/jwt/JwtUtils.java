    package com.apps.erte.security.jwt;

    import com.apps.erte.security.services.UserDetailsImpl;
    import io.jsonwebtoken.*;
    import lombok.Data;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.boot.context.properties.ConfigurationProperties;
    import org.springframework.security.core.Authentication;
    import org.springframework.stereotype.Component;
    import java.util.Date;

    @Component
    @Data
    @ConfigurationProperties(prefix = "jwt")
    public class JwtUtils {
        private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
        private String jwtSecret;
        private Long jwtExpirationMs;

        public String generateJwtToken(Authentication authentication) {
            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

            return Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        }
        public String getUserNameFromJwtToken(String token) {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        }
        public boolean validateJwtToken(String authToken) {
            try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
                return true;
            } catch (MalformedJwtException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.error("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                logger.error("JWT token is unsupported: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.error("JWT claims string is empty: {}", e.getMessage());
            }
            return false;
        }
        public Date getExpirationDateFromJwtToken(String token) {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return claims.getExpiration();
        }

    }
