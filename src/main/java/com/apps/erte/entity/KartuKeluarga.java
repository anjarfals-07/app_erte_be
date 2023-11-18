package com.apps.erte.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class KartuKeluarga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "no_kk",nullable = false, unique = true)
    private String noKK;
    @Column(nullable = true)
    private String namaKepalaKeluarga;// In KartuKeluarga class
}
