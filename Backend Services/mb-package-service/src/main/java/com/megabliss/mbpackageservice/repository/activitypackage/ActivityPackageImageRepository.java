package com.megabliss.mbpackageservice.repository.activitypackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megabliss.mbpackageservice.model.activitypackage.ActivityPackageImage;

@Repository
public interface ActivityPackageImageRepository extends JpaRepository<ActivityPackageImage, Long>{
    
}
