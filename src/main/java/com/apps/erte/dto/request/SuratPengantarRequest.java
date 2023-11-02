package com.apps.erte.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SuratPengantarRequest {
    private String noSuratPengantar;
    private PendudukRequest pendudukRequest;
    private LocalDate tanggalSurat;
    private String keterangan;
}