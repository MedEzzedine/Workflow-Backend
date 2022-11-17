package com.workflow.controller;

import com.workflow.entity.Demande;
import com.workflow.entity.Etat;
import com.workflow.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/file")
@CrossOrigin("*")
public class FileUploader {

    // define a location
    public static final String DIRECTORY = System.getProperty("user.home") + "\\Documents\\angular_Springboot\\workflow\\back\\src\\main\\resources\\uploads";
    @Autowired
    private DemandeService demandeservice;

    // Define a method to upload files
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles, @RequestParam("iddemande") String id) throws IOException {
        System.out.println(id);
        int i = Integer.parseInt(id);
        Demande d = demandeservice.getdemandebyid(i);
        d.setDecision(Etat.accepted);
        d.setPdf(true);
        demandeservice.savedeDemande(d);

        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            //String filename = StringUtils.cleanPath(file.getOriginalFilename());
            String filename = id + ".pdf";
            Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            filenames.add(filename);
        }
        return ResponseEntity.ok().body(filenames);
    }

    // Define a method to download files
    @GetMapping("download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        //httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        httpHeaders.add(CONTENT_DISPOSITION, "inline;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }


}