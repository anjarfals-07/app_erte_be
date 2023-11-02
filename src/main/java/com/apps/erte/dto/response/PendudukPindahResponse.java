package com.apps.erte.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PendudukPindahResponse {
    private Long id;
    private String kodePindah;
    private LocalDate tanggalPindah;
    private String alamatPindah;
    private String noKtp;
    private String namaLengkap;
    private String noSuratPindah;
    private String keteranganPindah;
}
