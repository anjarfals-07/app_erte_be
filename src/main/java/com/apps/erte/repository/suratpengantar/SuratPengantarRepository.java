package com.apps.erte.repository.suratpengantar;

import com.apps.erte.entity.pindah.PendudukPindah;
import com.apps.erte.entity.suratpengantar.SuratPengantar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SuratPengantarRepository extends JpaRepository<SuratPengantar, Long> {
    List<SuratPengantar> findByNoSuratPengantarOrPendudukNoKtpOrPendudukKartuKeluargaNoKKOrPendudukNamaLengkap(
            String noSuratPengantar, String noKtp, String noKK, String namaLengkap);
    Page<SuratPengantar> findByNoSuratPengantar(String noSurat, Pageable pageable);
//    Search
    @Query("SELECT sp FROM SuratPengantar sp " +
            "LEFT JOIN sp.penduduk p " +
            "LEFT JOIN p.kartuKeluarga kk " +
            "WHERE LOWER(sp.noSuratPengantar) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR sp.tanggalSurat = :tanggalSurat " +
            "   OR LOWER(sp.keperluan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(sp.keterangan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.noKtp) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(kk.noKK) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.namaLengkap) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SuratPengantar> search(
            @Param("keyword") String keyword,
            @Param("tanggalSurat") LocalDate tanggalSurat,
            Pageable pageable);

    Page<SuratPengantar> findByPendudukId(Long pendudukId, Pageable pageableWithSort);
}