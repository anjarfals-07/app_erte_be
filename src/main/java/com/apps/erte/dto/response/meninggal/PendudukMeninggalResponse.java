package com.apps.erte.dto.response.meninggal;

import com.apps.erte.dto.response.PendudukResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PendudukMeninggalResponse {
private Long id;
    private String kodeMeninggal;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalWafat;
    private String penyebab;
    private SuratKeteranganMeninggalResponse suratKeteranganMeninggal;
    private PendudukResponse penduduk;
}
