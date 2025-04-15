package com.example.barter.dto.entity;

import com.example.barter.dto.input.SaveLeadInput;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("leads")
@Builder
public class LeadEntity {

    @Id
    private final UUID id;

    @NonNull
    private final String name;

    @NonNull
    @Column("business_email")
    private final String businessEmail;

    @NonNull
    @Column("company_name")
    private final String companyName;

    @Column("created_at")
    private final LocalDateTime createdAt;

    public static LeadEntity fromSaveLeadInput(final SaveLeadInput input) {
        return LeadEntity.builder()
                .name(input.name())
                .businessEmail(input.businessEmail())
                .companyName(input.companyName())
                .createdAt(LocalDateTime.now())
                .build();
    }
} 