package com.mbpackageservice.dao.mapper;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dto.packagebusiness.PackageBusinessDTO;
import com.mbpackageservice.dto.packagebusiness.PackageBusinessUpdateDTO;
import com.mbpackageservice.model.packagebusiness.PackageBusiness;

@Component
@Getter
public class PackageBusinessMapper {

    @Autowired
    private ModelMapper modelMapper;

    // convert PackageBusiness Jpa Entity into PackageBusinessDTO
    public PackageBusinessDTO mapToPackageBusinessDto(PackageBusiness packageBusiness) {
        return modelMapper.map(packageBusiness, PackageBusinessDTO.class);
    }

    // Convert ActivityDTO to Activity JPA Entity
    public PackageBusiness mapToPackageBusiness(PackageBusinessDTO packageBusinessDTO) {
        return modelMapper.map(packageBusinessDTO, PackageBusiness.class);
    }

    public PackageBusiness updatePackageBusiness(PackageBusinessUpdateDTO updatePackageBusinessDTO,
            PackageBusiness packageBusiness) {
        Long packageBusinessId = packageBusiness.getId();
        modelMapper.map(updatePackageBusinessDTO, packageBusiness);
        packageBusiness.setId(packageBusinessId);
        return packageBusiness;
    }
}
