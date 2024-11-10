package com.mbpackageservice.dao.mapper;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageDTO;
import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageUpdateDTO;
import com.mbpackageservice.model.personalizedpackage.PersonalizedPackage;

@Component
@Getter
public class PersonalizedPackagMapper {

    @Autowired
    private ModelMapper modelMapper;

    // convert PersonalizedPackage Jpa Entity into PersonalisedPackageDTO
    public PersonalizedPackageDTO mapToPersonalizedPackageDto(PersonalizedPackage personalizedpackage) {
        return modelMapper.map(personalizedpackage, PersonalizedPackageDTO.class);
    }

    // Convert PersonalisedPackageDTO to PersonalizedPackage JPA Entity
    public PersonalizedPackage mapToPersonalizedPackage(PersonalizedPackageDTO personalizedPackageDTO) {
        return modelMapper.map(personalizedPackageDTO, PersonalizedPackage.class);
    }

    public PersonalizedPackage updatePersonalizedPackage(PersonalizedPackageUpdateDTO updatedPersonalizedPackage,
            PersonalizedPackage personalizedPackage) {
        Long personalizedPackageId = personalizedPackage.getId();
        modelMapper.map(updatedPersonalizedPackage, personalizedPackage);
        personalizedPackage.setId(personalizedPackageId);

        return personalizedPackage;
    }
}
