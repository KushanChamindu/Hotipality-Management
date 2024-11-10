package com.mbpackageservice.controller;

import com.google.maps.errors.ApiException;
import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.activitypackage.ActivityPackageBindDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageReviewDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageUpdateDTO;
import com.mbpackageservice.model.activitypackage.ActivityPackageReview;
import com.mbpackageservice.model.enums.EventLevel;
import com.mbpackageservice.model.enums.EventType;
import com.mbpackageservice.service.ActivityPackageService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin

@RestController
@RequestMapping("/api/v1/package-service/activity-package")
@Validated
public class ActivityPackageController {

        @Autowired
        private ActivityPackageService activityPackageService;

        @GetMapping("/nearest-activity")
        public ResponseEntity<ResponseModel<ActivityPackageDTO>> findActivitiesWithinDistance(
                        @RequestParam(name = "latitude", required = false) Double latitude,
                        @RequestParam(name = "longitude", required = false) Double longitude,
                        @RequestParam(name = "distance", required = false, defaultValue = "100") Double distance,
                        @RequestParam(name = "type", required = false) EventType type,
                        @RequestParam(name = "level", required = false) EventLevel level,
                        @RequestParam(name = "review", required = false) Float review,
                        @RequestParam(name = "availability", required = false, defaultValue = "true") Boolean availability,
                        @RequestParam(name = "lowerPrice", required = false) Double lowerPrice,
                        @RequestParam(name = "upperPrice", required = false) Double upperPrice,
                        @Positive(message = "Page number must be positive") @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
                        @Positive(message = "Page size must be positive") @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                        @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
                        @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction)
                        throws IOException, InterruptedException, ApiException {

                Pageable pageRequestData;
                if (latitude != null && longitude != null) { // if long lat are present sort by distance in query
                        pageRequestData = PageRequest.of(pageNo - 1, size);
                } else {
                        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                        : Sort.by(sortBy).descending();
                        pageRequestData = PageRequest.of(pageNo - 1, size, sort);
                }
                return new ResponseEntity<>(
                                activityPackageService.filteredActivityPackages(latitude, longitude, distance,
                                                type, level, review, availability, lowerPrice, upperPrice,
                                                pageRequestData),
                                HttpStatus.PARTIAL_CONTENT);
        }

        @PostMapping
        public ResponseEntity<ActivityPackageDTO> addActivityPackages(
                        @Valid @RequestBody ActivityPackageDTO activityPackageDTO) {
                return ResponseEntity.ok().body(activityPackageService.addActivityPackages(activityPackageDTO));
        }

        @GetMapping
        public ResponseEntity<ResponseModel<ActivityPackageDTO>> getActivityPackageByQuery(
                        @RequestParam(name = "title", required = true) @NotBlank(message = "Activity Title must not be blank or "
                                        +
                                        "null") String title,
                        @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number "
                                        +
                                        "must be positive") int pageNo,
                        @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Size must be "
                                        +
                                        "positive") int size,
                        @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
                        @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {
                Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();
                Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);
                ResponseModel<ActivityPackageDTO> searchedActivityPackages = activityPackageService
                                .findAllActivityPackagesByTitle(title, pageRequestData);
                return new ResponseEntity<>(searchedActivityPackages, HttpStatus.PARTIAL_CONTENT);
        }

        @GetMapping("/activity-package-id/{activityPackageId}")
        public ResponseEntity<ActivityPackageDTO> getActivityPackageById(
                        @NotNull(message = "Activity Package Id cannot be null!") @PositiveOrZero(message = "Activity Package Id must be positive or zero!") @PathVariable("activityPackageId") Long activityPackageId) {
                ActivityPackageDTO activityPackageDTO = activityPackageService
                                .findActivityPackageByActivityPackageId(activityPackageId);
                return new ResponseEntity<>(activityPackageDTO, HttpStatus.PARTIAL_CONTENT);
        }

        @PutMapping("/activity-package-id/{activityPackageId}")
        public ResponseEntity<ActivityPackageDTO> updateActivityPackage(
                        @PathVariable("activityPackageId") @PositiveOrZero(message = "Activity Package Id must be positive or "
                                        +
                                        "zero value Long") Long activityPackageId,
                        @Valid @RequestBody ActivityPackageUpdateDTO activityPackageUpdateDTO) {

                if (activityPackageId != null) {
                        ActivityPackageDTO activityPackageUpdateResponse = activityPackageService
                                        .updateActivityPackage(activityPackageId, activityPackageUpdateDTO);
                        return new ResponseEntity<>(activityPackageUpdateResponse, HttpStatus.OK);
                } else {
                        throw new NullArgumentException("Activity Package Id cannot be null");
                }
        }

        @PutMapping("/activity-package-id/{activityPackageId}/add-activities")
        public ResponseEntity<ActivityPackageDTO> addActivitiesToPackage(
                        @PathVariable("activityPackageId") @PositiveOrZero(message = "Activity Package Id must be positive or "
                                        +
                                        "zero value Long") Long activityPackageId,
                        @Valid @RequestBody List<ActivityPackageBindDTO> activityPackageBindDTOs) {

                if (activityPackageId != null) {
                        ActivityPackageDTO activityPackageUpdateResponse = activityPackageService
                                        .addActivitiesToPackage(activityPackageId, activityPackageBindDTOs);
                        return new ResponseEntity<>(activityPackageUpdateResponse, HttpStatus.OK);
                } else {
                        throw new NullArgumentException("Activity Package Id cannot be null");
                }
        }

        @PostMapping("/activity-package-id/{activityPackageId}/review")
        public ResponseEntity<ActivityPackageReviewDTO> addActivityPackageReview(
                        @PathVariable("activityPackageId") @PositiveOrZero(message = "Activity Package Id must be positive or "
                                        +
                                        "zero value Long") Long activityPackageId,
                        @Valid @RequestBody ActivityPackageReviewDTO activityPackageReview) {

                if (activityPackageId != null) {
                        ActivityPackageReviewDTO activityPackageReviewResponse = activityPackageService
                                        .addActivityPackageReview(activityPackageId, activityPackageReview);
                        return new ResponseEntity<>(activityPackageReviewResponse, HttpStatus.CREATED);
                } else {
                        throw new NullArgumentException("Activity Package Id cannot be null");
                }
        }

        @GetMapping("/activity-package-id/{activityPackageId}/review")
        public ResponseEntity<ResponseModel<ActivityPackageReview>> getActivityReveiws(
                        @PathVariable("activityPackageId") @PositiveOrZero(message = "Activity package Id must be positive or "
                                        +
                                        "zero value Long") Long activityPackageId,
                        @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number must be a positive integer") int pageNo,
                        @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must "
                                        +
                                        "be a positive integer") int size,
                        @RequestParam(name = "sortBy", required = false, defaultValue = "starRating") String sortBy,
                        @RequestParam(name = "direction", required = false, defaultValue = "DESC") String direction) {

                if (activityPackageId != null) {
                        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                        : Sort.by(sortBy).descending();
                        Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);
                        ResponseModel<ActivityPackageReview> activityPackgeReviews = activityPackageService
                                        .findActivityPackageReviews(activityPackageId, pageRequestData);
                        return new ResponseEntity<>(activityPackgeReviews, HttpStatus.PARTIAL_CONTENT);
                } else {
                        throw new NullArgumentException("Activity Package Id cannot be null");
                }
        }

        /**
         * Returns a provider a list of all their packages (no pagination)
         * 200 ok on success
         *
         * @param serviceProviderId
         * @return
         */
        @GetMapping("/service-provider-id/{serviceProviderId}")
        public ResponseEntity<List<ActivityPackageDTO>> getProviderPackages(
                        @PathVariable("serviceProviderId") @NotBlank(message = "Service provider id must not be blank or null") String serviceProviderId) {

                List<ActivityPackageDTO> activityPackageDTOs = activityPackageService
                                .findProviderActivityPackages(serviceProviderId);

                return new ResponseEntity<>(activityPackageDTOs, HttpStatus.OK);
        }

        @GetMapping("/all-package-activities")
        public ResponseEntity<ResponseModel<ActivityPackageDTO>> getAllMyPackageActivities(
                        @RequestParam(name = "isActive", required = false, defaultValue = "true") Boolean isActive,
                        @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number must be a positive integer") int pageNo,
                        @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must "
                                        +
                                        "be a positive integer") int size,
                        @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
                        @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {

                Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();
                Pageable pageable = PageRequest.of(pageNo - 1, size, sort);

                ResponseModel<ActivityPackageDTO> packageActivities = activityPackageService.getMyPackageActivities(
                                isActive,
                                pageable);
                return new ResponseEntity<>(packageActivities, HttpStatus.OK);
        }

        /**
         * Endpoint deletes a package by id but does not delete the activities.
         *
         * @param packageId
         * @return
         */
        @DeleteMapping
        public ResponseEntity<Void> deletePackageById(
                        @PositiveOrZero(message = "Invalid Package Id : Must be a positive Long") @RequestParam(name = "packageId", required = true) Long packageId) {

                activityPackageService.deleteActivityPackageById(packageId);

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
}
