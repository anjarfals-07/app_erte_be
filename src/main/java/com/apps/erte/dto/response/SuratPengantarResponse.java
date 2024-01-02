package com.apps.erte.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SuratPengantarResponse {
    private Long id;
    private String noSuratPengantar;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalSurat;
    private String keterangan;
    private String keperluan;
    private PendudukResponse penduduk;
}