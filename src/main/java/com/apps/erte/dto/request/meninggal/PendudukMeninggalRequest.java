package com.apps.erte.dto.request.meninggal;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PendudukMeninggalRequest {
    private String kodeMeninggal;
    private SuratKeteranganMeninggalRequest suratKeteranganMeninggalRequest;
    private Long pendudukId;
    private LocalDate tanggalWafat;
    private String penyebab;
}
