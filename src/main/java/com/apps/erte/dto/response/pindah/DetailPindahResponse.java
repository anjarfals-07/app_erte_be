package com.apps.erte.dto.response.pindah;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DetailPindahResponse {
    private Long id;
    private Long pendudukId;
    private Long pendudukPindahId;
    private String kodePindah;
    private String noKK;
    private String noKTP;
    private String nama;
    private String jenisKelamin;
    private String tempatLahir;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalLahir;
    private String agama;
    private String fotoUrl;
}
