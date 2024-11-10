package com.mbpackageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.PersonalizedPackageActivityBind;

@Repository
public interface PersonalizedPackageActivityBindRepository
        extends JpaRepository<PersonalizedPackageActivityBind, Long> {
    void deleteByActivity_Id(Long activityId);
}
