package br.com.pumpkin.desafio.controllers;

import br.com.pumpkin.desafio.models.File;
import br.com.pumpkin.desafio.repositories.FileRepository;
import br.com.pumpkin.desafio.repositories.RegisterRepository;
import br.com.pumpkin.desafio.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private final FileRepository fileRepository;

    @Autowired
    private final RegisterRepository registerRepository;

    private final FileService fileService;

    @Autowired
    public FileController(FileRepository fileRepository, RegisterRepository registerRepository) {
        this.fileRepository = fileRepository;
        this.registerRepository = registerRepository;
        this.fileService = new FileService(fileRepository);
    }

    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getFile(@PathVariable long id){
        return fileService.getFile(id);
    }

    @PostMapping
    public File uploadCSVFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            // Treatment when file is Empty
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                List<String[]> list = fileService.readAll(reader);

                return fileService.uploadCsvFile(list);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new File();
    }



}

