package com.apps.erte.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDate;
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class Penduduk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true)
    private String noKtp;
    @ManyToOne
    @JoinColumn(name = "kartu_keluarga_id")
    private KartuKeluarga kartuKeluarga;
    @Column(nullable = true)
    private String namaLengkap;
    @Column(nullable = true)
    private LocalDate tanggalLahir;
    @Column(nullable = true)
    private String tempatLahir;
    @Column(nullable = true)
    private String jenisKelamin;
    @Column(nullable = true)
    private String statuskeluarga;
    @Column(nullable = true)
    private String statusPerkawinan;
    @Column(nullable = true)
    private String agama;
    @Column(nullable = true)
    private String pendidikan;
    @Column(nullable = true)
    private String pekerjaan;
    @Column(nullable = true)
    private String telepon;
    @Column(nullable = true)
    private String alamat;
    @Column(nullable = true)
    private String rt;
    @Column(nullable = true)
    private String rw;
    @Column(nullable = true)
    private String kelurahan;
    @Column(nullable = true)
    private String kecamatan;
    @Column(nullable = true)
    private String kota;
    @Column(nullable = true)
    private String kodePos;
    @Column(nullable = true)
    private String fotoUrl;
}
