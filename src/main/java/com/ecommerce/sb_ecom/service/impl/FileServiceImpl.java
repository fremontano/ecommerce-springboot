package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String paht, MultipartFile file) throws IOException {
        // Obtener el nombre original del archivo
        String originalFileName = file.getOriginalFilename();

        // Generar un nombre aleatorio con la misma extension
        String randomId = UUID.randomUUID().toString();
        //image.jpg -> 1234 -> 1234.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        // Construir la ruta del archivo
        String filePath = paht + File.separator + fileName;

        // Verificar si la ruta existe, si no, crearla
        File folder = new File(paht);
        if (!folder.exists()) {
            folder.mkdir();
        }
        // Subir la imagen al servidor
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return filePath;
    }

}
