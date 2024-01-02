package com.apps.erte.dto.request.pindah;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SuratKeteranganPindahRequest {
    private String noSuratPindah;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalSuratPindah; // Tanggal Pindah dalam Surat Pindah
    private String keteranganPindah; // Deskripsi Surat Pindah
}
