package com.apps.erte.service;

import com.apps.erte.dto.request.SuratPengantarRequest;
import com.apps.erte.dto.request.meninggal.PendudukMeninggalRequest;
import com.apps.erte.dto.request.meninggal.SuratKeteranganMeninggalRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.dto.response.SuratPengantarResponse;
import com.apps.erte.dto.response.meninggal.PendudukMeninggalResponse;
import com.apps.erte.dto.response.meninggal.SuratKeteranganMeninggalResponse;
import com.apps.erte.dto.response.pindah.PendudukPindahResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.kematian.PendudukMeninggal;
import com.apps.erte.entity.kematian.SuratKeteranganMeninggal;
import com.apps.erte.entity.suratpengantar.SuratPengantar;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.repository.suratpengantar.SuratPengantarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        // Membuat objek surat pengantar dari request
        SuratPengantar suratPengantar = new SuratPengantar();
        suratPengantar.setNoSuratPengantar(request.getNoSuratPengantar());
        suratPengantar.setTanggalSurat(request.getTanggalSurat());
        suratPengantar.setKeperluan(request.getKeperluan());
        suratPengantar.setKeterangan(request.getKeterangan());
        Penduduk penduduk = pendudukRepository.findById(request.getPendudukId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penduduk not found"));
        // Menghubungkan penduduk dengan surat pengantar
        suratPengantar.setPenduduk(penduduk);
        // Menyimpan data ke database
        suratPengantar = suratPengantarRepository.save(suratPengantar);
        // Membuat response
        return buildSuratPengantarResponse(suratPengantar);
    }

    public SuratPengantarResponse updateSuratPengantar(Long id, SuratPengantarRequest request) {
        // Mencari data surat pengantar yang akan diupdate
        SuratPengantar suratPengantar = suratPengantarRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Surat Pengantar not found"));
        // Mengupdate data pada objek surat pengantar dari request
        suratPengantar.setNoSuratPengantar(request.getNoSuratPengantar());
        suratPengantar.setTanggalSurat(request.getTanggalSurat());
        suratPengantar.setKeperluan(request.getKeperluan());
        suratPengantar.setKeterangan(request.getKeterangan());
        // Mencari data penduduk yang akan diupdate
        Penduduk penduduk = pendudukRepository.findById(request.getPendudukId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penduduk not found"));
        // Menghubungkan penduduk dengan surat pengantar
        suratPengantar.setPenduduk(penduduk);
        // Menyimpan data ke database
        suratPengantar = suratPengantarRepository.save(suratPengantar);
        // Membuat response
        return buildSuratPengantarResponse(suratPengantar);
    }

    public void deleteSuratPengantar(Long id) {
        // Temukan surat pengantar yang akan dihapus
        SuratPengantar suratPengantar = suratPengantarRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Surat Pengantar not found"));
        // Hapus penduduk meninggal dari database
        suratPengantarRepository.delete(suratPengantar);
    }
    public Page<SuratPengantarResponse> getAllSuratPengantar(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<SuratPengantar> suratPengantars = suratPengantarRepository.findAll(pageableWithSort);
        return suratPengantars.map(this::buildSuratPengantarResponse);
    }
    public Page<SuratPengantarResponse> getSuratPengantarMobile(Long pendudukId, Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<SuratPengantar> suratPengantars = suratPengantarRepository.findByPendudukId(pendudukId, pageableWithSort);
        return suratPengantars.map(this::buildSuratPengantarResponse);
    }
    public Page<SuratPengantar> getSuratPengantarByNoSurat(String noSurat, Pageable pageable) {
        return suratPengantarRepository.findByNoSuratPengantar(noSurat, pageable);
    }
    public Page<SuratPengantarResponse> searchSuratPengantar(String keyword, Pageable pageable) {
        LocalDate tanggalSurat = null;
        try {
            // Attempt to parse dates from the keyword string
            tanggalSurat = LocalDate.parse(keyword);
        } catch (DateTimeParseException ignored) {
            // Ignore parsing errors, as the keyword might not be a date
        }
        return suratPengantarRepository.search(keyword, tanggalSurat, pageable)
                .map(this::buildSuratPengantarResponse);
    }

    private SuratPengantarResponse buildSuratPengantarResponse(SuratPengantar suratPengantar) {
        // Membuat response
        SuratPengantarResponse response = new SuratPengantarResponse();
        response.setId(suratPengantar.getId());
        response.setNoSuratPengantar(suratPengantar.getNoSuratPengantar());
        response.setTanggalSurat(suratPengantar.getTanggalSurat());
        response.setKeperluan(suratPengantar.getKeperluan());
        response.setKeterangan(suratPengantar.getKeterangan());
        // Mengisi data PendudukResponse
        PendudukResponse pendudukResponse = new PendudukResponse();
        Penduduk penduduk = suratPengantar.getPenduduk();
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
        response.setEmail(penduduk.getEmail());
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