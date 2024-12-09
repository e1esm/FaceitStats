package com.esm.faceitstats.service;

import com.esm.faceitstats.entity.File;
import com.esm.faceitstats.exception.ResourceNotFoundException;
import com.esm.faceitstats.repository.FileRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {
    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private MinioClient minioClient;
    private FileRepository fileRepository;

    private static final String BUCKET_NAME = "faceit-stats";

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void saveFile(String name, InputStream content) {
        try {
            this.minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .object(name)
                            .bucket(BUCKET_NAME)
                            .contentType("text/html")
                            .stream(content, -1, 10485760)
                            .build()
            );
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public List<String> getAllPaths(){
        List<String> paths = new ArrayList<>();

         this.fileRepository.findAll().forEach(file -> {
             paths.add(file.getFileName());
         });

         return paths;
    }

    public String getFilePath(Long id){
        var path = this.fileRepository.findById(id);
        if(path.isEmpty()){
            throw new ResourceNotFoundException("File not found");
        }

        return path.get().getFileName();
    }

    public InputStream getFile(String path){
        try(InputStream inputStream = this.minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(BUCKET_NAME)
                        .object(path)
                        .build()
        )){
            return inputStream;
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

}
