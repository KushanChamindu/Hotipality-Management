package com.mbpackageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.PackageActivityBind;

@Repository
public interface PackageActivityBindRepository extends JpaRepository<PackageActivityBind, Long> {

    void deleteByActivity_Id(Long activityId);

}