package com.example.barter.service;

import com.example.barter.dto.input.SaveGroupInput;
import com.example.barter.repository.GroupRepository;
import com.example.barter.utils.CloudinaryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class GroupService
{
    private final GroupRepository groupRepository;
    private final CloudinaryUtils cloudinaryUtils;

    @Autowired
    public GroupService(GroupRepository groupRepository, CloudinaryUtils cloudinaryUtils)
    {
        this.groupRepository=groupRepository;
        this.cloudinaryUtils=cloudinaryUtils;
    }


   public Mono<Void> saveGroup(SaveGroupInput saveGroupInput, MultipartFile iconFile) throws IOException {
       final String icon = cloudinaryUtils.uploadFileAndGetLink(iconFile,"group_icons");

       return groupRepository.saveGroup(saveGroupInput.name(), saveGroupInput.description(),icon);
   }
}
