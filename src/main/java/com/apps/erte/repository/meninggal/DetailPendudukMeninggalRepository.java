package com.apps.erte.repository.meninggal;

import com.apps.erte.entity.kematian.DetailPendudukMeninggal;
import com.apps.erte.entity.pindah.DetailPendudukPindah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailPendudukMeninggalRepository extends JpaRepository<DetailPendudukMeninggal, Long> {
    Page<DetailPendudukMeninggal> findByPendudukMeninggalId(Long pendudukMeninggalId, Pageable pageable);
}
