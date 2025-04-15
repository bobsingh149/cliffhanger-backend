package com.example.barter.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.barter.dto.entity.LeadEntity;
import com.example.barter.dto.input.SaveLeadInput;
import com.example.barter.repository.LeadRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    @Autowired
    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public Flux<LeadEntity> getAllLeads() {
        return leadRepository.findAllOrderByCreatedAtDesc();
    }

    public Mono<LeadEntity> saveLead(SaveLeadInput saveLeadInput) {
        final LeadEntity leadEntity = LeadEntity.fromSaveLeadInput(saveLeadInput);
        return leadRepository.save(leadEntity);
    }

    public Mono<LeadEntity> updateLead(UUID id, SaveLeadInput saveLeadInput) {
        return leadRepository.findById(id)
                .flatMap(existingLead -> {
                    return leadRepository.updateLead(
                            id,
                            saveLeadInput.name(),
                            saveLeadInput.businessEmail(),
                            saveLeadInput.companyName()
                    ).thenReturn(LeadEntity.builder()
                            .id(id)
                            .name(saveLeadInput.name())
                            .businessEmail(saveLeadInput.businessEmail())
                            .companyName(saveLeadInput.companyName())
                            .createdAt(existingLead.getCreatedAt())
                            .build());
                });
    }
} 