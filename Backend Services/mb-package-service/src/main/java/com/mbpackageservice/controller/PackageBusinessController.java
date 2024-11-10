package com.mbpackageservice.controller;

import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.packagebusiness.PackageBusinessDTO;
import com.mbpackageservice.dto.packagebusiness.PackageBusinessUpdateDTO;
import com.mbpackageservice.service.PackageBusinessService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/package-service/package-business")
@Validated
public class PackageBusinessController {

    @Autowired
    private PackageBusinessService packageBusinessService;

    @PostMapping()
    public ResponseEntity<PackageBusinessDTO> postMethodName(
            @Valid @RequestBody PackageBusinessDTO packageBusinessDTO) {

        return ResponseEntity.ok().body(packageBusinessService.addBusiness(packageBusinessDTO));
    }

    @GetMapping("/service-provider/{serviceProviderId}")
    public ResponseEntity<PackageBusinessDTO> getBusinessInfoByServiceProviderId(
            @PathVariable("serviceProviderId") @NotBlank(message = "Service provider id must not be blank or null") String serviceProviderId) {
        PackageBusinessDTO packageBusinessInfo = packageBusinessService
                .getBusinessInfoByServiceProviderId(serviceProviderId);
        return new ResponseEntity<>(packageBusinessInfo, HttpStatus.OK);
    }

    @GetMapping("/all-package-business")
    public ResponseEntity<ResponseModel<PackageBusinessDTO>> getAllPackageBusiness(
            @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number must be positive") int pageNo,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must " +
                    "be " +
                    "positive") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, size, sort);
        ResponseModel<PackageBusinessDTO> allActivities = packageBusinessService.getAllPackageBusiness(pageable);
        return new ResponseEntity<>(allActivities, HttpStatus.OK);
    }

    @PutMapping("/service-provider/{serviceProviderId}")
    public ResponseEntity<PackageBusinessDTO> updatePackageBusiness(
            @PathVariable("serviceProviderId") @NotBlank(message = "Service provider id must not be blank or null") String serviceProviderId,
            @Valid @RequestBody PackageBusinessUpdateDTO packageBusinessUpdateDTO) {

        if (serviceProviderId != null) {
            PackageBusinessDTO packageBusinessUpdatedResponse = packageBusinessService
                    .updatePackageBusiness(serviceProviderId, packageBusinessUpdateDTO);
            return new ResponseEntity<>(packageBusinessUpdatedResponse, HttpStatus.OK);
        } else {
            throw new NullArgumentException("serviceProviderId cannot be null");
        }
    }

    @DeleteMapping("/service-provider/{serviceProviderId}")
    public ResponseEntity<Void> deletePackageBusiness(
            @PathVariable("serviceProviderId") @NotBlank(message = "Service provider id must not be blank or null") String serviceProviderId) {

        if (serviceProviderId != null) {
            packageBusinessService
                    .deletePackageBusiness(serviceProviderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NullArgumentException("serviceProviderId cannot be null");
        }
    }

}
