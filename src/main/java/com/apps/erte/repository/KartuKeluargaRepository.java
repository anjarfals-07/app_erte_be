package com.apps.erte.repository;

import com.apps.erte.entity.KartuKeluarga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KartuKeluargaRepository extends JpaRepository<KartuKeluarga, Long> {
    Page<KartuKeluarga> findAll(Pageable pageable);
    KartuKeluarga findByNoKK(String noKK);
}
