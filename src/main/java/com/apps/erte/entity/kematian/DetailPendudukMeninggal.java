package com.apps.erte.entity.kematian;

import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.pindah.PendudukPindah;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class DetailPendudukMeninggal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "penduduk_meninggal_id")
    private PendudukMeninggal pendudukMeninggal;
    @ManyToOne
    @JoinColumn(name = "penduduk_id")
    private Penduduk penduduk;
}
