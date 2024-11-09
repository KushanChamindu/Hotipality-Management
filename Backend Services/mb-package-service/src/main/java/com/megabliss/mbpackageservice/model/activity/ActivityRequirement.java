package com.megabliss.mbpackageservice.model.activity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_requirement")
@Data @NoArgsConstructor @AllArgsConstructor
public class ActivityRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String requirementName;
    private String requirementDescription;
}
