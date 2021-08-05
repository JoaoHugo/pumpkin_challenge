package br.com.pumpkin.desafio.services;

import br.com.pumpkin.desafio.models.File;
import br.com.pumpkin.desafio.models.Register;
import br.com.pumpkin.desafio.repositories.FileRepository;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public byte[] getFile(long id){
        try {
            File file = fileRepository.findById(id).get();

            String header = file.getColumns();

            FileWriter myWriter = new FileWriter("output.csv");
            myWriter.write(header + "\n");

            for (Register r : file.getRegister()) {
                String formattedRegister = r.getRegister();
                formattedRegister = formattedRegister + ",";
                myWriter.write(formattedRegister + "\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            java.io.File returnedFile = new java.io.File("output.csv");

            System.out.println(returnedFile.getName());
            return Files.readAllBytes(returnedFile.toPath());
        }catch (Exception ex){
            System.out.println("Exception found: \n");
            ex.printStackTrace();
            return new byte [4];
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
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();

        reader.close();
        csvReader.close();
        return list;
    }

}
