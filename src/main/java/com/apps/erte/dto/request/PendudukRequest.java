package com.apps.erte.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class PendudukRequest {
    private String noKtp;
    private KartuKeluargaRequest kartuKeluarga;
    private String namaLengkap;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggallahir;
    private String tempatLahir;
    private String jenisKelamin;
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
    private MultipartFile foto;
}
