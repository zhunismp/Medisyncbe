package com.mahidol.drugapi.druggroup.entities;

import com.mahidol.drugapi.drug.models.entites.Drug;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

    private Boolean isArchived = false;

    @ManyToMany
    @JoinTable(
            name = "drug_group_drug",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "drug_id")
    )
    private Set<Drug> drugs = new HashSet<>();

    @PostLoad
    public void checkIsArchived() {
        this.isArchived = drugs.stream().allMatch(Drug::getIsArchived);
    }

    public DrugGroup addDrug(Drug drug) {
        this.drugs.add(drug);
        return this;
    }

    public DrugGroup removeDrug(Drug drug) {
        if (!this.drugs.remove(drug))
            throw new IllegalArgumentException("Drug not found in group");

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrugGroup g = (DrugGroup) o;
        return Objects.equals(this.getId(), g.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
