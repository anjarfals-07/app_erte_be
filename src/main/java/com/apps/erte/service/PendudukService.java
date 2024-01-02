package com.apps.erte.service;
import com.apps.erte.config.MinioConfig;
import com.apps.erte.dto.request.KartuKeluargaRequest;
import com.apps.erte.dto.request.PendudukRequest;
import com.apps.erte.dto.response.PendudukResponse;
import com.apps.erte.entity.KartuKeluarga;
import com.apps.erte.entity.Penduduk;
import com.apps.erte.repository.KartuKeluargaRepository;
import com.apps.erte.repository.PendudukRepository;
import com.apps.erte.util.FileValidationException;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PendudukService {
    private final PendudukRepository pendudukRepository;
    private final KartuKeluargaRepository kartuKeluargaRepository;
    private final MinioClient minioClient;
    @Autowired
    private  MinioConfig minioConfig;

    @Autowired
    public PendudukService(PendudukRepository pendudukRepository, KartuKeluargaRepository kartuKeluargaRepository, MinioClient minioClient) {
        this.pendudukRepository = pendudukRepository;
        this.kartuKeluargaRepository = kartuKeluargaRepository;
        this.minioClient = minioClient;
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
                kartuKeluarga = kartuKeluargaRepository.save(kartuKeluarga);

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

    public PendudukResponse updatePenduduk(Long id, PendudukRequest request) {
        Optional<Penduduk> pendudukOptional = pendudukRepository.findById(id);

        if (pendudukOptional.isPresent()) {
            Penduduk existingPenduduk = pendudukOptional.get();
            String oldFotoUrl = existingPenduduk.getFotoUrl();
            System.out.println("old photo : " + oldFotoUrl);


            // Update data penduduk tanpa foto
            mapRequestToResident(request, existingPenduduk);

            // Update data kartu keluarga jika ada
            if (request.getKartuKeluarga() != null) {
                KartuKeluargaRequest kkRequest = request.getKartuKeluarga();

                // Inisialisasi atau perbarui objek KartuKeluarga
                KartuKeluarga kartuKeluarga = existingPenduduk.getKartuKeluarga();
                if (kartuKeluarga == null) {
                    kartuKeluarga = new KartuKeluarga();
                    existingPenduduk.setKartuKeluarga(kartuKeluarga);
                }

                kartuKeluarga.setNoKK(kkRequest.getNoKK());
                kartuKeluarga.setNamaKepalaKeluarga(kkRequest.getNamaKepalaKeluarga());

                // Simpan objek KartuKeluarga yang telah diperbarui
                existingPenduduk.setKartuKeluarga(kartuKeluargaRepository.save(kartuKeluarga));
            }

            // Update foto jika ada perubahan
            if (request.getFoto() != null && !request.getFoto().isEmpty()) {
                if (request.getFoto().getOriginalFilename() != null && request.getFoto().getContentType() != null) {
                    // Hapus foto lama di Minio
                    if (oldFotoUrl != null) {
                        deleteFotoFromMinio(oldFotoUrl);
                    }
                    // Simpan foto baru di Minio
                    String newFotoUrl = saveFotoToMinio(request.getFoto(), request.getNoKtp());
                    existingPenduduk.setFotoUrl(newFotoUrl);  // Set FotoUrl dengan URL baru
                }
            } else {
                // Jika foto tidak berubah, tetap gunakan foto yang lama
                existingPenduduk.setFotoUrl(oldFotoUrl);
            }
            // Simpan perubahan
            Penduduk savedPenduduk = pendudukRepository.save(existingPenduduk);

            // Mengembalikan respons dengan data yang sudah diupdate
            return mapResidentToResponse(savedPenduduk);
        }

        // Jika penduduk tidak ditemukan, bisa dikembalikan null atau respons error sesuai kebutuhan
        return null;
    }

    public void deletePenduduk(Long id) {
        Optional<Penduduk> pendudukOptional = pendudukRepository.findById(id);

        if (pendudukOptional.isPresent()) {
            Penduduk penduduk = pendudukOptional.get();
            // Hapus foto dari Minio
            if (penduduk.getFotoUrl() != null) {
                deleteFotoFromMinio(penduduk.getFotoUrl());
            }
            // Hapus penduduk
            pendudukRepository.delete(penduduk);
            // Cek apakah kartu_keluarga masih memiliki referensi di penduduk
            List<Penduduk> otherResidentsWithSameKK = pendudukRepository
                    .findByKartuKeluargaNoKK(penduduk.getKartuKeluarga().getNoKK());

            if (otherResidentsWithSameKK.isEmpty()) {
                // Hapus kartu_keluarga jika tidak ada penduduk lain dengan kartu_keluarga yang sama
                kartuKeluargaRepository.delete(penduduk.getKartuKeluarga());
            }
        }
    }

    public List<PendudukResponse> getAllPenduduk() {
        List<Penduduk> pendudukList = pendudukRepository.findAll();
        return pendudukList.stream().map(this::mapResidentToResponse).collect(Collectors.toList());
    }
    public List<PendudukResponse> getPendudukByNoKK(String noKK) {
        List<Penduduk> pendudukList = pendudukRepository.findByKartuKeluargaNoKK(noKK);
        return pendudukList.stream().map(this::mapResidentToResponse).collect(Collectors.toList());
    }

    private PendudukResponse mapResidentToResponse(Penduduk resident) {
        PendudukResponse response = new PendudukResponse();
        response.setId(resident.getId());
        response.setNoKtp(resident.getNoKtp());
        response.setKartuKeluarga(resident.getKartuKeluarga());
        response.setNamaLengkap(resident.getNamaLengkap());
        response.setTanggallahir(resident.getTanggalLahir());
        response.setTempatLahir(resident.getTempatLahir());
        response.setJenisKelamin(resident.getJenisKelamin());
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
        response.setStatusPenduduk(resident.getStatusPenduduk());
        response.setNewStatusPenduduk(resident.getNewStatusPenduduk());
        return response;
    }

    private void mapRequestToResident(PendudukRequest request, Penduduk resident) {
        resident.setNoKtp(request.getNoKtp());

        if (request.getKartuKeluarga() != null) {
            KartuKeluargaRequest kartuKeluargaRequest = request.getKartuKeluarga();

            // Inisialisasi atau perbarui objek KartuKeluarga
            KartuKeluarga kartuKeluarga = resident.getKartuKeluarga();
            if (kartuKeluarga == null) {
                kartuKeluarga = new KartuKeluarga();
                resident.setKartuKeluarga(kartuKeluarga);
            }

            kartuKeluarga.setNoKK(kartuKeluargaRequest.getNoKK());
            kartuKeluarga.setNamaKepalaKeluarga(kartuKeluargaRequest.getNamaKepalaKeluarga());
        }

        if (request.getFoto() != null) {
            String fotoUrl = saveFotoToMinio(request.getFoto(), request.getNoKtp());
            resident.setFotoUrl(fotoUrl);
        }
        resident.setNamaLengkap(request.getNamaLengkap());
        resident.setTanggalLahir(request.getTanggallahir());
        resident.setTempatLahir(request.getTempatLahir());
        resident.setJenisKelamin(request.getJenisKelamin());
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
        resident.setStatusPenduduk(request.getStatusPenduduk());
        resident.setNewStatusPenduduk(request.getNewStatusPenduduk());

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

            String minioEndpoint = "http://127.0.0.1:9000";
            String bucketName = minioConfig.getBucketName();
            System.out.println("Saved photo URL: " + minioEndpoint + "/" + bucketName + "/" + fotoObjectName);
            return minioEndpoint + "/" + bucketName + "/" + fotoObjectName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload a photo to Minio: " + e.getMessage());
        }
    }

    private void deleteFotoFromMinio(String fotoUrl) {
        try {
            // Mendapatkan nama objek dari URL
            String objectName = fotoUrl.substring(fotoUrl.indexOf(minioConfig.getBucketName()) + minioConfig.getBucketName().length() + 1);
            System.out.println("object name: " + objectName);

            // Menghapus objek dari Minio
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete photo from Minio: " + e.getMessage());
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
