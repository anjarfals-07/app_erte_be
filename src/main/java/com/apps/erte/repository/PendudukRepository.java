package com.apps.erte.repository;

import com.apps.erte.entity.Penduduk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendudukRepository extends JpaRepository<Penduduk, Long> {
    Penduduk findByNoKtp(String noKtp);
    Optional<Penduduk> findByKartuKeluargaNoKKAndNoKtp(String noKk, String noKtp);
}
