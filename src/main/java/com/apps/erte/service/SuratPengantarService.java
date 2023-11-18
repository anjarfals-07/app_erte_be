package com.apps.erte.service;

import com.apps.erte.dto.request.SuratPengantarRequest;
import com.apps.erte.dto.response.SuratPengantarResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.suratpengantar.SuratPengantar;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.repository.suratpengantar.SuratPengantarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SuratPengantarService {

    @Autowired
    private SuratPengantarRepository suratPengantarRepository;

    @Autowired
    private PendudukRepository pendudukRepository;

    public SuratPengantarResponse createSuratPengantar(SuratPengantarRequest request) {
        SuratPengantar suratPengantar = new SuratPengantar();
        suratPengantar.setNoSuratPengantar(request.getNoSuratPengantar());
        suratPengantar.setTanggalSurat(request.getTanggalSurat());
        suratPengantar.setKeterangan(request.getKeterangan());

        Optional<Penduduk> pendudukOptional = pendudukRepository.findByKartuKeluargaNoKKAndNoKtp(request.getPendudukRequest().getKartuKeluarga().getNoKK(), request.getPendudukRequest().getNoKtp());
        if (pendudukOptional.isPresent()) {
            suratPengantar.setPenduduk(pendudukOptional.get());
        }

        SuratPengantar savedSuratPengantar = suratPengantarRepository.save(suratPengantar);
        SuratPengantarResponse response = new SuratPengantarResponse();
        response.setId(savedSuratPengantar.getId());
        response.setNoSuratPengantar(savedSuratPengantar.getNoSuratPengantar());
        response.setNoKk(request.getPendudukRequest().getKartuKeluarga().getNoKK());
        response.setNoKtp(request.getPendudukRequest().getNoKtp());
        response.setTanggalSurat(savedSuratPengantar.getTanggalSurat());
        response.setKeterangan(savedSuratPengantar.getKeterangan());

        if (suratPengantar.getPenduduk() != null) {
            Penduduk penduduk = suratPengantar.getPenduduk();
            response.setNamaLengkap(penduduk.getNamaLengkap());
            response.setTanggallahir(penduduk.getTanggalLahir());
            response.setJeniskelamin(penduduk.getJenisKelamin());
            // tambahkan atribut lain dari entity Penduduk yang perlu ditampilkan
        }

        return response;
    }


    public SuratPengantarResponse updateSuratPengantar(Long id, SuratPengantarRequest request) {
        Optional<SuratPengantar> existingSuratPengantar = suratPengantarRepository.findById(id);
        if (existingSuratPengantar.isPresent()) {
            SuratPengantar suratPengantar = existingSuratPengantar.get();
            suratPengantar.setNoSuratPengantar(request.getNoSuratPengantar());
            suratPengantar.setTanggalSurat(request.getTanggalSurat());
            suratPengantar.setKeterangan(request.getKeterangan());

            // Update other fields as needed

            SuratPengantar updatedSuratPengantar = suratPengantarRepository.save(suratPengantar);

            // Create and return the response
            SuratPengantarResponse response = new SuratPengantarResponse();
            response.setId(updatedSuratPengantar.getId());
            response.setNoSuratPengantar(updatedSuratPengantar.getNoSuratPengantar());
            response.setTanggalSurat(updatedSuratPengantar.getTanggalSurat());
            response.setKeterangan(updatedSuratPengantar.getKeterangan());

            // Set other fields in the response

            return response;
        } else {
            throw new EntityNotFoundException("SuratPengantar not found with id: " + id);
        }
    }

    public void deleteSuratPengantar(Long id) {
        suratPengantarRepository.deleteById(id);
    }

    public List<SuratPengantarResponse> searchSuratPengantar(String noSuratPengantar, String noKtp, String noKk, String namaLengkap) {
        List<SuratPengantar> suratPengantars = suratPengantarRepository.findByNoSuratPengantarOrPendudukNoKtpOrPendudukKartuKeluargaNoKKOrPendudukNamaLengkap(noSuratPengantar, noKtp, noKk, namaLengkap);
        List<SuratPengantarResponse> responses = new ArrayList<>();
        for (SuratPengantar suratPengantar : suratPengantars) {
            SuratPengantarResponse response = mapSuratPengantarToResponse(suratPengantar);
            responses.add(response);
        }
        return responses;
    }

    public Page<SuratPengantarResponse> getAllSuratPengantars(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<SuratPengantar> suratPengantarPage = suratPengantarRepository.findAll(pageable);
        return suratPengantarPage.map(this::mapSuratPengantarToResponse);
    }

//    private SuratPengantarResponse mapSuratPengantarToResponse(SuratPengantar suratPengantar) {
//        SuratPengantarResponse response = new SuratPengantarResponse();
//        response.setId(suratPengantar.getId());
//        response.setNoSuratPengantar(suratPengantar.getNoSuratPengantar());
//        response.setTanggalSurat(suratPengantar.getTanggalSurat());
//        response.setKeterangan(suratPengantar.getKeterangan());
//
//        // Now, if SuratPengantar has a reference to Penduduk, you can include Penduduk-related fields in the response
//        if (suratPengantar.getPenduduk() != null) {
//            Penduduk penduduk = suratPengantar.getPenduduk();
//            response.setNoKk(penduduk.getKartuKeluarga().getNoKK());
//            response.setNoKtp(penduduk.getNoKtp());
//            response.setNamaLengkap(penduduk.getNamaLengkap());
//            response.setTanggallahir(penduduk.getTanggalLahir());
//            response.setJeniskelamin(penduduk.getJenisKelamin());
//            // Map other Penduduk-related fields as needed
//        }
//
//        return response;
//    }

    private SuratPengantarResponse mapSuratPengantarToResponse(SuratPengantar suratPengantar) {
        SuratPengantarResponse response = new SuratPengantarResponse();
        response.setId(suratPengantar.getId());
        response.setNoSuratPengantar(suratPengantar.getNoSuratPengantar());
        response.setNoKk(suratPengantar.getPenduduk().getKartuKeluarga().getNoKK());
        response.setNoKtp(suratPengantar.getPenduduk().getNoKtp());
        response.setNamaLengkap(suratPengantar.getPenduduk().getNamaLengkap());
        response.setTanggallahir(suratPengantar.getPenduduk().getTanggalLahir());
        response.setJeniskelamin(suratPengantar.getPenduduk().getJenisKelamin());
        response.setTanggalSurat(suratPengantar.getTanggalSurat());
        response.setKeterangan(suratPengantar.getKeterangan());

        // Map other fields as needed

        return response;
    }

}