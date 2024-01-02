package com.apps.erte.dto.request.pindah;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PendudukPindahRequest {
    private String kodePindah;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalPindah;
    private String alamatPindah;
    private SuratKeteranganPindahRequest suratKeteranganPindah;
}
