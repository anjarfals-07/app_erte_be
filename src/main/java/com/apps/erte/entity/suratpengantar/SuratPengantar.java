package com.apps.erte.entity.suratpengantar;

import com.apps.erte.entity.Penduduk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SuratPengantar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String noSuratPengantar;
    private LocalDate tanggalSurat;
    private String keterangan;

    @ManyToOne
    @JoinColumn(name = "penduduk_id")
    private Penduduk penduduk;
}