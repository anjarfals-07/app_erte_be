package com.apps.erte.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class KartuKeluargaResponse {
    private Long id;
    private String noKK;
    private String namaKepalaKeluarga;
    private List<PendudukResponse> pendudukList; // Add this line
}
