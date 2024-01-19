package com.apps.erte.repository.meninggal;

import com.apps.erte.entity.kematian.PendudukMeninggal;
import com.apps.erte.entity.suratpengantar.SuratPengantar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PendudukMeninggalRepository extends JpaRepository<PendudukMeninggal, Long> {
    Page<PendudukMeninggal> findByKodeMeninggal(String kodeMeninggal, Pageable pageable);
    @Query("SELECT pm FROM PendudukMeninggal pm " +
            "LEFT JOIN pm.suratKeteranganMeninggal s " +
            "LEFT JOIN pm.penduduk p " +
            "LEFT JOIN p.kartuKeluarga kk " +
            "WHERE LOWER(pm.kodeMeninggal) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(s.noSuratMeninggal) LIKE LOWER(CONCAT('%', :keyword, '%')) " +  // Fix the alias here
            "   OR pm.tanggalWafat = :tanggalWafat " +
            "   OR s.tanggalSuratMeninggal = :tanggalSuratMeninggal " +
            "   OR LOWER(pm.penyebab) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(s.keteranganSuratMeninggal) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.noKtp) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(kk.noKK) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.namaLengkap) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PendudukMeninggal> search(
            @Param("keyword") String keyword,
            @Param("tanggalWafat") LocalDate tanggalWafat,
            @Param("tanggalSuratMeninggal") LocalDate tanggalSuratMeninggal,
            Pageable pageable);

}
