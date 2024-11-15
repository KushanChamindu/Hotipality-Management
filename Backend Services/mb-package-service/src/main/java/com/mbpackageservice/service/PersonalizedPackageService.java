package com.mbpackageservice.service;

import org.springframework.data.domain.Pageable;

import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageActivityBindDTO;
import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageDTO;
import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageUpdateDTO;

import java.util.List;

public interface PersonalizedPackageService {

    public PersonalizedPackageDTO addPersonalizedPackage(PersonalizedPackageDTO personalizedPackageDTO)
            throws Exception;

    public ResponseModel<PersonalizedPackageDTO> findAllPersonalizedPackage(String userId, Pageable pageable);

    public PersonalizedPackageDTO addActivitiesToPersonalPackage(Long personalPackageId,
            List<PersonalizedPackageActivityBindDTO> personalPackageBindDTO) throws Exception;

    /**
     * Method finds a list of ActivityPackages for a provider by their ID.
     *
     * @param personalPackageId
     * @return A list of ActivityPackageDTOs
     */
    public PersonalizedPackageDTO findPersonalizedPackageById(Long personalPackageId);

    /**
     * Method deletes a personalized package by the package ID.
     *
     * @param personalPackageId
     */
    public void deletePersonalizedPackageById(Long personalPackageId);

    /**
     * Method updates a personalized package where each field in the
     * personalizedPackageDTO is filled out. Returns
     * the updated fields.
     *
     * @param personalPackageId
     * @param personalizedPackageDTO
     * @return
     */
    public PersonalizedPackageDTO updatePersonalizedPackageById(Long personalPackageId,
            PersonalizedPackageUpdateDTO personalizedPackageUpdateDTO);
}
