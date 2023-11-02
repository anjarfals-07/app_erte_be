package com.apps.erte.entity.pindah;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class PendudukPindah {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kodePindah;
    private LocalDate tanggalPindah;
    private String alamatPindah;
}
