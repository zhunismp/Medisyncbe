package com.mahidol.drugapi.drug.models.entites;

import com.mahidol.drugapi.drug.models.type.MealCondition;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "drug")
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "generic_name", columnDefinition = "TEXT")
    private String genericName;

    @Column(name = "dosage_form", columnDefinition = "TEXT")
    private String dosageForm;

    @Column(name = "unit", columnDefinition = "TEXT")
    private String unit;

    @Column(name = "strength", columnDefinition = "TEXT")
    private String strength;

    @Column(name = "amount", columnDefinition = "FLOAT")
    private double amount;

    @Column(name = "dose", columnDefinition = "FLOAT")
    private double dose;

    @Column(name = "taken_amount", columnDefinition = "FLOAT")
    private double takenAmount;

    @Column(name = "usage_time", columnDefinition = "INT")
    private MealCondition usageTime;

    @Column(name = "is_internal_drug", columnDefinition = "BOOLEAN")
    private Boolean isInternalDrug = false;

    @ManyToMany(mappedBy = "drugs")
    private List<DrugGroup> groups;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Drug d) return d.getId().equals(this.id);
        else return false;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
