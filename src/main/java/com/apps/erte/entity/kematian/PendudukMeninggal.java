package com.apps.erte.entity.kematian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private LocalDate tanggalWafat;
    private String penyebab;
}
