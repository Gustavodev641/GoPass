package com.GoPass.GoPass.service;


import com.GoPass.GoPass.domain.event.Event;
import com.GoPass.GoPass.domain.event.EventRequestDTO;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;
    private String filename;

    public Event createEvent(EventRequestDTO data){
        String imgUrl = (null);

        if(data.image() != null){
            this.uploadImg(data.image());
        }
        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);

        return newEvent;
    }
    private String uploadImg(MultipartFile multipartFile) {
        // 1. Defina o nome
        String imgName = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();

        try {
            // 2. Corrija o nome do método (File com F)
            File file = this.convertMultipartToDile(multipartFile);

            // 3. Use imgName (o mesmo nome da variável lá de cima)
            s3Client.putObject(bucketName, imgName, file);

            file.delete();

            // 4. Use imgName aqui também
            return s3Client.getUrl(bucketName, imgName).toString();

        } catch (Exception e) {
            System.out.println("Erro ao upload img: " + e.getMessage());
            return null;
        }
    }
    private File convertMultipartToDile(MultipartFile multipartFile)throws IOException{
        File convFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;

    }
}
