package com.megabliss.mbpackageservice.repository.activity;

import com.megabliss.mbpackageservice.model.activity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("SELECT p FROM Activity p WHERE " +
            "p.title LIKE CONCAT('%', :title, '%') " +
            "and p.location = :location")
    Page<Activity> searchByTitleContainingAndLocation(@Param("title") String title, @Param("location") String location, Pageable pageable);

    Page<Activity> findByTitleContaining(String title, Pageable pageable);

    Page<Activity> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Activity> findByServiceProviderId(String serviceProviderId, Pageable pageable);

    Page<Activity> findAll(Specification<Activity> spec, Pageable pageable);

    Boolean existsByServiceProviderId(String serviceProviderId);

}