package com.apps.erte.service;

import com.apps.erte.dto.request.PendudukPindahRequest;
import com.apps.erte.dto.response.PendudukPindahResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.pindah.DetailPendudukPindah;
import com.apps.erte.entity.pindah.PendudukPindah;
import com.apps.erte.entity.pindah.SuratKeteranganPindah;
import com.apps.erte.repository.pindah.DetailPendudukPindahRepository;
import com.apps.erte.repository.pindah.PendudukPindahRepository;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.repository.pindah.SuratPindahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PendudukPindahService {
    @Autowired
    private PendudukPindahRepository pendudukPindahRepository;

    @Autowired
    private DetailPendudukPindahRepository detailPendudukPindahRepository;
    @Autowired
    private PendudukRepository pendudukRepository;

    @Autowired
    private SuratPindahRepository suratPindahRepository;

    public PendudukPindahResponse requestPendudukPindah(PendudukPindahRequest requestDTO) {
        // Lakukan logika untuk menghasilkan moveCode secara otomatis
//        String moveCode = generateMoveCode();

        // Buat entitas ResidentsMove
        PendudukPindah pendudukPindah = new PendudukPindah();
        pendudukPindah.setKodePindah(requestDTO.getKodePindah());
        pendudukPindah.setTanggalPindah(requestDTO.getTanggalPindah());
        pendudukPindah.setAlamatPindah(requestDTO.getAlamatPindah());

        // Simpan ResidentsMove
        pendudukPindahRepository.save(pendudukPindah);

        // Cari entitas Resident berdasarkan noKtp
        Penduduk penduduk = pendudukRepository.findByNoKtp(requestDTO.getNoKtp());

        // Buat entitas DetailsResidentMove
        DetailPendudukPindah detailPendudukPindah = new DetailPendudukPindah();
        detailPendudukPindah.setPendudukPindah(pendudukPindah);
        detailPendudukPindah.setPenduduk(penduduk);


        // Simpan DetailsResidentMove
        detailPendudukPindahRepository.save(detailPendudukPindah);

        // Buat entitas TransferCertificate
        SuratKeteranganPindah suratKeteranganPindah = new SuratKeteranganPindah();
        suratKeteranganPindah.setNoSuratPindah(requestDTO.getNoSuratPindah());
        suratKeteranganPindah.setTanggalSuratpindah(LocalDate.now()); // Tanggal penerbitan saat ini
//        affidavit.setDateOfTransferCertificate(requestDTO.getDateOfTransferCertificate()); // Tanggal Pindah dalam Surat Pindah
        suratKeteranganPindah.setKeteranganPindah(requestDTO.getKeterangan()); // Deskripsi Surat Pindah
        suratKeteranganPindah.setPendudukPindah(pendudukPindah);

        // Simpan TransferCertificate
        suratPindahRepository.save(suratKeteranganPindah);

        // Buat dan kembalikan DTO response
        PendudukPindahResponse responseDTO = new PendudukPindahResponse();
        responseDTO.setId(pendudukPindah.getId());
        responseDTO.setNoKtp(penduduk.getNoKtp());
        responseDTO.setNamaLengkap(penduduk.getNamaLengkap());
        responseDTO.setKodePindah(pendudukPindah.getKodePindah());
        responseDTO.setTanggalPindah(pendudukPindah.getTanggalPindah());
        responseDTO.setAlamatPindah(pendudukPindah.getAlamatPindah());
        responseDTO.setNoSuratPindah(suratKeteranganPindah.getNoSuratPindah());
        responseDTO.setKeteranganPindah(suratKeteranganPindah.getKeteranganPindah());

        return responseDTO;
    }

    public Page<PendudukPindah> getPendudukPindahByCode(String kodePindah, Pageable pageable) {
        return pendudukPindahRepository.findByKodePindah(kodePindah, pageable);
    }

    public Page<DetailPendudukPindah> getDetailPendudukPindahByKodePindah(Long pendudukPindahId, Pageable pageable) {
        return detailPendudukPindahRepository.findByPendudukPindahId(pendudukPindahId, pageable);
    }

//    private String generateMoveCode() {
//        // Logika penghasilan moveCode
//        UUID uniqueCode = UUID.randomUUID();
//        String uuidSubstring = uniqueCode.toString().replace("-", "").substring(0, 8);
//
//        // Gabungkan dengan "PINDAH-" di depannya
//        String moveCode = "PINDAH-" + uuidSubstring;
//
//        return moveCode;
//    }

//    private String generateCertificateNumber() {
//        // Logika penghasilan moveCode
//        UUID uniqueCode = UUID.randomUUID();
//        String uuidSubstring = uniqueCode.toString().replace("-", "").substring(0, 8);
//
//        // Gabungkan dengan "PINDAH-" di depannya
//        String moveCode = "SP-" + uuidSubstring;
//
//        return moveCode;
//    }
}
