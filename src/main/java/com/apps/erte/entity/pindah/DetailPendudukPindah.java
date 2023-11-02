package com.apps.erte.entity.pindah;

import com.apps.erte.entity.Penduduk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class DetailPendudukPindah {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "penduduk_pindah_id")
    private PendudukPindah pendudukPindah;
    @ManyToOne
    @JoinColumn(name = "penduduk_id")
    private Penduduk penduduk;
}
