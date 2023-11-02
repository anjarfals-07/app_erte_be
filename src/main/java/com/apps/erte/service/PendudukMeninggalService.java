package com.apps.erte.service;

import com.apps.erte.dto.request.PendudukMeninggalRequest;
import com.apps.erte.dto.response.PendudukMeninggalResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.kematian.DetailPendudukMeninggal;
import com.apps.erte.entity.kematian.PendudukMeninggal;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.repository.meninggal.DetailPendudukMeninggalRepository;
import com.apps.erte.repository.meninggal.PendudukMeninggalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PendudukMeninggalService {

    @Autowired
    private final PendudukMeninggalRepository pendudukMeninggalRepository;
    @Autowired
    private final DetailPendudukMeninggalRepository detailPendudukMeninggalRepository;
    @Autowired
    private final PendudukRepository pendudukRepository;

    public PendudukMeninggalService(PendudukMeninggalRepository pendudukMeninggalRepository, DetailPendudukMeninggalRepository detailPendudukMeninggalRepository, PendudukRepository pendudukRepository) {
        this.pendudukMeninggalRepository = pendudukMeninggalRepository;
        this.detailPendudukMeninggalRepository = detailPendudukMeninggalRepository;
        this.pendudukRepository = pendudukRepository;
    }

    public PendudukMeninggalResponse requestPendudukMeninggal(PendudukMeninggalRequest request) {
        PendudukMeninggal pendudukMeninggal = new PendudukMeninggal();
        pendudukMeninggal.setKodeMeninggal(request.getKodeMeninggal());
        pendudukMeninggal.setTanggalWafat(request.getTanggalWafat());
        pendudukMeninggal.setPenyebab(request.getPenyebab());

        pendudukMeninggalRepository.save(pendudukMeninggal);

        Penduduk penduduk = pendudukRepository.findByNoKtp(request.getNoKtp());

        DetailPendudukMeninggal detailPendudukMeninggal = new DetailPendudukMeninggal();
        detailPendudukMeninggal.setPenduduk(penduduk);
        detailPendudukMeninggal.setPendudukMeninggal(pendudukMeninggal);
        detailPendudukMeninggalRepository.save(detailPendudukMeninggal);

//        Add Surat Meninggal

        PendudukMeninggalResponse responseDTO = new PendudukMeninggalResponse();
        responseDTO.setId(pendudukMeninggal.getId());
        responseDTO.setNoKtp(penduduk.getNoKtp());
        responseDTO.setNamaLengkap(penduduk.getNamaLengkap());
        responseDTO.setKodeMeninggal(pendudukMeninggal.getKodeMeninggal());
        responseDTO.setTanggalWafat(pendudukMeninggal.getTanggalWafat());
        responseDTO.setPenyebab(pendudukMeninggal.getPenyebab());

        return responseDTO;

    }

    public Page<PendudukMeninggal> getPendudukMeninggalByCode(String kodeMeninggal, Pageable pageable) {
        return pendudukMeninggalRepository.findByKodeMeninggal(kodeMeninggal, pageable);
    }

    public Page<DetailPendudukMeninggal> getDetailPendudukMeninggalByKodeMeninggal(Long pendudukMeninggalId, Pageable pageable) {
        return detailPendudukMeninggalRepository.findByPendudukMeninggalId(pendudukMeninggalId, pageable);
    }
}
