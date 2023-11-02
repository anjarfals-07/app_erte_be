package com.apps.erte.repository.meninggal;

import com.apps.erte.entity.kematian.PendudukMeninggal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendudukMeninggalRepository extends JpaRepository<PendudukMeninggal, Long> {
    Page<PendudukMeninggal> findByKodeMeninggal(String kodeMeninggal, Pageable pageable);
}
