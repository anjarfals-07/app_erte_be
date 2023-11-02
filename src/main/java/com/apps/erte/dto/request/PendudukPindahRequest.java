package com.apps.erte.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PendudukPindahRequest {
    private String kodePindah;
    private String noSuratPindah;
    private String noKtp; // No. KTP of the resident who is moving
    private LocalDate tanggalPindah;
    private String alamatPindah;
    private LocalDate tanggalSuratPindah; // Tanggal Pindah dalam Surat Pindah
    private String keterangan; // Deskripsi Surat Pindah
}
