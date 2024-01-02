package com.apps.erte.dto.response.meninggal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SuratKeteranganMeninggalResponse {
    private Long id;
    private String noSuratMeninggal;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalSuratMeninggal;
    private String keteranganSuratMeninggal;
}
