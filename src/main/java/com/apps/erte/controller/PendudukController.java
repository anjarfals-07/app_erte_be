package com.apps.erte.controller;

import com.apps.erte.dto.request.KartuKeluargaRequest;
import com.apps.erte.dto.request.PendudukRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.service.PendudukService;
import com.apps.erte.util.ApiError;
import com.apps.erte.util.FileValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/app/penduduk")
@CrossOrigin
public class PendudukController {
    private final PendudukService pendudukService;
    @Autowired
    public PendudukController(PendudukService pendudukService) {
        this.pendudukService = pendudukService;
    }

    @GetMapping()
    public Map<String, List<PendudukResponse>> getAllPenduduk(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<PendudukResponse> pendudukList = pendudukService.findAll(pageable, Sort.unsorted()).getContent();
        Map<String, List<PendudukResponse>> response = new HashMap<>();
        response.put("penduduk", pendudukList);
        return response;
    }

    @GetMapping("/{id}")
    public PendudukResponse getPendudukById(@PathVariable Long id) {
        return pendudukService.getPendudukById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createPenduduk(@ModelAttribute PendudukRequest request) {
        System.out.println("penduduk req {}" + request);
        try {
            PendudukResponse response = pendudukService.create(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            List<String> errors = List.of(Objects.requireNonNull(e.getMessage()));
            ApiError error = new ApiError(HttpStatus.CONFLICT,  "Duplicate noKtp", errors);
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        } catch (FileValidationException e) {
            List<String> errors = List.of(e.getMessage());
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Invalid file uploaded",errors);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            List<String> errors = List.of(e.getMessage());
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", errors);
            return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "/{id}", consumes  = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updatePenduduk( @PathVariable Long id,
                                                  @ModelAttribute PendudukRequest request,
                                                  @RequestHeader HttpHeaders headers) {
        System.out.println("Request Headers: " + headers);
        PendudukResponse updatedPenduduk = pendudukService.updatePenduduk(id, request);

        if (updatedPenduduk != null) {
            return ResponseEntity.ok(updatedPenduduk);
        } else {
            // Jika penduduk tidak ditemukan, bisa mengembalikan respons error sesuai kebutuhan
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Penduduk not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePenduduk(@PathVariable Long id) {
        try {
            pendudukService.deletePenduduk(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            List<String> errors = List.of(e.getMessage());
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", errors);
            return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
