package com.megabliss.mbpackageservice.service;

import org.springframework.data.domain.Pageable;

import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.packagebusiness.PackageBusinessDTO;
import com.megabliss.mbpackageservice.dto.packagebusiness.PackageBusinessUpdateDTO;

public interface PackageBusinessService {

    public PackageBusinessDTO addBusiness(PackageBusinessDTO packageBusinessDTO);

    public PackageBusinessDTO getBusinessInfoByServiceProviderId(String serviceProviderId);

    public ResponseModel<PackageBusinessDTO> getAllPackageBusiness(Pageable pageable);

    public PackageBusinessDTO updatePackageBusiness(String serviceProviderId,
            PackageBusinessUpdateDTO packageBusinessUpdateDTO);

    public void deletePackageBusiness(String serviceProviderId);
}
