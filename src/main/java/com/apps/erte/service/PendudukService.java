package com.apps.erte.service;

import com.apps.erte.dto.request.PendudukRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.entity.KartuKeluarga;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.repository.KartuKeluargaRepository;
import com.apps.erte.repository.PendudukRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PendudukService {
    private final PendudukRepository pendudukRepository;
    private final KartuKeluargaRepository kartuKeluargaRepository;

    @Autowired
    public PendudukService(PendudukRepository pendudukRepository, KartuKeluargaRepository kartuKeluargaRepository) {
        this.pendudukRepository = pendudukRepository;
        this.kartuKeluargaRepository = kartuKeluargaRepository;
    }

    public Page<PendudukResponse> findAll(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Penduduk> residents = pendudukRepository.findAll(pageableWithSort);
        return residents.map(this::mapResidentToResponse);
    }
//    public List<ResidentResponse> getAllResidents() {
//        List<Resident> residents = residentRepository.findAll();
//        return residents.stream()
//                .map(this::mapResidentToResponse)
//                .collect(Collectors.toList());
//    }

    public PendudukResponse getPendudukById(Long id){
        Optional<Penduduk>residentOptional = pendudukRepository.findById(id);
        if (residentOptional.isPresent()){
            Penduduk penduduk = residentOptional.get();
            return mapResidentToResponse(penduduk);
        }
        return null;
    }


/*   public ResidentResponse createResident(ResidentRequest request) {
      Resident resident = new Resident();
        mapRequestToResident(request, resident);
       Resident savedResident = residentRepository.save(resident);
       return mapResidentToResponse(savedResident);
  }
 */

    public PendudukResponse createPenduduk(PendudukRequest request) {
        // Cek apakah FamilyCard dengan nomor KK sudah ada
        KartuKeluarga kartuKeluarga = kartuKeluargaRepository.findByNoKK(request.getKartuKeluargaRequest().getNoKK());
        if (kartuKeluarga == null) {
            // Jika tidak ada, maka buat FamilyCard baru
            kartuKeluarga = new KartuKeluarga();
            kartuKeluarga.setNoKK(request.getKartuKeluargaRequest().getNoKK());
            kartuKeluarga.setNamaKepalaKeluarga(request.getKartuKeluargaRequest().getNamaKepalaKeluarga());
            kartuKeluargaRepository.save(kartuKeluarga);
        }

        Penduduk penduduk = new Penduduk();
        mapRequestToResident(request, penduduk);
        penduduk.setKartuKeluarga(kartuKeluarga); // Gunakan FamilyCard yang ada atau baru saja dibuat
        Penduduk savedResident = pendudukRepository.save(penduduk);
        return mapResidentToResponse(savedResident);
    }

    public PendudukResponse updatePenduduk(Long id, PendudukRequest request) {
        Optional<Penduduk> residentOptional = pendudukRepository.findById(id);
        if (residentOptional.isPresent()) {
            Penduduk penduduk = residentOptional.get();
            mapRequestToResident(request, penduduk);
            Penduduk updatedResident = pendudukRepository.save(penduduk);
            return mapResidentToResponse(updatedResident);
        }
        return null; // atau throw exception jika tidak ditemukan
    }

    public void deletePenduduk(Long id) {
        pendudukRepository.deleteById(id);
    }

    private PendudukResponse mapResidentToResponse(Penduduk resident) {
        PendudukResponse response = new PendudukResponse();
        response.setId(resident.getId());
        response.setNoKtp(resident.getNoKtp());
        response.setIdKartuKeluarga(resident.getKartuKeluarga().getId());
        response.setNamaLengkap(resident.getNamaLengkap());
        response.setTanggallahir(resident.getTanggalLahir());
        response.setTempatLahir(resident.getTempatLahir());
        response.setJeniskelamin(resident.getJenisKelamin());
        response.setStatusKeluarga(resident.getStatuskeluarga());
        response.setStatusPerkawinan(resident.getStatusPerkawinan());
        response.setAgama(resident.getAgama());
        response.setPendidikan(resident.getPendidikan());
        response.setPekerjaan(resident.getPekerjaan());
        response.setTelepon(resident.getTelepon());
        response.setAlamat(resident.getAlamat());
        response.setRt(resident.getRt());
        response.setRw(resident.getRw());
        response.setKelurahan(resident.getKelurahan());
        response.setKecamatan(resident.getKecamatan());
        response.setKota(resident.getKota());
        response.setKodePos(resident.getKodePos());
        return response;
    }

    private void mapRequestToResident(PendudukRequest request, Penduduk resident) {
        resident.setNoKtp(request.getNoKtp());
        KartuKeluarga familyCard = kartuKeluargaRepository.findByNoKK(request.getKartuKeluargaRequest().getNoKK());

        if (familyCard == null) {
            throw new RuntimeException("Kartu Keluarga tidak ditemukan");
        }

        resident.setKartuKeluarga(familyCard);
        resident.setNamaLengkap(request.getNamaLengkap());
        resident.setTanggalLahir(request.getTanggallahir());
        resident.setTempatLahir(request.getTempatLahir());
        resident.setJenisKelamin(request.getJeniskelamin());
        resident.setStatuskeluarga(request.getStatusKeluarga());
        resident.setStatusPerkawinan(request.getStatusPerkawinan());
        resident.setAgama(request.getAgama());
        resident.setPendidikan(request.getPendidikan());
        resident.setPekerjaan(request.getPekerjaan());
        resident.setTelepon(request.getTelepon());
        resident.setAlamat(request.getAlamat());
        resident.setRt(request.getRt());
        resident.setRw(request.getRw());
        resident.setKelurahan(request.getKelurahan());
        resident.setKecamatan(request.getKecamatan());
        resident.setKota(request.getKota());
        resident.setKodePos(request.getKodePos());
    }
}
