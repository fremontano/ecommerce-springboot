package com.ecommerce.sb_ecom.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileService {
    String uploadImage(String paht, MultipartFile file) throws IOException;
}
