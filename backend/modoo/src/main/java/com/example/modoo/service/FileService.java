package com.example.modoo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final Path fileStorageLocation;

    // 생성자: 파일 저장 위치를 설정하고, 디렉토리를 생성합니다
    public FileService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    /**
     * 여러 파일을 저장하고, 저장된 파일들의 이름을 리스트로 반환합니다.
     *
     * @param files 저장할 MultipartFile 리스트
     * @return 저장된 파일 이름들의 리스트
     */
    public List<String> saveFiles(List<MultipartFile> files){
        return files.stream()
                .map(this::saveOneFile)
                .collect(Collectors.toList());
    }

    /**
     * 단일 파일을 저장하고, 저장된 파일의 고유한 이름을 반환.
     *
     * @param file 저장할 MultipartFile
     * @return 저장된 파일의 고유한 이름
     */
    public String saveOneFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String uniqueFileName = generateUniqueFileName(fileName);

        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 파일 URL 생성
            String fileUrl = uniqueFileName;
            return fileUrl;
        } catch (IOException ex) {
            throw new RuntimeException("파일 저장에 실패하였습니다. " + fileName, ex);
        }
    }

    /**
     * 파일 이름이 고유하도록 타임스탬프를 추가하여 새로운 파일 이름을 생성.
     *
     * @param fileName 원래 파일 이름
     * @return 고유한 파일 이름
     */
    private String generateUniqueFileName(String fileName) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return timeStamp + "_" + fileName;
    }
}
