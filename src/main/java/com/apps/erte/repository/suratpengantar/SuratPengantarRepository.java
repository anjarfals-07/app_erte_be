package com.apps.erte.repository.suratpengantar;

import com.apps.erte.entity.suratpengantar.SuratPengantar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuratPengantarRepository extends JpaRepository<SuratPengantar, Long> {
//    List<SuratPengantar> searchByFields(String noSuratPengantar, String noKtp, String noKk, String namaLengkap);
//    List<SuratPengantar> findByNoSuratPengantarContainingAndPenduduk_NoKtpContainingAndPenduduk_KartuKeluarga_NoKKContainingAndPenduduk_NamaLengkapContaining(
//            String noSuratPengantar, String noKtp, String noKk, String namaLengkap);
List<SuratPengantar> findByNoSuratPengantarOrPendudukNoKtpOrPendudukKartuKeluargaNoKKOrPendudukNamaLengkap(
        String noSuratPengantar, String noKtp, String noKK, String namaLengkap);



}