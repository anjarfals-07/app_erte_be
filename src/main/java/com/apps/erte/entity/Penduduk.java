package com.apps.erte.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String noKtp;
    @ManyToOne
    @JoinColumn(name = "kartu_keluarga_id")
    private KartuKeluarga kartuKeluarga;
    private String namaLengkap;
    private LocalDate tanggalLahir;
    private String tempatLahir;
    private String jenisKelamin;
    private String statuskeluarga;
    private String statusPerkawinan;
    private String agama;
    private String pendidikan;
    private String pekerjaan;
    private String telepon;
    private String alamat;
    private String rt;
    private String rw;
//    Kelurahan
    private String kelurahan;
//    Kecamatan
    private String kecamatan;
//    Kota
    private String kota;
    private String kodePos;

}
