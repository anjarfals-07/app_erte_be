package com.apps.erte.controller;

import com.apps.erte.dto.request.pindah.DetailPindahRequest;
import com.apps.erte.dto.response.pindah.DetailPindahResponse;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.service.DetailPindahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/app/detail-pindah")
public class DetailPindahController {
    private final DetailPindahService detailPindahService;
    @Autowired
    public DetailPindahController(DetailPindahService detailPindahService) {
        this.detailPindahService = detailPindahService;
    }
    @PostMapping
    public ResponseEntity<DetailPindahResponse> createDetailPindah(@RequestBody DetailPindahRequest request) {
        DetailPindahResponse response = detailPindahService.createDetailPindah(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
    public List<DetailPindahResponse> getAllDetailPindah(@RequestParam(required = false) String kodePindah) {
        return detailPindahService.getAllDetailPindah(kodePindah);
    }
//    @GetMapping("/get-penduduk-by-no-kk")
//    public List<PendudukResponse> getPendudukByNoKK(@RequestParam String noKK) {
//        return detailPindahService.getPendudukByNoKK(noKK);
//    }
    @DeleteMapping("/{detailPindahId}")
    public ResponseEntity<String> deleteDetailPindah(@PathVariable Long detailPindahId) {
        detailPindahService.deleteDetailPindah(detailPindahId);
        return new ResponseEntity<>("Detail Pindah berhasil dihapus", HttpStatus.OK);
    }
}
