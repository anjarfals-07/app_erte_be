package com.apps.erte.dto.request.meninggal;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SuratKeteranganMeninggalRequest {
    private String noSuratMeninggal;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalSuratMeninggal; // Tanggal Pindah dalam Surat Pindah
    private String keteranganSuratMeninggal; // Deskripsi Surat Pindah
}
