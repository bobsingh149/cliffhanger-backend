package com.example.barter.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.barter.exception.customexception.ImageUploadFailed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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



    
    public String uploadFromNetworkAndGetLink(String imageLink, String folderName) throws  IOException
    {

        Map params1 = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );

        final Map uploadedFile=
                cloudinary.uploader().upload("https://books.google.com/books/content?id=mpuhNtrkrV8C&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api", params1);


        final var publicId = (String) uploadedFile.get("public_id");

        return cloudinary.url().secure(true).generate(publicId);
  

    }
}
