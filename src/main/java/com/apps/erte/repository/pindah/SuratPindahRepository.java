package com.apps.erte.repository.pindah;

import com.apps.erte.entity.pindah.SuratKeteranganPindah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuratPindahRepository extends JpaRepository<SuratKeteranganPindah, Long> {
}
