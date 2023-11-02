package com.apps.erte.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SuratPengantarResponse {
    private Long id;
    private String noSuratPengantar;
    private String noKk;
    private String noKtp;
    private String namaLengkap;
    private LocalDate tanggallahir;
    private String jeniskelamin;
    private LocalDate tanggalSurat;
    private String keterangan;
}