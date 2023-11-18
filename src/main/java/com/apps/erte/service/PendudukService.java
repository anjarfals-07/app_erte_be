package com.apps.erte.service;
import com.apps.erte.config.MinioConfig;
import com.apps.erte.dto.request.PendudukRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.entity.KartuKeluarga;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.repository.KartuKeluargaRepository;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.util.FileValidationException;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
@Service
public class PendudukService {
    private final PendudukRepository pendudukRepository;
    private final KartuKeluargaRepository kartuKeluargaRepository;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private  MinioConfig minioConfig;

    @Autowired
    public PendudukService(PendudukRepository pendudukRepository, KartuKeluargaRepository kartuKeluargaRepository, MinioClient minioClient) {
        this.pendudukRepository = pendudukRepository;
        this.kartuKeluargaRepository = kartuKeluargaRepository;
    }
    public Page<PendudukResponse> findAll(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Penduduk> residents = pendudukRepository.findAll(pageableWithSort);
        return residents.map(this::mapResidentToResponse);
    }
    public PendudukResponse getPendudukById(Long id){
        Optional<Penduduk>residentOptional = pendudukRepository.findById(id);
        if (residentOptional.isPresent()){
            Penduduk penduduk = residentOptional.get();
            return mapResidentToResponse(penduduk);
        }
        return null;
    }
    public PendudukResponse create(PendudukRequest request)  {

        if (pendudukRepository.existsByNoKtp(request.getNoKtp())) {
            throw new DataIntegrityViolationException("NoKtp already exists");
        }
        // Cek apakah FamilyCard dengan nomor KK sudah ada
        KartuKeluarga kartuKeluarga = null;

        if (request.getKartuKeluarga() != null) {
            kartuKeluarga = kartuKeluargaRepository.findByNoKK(request.getKartuKeluarga().getNoKK());

            if (kartuKeluarga == null) {
                kartuKeluarga = new KartuKeluarga();
                kartuKeluarga.setNoKK(request.getKartuKeluarga().getNoKK());
                kartuKeluarga.setNamaKepalaKeluarga(request.getKartuKeluarga().getNamaKepalaKeluarga());
                kartuKeluarga = kartuKeluargaRepository.save(kartuKeluarga); // Save the KartuKeluarga before using it in Penduduk
            }
        }

        Penduduk penduduk = new Penduduk();
        // Set KartuKeluarga in Penduduk
        penduduk.setKartuKeluarga(kartuKeluarga);
        mapRequestToResident(request, penduduk);

        if (request.getFoto() != null && !request.getFoto().isEmpty()) {
            // Check if the file is a regular file
            if (request.getFoto().getOriginalFilename() != null && request.getFoto().getContentType() != null) {
                try {
                    String fotoUrl = saveFotoToMinio(request.getFoto(), penduduk.getNoKtp());
                    penduduk.setFotoUrl(fotoUrl);
                } catch (RuntimeException e) {
                    throw new FileValidationException("Failed to save the photo", e);
                }
            } else {
                throw new FileValidationException("Invalid file uploaded");
            }
        }
        System.out.println("penduduk req {}" + request);

        Penduduk savedResident = pendudukRepository.save(penduduk);
        // Log saved resident details
        System.out.println("Saved Resident Details: " + savedResident);

        return mapResidentToResponse(savedResident);
    }
    public PendudukResponse update(Long id, PendudukRequest request) {
        Optional<Penduduk> residentOptional = pendudukRepository.findById(id);
        if (residentOptional.isPresent()) {
            Penduduk penduduk = residentOptional.get();
            mapRequestToResident(request, penduduk);
            Penduduk updatedResident = pendudukRepository.save(penduduk);
            return mapResidentToResponse(updatedResident);
        }
        return null; // atau throw exception jika tidak ditemukan
    }
    public void deletePenduduk(Long id) {
        pendudukRepository.deleteById(id);
    }
    private PendudukResponse mapResidentToResponse(Penduduk resident) {
        PendudukResponse response = new PendudukResponse();
        response.setId(resident.getId());
        response.setNoKtp(resident.getNoKtp());
        response.setKartuKeluarga(resident.getKartuKeluarga()); // Mengganti idKartuKeluarga dengan objek KartuKeluarga
        response.setNamaLengkap(resident.getNamaLengkap());
        response.setTanggallahir(resident.getTanggalLahir());
        response.setTempatLahir(resident.getTempatLahir());
        response.setJeniskelamin(resident.getJenisKelamin());
        response.setStatusKeluarga(resident.getStatuskeluarga());
        response.setStatusPerkawinan(resident.getStatusPerkawinan());
        response.setAgama(resident.getAgama());
        response.setPendidikan(resident.getPendidikan());
        response.setPekerjaan(resident.getPekerjaan());
        response.setTelepon(resident.getTelepon());
        response.setAlamat(resident.getAlamat());
        response.setRt(resident.getRt());
        response.setRw(resident.getRw());
        response.setKelurahan(resident.getKelurahan());
        response.setKecamatan(resident.getKecamatan());
        response.setKota(resident.getKota());
        response.setKodePos(resident.getKodePos());
        response.setFotoUrl(resident.getFotoUrl());
        return response;
    }

    private void mapRequestToResident(PendudukRequest request, Penduduk resident) {
        resident.setNoKtp(request.getNoKtp());
        // Set KartuKeluarga from request to Penduduk
        if (request.getKartuKeluarga() != null) {
            KartuKeluarga familyCard = kartuKeluargaRepository.findByNoKK(request.getKartuKeluarga().getNoKK());

            if (familyCard == null) {
                throw new RuntimeException("Kartu Keluarga tidak ditemukan");
            }

            resident.setKartuKeluarga(familyCard);
        }

        if (request.getFoto() != null) {
            String fotoUrl = saveFotoToMinio(request.getFoto(), request.getNoKtp());
            resident.setFotoUrl(fotoUrl);
        }
//        resident.setNoKtp(request.getNoKtp());
//        if (request.getKartuKeluargaRequest() != null) {
//            KartuKeluarga familyCard = resident.getKartuKeluarga();
//            if (familyCard == null) {
//                throw new RuntimeException("Kartu Keluarga tidak ditemukan");
//            }
//        }
//        resident.setKartuKeluarga(familyCard);
        resident.setNamaLengkap(request.getNamaLengkap());
        resident.setTanggalLahir(request.getTanggallahir());
        resident.setTempatLahir(request.getTempatLahir());
        resident.setJenisKelamin(request.getJeniskelamin());
        resident.setStatuskeluarga(request.getStatusKeluarga());
        resident.setStatusPerkawinan(request.getStatusPerkawinan());
        resident.setAgama(request.getAgama());
        resident.setPendidikan(request.getPendidikan());
        resident.setPekerjaan(request.getPekerjaan());
        resident.setTelepon(request.getTelepon());
        resident.setAlamat(request.getAlamat());
        resident.setRt(request.getRt());
        resident.setRw(request.getRw());
        resident.setKelurahan(request.getKelurahan());
        resident.setKecamatan(request.getKecamatan());
        resident.setKota(request.getKota());
        resident.setKodePos(request.getKodePos());
    }

    private String saveFotoToMinio(MultipartFile foto,  String noKtp) {
        try {
            String fotoObjectName = "penduduk/" + noKtp + "/" + foto.getOriginalFilename();
            File file = convertMultipartFileToFile(foto);
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(fotoObjectName)
                            .filename(file.getName())
                            .contentType(foto.getContentType())
                            .build()
            );
            // URL Manual
//            String minioEndpoint = minioConfig.getEndpoint();
            String minioEndpoint = "http://127.0.0.1:9000";
            String bucketName = minioConfig.getBucketName();
            return minioEndpoint + "/" + bucketName + "/" + fotoObjectName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload a photo to Minio: " + e.getMessage());
        }
    }
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}
