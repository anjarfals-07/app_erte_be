package com.apps.erte.service;

import com.apps.erte.dto.request.meninggal.PendudukMeninggalRequest;
import com.apps.erte.dto.request.meninggal.SuratKeteranganMeninggalRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.dto.response.meninggal.PendudukMeninggalResponse;
import com.apps.erte.dto.response.meninggal.SuratKeteranganMeninggalResponse;
import com.apps.erte.dto.response.pindah.PendudukPindahResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.kematian.PendudukMeninggal;
import com.apps.erte.entity.kematian.SuratKeteranganMeninggal;
import com.apps.erte.entity.pindah.PendudukPindah;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.repository.meninggal.PendudukMeninggalRepository;
import com.apps.erte.repository.meninggal.SuratKeteranganMeninggalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PendudukMeninggalService {
    @Autowired
    private final PendudukMeninggalRepository pendudukMeninggalRepository;
    @Autowired
    private SuratKeteranganMeninggalRepository suratKeteranganMeninggalRepository;
    @Autowired
    private final PendudukRepository pendudukRepository;

    public PendudukMeninggalService(PendudukMeninggalRepository pendudukMeninggalRepository, SuratKeteranganMeninggalRepository suratKeteranganMeninggalRepository, PendudukRepository pendudukRepository) {
        this.pendudukMeninggalRepository = pendudukMeninggalRepository;
        this.suratKeteranganMeninggalRepository = suratKeteranganMeninggalRepository;
        this.pendudukRepository = pendudukRepository;
    }

    public PendudukMeninggalResponse createPendudukMeninggal(PendudukMeninggalRequest request) {
        // Membuat objek PendudukMeninggal dari request
        PendudukMeninggal pendudukMeninggal = new PendudukMeninggal();
        pendudukMeninggal.setKodeMeninggal(request.getKodeMeninggal());
        pendudukMeninggal.setTanggalWafat(request.getTanggalWafat());
        pendudukMeninggal.setPenyebab(request.getPenyebab());
        // Membuat objek SuratKeteranganMeninggal dari request
        SuratKeteranganMeninggalRequest suratRequest = request.getSuratKeteranganMeninggalRequest();
        SuratKeteranganMeninggal suratKeteranganMeninggal = new SuratKeteranganMeninggal();
        suratKeteranganMeninggal.setNoSuratMeninggal(suratRequest.getNoSuratMeninggal());
        suratKeteranganMeninggal.setTanggalSuratMeninggal(suratRequest.getTanggalSuratMeninggal());
        suratKeteranganMeninggal.setKeteranganSuratMeninggal(suratRequest.getKeteranganSuratMeninggal());
        suratKeteranganMeninggal = suratKeteranganMeninggalRepository.save(suratKeteranganMeninggal);
        // Menghubungkan surat keterangan dengan penduduk meninggal
        pendudukMeninggal.setSuratKeteranganMeninggal(suratKeteranganMeninggal);
        Penduduk penduduk = pendudukRepository.findById(request.getPendudukId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penduduk not found"));
        penduduk.setNewStatusPenduduk("MENINGGAL");
        pendudukRepository.save(penduduk);

        // Menghubungkan penduduk dengan penduduk meninggal
        pendudukMeninggal.setPenduduk(penduduk);
        // Menyimpan data ke database
        pendudukMeninggal = pendudukMeninggalRepository.save(pendudukMeninggal);
        // Membuat response
        return buildPendudukMeninggalResponse(pendudukMeninggal);
    }

    public Page<PendudukMeninggalResponse> getAllPendudukMeninggal(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<PendudukMeninggal> pendudukMeninggals = pendudukMeninggalRepository.findAll(pageableWithSort);
        return pendudukMeninggals.map(this::buildPendudukMeninggalResponse);
    }
    public Page<PendudukMeninggal> getPendudukMeninggalByCode(String kodeMeninggal, Pageable pageable) {
        return pendudukMeninggalRepository.findByKodeMeninggal(kodeMeninggal, pageable);
    }

    public PendudukMeninggalResponse updatePendudukMeninggal(Long id, PendudukMeninggalRequest request) {
        // Mencari data penduduk meninggal yang akan diupdate
        PendudukMeninggal pendudukMeninggal = pendudukMeninggalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penduduk Meninggal not found"));

        // Mengupdate data pada objek PendudukMeninggal dari request
        pendudukMeninggal.setKodeMeninggal(request.getKodeMeninggal());
        pendudukMeninggal.setTanggalWafat(request.getTanggalWafat());
        pendudukMeninggal.setPenyebab(request.getPenyebab());

        // Mengupdate data pada objek SuratKeteranganMeninggal
        SuratKeteranganMeninggalRequest suratRequest = request.getSuratKeteranganMeninggalRequest();
        SuratKeteranganMeninggal suratKeteranganMeninggal = pendudukMeninggal.getSuratKeteranganMeninggal();
        suratKeteranganMeninggal.setNoSuratMeninggal(suratRequest.getNoSuratMeninggal());
        suratKeteranganMeninggal.setTanggalSuratMeninggal(suratRequest.getTanggalSuratMeninggal());
        suratKeteranganMeninggal.setKeteranganSuratMeninggal(suratRequest.getKeteranganSuratMeninggal());

        // Menghubungkan surat keterangan dengan penduduk meninggal
        pendudukMeninggal.setSuratKeteranganMeninggal(suratKeteranganMeninggal);

        // Mencari data penduduk yang akan diupdate
        Penduduk penduduk = pendudukRepository.findById(request.getPendudukId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penduduk not found"));

        // Mengupdate newStatus penduduk menjadi null pada penduduk sebelumnya
        if (pendudukMeninggal.getPenduduk() != null) {
            Penduduk previousPenduduk = pendudukMeninggal.getPenduduk();
            previousPenduduk.setNewStatusPenduduk(null);
            pendudukRepository.save(previousPenduduk);
        }

        // Mengupdate newStatus penduduk menjadi MENINGGAL pada penduduk yang akan diupdate
        penduduk.setNewStatusPenduduk("MENINGGAL");
        pendudukRepository.save(penduduk);

        // Menghubungkan penduduk dengan penduduk meninggal
        pendudukMeninggal.setPenduduk(penduduk);

        // Menyimpan data ke database
        pendudukMeninggal = pendudukMeninggalRepository.save(pendudukMeninggal);

        // Membuat response
        return buildPendudukMeninggalResponse(pendudukMeninggal);
    }

    public void deletePendudukMeninggal(Long id) {
        // Temukan penduduk meninggal yang akan dihapus
        PendudukMeninggal pendudukMeninggal = pendudukMeninggalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penduduk Meninggal not found"));

        // Reset newStatusPenduduk menjadi NULL untuk penduduk yang dihapus
        Penduduk penduduk = pendudukMeninggal.getPenduduk();
        penduduk.setNewStatusPenduduk(null);
        pendudukRepository.save(penduduk);

        // Hapus penduduk meninggal dari database
        pendudukMeninggalRepository.delete(pendudukMeninggal);
    }
    //    Response
private PendudukMeninggalResponse buildPendudukMeninggalResponse(PendudukMeninggal pendudukMeninggal) {
    // Membuat response
    PendudukMeninggalResponse response = new PendudukMeninggalResponse();
    response.setId(pendudukMeninggal.getId());
    response.setKodeMeninggal(pendudukMeninggal.getKodeMeninggal());
    response.setTanggalWafat(pendudukMeninggal.getTanggalWafat());
    response.setPenyebab(pendudukMeninggal.getPenyebab());
    // Mengisi data SuratKeteranganMeninggalResponse
    SuratKeteranganMeninggalResponse suratResponse = new SuratKeteranganMeninggalResponse();
    SuratKeteranganMeninggal suratKeteranganMeninggal = pendudukMeninggal.getSuratKeteranganMeninggal();
    suratResponse.setId(suratKeteranganMeninggal.getId());
    suratResponse.setNoSuratMeninggal(suratKeteranganMeninggal.getNoSuratMeninggal());
    suratResponse.setTanggalSuratMeninggal(suratKeteranganMeninggal.getTanggalSuratMeninggal());
    suratResponse.setKeteranganSuratMeninggal(suratKeteranganMeninggal.getKeteranganSuratMeninggal());
    response.setSuratKeteranganMeninggal(suratResponse);
    // Mengisi data PendudukResponse
    PendudukResponse pendudukResponse = new PendudukResponse();
    Penduduk penduduk = pendudukMeninggal.getPenduduk();
    // Menambahkan data lainnya sesuai kebutuhan
    response.setPenduduk(buildPendudukResponse(penduduk));
    return response;
   }

    private PendudukResponse buildPendudukResponse(Penduduk penduduk) {
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
        // Tambahan data lainnya sesuai kebutuhan
        return response;
    }
}
