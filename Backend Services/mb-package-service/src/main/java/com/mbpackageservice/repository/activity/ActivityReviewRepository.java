package com.mbpackageservice.repository.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.activity.ActivityReview;

import java.util.List;

@Repository
public interface ActivityReviewRepository extends JpaRepository<ActivityReview, Long> {

    Page<ActivityReview> findByActivityId(Long activityId, Pageable pageable);

    @Query(value = "SELECT p.star_rating FROM activity_review p WHERE p.activity_id = :activityId", nativeQuery = true)
    List<Float> findAllStarRatingByActivityId(Long activityId);

    void deleteByActivity_Id(Long activityId);
}