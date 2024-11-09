package com.megabliss.mbpackageservice.repository.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.megabliss.mbpackageservice.model.activity.ActivityRequirement;

@Repository
public interface ActivityRequirementRepository extends JpaRepository<ActivityRequirement, Long>{
    
}