package com.apps.erte.service;

import com.apps.erte.dto.request.pindah.DetailPindahRequest;
import com.apps.erte.dto.response.pindah.DetailPindahResponse;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.pindah.DetailPendudukPindah;
import com.apps.erte.entity.pindah.PendudukPindah;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.repository.pindah.DetailPendudukPindahRepository;
import com.apps.erte.repository.pindah.PendudukPindahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetailPindahService {
    private final DetailPendudukPindahRepository detailPindahRepository;
    private final PendudukPindahRepository pendudukPindahRepository;
    private final PendudukRepository pendudukRepository;

    @Autowired
    public DetailPindahService(DetailPendudukPindahRepository detailPindahRepository,
                               PendudukPindahRepository pendudukPindahRepository,
                               PendudukRepository pendudukRepository) {
        this.detailPindahRepository = detailPindahRepository;
        this.pendudukPindahRepository = pendudukPindahRepository;
        this.pendudukRepository = pendudukRepository;
    }

        public DetailPindahResponse createDetailPindah(DetailPindahRequest request) {
            Optional<PendudukPindah> pendudukPindahOptional = pendudukPindahRepository.findById(request.getPendudukPindahId());
            Optional<Penduduk> pendudukOptional = pendudukRepository.findById(request.getPendudukId());

            if (pendudukPindahOptional.isPresent() && pendudukOptional.isPresent()) {
                PendudukPindah pendudukPindah = pendudukPindahOptional.get();
                Penduduk penduduk = pendudukOptional.get();

                DetailPendudukPindah detailPindah = new DetailPendudukPindah();
                detailPindah.setPendudukPindah(pendudukPindah);
                detailPindah.setPenduduk(penduduk);

                // Set newStatusPenduduk to "PINDAH" and update the Penduduk entity
                penduduk.setNewStatusPenduduk("PINDAH");
                Penduduk updatedPenduduk = pendudukRepository.save(penduduk);

                // Save the DetailPendudukPindah entity
                detailPindahRepository.save(detailPindah);

                return mapToDetailPindahResponse(detailPindah);
            } else {
                throw new RuntimeException("Penduduk Pindah atau Penduduk tidak ditemukan.");
            }
        }
    public List<DetailPindahResponse> getAllDetailPindah(String kodePindah) {
        List<DetailPendudukPindah> detailPindahList;

        if (kodePindah != null && !kodePindah.isEmpty()) {
            PendudukPindah pendudukPindah = pendudukPindahRepository.findByKodePindah(kodePindah);
            if (pendudukPindah != null) {
                detailPindahList = detailPindahRepository.findByPendudukPindah(pendudukPindah);
            } else {
                throw new RuntimeException("Penduduk Pindah dengan kode " + kodePindah + " tidak ditemukan.");
            }
        } else {
            detailPindahList = detailPindahRepository.findAll();
        }

        return detailPindahList.stream().map(this::mapToDetailPindahResponse).collect(Collectors.toList());
    }

//    public List<PendudukResponse> getPendudukByNoKK(String noKK) {
//        List<Penduduk> pendudukList = pendudukRepository.findByKartuKeluargaNoKK(noKK);
//        return pendudukList.stream().map(this::mapToPendudukResponse).collect(Collectors.toList());
//    }

    public void deleteDetailPindah(Long detailPindahId) {
        Optional<DetailPendudukPindah> detailPindahOptional = detailPindahRepository.findById(detailPindahId);
        if (detailPindahOptional.isPresent()) {
            DetailPendudukPindah detailPindah = detailPindahOptional.get();
            Penduduk penduduk = detailPindah.getPenduduk();
            // Set newStatusPenduduk to null and update the Penduduk entity
            penduduk.setNewStatusPenduduk(null);
            pendudukRepository.save(penduduk);
            // Delete the DetailPendudukPindah entity
            detailPindahRepository.deleteById(detailPindahId);
        } else {
            throw new RuntimeException("Detail Pindah tidak ditemukan.");
        }
    }

    private PendudukResponse mapToPendudukResponse(Penduduk penduduk) {
        PendudukResponse response = new PendudukResponse();
        response.setId(penduduk.getId());
        response.setNoKtp(penduduk.getNoKtp());
        response.setKartuKeluarga(penduduk.getKartuKeluarga());
        response.setNamaLengkap(penduduk.getNamaLengkap());
        response.setTanggallahir(penduduk.getTanggalLahir());
        response.setTempatLahir(penduduk.getTempatLahir());
        response.setJenisKelamin(penduduk.getJenisKelamin());
        response.setStatusKeluarga(penduduk.getStatuskeluarga());
        response.setStatusPerkawinan(penduduk.getStatusPerkawinan());
        response.setAgama(penduduk.getAgama());
        response.setPendidikan(penduduk.getPendidikan());
        response.setPekerjaan(penduduk.getPekerjaan());
        response.setTelepon(penduduk.getTelepon());
        response.setEmail(penduduk.getEmail());
        response.setAlamat(penduduk.getAlamat());
        response.setRt(penduduk.getRt());
        response.setRw(penduduk.getRw());
        response.setKelurahan(penduduk.getKelurahan());
        response.setKecamatan(penduduk.getKecamatan());
        response.setKota(penduduk.getKota());
        response.setKodePos(penduduk.getKodePos());
        response.setFotoUrl(penduduk.getFotoUrl());
        response.setStatusPenduduk(penduduk.getStatusPenduduk());
        response.setNewStatusPenduduk(penduduk.getNewStatusPenduduk());
        return response;
    }
    private DetailPindahResponse mapToDetailPindahResponse(DetailPendudukPindah detailPindah) {
        DetailPindahResponse response = new DetailPindahResponse();
        response.setId(detailPindah.getId());
        response.setPendudukPindahId(detailPindah.getPendudukPindah().getId());
        response.setKodePindah(detailPindah.getPendudukPindah().getKodePindah());
        response.setPendudukId(detailPindah.getPenduduk().getId());
        response.setNoKK(detailPindah.getPenduduk().getKartuKeluarga().getNoKK());
        response.setNoKTP(detailPindah.getPenduduk().getNoKtp());
        response.setNama(detailPindah.getPenduduk().getNamaLengkap());
        response.setJenisKelamin(detailPindah.getPenduduk().getJenisKelamin());
        response.setTempatLahir(detailPindah.getPenduduk().getTempatLahir());
        response.setTanggalLahir(detailPindah.getPenduduk().getTanggalLahir());
        response.setAgama(detailPindah.getPenduduk().getAgama());
        response.setFotoUrl(detailPindah.getPenduduk().getFotoUrl());
        return response;
    }

}
