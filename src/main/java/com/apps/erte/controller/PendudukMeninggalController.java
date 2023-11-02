package com.apps.erte.controller;

import com.apps.erte.dto.request.PendudukMeninggalRequest;
import com.apps.erte.dto.response.PendudukMeninggalResponse;
import com.apps.erte.entity.kematian.DetailPendudukMeninggal;
import com.apps.erte.entity.kematian.PendudukMeninggal;
import com.apps.erte.service.PendudukMeninggalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("app/penduduk-meninggal")
public class PendudukMeninggalController {

    @Autowired
    private PendudukMeninggalService pendudukMeninggalService;

    @PostMapping("/request")
    public ResponseEntity<PendudukMeninggalResponse> request(@RequestBody PendudukMeninggalRequest requestDTO) {
        PendudukMeninggalResponse responseDTO = pendudukMeninggalService.requestPendudukMeninggal(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/penduduk-meninggal/{kodeMeninggal}")
    public ResponseEntity<Page<PendudukMeninggal>> getPendudukmeninggalByCode(
            @PathVariable String kodeMeninggal,
            @PageableDefault(size = 10, page = 0, sort = "tanggalWafat", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PendudukMeninggal> pendudukMeninggals = pendudukMeninggalService.getPendudukMeninggalByCode(kodeMeninggal, pageable);
        return ResponseEntity.ok(pendudukMeninggals);
    }

    @GetMapping("/detail-penduduk-meninggal/{pendudukMeninggalId}")
    public ResponseEntity<Page<DetailPendudukMeninggal>> getDetailPendudukMeninggalByMeninggalId(
            @PathVariable Long pendudukMeninggalId,
            @PageableDefault(size = 10, page = 0, sort = "resident.namaLengkap", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<DetailPendudukMeninggal> detailPendudukMeninggals = pendudukMeninggalService.getDetailPendudukMeninggalByKodeMeninggal(pendudukMeninggalId, pageable);
        return ResponseEntity.ok(detailPendudukMeninggals);
    }
}
