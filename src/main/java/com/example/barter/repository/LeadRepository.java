package com.example.barter.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.barter.dto.entity.LeadEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LeadRepository extends R2dbcRepository<LeadEntity, UUID> {

    @Query("SELECT * FROM leads ORDER BY created_at DESC")
    Flux<LeadEntity> findAllOrderByCreatedAtDesc();
    
    @Query("""
            UPDATE leads SET 
            name = :name, 
            business_email = :businessEmail, 
            company_name = :companyName 
            WHERE id = :id
            """)
    Mono<Void> updateLead(UUID id, String name, String businessEmail, String companyName);
} 