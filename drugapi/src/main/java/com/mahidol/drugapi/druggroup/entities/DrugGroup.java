package com.mahidol.drugapi.druggroup.entities;

import com.mahidol.drugapi.drug.models.entites.Drug;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "drug_group")
public class DrugGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "group_name", columnDefinition = "TEXT")
    private String groupName;

    @ManyToMany
    @JoinTable(
            name = "drug_group_drug",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "drug_id")
    )
    private List<Drug> drugs;

}
