package com.apps.erte.controller;

import com.apps.erte.dto.request.PendudukRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.service.PendudukService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/app/penduduk")
public class PendudukController {
    private final PendudukService pendudukService;

    @Autowired
    public PendudukController(PendudukService pendudukService) {
        this.pendudukService = pendudukService;
    }
    @GetMapping
    public Page<PendudukResponse> getAllPenduduk(
            @PageableDefault Pageable pageable, @SortDefault Sort sort) {
        if (sort.isSorted()) {
            return pendudukService.findAll(pageable, sort);
        } else {
            return pendudukService.findAll(pageable, Sort.unsorted());
        }
    }
//    @GetMapping
//    public List<ResidentResponse> getAllResidents(){
//        return residentService.getAllResidents();
//    }

    @GetMapping("/{id}")
    public PendudukResponse getPendudukById(@PathVariable Long id) {
        return pendudukService.getPendudukById(id);
    }
    @PostMapping("/create")
    public PendudukResponse createPenduduk(@RequestBody PendudukRequest request) {
        return pendudukService.createPenduduk(request);
    }

    @PutMapping("/update/{id}")
    public PendudukResponse updatePenduduk(@PathVariable Long id, PendudukRequest request){
        return pendudukService.updatePenduduk(id,request);
    }
}
