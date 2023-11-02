package com.apps.erte.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PendudukMeninggalResponse {
    private Long id;
    private String kodeMeninggal;
    private LocalDate tanggalWafat;
    private String penyebab;
    private String noKtp;
    private String namaLengkap;
    private String tempatlahir;
    private String tanggalLahir;
    private String jenisKelamin;
    private String noSuratMeninggal;
}
