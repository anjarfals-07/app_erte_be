package com.apps.erte.controller;

import com.apps.erte.dto.response.KartuKeluargaResponse;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.service.KartuKeluargaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/kartu-keluarga")
@CrossOrigin
public class KartuKeluargaController {
    private final KartuKeluargaService kartuKeluargaService;

    public KartuKeluargaController(KartuKeluargaService kartuKeluargaService) {
        this.kartuKeluargaService = kartuKeluargaService;
    }
    @GetMapping()
    public Map<String, List<KartuKeluargaResponse>> getAllKartuKeluarga(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<KartuKeluargaResponse> kartuKeluargaResponseList = kartuKeluargaService.findAll(pageable, Sort.unsorted()).getContent();
        Map<String, List<KartuKeluargaResponse>> response = new HashMap<>();
        response.put("kartuKeluarga", kartuKeluargaResponseList);
        return response;
    }

    @GetMapping("/{noKK}")
    public KartuKeluargaResponse getKartuKeluargaByNoKK(@PathVariable String noKK) {
        return kartuKeluargaService.getKartuKeluargaByNoKK(noKK);
    }
}
