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
import java.util.stream.Collectors;

@Service
public class KartuKeluargaService {
    private final KartuKeluargaRepository kartuKeluargaRepository;
    private final PendudukRepository pendudukRepository;

    public KartuKeluargaService(KartuKeluargaRepository kartuKeluargaRepository,
                                PendudukRepository pendudukRepository) {
        this.kartuKeluargaRepository = kartuKeluargaRepository;
        this.pendudukRepository = pendudukRepository;
    }

    public Page<KartuKeluargaResponse> getAllKartuKeluargaWithPenduduk(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<KartuKeluarga> kartuKeluargaPage = kartuKeluargaRepository.findAll(pageableWithSort);
        return kartuKeluargaPage.map(this::mapKartuKeluargaToResponse);
    }

    private KartuKeluargaResponse mapKartuKeluargaToResponse(KartuKeluarga kartuKeluarga) {
        KartuKeluargaResponse response = new KartuKeluargaResponse();
        response.setId(kartuKeluarga.getId());
        response.setNoKK(kartuKeluarga.getNoKK());
        response.setNamaKepalaKeluarga(kartuKeluarga.getNamaKepalaKeluarga());

        // Mendapatkan list data Penduduk untuk Kartu Keluarga tertentu
        List<Penduduk> pendudukList = pendudukRepository.findByKartuKeluarga(kartuKeluarga);
        List<PendudukResponse> pendudukResponses = mapPendudukListToResponseList(pendudukList);
        response.setPendudukList(pendudukResponses);

        return response;
    }

    private List<PendudukResponse> mapPendudukListToResponseList(List<Penduduk> pendudukList) {
        return pendudukList.stream()
                .map(resident -> {
                    PendudukResponse response = new PendudukResponse();
                    response.setId(resident.getId());
                    response.setNoKtp(resident.getNoKtp());
                    response.setNamaLengkap(resident.getNamaLengkap());
                    response.setTanggallahir(resident.getTanggalLahir());
                    response.setTempatLahir(resident.getTempatLahir());
                    response.setJenisKelamin(resident.getJenisKelamin());
                    response.setStatusKeluarga(resident.getStatuskeluarga());
                    response.setStatusPerkawinan(resident.getStatusPerkawinan());
                    response.setAgama(resident.getAgama());
                    response.setPendidikan(resident.getPendidikan());
                    response.setPekerjaan(resident.getPekerjaan());
                    response.setTelepon(resident.getTelepon());
                    response.setEmail(resident.getEmail());
                    response.setAlamat(resident.getAlamat());
                    response.setRt(resident.getRt());
                    response.setRw(resident.getRw());
                    response.setKelurahan(resident.getKelurahan());
                    response.setKecamatan(resident.getKecamatan());
                    response.setKota(resident.getKota());
                    response.setKodePos(resident.getKodePos());
                    response.setFotoUrl(resident.getFotoUrl());
                    response.setStatusPenduduk(resident.getStatusPenduduk());
                    response.setNewStatusPenduduk(resident.getNewStatusPenduduk());

                    return response;
                })
                .collect(Collectors.toList());
    }

    public KartuKeluargaResponse getKartuKeluargaByNoKK(String noKK) {
        KartuKeluarga kartuKeluarga = kartuKeluargaRepository.findByNoKK(noKK);
        return mapKartuKeluargaToResponse(kartuKeluarga);
    }

}
