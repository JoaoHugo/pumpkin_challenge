package br.com.pumpkin.desafio.controllers;

import br.com.pumpkin.desafio.models.File;
import br.com.pumpkin.desafio.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getFile(@PathVariable long id){
        return fileService.getFile(id);
    }

    @PostMapping
    public File uploadCSVFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            System.out.println("Empty file!");
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                return fileService.uploadCsvFile(fileService.readAll(reader));

            } catch (Exception ex) {
                System.out.println("Exception found: \n");
                ex.printStackTrace();
            }
        }
        return null;
    }

}

