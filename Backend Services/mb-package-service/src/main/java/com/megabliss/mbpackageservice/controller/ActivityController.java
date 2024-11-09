package com.megabliss.mbpackageservice.controller;

import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.activity.ActivityDTO;
import com.megabliss.mbpackageservice.dto.activity.ActivityReviewDTO;
import com.megabliss.mbpackageservice.dto.activity.ActivityUpdateDTO;
import com.megabliss.mbpackageservice.model.activity.ActivityReview;
import com.megabliss.mbpackageservice.model.enums.EventLevel;
import com.megabliss.mbpackageservice.model.enums.EventType;
import com.megabliss.mbpackageservice.service.ActivityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/package-service/activity")
@Validated
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * Returns activities based on optional filters.
     * Upper and Lower Price must both be present.
     *
     * @param latitude
     * @param longitude
     * @param distance
     * @param type
     * @param level
     * @param review
     * @param availability
     * @param lowerPrice
     * @param upperPrice
     * @param pageNo
     * @param size
     * @param sortBy
     * @param direction
     * @return
     */
    @GetMapping("/nearest-activity")
    public ResponseEntity<ResponseModel<ActivityDTO>> findActivitiesWithinDistance(
            @RequestParam(name = "latitude", required = false) Double latitude,
            @RequestParam(name = "longitude", required = false) Double longitude,
            @RequestParam(name = "distance", required = false, defaultValue = "100") Double distance,
            @RequestParam(name = "type", required = false) EventType type,
            @RequestParam(name = "level", required = false) EventLevel level,
            @RequestParam(name = "review", required = false) Float review,
            @RequestParam(name = "availability", required = false, defaultValue = "true") Boolean availability,
            @Positive(message = "Upper Bound Price must be positive") @RequestParam(name = "lowerPrice", required = false) Double lowerPrice,
            @Positive(message = "Lower Bound Price must be positive") @RequestParam(name = "upperPrice", required = false) Double upperPrice,
            @Positive(message = "Page number must be positive") @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
            @Positive(message = "Page size must be positive") @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {
        Pageable pageRequestData;
        if (latitude != null && longitude != null) { // if long lat are present sort by distance in query
            pageRequestData = PageRequest.of(pageNo - 1, size);
        } else {
            Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            pageRequestData = PageRequest.of(pageNo - 1, size, sort);
        }

        return new ResponseEntity<>(activityService.filteredActivities(latitude, longitude, distance,
                type, level, review, availability, lowerPrice, upperPrice, pageRequestData),
                HttpStatus.PARTIAL_CONTENT);
    }

    @GetMapping
    public ResponseEntity<ResponseModel<ActivityDTO>> getActivityByQuery(
            @NotBlank(message = "Title must not be blank") @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "location", required = false, defaultValue = "Auckland/NZ") String location,
            @Positive(message = "Page number must be positive") @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
            @Positive(message = "Page size must be positive") @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);
        ResponseModel<ActivityDTO> searchedActivity = activityService.findAllActivityByTitle(title, location,
                pageRequestData);
        return new ResponseEntity<>(searchedActivity, HttpStatus.PARTIAL_CONTENT);
    }

    @GetMapping("/activity-id/{activityId}")
    public ResponseEntity<ActivityDTO> getActivityById(
            @NotNull(message = "Activity Id cannot be null!") @PositiveOrZero(message = "Activity Id must be positive or zero!") @PathVariable("activityId") Long activityId) {
        ActivityDTO activityDTO = activityService.findActivityById(activityId);
        return new ResponseEntity<>(activityDTO, HttpStatus.PARTIAL_CONTENT);
    }

    @GetMapping("/service-provider-id/{serviceProviderId}")
    public ResponseEntity<ResponseModel<ActivityDTO>> getActivityByServiceProviderId(
            @NotNull(message = "Invalid Id : Service Provider Id cannot be null") @NotBlank(message = "Invalid Id : Service Provider Id cannot be blank") @PathVariable("serviceProviderId") String serviceProviderId,
            @Positive(message = "Page number must be positive!") @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
            @Pattern(regexp = "ASC|DESC", message = "Sorting direction must be 'ASC' or 'DESC'!") @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);
        ResponseModel<ActivityDTO> searchedActivity = activityService.findActivityByServiceProviderId(serviceProviderId,
                pageRequestData);
        return new ResponseEntity<>(searchedActivity, HttpStatus.PARTIAL_CONTENT);
    }

    @PostMapping
    public ResponseEntity<ActivityDTO> addActivity(@Valid @RequestBody ActivityDTO activityDTO) {
        if (activityDTO.getIsOneTimeEvent() && activityDTO.getActivityTimes() != null) {
            throw new IllegalArgumentException("A one time activity cannot have activity times (recurring)");
        } else if (!activityDTO.getIsOneTimeEvent() && activityDTO.getSpecialActivityTimes() != null) {
            throw new IllegalArgumentException("A normal activity (recurring) cannot have special activity times" +
                    "(one time)");
        }
        return ResponseEntity.ok().body(activityService.addActivity(activityDTO));
    }

    @PutMapping("/activity-id/{activityId}")
    public ResponseEntity<ActivityDTO> updateActivity(@PositiveOrZero(message = "Activity Id must be positive or " +
            "zero value Long") @PathVariable("activityId") Long activityId,
            @Valid @RequestBody ActivityUpdateDTO activityUpdateDTO) {

        if (activityId != null) {
            ActivityDTO activityUpdatedResponse = activityService.updateActivity(activityId, activityUpdateDTO);
            return new ResponseEntity<>(activityUpdatedResponse, HttpStatus.OK);
        } else {
            throw new NullArgumentException("Activity Id cannot be null");
        }
    }

    @PostMapping("/activity-id/{activityId}/review")
    public ResponseEntity<ActivityReviewDTO> addActivityReview(
            @PositiveOrZero(message = "Activity Id must be a positive or zero value Long") @PathVariable("activityId") Long activityId,
            @Valid @RequestBody ActivityReviewDTO activityReview) {

        if (activityId != null) {
            ActivityReviewDTO activityReviewResponse = activityService.addActivityReview(activityId, activityReview);
            return new ResponseEntity<>(activityReviewResponse, HttpStatus.CREATED);
        } else {
            throw new NullArgumentException("Activity Id cannot be null");
        }

    }

    @GetMapping("/activity-id/{activityId}/review")
    public ResponseEntity<ResponseModel<ActivityReview>> getActivityReviews(
            @PositiveOrZero(message = "Activity Id must be a positive or zero value Long") @PathVariable("activityId") Long activityId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number must be positive") int pageNo,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must " +
                    "be " +
                    "positive") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "starRating") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "DESC") String direction) {

        if (activityId != null) {
            Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);
            ResponseModel<ActivityReview> activityReviews = activityService.findActivityReviews(activityId,
                    pageRequestData);
            return new ResponseEntity<>(activityReviews, HttpStatus.PARTIAL_CONTENT);
        } else {
            throw new NullArgumentException("Activity Id cannot be null");
        }
    }

    @GetMapping("/activity-id/all-activities")
    public ResponseEntity<ResponseModel<ActivityDTO>> getAllActivities(
            @RequestParam(name = "isActive", required = false, defaultValue = "true") Boolean isActive,
            @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number must be positive") int pageNo,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must " +
                    "be " +
                    "positive") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, size, sort);
        ResponseModel<ActivityDTO> allActivities = activityService.getAllActivities(isActive, pageable);
        return new ResponseEntity<>(allActivities, HttpStatus.OK);
    }

    /**
     * Endpoint deletes an activity and all the bindings between it and packages.
     * The packages are not deleted however
     *
     * @param activityId
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteActivity(@PositiveOrZero(message = "Invalid Activity Id : Must be a positive " +
            "Long") @RequestParam(name = "activityId", required = true) Long activityId) {
        activityService.deleteActivity(activityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
