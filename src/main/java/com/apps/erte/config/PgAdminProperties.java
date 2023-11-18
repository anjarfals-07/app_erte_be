package com.apps.erte.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pgadmin")
public class PgAdminProperties {
    private String url;
    private String username;
    private String password;
}
