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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> createPenduduk( @ModelAttribute PendudukRequest request) {
        System.out.println("penduduk req {}" + request);
        try {
            PendudukResponse response = pendudukService.create(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            ApiError error = new ApiError(HttpStatus.CONFLICT.value(), e.getMessage(), "Duplicate noKtp", new Date());
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        } catch (FileValidationException e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "Invalid file uploaded", new Date());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage(), new Date());
            return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public PendudukResponse updatePenduduk(@PathVariable Long id, PendudukRequest request) {
        return pendudukService.update(id, request);
    }
}
