package com.megabliss.mbpackageservice.service.impl;

import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.personalizedpackage.PersonalizedPackageActivityBindDTO;
import com.megabliss.mbpackageservice.dto.personalizedpackage.PersonalizedPackageDTO;
import com.megabliss.mbpackageservice.dto.personalizedpackage.PersonalizedPackageUpdateDTO;
import com.megabliss.mbpackageservice.exception.ResourceNotFoundException;
import com.megabliss.mbpackageservice.model.PersonalizedPackageActivityBind;
import com.megabliss.mbpackageservice.model.activity.Activity;
import com.megabliss.mbpackageservice.model.activity.ActivityTime;
import com.megabliss.mbpackageservice.model.personalizedpackage.PersonalizedPackage;
import com.megabliss.mbpackageservice.repository.activity.ActivityRepository;
import com.megabliss.mbpackageservice.repository.personalizepackage.PersonalizedPackageRepository;
import com.megabliss.mbpackageservice.service.PersonalizedPackageService;
import com.megabliss.mbpackageservice.utils.PersonalizedPackageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonalizedPackageServiceImpl implements PersonalizedPackageService {

    @Autowired
    private PersonalizedPackageUtils personalizedPackageUtils;

    @Autowired
    private PersonalizedPackageRepository personalizedPackageRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public PersonalizedPackageDTO addPersonalizedPackage(PersonalizedPackageDTO personalizedPackageDTO) throws Exception {
        PersonalizedPackage personalizedPackage = personalizedPackageUtils.getPersonalizedPackagMapper()
                .mapToPersonalizedPackage(personalizedPackageDTO);
        List<PersonalizedPackageActivityBind> packageActivityBinds = (personalizedPackageDTO
                .getPersonalizedPackageActivityBinds() == null) ? new ArrayList<>()
                : createPersonalActivityBindList(personalizedPackageDTO.getPersonalizedPackageActivityBinds());
        personalizedPackage.setPersonalizedPackageActivityBinds(packageActivityBinds);
        PersonalizedPackage savedPersonalizedPackage = this.personalizedPackageRepository.save(personalizedPackage);
        return personalizedPackageUtils.getPersonalizedPackagMapper()
                .mapToPersonalizedPackageDto(savedPersonalizedPackage);
    }

    @Override
    public ResponseModel<PersonalizedPackageDTO> findAllPersonalizedPackage(String userId, Pageable pageable) {
        Page<PersonalizedPackage> personalizedPackages = personalizedPackageRepository.findAllByUserId(userId,
                pageable);
        return personalizedPackageUtils.buildPersonalizedPackageDTOResponse(personalizedPackages);
    }

    @Override
    public PersonalizedPackageDTO addActivitiesToPersonalPackage(Long personalPackageId,
                                                                 List<PersonalizedPackageActivityBindDTO> personalPackageBindDTO) throws Exception {
        Optional<PersonalizedPackage> personalPackageDbResponse = personalizedPackageRepository
                .findById(personalPackageId);
        if (personalPackageDbResponse.isPresent()) {
            PersonalizedPackage personalPackage = personalPackageDbResponse.get();
            List<PersonalizedPackageActivityBind> packageActivityBinds = createPersonalActivityBindList(
                    personalPackageBindDTO);
            personalPackage.getPersonalizedPackageActivityBinds().clear();
            personalPackage.getPersonalizedPackageActivityBinds().addAll(packageActivityBinds);
            PersonalizedPackage savedPersonalPackage = personalizedPackageRepository.save(personalPackage);
            return personalizedPackageUtils.getPersonalizedPackagMapper()
                    .mapToPersonalizedPackageDto(savedPersonalPackage);
        } else {
            throw new ResourceNotFoundException("Record not found personal package with id : " + personalPackageId);
        }
    }

    @Override
    public PersonalizedPackageDTO findPersonalizedPackageById(Long personalPackageId) {
        Optional<PersonalizedPackage> personalPackageDbResponse = personalizedPackageRepository
                .findById(personalPackageId);
        if (personalPackageDbResponse.isPresent()) {

            PersonalizedPackage personalPackage = personalPackageDbResponse.get();

            return personalizedPackageUtils.getPersonalizedPackagMapper()
                    .mapToPersonalizedPackageDto(personalPackage);
        } else {
            throw new ResourceNotFoundException("Record not found personal package with id : " + personalPackageId);
        }

    }

    @Override
    public void deletePersonalizedPackageById(Long personalPackageId) {
        if (personalizedPackageRepository.existsById(personalPackageId)) {
            personalizedPackageRepository.deleteById(personalPackageId);
        }else {
            throw new ResourceNotFoundException("Record not found personal package with id : " + personalPackageId);
        }
    }

    @Override
    public PersonalizedPackageDTO updatePersonalizedPackageById(Long personalPackageId,
                                                                PersonalizedPackageUpdateDTO personalizedPackageUpdateDTO) {

        Optional<PersonalizedPackage> retrievePersonalPackage = personalizedPackageRepository
                .findById(personalPackageId);
        if (retrievePersonalPackage.isPresent()) {
            PersonalizedPackage personalPackage = retrievePersonalPackage.get();
            PersonalizedPackage updatedPersonalPackage = personalizedPackageUtils.getPersonalizedPackagMapper()
                    .updatePersonalizedPackage(personalizedPackageUpdateDTO, personalPackage);
            PersonalizedPackage updatedAndSavedPersonalPackage = personalizedPackageRepository
                    .save(updatedPersonalPackage);

            return personalizedPackageUtils.getPersonalizedPackagMapper()
                    .mapToPersonalizedPackageDto(updatedAndSavedPersonalPackage);

        } else {
            throw new ResourceNotFoundException("Record not found personal package with id : " + personalPackageId);
        }

    }

    private List<PersonalizedPackageActivityBind> createPersonalActivityBindList(
            List<PersonalizedPackageActivityBindDTO> personalPackageBindDTOs) throws Exception {
        List<Long> activityIdList = personalPackageBindDTOs.stream()
                .map(PersonalizedPackageActivityBindDTO::getActivityId).collect(Collectors.toList());
        List<Activity> activityList = activityRepository.findAllById(activityIdList);
        List<PersonalizedPackageActivityBind> personalActivityBinds = new ArrayList<>();
        for (PersonalizedPackageActivityBindDTO personalPackageBindDTO : personalPackageBindDTOs) {
            PersonalizedPackageActivityBind personalActivityBind = new PersonalizedPackageActivityBind();

            // get related activity to bind with package
            Activity relatedActivity = activityList.stream()
                    .filter(activity -> personalPackageBindDTO.getActivityId().equals(activity.getId())).findAny()
                    .orElse(null);
            if (relatedActivity != null && relatedActivity.getIsOneTimeEvent()) {
                throw new Exception("One time activity booking is forbidden in personal packages");
            }
            // check for activity time id
            ActivityTime relatedActivityTime = relatedActivity == null ? null
                    : relatedActivity.getActivityTimes().stream()
                    .filter(activityTime -> personalPackageBindDTO.getActivityTimeId()
                            .equals(activityTime.getId()))
                    .findAny().orElse(null);
            if (relatedActivity != null && relatedActivityTime != null) {
                personalActivityBind.setActivity(relatedActivity);
                personalActivityBind.setActivityTimeId(personalPackageBindDTO.getActivityTimeId());
                personalActivityBind.setDayNumber(personalPackageBindDTO.getDayNumber());
                personalActivityBinds.add(personalActivityBind);
            }
        }
        return personalActivityBinds;
    }

}
