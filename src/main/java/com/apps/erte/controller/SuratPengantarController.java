package com.apps.erte.controller;

import com.apps.erte.dto.request.SuratPengantarRequest;
import com.apps.erte.dto.request.meninggal.PendudukMeninggalRequest;
import com.apps.erte.dto.response.SuratPengantarResponse;
import com.apps.erte.dto.response.meninggal.PendudukMeninggalResponse;
import com.apps.erte.entity.kematian.PendudukMeninggal;
import com.apps.erte.entity.suratpengantar.SuratPengantar;
import com.apps.erte.service.SuratPengantarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surat-pengantar")
@CrossOrigin
public class SuratPengantarController {

    @Autowired
    private SuratPengantarService suratPengantarService;

    @GetMapping()
    public List<SuratPengantarResponse> getAllSuratPengantar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return suratPengantarService.getAllSuratPengantar(pageable, Sort.unsorted()).getContent();
    }
    @PostMapping
    public ResponseEntity<SuratPengantarResponse> createSuratPengantar(
            @RequestBody SuratPengantarRequest suratPengantarRequest) {
        SuratPengantarResponse response = suratPengantarService.createSuratPengantar(suratPengantarRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/surat-pengantar/{noSurat}")
    public ResponseEntity<Page<SuratPengantar>> getSuratPengantarByNoSurat(
            @PathVariable String noSurat,
            @PageableDefault(size = 10, page = 0, sort = "tanggalSurat", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SuratPengantar> suratPengantars = suratPengantarService.getSuratPengantarByNoSurat(noSurat, pageable);
        return ResponseEntity.ok(suratPengantars);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuratPengantarResponse> updateSuratPengantar(
            @PathVariable Long id,
            @RequestBody SuratPengantarRequest suratPengantarRequest) {
        SuratPengantarResponse response = suratPengantarService.updateSuratPengantar(id, suratPengantarRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSuratPengantar(@PathVariable Long id) {
        // Hapus penduduk meninggal dan reset newStatusPenduduk menjadi NULL
        suratPengantarService.deleteSuratPengantar(id);
        return new ResponseEntity<>("Penduduk Meninggal deleted successfully", HttpStatus.OK);
    }
}