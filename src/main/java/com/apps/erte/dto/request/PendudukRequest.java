package com.apps.erte.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PendudukRequest {
    private String noKtp;
    private KartuKeluargaRequest kartuKeluargaRequest;
    private String namaLengkap;
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

}
