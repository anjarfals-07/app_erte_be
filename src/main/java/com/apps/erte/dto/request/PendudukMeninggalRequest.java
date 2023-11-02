package com.apps.erte.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PendudukMeninggalRequest {
    private String kodeMeninggal;
    private String noSuratMeninggal;
    private String noKtp; // No. KTP of the resident who is moving
    private LocalDate tanggalWafat;
    private String penyebab;
    private LocalDate tanggalSuratMeninggal; // Tanggal Pindah dalam Surat Pindah
//    private String keterangan; // Deskripsi Surat Pindah
}
