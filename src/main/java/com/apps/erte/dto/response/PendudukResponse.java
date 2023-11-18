package com.apps.erte.dto.response;

import com.apps.erte.entity.KartuKeluarga;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PendudukResponse {
    private Long id;
    private String noKtp;
    private KartuKeluarga kartuKeluarga;
    private String namaLengkap;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggallahir;
    private String tempatLahir;
    private String jeniskelamin;
    private String statusKeluarga;
    private String statusPerkawinan;
    private String agama;
    private String pendidikan;
    private String pekerjaan;
    private String telepon;
    private String alamat;
    private String rt;
    private String rw;
    private String kelurahan;
    private String kecamatan;
    private String kota;
    private String kodePos;
    private String fotoUrl;
}
