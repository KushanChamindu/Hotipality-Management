package com.mbpackageservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.packagebusiness.PackageBusinessDTO;
import com.mbpackageservice.dto.packagebusiness.PackageBusinessUpdateDTO;
import com.mbpackageservice.exception.GeneralException;
import com.mbpackageservice.exception.ResourceNotFoundException;
import com.mbpackageservice.model.activity.Activity;
import com.mbpackageservice.model.activitypackage.ActivityPackage;
import com.mbpackageservice.model.packagebusiness.PackageBusiness;
import com.mbpackageservice.repository.activity.ActivityRepository;
import com.mbpackageservice.repository.activitypackage.ActivityPackageRepository;
import com.mbpackageservice.repository.packagebusiness.PackageBusinessRepository;
import com.mbpackageservice.service.PackageBusinessService;
import com.mbpackageservice.utils.PackageBusinessUtils;

@Service
@Transactional
public class PackageBusinessServiceImpl implements PackageBusinessService {

    @Autowired
    private PackageBusinessRepository packageBusinessRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityPackageRepository activityPackageRepository;

    @Autowired
    private PackageBusinessUtils packageBusinessUtils;

    @Override
    public PackageBusinessDTO addBusiness(PackageBusinessDTO packageBusinessDTO) {
        try {
            PackageBusiness packageBusiness = packageBusinessUtils.getPackageBusinessMapper()
                    .mapToPackageBusiness(packageBusinessDTO);

            PackageBusiness packageBusinessSaved = packageBusinessRepository.save(packageBusiness);
            return packageBusinessUtils.getPackageBusinessMapper().mapToPackageBusinessDto(packageBusinessSaved);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(
                    "Service Provider id should be unique: " + packageBusinessDTO.getServiceProviderId());
        }

    }

    @Override
    public PackageBusinessDTO getBusinessInfoByServiceProviderId(String serviceProviderId) {
        PackageBusiness packageBusiness = packageBusinessRepository.findByServiceProviderId(serviceProviderId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Couldn't find business info for: " + serviceProviderId));
        return packageBusinessUtils.getPackageBusinessMapper().mapToPackageBusinessDto(packageBusiness);
    }

    @Override
    public ResponseModel<PackageBusinessDTO> getAllPackageBusiness(Pageable pageable) {
        Page<PackageBusiness> packageBusiness = packageBusinessRepository.findAll(pageable);
        return packageBusinessUtils.buildActivityDTOResponse(packageBusiness);
    }

    @Override
    public PackageBusinessDTO updatePackageBusiness(String serviceProviderId,
            PackageBusinessUpdateDTO packageBusinessUpdateDTO) {
        Optional<PackageBusiness> packageBusinessDbResponse = packageBusinessRepository
                .findByServiceProviderId(serviceProviderId);
        if (packageBusinessDbResponse.isPresent()) {
            PackageBusiness packageBusiness = packageBusinessDbResponse.get();
            PackageBusiness update = packageBusinessUtils.getPackageBusinessMapper().updatePackageBusiness(
                    packageBusinessUpdateDTO,
                    packageBusiness);
            PackageBusiness updateAndSaved = packageBusinessRepository.save(update);
            return packageBusinessUtils.getPackageBusinessMapper().mapToPackageBusinessDto(updateAndSaved);
        } else {
            throw new ResourceNotFoundException("Record not found serviceProviderId with id : " + serviceProviderId);
        }
    }

    @Override
    public void deletePackageBusiness(String serviceProviderId) {
        Optional<PackageBusiness> packageBusiness = packageBusinessRepository
                .findByServiceProviderId(serviceProviderId);

        if (packageBusiness.isPresent()) {
            Boolean isExistActivities = activityRepository.existsByServiceProviderId(serviceProviderId);
            if (Boolean.TRUE.equals(isExistActivities)) {
                throw new GeneralException(
                        "Unable to delete business since acitivies can be found under this business");
            }
            Boolean isExistPackages = activityPackageRepository
                    .existsByServiceProviderId(serviceProviderId);
            if (Boolean.TRUE.equals(isExistPackages)) {
                throw new GeneralException(
                        "Unable to delete business since packages can be found under this business");
            }
            packageBusinessRepository.deleteByServiceProviderId(serviceProviderId);
        } else {
            throw new ResourceNotFoundException(
                    "Record not found packageBusiness with service provider id : " + serviceProviderId);
        }
    }

}
