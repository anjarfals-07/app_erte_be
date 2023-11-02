package com.apps.erte.repository.pindah;

import com.apps.erte.entity.pindah.PendudukPindah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendudukPindahRepository extends JpaRepository<PendudukPindah,Long> {
    Page<PendudukPindah> findByKodePindah(String kodePindah, Pageable pageable);
}
