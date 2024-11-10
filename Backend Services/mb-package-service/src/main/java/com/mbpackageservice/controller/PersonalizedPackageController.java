package com.mbpackageservice.controller;

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

import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageActivityBindDTO;
import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageDTO;
import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageUpdateDTO;
import com.mbpackageservice.service.PersonalizedPackageService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/package-service/personalized-package")
@Validated
public class PersonalizedPackageController {

    @Autowired
    private PersonalizedPackageService personalizedPackageService;

    @PostMapping
    public ResponseEntity<PersonalizedPackageDTO> addActivity(
            @Valid @RequestBody PersonalizedPackageDTO personalizedPackageDTO) throws Exception {
        return ResponseEntity.ok().body(personalizedPackageService.addPersonalizedPackage(personalizedPackageDTO));
    }

    @GetMapping
    public ResponseEntity<ResponseModel<PersonalizedPackageDTO>> getAllPersonalizedPackage(
            @RequestParam(name = "userId", required = true) @NotBlank(message = "User id must not be blank or null") String userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number " +
                    "must be a positive integer") int pageNo,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must " +
                    "be a positive integer") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);
        ResponseModel<PersonalizedPackageDTO> pagedPersonalizdPackages = personalizedPackageService
                .findAllPersonalizedPackage(userId, pageRequestData);
        return new ResponseEntity<>(pagedPersonalizdPackages, HttpStatus.PARTIAL_CONTENT);
    }

    @PutMapping("/personal-package-id/{personalPackageId}/add-activities")
    public ResponseEntity<PersonalizedPackageDTO> addActivitiesToPersonalPackage(
            @PathVariable("personalPackageId") @PositiveOrZero(message = "Personal package id must be a positive or " +
                    "zero value Long") Long personalPackageId,
            @Valid @RequestBody List<PersonalizedPackageActivityBindDTO> personalPackageBindDTOs) throws Exception {

        if (personalPackageId != null) {
            PersonalizedPackageDTO personalPackageUpdateResponse = personalizedPackageService
                    .addActivitiesToPersonalPackage(personalPackageId, personalPackageBindDTOs);
            return new ResponseEntity<>(personalPackageUpdateResponse, HttpStatus.OK);
        } else {
            throw new NullArgumentException("Personal package id must not be null");
        }
    }

    /**
     * This endpoint updates a personalized package where each field is filled out
     * in the DTO.
     * Returns the updated DTO.
     *
     * @param personalizedPackageId
     * @param personalizedPackageUpdateDTO
     * @return PersonalizedPackageDTO updatedPersonalizedPackage
     */
    @PutMapping("personalized-package-id/{personalized-package-id}")
    public ResponseEntity<PersonalizedPackageDTO> updatePersonalizedPackage(
            @PathVariable("personalized-package-id") @PositiveOrZero(message = "Personal package id must be a positive or "
                    +
                    "zero value Long") Long personalizedPackageId,
            @Valid @RequestBody PersonalizedPackageUpdateDTO personalizedPackageUpdateDTO) {
        if (personalizedPackageId != null) {
            return new ResponseEntity<>(personalizedPackageService.updatePersonalizedPackageById(personalizedPackageId,
                    personalizedPackageUpdateDTO), HttpStatus.OK);
        } else {
            throw new NullArgumentException("Personal package id must not be null");
        }
    }

    /**
     * This endpoint retrieves a PersonalizedPackageDTO by its ID (unique
     * identifier).
     *
     * @param personalPackageId
     * @return
     */
    @GetMapping("/personal-package-id")
    public ResponseEntity<PersonalizedPackageDTO> getPersonalizedPackageById(
            @RequestParam(name = "personalPackageId", required = true) @PositiveOrZero(message = "Personal package " +
                    "id must be a positive or zero value Long") Long personalPackageId) {

        if (personalPackageId != null) {
            return new ResponseEntity<>(personalizedPackageService.findPersonalizedPackageById(personalPackageId),
                    HttpStatus.OK);
        } else {
            throw new NullArgumentException("Personal package id must not be null");
        }
    }

    /**
     * This endpoint deletes a PersonalizedPackage by its ID (unique identifier).
     *
     * @param personalPackageId
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deletePersonalizedPackageById(
            @RequestParam(name = "personalPackageId", required = true) @PositiveOrZero(message = "Personal package " +
                    "id must be a positive or zero value Long") Long personalPackageId) {

        if (personalPackageId != null) {
            personalizedPackageService.deletePersonalizedPackageById(personalPackageId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NullArgumentException("Personal package id must not be null");
        }
    }

}
