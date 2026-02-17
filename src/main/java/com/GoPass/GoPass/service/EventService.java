package com.GoPass.GoPass.service;

import com.GoPass.GoPass.domain.event.Event;
import com.GoPass.GoPass.domain.event.EventRequestDTO;
import com.GoPass.GoPass.repositories.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

    @Autowired
    private EventRepository repository;

    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        // Verifica se a imagem existe e não está vazia antes de tentar o upload
        if (data.image() != null && !data.image().isEmpty()) {
            imgUrl = this.uploadImg(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        // Agora o save vai persistir no banco e retornar o objeto com ID e URL
        return this.repository.save(newEvent);
    }

    public String uploadImg(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        try {
            File file = this.convertMultipartToFile(multipartFile);

            // Use o PutObjectRequest para definir o ACL como Público
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            file.delete(); // Limpa o arquivo temporário
            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            System.out.println("Erro detalhado: " + e.getMessage());
            return null;
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        // Correção de segurança: garante que o nome do arquivo original seja usado corretamente
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}