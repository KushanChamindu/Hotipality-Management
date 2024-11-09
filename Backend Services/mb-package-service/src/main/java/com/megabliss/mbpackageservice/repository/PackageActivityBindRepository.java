package com.megabliss.mbpackageservice.repository;

import com.megabliss.mbpackageservice.model.PackageActivityBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageActivityBindRepository extends JpaRepository<PackageActivityBind, Long> {

    void deleteByActivity_Id(Long activityId);

}