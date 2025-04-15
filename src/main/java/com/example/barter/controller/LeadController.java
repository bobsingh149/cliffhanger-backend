package com.example.barter.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.barter.dto.input.SaveLeadInput;
import com.example.barter.dto.response.ApiResponse;
import com.example.barter.service.LeadService;
import com.example.barter.utils.ControllerUtils;
import com.example.barter.utils.ControllerUtils.ResponseMessage;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;

    @Autowired
    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping
    public ResponseEntity<Mono<ApiResponse<Object>>> getAllLeads() {
        final var response = leadService.getAllLeads();
        return ControllerUtils.mapFLuxToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Mono<ApiResponse<Object>>> createLead(@RequestBody SaveLeadInput saveLeadInput) {
        final var response = leadService.saveLead(saveLeadInput);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> updateLead(
            @PathVariable UUID id,
            @RequestBody SaveLeadInput saveLeadInput) {
        final var response = leadService.updateLead(id, saveLeadInput);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }
} 