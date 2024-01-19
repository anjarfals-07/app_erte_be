package com.apps.erte.repository;

import com.apps.erte.entity.KartuKeluarga;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.pindah.PendudukPindah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PendudukRepository extends JpaRepository<Penduduk, Long> {
    Optional<Penduduk> findByNoKtp(String noKtp);
    Optional<Penduduk> findByKartuKeluargaNoKKAndNoKtp(String noKk, String noKtp);
    boolean existsByNoKtp(String noKtp);
    List<Penduduk> findByKartuKeluargaNoKK(String noKK);
    List<Penduduk> findByKartuKeluarga(KartuKeluarga kartuKeluarga);

    @Query("SELECT p FROM Penduduk p " +
            "LEFT JOIN p.kartuKeluarga kk " +
            "WHERE LOWER(p.noKtp) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.namaLengkap) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.jenisKelamin) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.agama) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.pendidikan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.pekerjaan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.newStatusPenduduk) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.statusPenduduk) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.statuskeluarga) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.statusPerkawinan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.telepon) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.alamat) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(kk.noKK) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(kk.namaKepalaKeluarga) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR p.tanggalLahir = :tanggallahir")
    Page<Penduduk> search(
            @Param("keyword") String keyword,
            @Param("tanggallahir") LocalDate tanggallahir,
            Pageable pageable);

}
