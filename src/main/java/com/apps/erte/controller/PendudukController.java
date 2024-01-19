package com.apps.erte.controller;

import com.apps.erte.dto.request.KartuKeluargaRequest;
import com.apps.erte.dto.request.PendudukRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.dto.response.SuratPengantarResponse;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.entity.user.User;
import com.apps.erte.repository.PendudukRepository;
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
import java.util.*;

@RestController
@RequestMapping("/app/penduduk")
@CrossOrigin
public class PendudukController {
    private final PendudukService pendudukService;
    private final PendudukRepository pendudukRepository;

    @Autowired
    public PendudukController(PendudukService pendudukService,
                              PendudukRepository pendudukRepository) {
        this.pendudukService = pendudukService;
        this.pendudukRepository = pendudukRepository;
    }

    @GetMapping()
    public List<PendudukResponse> getAllPenduduk(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return pendudukService.findAll(pageable, Sort.unsorted()).getContent();
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
    @GetMapping("/get-all-penduduk")
    public List<PendudukResponse> getDataPenduduk() {
        return pendudukService.getAllPenduduk();
    }
    @GetMapping("/get-penduduk-by-no-kk")
    public List<PendudukResponse> getPendudukByNoKK(@RequestParam String noKK) {
        return pendudukService.getPendudukByNoKK(noKK);
    }
    @GetMapping("/get-penduduk-by-noktp")
    public List<PendudukResponse> getPendudukByNoKtp(@RequestParam String noKtp) {
        return pendudukService.getPendudukByNoKtp(noKtp);
    }
    @GetMapping("/search")
    public List<PendudukResponse> searchPenduduk(
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
        return pendudukService.searchPenduduk(keyword, pageable).getContent();
    }

    @GetMapping("/check-ktp/{noKtp}")
    public ResponseEntity<Boolean> checkKtpAvailability(@PathVariable String noKtp) {
        Optional<Penduduk> existingUser = pendudukRepository.findByNoKtp(noKtp);
        return ResponseEntity.ok(existingUser.isEmpty());
    }


}
