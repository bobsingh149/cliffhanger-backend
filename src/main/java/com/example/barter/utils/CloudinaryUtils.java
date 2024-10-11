package com.example.barter.utils;

import com.cloudinary.Cloudinary;
import com.example.barter.exception.customexception.ImageUploadFailed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryUtils {

    Cloudinary cloudinary;

    @Autowired
    public CloudinaryUtils(Cloudinary cloudinary)
    {
        this.cloudinary=cloudinary;
    }


    public  String uploadFileAndGetLink(MultipartFile file,String folderName) throws IOException {


            Map<Object, Object> options = new HashMap<>();

            options.put("folder", folderName);

            final Map uploadedFile = this.cloudinary.uploader().upload(file.getBytes(), options);

            final var publicId = (String) uploadedFile.get("public_id");

            return cloudinary.url().secure(true).generate(publicId);


    }
}
