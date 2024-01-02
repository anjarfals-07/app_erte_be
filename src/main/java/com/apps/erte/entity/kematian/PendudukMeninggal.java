package com.apps.erte.entity.kematian;

import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.pindah.SuratKeteranganPindah;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class PendudukMeninggal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kodeMeninggal;
    @OneToOne
    private SuratKeteranganMeninggal suratKeteranganMeninggal;
    @OneToOne
    @JoinColumn(name = "penduduk_id")
    private Penduduk penduduk;
    private LocalDate tanggalWafat;
    private String penyebab;

}
