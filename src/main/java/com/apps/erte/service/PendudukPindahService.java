package com.apps.erte.service;

import com.apps.erte.dto.request.pindah.PendudukPindahRequest;
import com.apps.erte.dto.response.pindah.PendudukPindahResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.pindah.DetailPendudukPindah;
import com.apps.erte.entity.pindah.PendudukPindah;
import com.apps.erte.entity.pindah.SuratKeteranganPindah;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.repository.pindah.DetailPendudukPindahRepository;
import com.apps.erte.repository.pindah.PendudukPindahRepository;
import com.apps.erte.repository.pindah.SuratPindahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class PendudukPindahService {
    private final PendudukPindahRepository pendudukPindahRepository;
    private final SuratPindahRepository suratPindahRepository;
    private final DetailPendudukPindahRepository detailPendudukPindahRepository;
    private final PendudukRepository pendudukRepository;

    @Autowired
    public PendudukPindahService(PendudukPindahRepository pendudukPindahRepository, SuratPindahRepository suratPindahRepository, DetailPendudukPindahRepository detailPendudukPindahRepository, PendudukRepository pendudukRepository) {
        this.pendudukPindahRepository = pendudukPindahRepository;
        this.suratPindahRepository = suratPindahRepository;
        this.detailPendudukPindahRepository = detailPendudukPindahRepository;
        this.pendudukRepository = pendudukRepository;
    }
    public PendudukPindahResponse createPendudukPindah(PendudukPindahRequest request) {
        // Validasi nomor surat pindah unique
        if (suratPindahRepository.existsByNoSuratPindah(request.getSuratKeteranganPindah().getNoSuratPindah())) {
            throw new RuntimeException("Nomor Surat Pindah sudah digunakan.");
        }

        PendudukPindah pendudukPindah = new PendudukPindah();
        pendudukPindah.setKodePindah(request.getKodePindah());
        pendudukPindah.setTanggalPindah(request.getTanggalPindah());
        pendudukPindah.setAlamatPindah(request.getAlamatPindah());

        SuratKeteranganPindah suratKeteranganPindah = new SuratKeteranganPindah();
        suratKeteranganPindah.setNoSuratPindah(request.getSuratKeteranganPindah().getNoSuratPindah());
        suratKeteranganPindah.setTanggalSuratPindah(request.getSuratKeteranganPindah().getTanggalSuratPindah());
        suratKeteranganPindah.setKeteranganPindah(request.getSuratKeteranganPindah().getKeteranganPindah());

        suratPindahRepository.save(suratKeteranganPindah);

        pendudukPindah.setSuratKeteranganPindah(suratKeteranganPindah);

        PendudukPindah savedPendudukPindah = pendudukPindahRepository.save(pendudukPindah);

        return mapToPendudukPindahResponse(savedPendudukPindah);
    }
    public PendudukPindahResponse updatePendudukPindah(Long id, PendudukPindahRequest request) {
        Optional<PendudukPindah> existingPendudukPindah = pendudukPindahRepository.findById(id);

        if (existingPendudukPindah.isEmpty()) {
            throw new RuntimeException("Penduduk Pindah dengan ID " + id + " tidak ditemukan.");
        }
        PendudukPindah pendudukPindah = existingPendudukPindah.get();
        pendudukPindah.setKodePindah(request.getKodePindah());
        pendudukPindah.setTanggalPindah(request.getTanggalPindah());
        pendudukPindah.setAlamatPindah(request.getAlamatPindah());

        SuratKeteranganPindah suratKeteranganPindah = pendudukPindah.getSuratKeteranganPindah();
        suratKeteranganPindah.setNoSuratPindah(request.getSuratKeteranganPindah().getNoSuratPindah());
        suratKeteranganPindah.setTanggalSuratPindah(request.getSuratKeteranganPindah().getTanggalSuratPindah());
        suratKeteranganPindah.setKeteranganPindah(request.getSuratKeteranganPindah().getKeteranganPindah());

        suratPindahRepository.save(suratKeteranganPindah);

        pendudukPindah.setSuratKeteranganPindah(suratKeteranganPindah);

        PendudukPindah updatedPendudukPindah = pendudukPindahRepository.save(pendudukPindah);

        return mapToPendudukPindahResponse(updatedPendudukPindah);
    }
    public Page<PendudukPindahResponse> getAllPendudukPindah(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<PendudukPindah> residentsPindahs = pendudukPindahRepository.findAll(pageableWithSort);
        return residentsPindahs.map(this::mapToPendudukPindahResponse);
    }
    public Page<PendudukPindahResponse> searchPendudukPindah(String keyword, Pageable pageable) {
        LocalDate tanggalPindah = null;
        LocalDate tanggalSuratPindah = null;

        try {
            // Attempt to parse dates from the keyword string
            tanggalPindah = LocalDate.parse(keyword);
            tanggalSuratPindah = LocalDate.parse(keyword);
        } catch (DateTimeParseException ignored) {
            // Ignore parsing errors, as the keyword might not be a date
        }
        return pendudukPindahRepository.search(keyword, tanggalPindah, tanggalSuratPindah, pageable)
                .map(this::mapToPendudukPindahResponse);
    }
    private PendudukPindahResponse mapToPendudukPindahResponse(PendudukPindah pendudukPindah) {
        PendudukPindahResponse response = new PendudukPindahResponse();
        response.setId(pendudukPindah.getId());
        response.setKodePindah(pendudukPindah.getKodePindah());
        response.setTanggalPindah(pendudukPindah.getTanggalPindah());
        response.setAlamatPindah(pendudukPindah.getAlamatPindah());

        // Ubah format tanggalSuratPindah ke "yyyy-MM-dd"
        if (pendudukPindah.getSuratKeteranganPindah() != null) {
            SuratKeteranganPindah suratKeteranganPindah = pendudukPindah.getSuratKeteranganPindah();
            SuratKeteranganPindah suratKeteranganPindahResponse = new SuratKeteranganPindah();
            suratKeteranganPindahResponse.setId(suratKeteranganPindah.getId());
            suratKeteranganPindahResponse.setNoSuratPindah(suratKeteranganPindah.getNoSuratPindah());
            suratKeteranganPindahResponse.setTanggalSuratPindah(suratKeteranganPindah.getTanggalSuratPindah());
            suratKeteranganPindahResponse.setKeteranganPindah(suratKeteranganPindah.getKeteranganPindah());

            response.setSuratKeteranganPindah(suratKeteranganPindahResponse);
        }
        return response;
    }
    public PendudukPindahResponse getPendudukPindahById(Long id) {
        Optional<PendudukPindah> pendudukPindah = pendudukPindahRepository.findById(id);
        if (pendudukPindah.isPresent()) {
            return mapToPendudukPindahResponse(pendudukPindah.get());
        } else {
            return null;
        }
    }

    public void deletePendudukPindah(Long id) {
        Optional<PendudukPindah> pendudukPindahOptional = pendudukPindahRepository.findById(id);
        if (pendudukPindahOptional.isPresent()) {
            PendudukPindah pendudukPindah = pendudukPindahOptional.get();

            // Hapus detail penduduk pindah
            // 1. Delete DetailPendudukPindah
            List<DetailPendudukPindah> details = detailPendudukPindahRepository.findByPendudukPindahId(id);
            for (DetailPendudukPindah detail : details) {
                Penduduk penduduk = detail.getPenduduk();
                penduduk.setNewStatusPenduduk(null);
                pendudukRepository.save(penduduk);
            }
            detailPendudukPindahRepository.deleteAll(details);

            // 3. Delete PendudukPindah
            pendudukPindahRepository.delete(pendudukPindah);

            // 4. Delete SuratKeteranganPindah
            SuratKeteranganPindah suratKeteranganPindah = pendudukPindah.getSuratKeteranganPindah();
            suratPindahRepository.delete(suratKeteranganPindah);
        }
    }

}
