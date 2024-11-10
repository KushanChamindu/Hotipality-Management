package com.mbpackageservice.repository.activitypackage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.activitypackage.ActivityPackage;

import java.util.List;

@Repository
public interface ActivityPackageRepository extends JpaRepository<ActivityPackage, Long> {

    Page<ActivityPackage> findByTitleContaining(String title, Pageable pageable);

    Page<ActivityPackage> findByIsActive(Boolean isActive, Pageable pageable);

    List<ActivityPackage> findByServiceProviderId(String id);

    Page<ActivityPackage> findAll(Specification<ActivityPackage> spec, Pageable pageable);

    Boolean existsByServiceProviderId(String serviceProviderId);

}
