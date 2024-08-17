package com.mahidol.drugapi.Drug.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pildatabase")
public class InternalDrug {
    @Id
    private UUID id;

    @Column(name = "generic_name", columnDefinition = "TEXT")
    private String genericName;

    @Column(name = "dosage_form", columnDefinition = "TEXT")
    private String dosageForm;

    @Column(name = "strength_unit", columnDefinition = "TEXT")
    private String strengthUnit;

    @Column(name = "related_link", columnDefinition = "TEXT")
    private String relatedLink;

    @Column(name = "during_use", columnDefinition = "TEXT[]")
    private List<String> duringUse;

    @Column(name = "storage", columnDefinition = "TEXT[]")
    private List<String> storage;

    @Column(name = "usage", columnDefinition = "TEXT[]")
    private List<String> usage;

    @Column(name = "warning", columnDefinition = "TEXT[]")
    private List<String> warning;

    @Column(name = "avoidance", columnDefinition = "TEXT[]")
    private List<String> avoidance;

    @Column(name = "forgetting", columnDefinition = "TEXT[]")
    private List<String> forget;

    @Column(name = "overdose", columnDefinition = "TEXT[]")
    private List<String> overdose;

    @Column(name = "danger_side_effect", columnDefinition = "TEXT[]")
    private List<String> dangerSideEffect;

    @Column(name = "side_effect", columnDefinition = "TEXT[]")
    private List<String> sideEffect;
}
