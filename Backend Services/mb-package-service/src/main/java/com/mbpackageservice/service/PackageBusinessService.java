package com.mbpackageservice.service;

import org.springframework.data.domain.Pageable;

import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.packagebusiness.PackageBusinessDTO;
import com.mbpackageservice.dto.packagebusiness.PackageBusinessUpdateDTO;

public interface PackageBusinessService {

    public PackageBusinessDTO addBusiness(PackageBusinessDTO packageBusinessDTO);

    public PackageBusinessDTO getBusinessInfoByServiceProviderId(String serviceProviderId);

    public ResponseModel<PackageBusinessDTO> getAllPackageBusiness(Pageable pageable);

    public PackageBusinessDTO updatePackageBusiness(String serviceProviderId,
            PackageBusinessUpdateDTO packageBusinessUpdateDTO);

    public void deletePackageBusiness(String serviceProviderId);
}
