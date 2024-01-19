package com.apps.erte.controller;

import com.apps.erte.dto.request.pindah.PendudukPindahRequest;
import com.apps.erte.dto.response.pindah.PendudukPindahResponse;
import com.apps.erte.service.PendudukPindahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("app/penduduk-pindah")
@CrossOrigin
public class PendudukPindahController {
    @Autowired
    private PendudukPindahService pendudukPindahService;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PendudukPindahResponse> createPendudukPindah(@RequestBody PendudukPindahRequest request) {
        PendudukPindahResponse response = pendudukPindahService.createPendudukPindah(request);
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PendudukPindahResponse> updatePendudukPindah(
            @PathVariable Long id, @RequestBody PendudukPindahRequest request) {
        PendudukPindahResponse response = pendudukPindahService.updatePendudukPindah(id, request);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping()
    public List<PendudukPindahResponse> getAllPendudukPindah(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
//        List<PendudukPindahResponse> pendudukPindahResponseList = pendudukPindahService.getAllPendudukPindah(pageable, Sort.unsorted()).getContent();
//        Map<String, List<PendudukPindahResponse>> response = new HashMap<>();
//        response.put("pendudukPindah", pendudukPindahResponseList);
        return pendudukPindahService.getAllPendudukPindah(pageable, Sort.unsorted()).getContent();
    }
    @GetMapping("/search")
    public  List<PendudukPindahResponse>searchPendudukPindah(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id,asc") String sort) {

        String[] sortProperties = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortProperties.length > 1 && sortProperties[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, direction, sortProperties[0]);
        return pendudukPindahService.searchPendudukPindah(keyword, pageable).getContent();
    }
    @GetMapping("/{id}")
    public ResponseEntity<PendudukPindahResponse> getPendudukPindahById(@PathVariable Long id) {
        PendudukPindahResponse pendudukPindahResponse = pendudukPindahService.getPendudukPindahById(id);

        if (pendudukPindahResponse != null) {
            return new ResponseEntity<>(pendudukPindahResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePendudukPindah(@PathVariable Long id) {
        try {
            pendudukPindahService.deletePendudukPindah(id);
            return new ResponseEntity<>("Penduduk Pindah dengan ID " + id + " berhasil dihapus.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Gagal menghapus Penduduk Pindah dengan ID " + id + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
