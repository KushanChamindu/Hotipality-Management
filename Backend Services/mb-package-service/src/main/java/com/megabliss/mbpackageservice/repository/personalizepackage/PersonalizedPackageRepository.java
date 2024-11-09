package com.megabliss.mbpackageservice.repository.personalizepackage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megabliss.mbpackageservice.model.personalizedpackage.PersonalizedPackage;

@Repository
public interface PersonalizedPackageRepository extends JpaRepository<PersonalizedPackage, Long> {

    Page<PersonalizedPackage> findAllByUserId(String userId, Pageable pageable);

}