package com.apps.erte.controller;

import com.apps.erte.dto.request.meninggal.PendudukMeninggalRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.dto.response.meninggal.PendudukMeninggalResponse;
import com.apps.erte.dto.response.pindah.PendudukPindahResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.kematian.PendudukMeninggal;
import com.apps.erte.service.PendudukMeninggalService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("app/penduduk-meninggal")
@CrossOrigin
public class PendudukMeninggalController {

    @Autowired
    private PendudukMeninggalService pendudukMeninggalService;

    @GetMapping()
    public List<PendudukMeninggalResponse> getAllMeninggal(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return pendudukMeninggalService.getAllPendudukMeninggal(pageable, Sort.unsorted()).getContent();
    }
    @PostMapping
    public ResponseEntity<PendudukMeninggalResponse> createPendudukMeninggal(
            @RequestBody PendudukMeninggalRequest pendudukMeninggalRequest) {
        PendudukMeninggalResponse response = pendudukMeninggalService.createPendudukMeninggal(pendudukMeninggalRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/penduduk-meninggal/{kodeMeninggal}")
    public ResponseEntity<Page<PendudukMeninggal>> getPendudukmeninggalByCode(
            @PathVariable String kodeMeninggal,
            @PageableDefault(size = 10, page = 0, sort = "tanggalWafat", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PendudukMeninggal> pendudukMeninggals = pendudukMeninggalService.getPendudukMeninggalByCode(kodeMeninggal, pageable);
        return ResponseEntity.ok(pendudukMeninggals);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PendudukMeninggalResponse> updatePendudukMeninggal(
            @PathVariable Long id,
            @RequestBody PendudukMeninggalRequest pendudukMeninggalRequest) {
        PendudukMeninggalResponse response = pendudukMeninggalService.updatePendudukMeninggal(id, pendudukMeninggalRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePendudukMeninggal(@PathVariable Long id) {
        // Hapus penduduk meninggal dan reset newStatusPenduduk menjadi NULL
        pendudukMeninggalService.deletePendudukMeninggal(id);
        return new ResponseEntity<>("Penduduk Meninggal deleted successfully", HttpStatus.OK);
    }
}
