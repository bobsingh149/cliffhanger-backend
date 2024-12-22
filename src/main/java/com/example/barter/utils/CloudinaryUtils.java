package com.example.barter.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.barter.exception.customexception.ImageUploadFailed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        final var transformation = new Transformation()
                .aspectRatio("5:6")
                .crop("fill")
                .gravity("auto");


        final var options = ObjectUtils.asMap(
                    "unique_filename", false,
                    "overwrite", true,
                    "folder", folderName,
                    "transformation", transformation

        );


            final var uploadedFile = this.cloudinary.uploader().upload(file.getBytes(), options);

            final var publicId = (String) uploadedFile.get("public_id");

            return cloudinary.url().secure(true).generate(publicId);

    }



    
    public String uploadFromNetworkAndGetLink(String imageLink, String folderName) throws IOException { 
        final var transformation = new Transformation()
            .aspectRatio("5:6")
            .crop("fill")
            .gravity("auto");

        final var params = ObjectUtils.asMap(
                "unique_filename", false,
                "overwrite", true,
                "folder", folderName,
                "transformation", transformation
        );

        final var uploadedFile = cloudinary.uploader().upload(imageLink, params);

        final var publicId = (String) uploadedFile.get("public_id");

        return cloudinary.url().secure(true).generate(publicId);
    }
}
