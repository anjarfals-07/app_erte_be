package com.apps.erte.entity.pindah;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class SuratKeteranganPindah {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String noSuratPindah;
    private LocalDate tanggalSuratpindah;
    private String keteranganPindah;
    @OneToOne
    @JoinColumn(name = "penduduk_pindah_id")
    private PendudukPindah pendudukPindah;
}
