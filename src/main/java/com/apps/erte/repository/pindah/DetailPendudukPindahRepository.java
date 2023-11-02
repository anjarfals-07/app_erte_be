package com.apps.erte.repository.pindah;

import com.apps.erte.entity.pindah.DetailPendudukPindah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailPendudukPindahRepository extends JpaRepository<DetailPendudukPindah, Long> {
    Page<DetailPendudukPindah> findByPendudukPindahId(Long pendudukPindahId, Pageable pageable);
}
