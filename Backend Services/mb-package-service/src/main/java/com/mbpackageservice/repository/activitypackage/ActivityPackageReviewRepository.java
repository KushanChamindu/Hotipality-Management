package com.mbpackageservice.repository.activitypackage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.activitypackage.ActivityPackageReview;

import java.util.List;

@Repository
public interface ActivityPackageReviewRepository extends JpaRepository<ActivityPackageReview, Long> {
    Page<ActivityPackageReview> findByActivityPackageId(Long activityPackageId, Pageable pageable);

    @Query(value = "SELECT p.star_rating FROM activity_package_review p WHERE p.activity_package_id = :activityPackageId", nativeQuery = true)
    List<Float> findAllStarRatingByActivityId(Long activityPackageId);

    void deleteByActivityPackage_Id(Long activityPackageId);
}
