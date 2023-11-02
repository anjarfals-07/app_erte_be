package com.apps.erte.controller;

import com.apps.erte.dto.request.SuratPengantarRequest;
import com.apps.erte.dto.response.SuratPengantarResponse;
import com.apps.erte.service.SuratPengantarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surat-pengantar")
public class SuratPengantarController {

    @Autowired
    private SuratPengantarService suratPengantarService;

    @PostMapping("/create")
    public ResponseEntity<SuratPengantarResponse> createSuratPengantar(@RequestBody SuratPengantarRequest request) {
        SuratPengantarResponse response = suratPengantarService.createSuratPengantar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SuratPengantarResponse> updateSuratPengantar(@PathVariable Long id, @RequestBody SuratPengantarRequest request) {
        SuratPengantarResponse response = suratPengantarService.updateSuratPengantar(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSuratPengantar(@PathVariable Long id) {
        suratPengantarService.deleteSuratPengantar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SuratPengantarResponse>> searchSuratPengantar(
            @RequestParam(required = false) String noSuratPengantar,
            @RequestParam(required = false) String noKtp,
            @RequestParam(required = false) String noKk,
            @RequestParam(required = false) String namaLengkap) {
        List<SuratPengantarResponse> responses = suratPengantarService.searchSuratPengantar(noSuratPengantar, noKtp, noKk, namaLengkap);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<SuratPengantarResponse>> getAllSuratPengantars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<SuratPengantarResponse> suratPengantarPage = suratPengantarService.getAllSuratPengantars(page, size, sortBy);
        return new ResponseEntity<>(suratPengantarPage, HttpStatus.OK);
    }
}