package com.mbpackageservice.repository.activitypackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.activitypackage.ActivityPackageRequirement;

@Repository
public interface ActivityPackageRequirementRepository extends JpaRepository<ActivityPackageRequirement, Long> {

}
