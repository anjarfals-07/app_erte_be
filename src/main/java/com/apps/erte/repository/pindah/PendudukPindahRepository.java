package com.apps.erte.repository.pindah;

import com.apps.erte.entity.pindah.PendudukPindah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface PendudukPindahRepository extends JpaRepository<PendudukPindah,Long> {
    @Query("SELECT p FROM PendudukPindah p " +
            "LEFT JOIN p.suratKeteranganPindah s " +
            "WHERE LOWER(p.kodePindah) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR p.tanggalPindah = :tanggalPindah " +
            "   OR LOWER(p.alamatPindah) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(s.noSuratPindah) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR s.tanggalSuratPindah = :tanggalSuratPindah")
    Page<PendudukPindah> search(
            @Param("keyword") String keyword,
            @Param("tanggalPindah") LocalDate tanggalPindah,
            @Param("tanggalSuratPindah") LocalDate tanggalSuratPindah,
            Pageable pageable);

    PendudukPindah findByKodePindah(String kodePindah);

//    PendudukPindah findByNoKK(String noKK);







}
