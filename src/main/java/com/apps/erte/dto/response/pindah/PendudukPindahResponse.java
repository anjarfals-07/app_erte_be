package com.apps.erte.dto.response.pindah;

import com.apps.erte.entity.pindah.SuratKeteranganPindah;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PendudukPindahResponse {
    private Long id;
    private String kodePindah;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalPindah;
    private String alamatPindah;
    private SuratKeteranganPindah suratKeteranganPindah;
}
