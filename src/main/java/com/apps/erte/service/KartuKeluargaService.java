package com.apps.erte.service;

import com.apps.erte.dto.response.KartuKeluargaResponse;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.entity.KartuKeluarga;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.repository.KartuKeluargaRepository;
import com.apps.erte.repository.PendudukRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KartuKeluargaService {
    private final KartuKeluargaRepository kartuKeluargaRepository;

    public KartuKeluargaService(KartuKeluargaRepository kartuKeluargaRepository) {
        this.kartuKeluargaRepository = kartuKeluargaRepository;
    }

//    public Page<KartuKeluargaResponse> getAllKartuKeluarga(Pageable pageable) {
//        Page<KartuKeluarga> kartuKeluargaPage = kartuKeluargaRepository.findAll(pageable);
//        return kartuKeluargaPage.map(this::mapKartuKeluargaToResponse);
//    }

    public Page<KartuKeluargaResponse> findAll(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<KartuKeluarga> residents = kartuKeluargaRepository.findAll(pageableWithSort);
        return residents.map(this::mapKartuKeluargaToResponse);
    }

    public KartuKeluargaResponse getKartuKeluargaByNoKK(String noKK) {
        KartuKeluarga kartuKeluarga = kartuKeluargaRepository.findByNoKK(noKK);
        return mapKartuKeluargaToResponse(kartuKeluarga);
    }

    private KartuKeluargaResponse mapKartuKeluargaToResponse(KartuKeluarga kartuKeluarga) {
        KartuKeluargaResponse response = new KartuKeluargaResponse();
        response.setId(kartuKeluarga.getId());
        response.setNoKK(kartuKeluarga.getNoKK());
        response.setNamaKepalaKeluarga(kartuKeluarga.getNamaKepalaKeluarga());
        return response;
    }
}
