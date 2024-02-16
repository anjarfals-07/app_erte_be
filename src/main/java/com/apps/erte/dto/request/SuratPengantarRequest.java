package com.apps.erte.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SuratPengantarRequest {
    private String noSuratPengantar;
    private Long pendudukId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalSurat;
    private String keperluan;
    private String keterangan;
}