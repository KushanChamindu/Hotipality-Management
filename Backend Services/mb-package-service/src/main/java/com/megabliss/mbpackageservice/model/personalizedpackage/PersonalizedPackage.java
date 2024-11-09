package com.megabliss.mbpackageservice.model.personalizedpackage;

import java.util.ArrayList;
import java.util.List;
import com.megabliss.mbpackageservice.model.PersonalizedPackageActivityBind;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personalized_package")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userId;

    private String title;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "personalized_package_id", referencedColumnName = "id")
    private List<PersonalizedPackageActivityBind> personalizedPackageActivityBinds = new ArrayList<>();

}
