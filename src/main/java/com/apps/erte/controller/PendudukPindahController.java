package com.apps.erte.controller;

import com.apps.erte.dto.request.PendudukPindahRequest;
import com.apps.erte.dto.response.PendudukPindahResponse;
import com.apps.erte.entity.pindah.DetailPendudukPindah;
import com.apps.erte.entity.pindah.PendudukPindah;
import com.apps.erte.service.PendudukPindahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("app/penduduk-pindah")
public class PendudukPindahController {
    @Autowired
    private PendudukPindahService pendudukPindahService;

    @PostMapping("/request")
    public ResponseEntity<PendudukPindahResponse> requestPindah(@RequestBody PendudukPindahRequest requestDTO) {
        PendudukPindahResponse responseDTO = pendudukPindahService.requestPendudukPindah(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/penduduk-pindah/{kodePindah}")
    public ResponseEntity<Page<PendudukPindah>> getResidentsMovesByMoveCode(
            @PathVariable String kodePindah,
            @PageableDefault(size = 10, page = 0, sort = "tanggalPindah", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PendudukPindah> pendudukPindahs = pendudukPindahService.getPendudukPindahByCode(kodePindah, pageable);
        return ResponseEntity.ok(pendudukPindahs);
    }

    @GetMapping("/detail-penduduk-pindah/{pendudukPindahId}")
    public ResponseEntity<Page<DetailPendudukPindah>> getDetailsResidentMoveByResidentsMoveId(
            @PathVariable Long pendudukPindahId,
            @PageableDefault(size = 10, page = 0, sort = "resident.namaLengkap", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<DetailPendudukPindah> detailPendudukPindahs = pendudukPindahService.getDetailPendudukPindahByKodePindah(pendudukPindahId, pageable);
        return ResponseEntity.ok(detailPendudukPindahs);
    }
}
