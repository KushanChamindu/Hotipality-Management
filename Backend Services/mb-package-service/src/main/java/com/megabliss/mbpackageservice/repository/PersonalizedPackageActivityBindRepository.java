package com.megabliss.mbpackageservice.repository;

import com.megabliss.mbpackageservice.model.PersonalizedPackageActivityBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalizedPackageActivityBindRepository extends JpaRepository<PersonalizedPackageActivityBind, Long> {
    void deleteByActivity_Id(Long activityId);
}
