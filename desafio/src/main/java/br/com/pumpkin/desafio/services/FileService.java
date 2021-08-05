package br.com.pumpkin.desafio.services;

import br.com.pumpkin.desafio.models.File;
import br.com.pumpkin.desafio.models.Register;
import br.com.pumpkin.desafio.repositories.FileRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public byte[] getFile(long id){
        try {
            File file = fileRepository.findById(id).get();

            String header = file.getColumns();
            List<String[]> lines = new ArrayList<>();
            lines.add(header.split(","));

            for (Register r : file.getRegister()) {
                String formattedRegister = r.getRegister();
                formattedRegister = formattedRegister + ",";
                lines.add(formattedRegister.split(","));
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
            CSVWriter csvWriter = new CSVWriter(streamWriter);

            csvWriter.writeAll(lines);
            csvWriter.flush();

            System.out.println("Successfully wrote to the file.");

            return stream.toByteArray();
        }catch (Exception ex){
            System.out.println("Exception found: \n");
            ex.printStackTrace();
            return null;
        }
    }

    public File uploadCsvFile(List<String[]> list){
        String[] header = list.get(0);
        list.remove(0);
        String finalHeader = new String();

        for(String s : header)
            finalHeader = finalHeader + "," + s;

        finalHeader = finalHeader.substring(1);
        File newFile = new File();
        newFile.setColumns(finalHeader);

        List<Register> newRegisters = new ArrayList<Register>();

        for(String[] s : list){
            String newRegister = new String();
            for(String si : s){
                newRegister = newRegister + "," + si;
            }
            newRegister = newRegister.substring(1,newRegister.length()-1);
            Register r = new Register();
            r.setRegister(newRegister);
            newRegisters.add(r);
        }

        newFile.setRegister(newRegisters);

        fileRepository.save(newFile);
        return newFile;
    }

    public List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list;
        list = csvReader.readAll();

        reader.close();
        csvReader.close();
        return list;
    }

}
