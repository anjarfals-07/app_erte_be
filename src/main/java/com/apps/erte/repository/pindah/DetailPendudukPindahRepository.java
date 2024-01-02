package com.apps.erte.repository.pindah;

import com.apps.erte.entity.pindah.DetailPendudukPindah;
import com.apps.erte.entity.pindah.PendudukPindah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailPendudukPindahRepository extends JpaRepository<DetailPendudukPindah, Long> {
    List<DetailPendudukPindah> findByPendudukPindahId(Long pendudukPindahId);

    List<DetailPendudukPindah> findByPendudukPindah(PendudukPindah pendudukPindah);
}
