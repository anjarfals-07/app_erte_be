package com.apps.erte.entity.user;

import com.apps.erte.entity.Penduduk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @OneToOne
    @JoinColumn(name = "penduduk_id")
    private Penduduk penduduk;
    private String role;
}
