package com.megabliss.mbpackageservice.config;

import com.megabliss.mbpackageservice.dto.activity.ActivityDTO;
import com.megabliss.mbpackageservice.dto.activitypackage.ActivityPackageDTO;
import com.megabliss.mbpackageservice.dto.booking.BookingDTO;
import com.megabliss.mbpackageservice.dto.booking.PersonalizedPackageBookingDTO;
import com.megabliss.mbpackageservice.dto.packagebusiness.PackageBusinessDTO;
import com.megabliss.mbpackageservice.dto.payment.PaymentIntentResponse;
import com.megabliss.mbpackageservice.dto.personalizedpackage.PersonalizedPackageDTO;
import com.megabliss.mbpackageservice.dto.personalizedpackage.PersonalizedPackageUpdateDTO;
import com.megabliss.mbpackageservice.model.Booking;
import com.megabliss.mbpackageservice.model.activity.Activity;
import com.megabliss.mbpackageservice.model.activitypackage.ActivityPackage;
import com.megabliss.mbpackageservice.model.packagebusiness.PackageBusiness;
import com.megabliss.mbpackageservice.model.payment.PaymentInfo;
import com.megabliss.mbpackageservice.model.personalizedpackage.PersonalizedPackage;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    private PropertyMap<ActivityPackage, ActivityPackageDTO> activityPackageToDTO = new PropertyMap<ActivityPackage, ActivityPackageDTO>() {
        protected void configure() {
            map().setActivityPackageId(source.getId());
        }
    };

    private PropertyMap<ActivityPackageDTO, ActivityPackage> activityPackageDTOtoEntity = new PropertyMap<ActivityPackageDTO, ActivityPackage>() {
        protected void configure() {
            map().setId(source.getActivityPackageId());
        }
    };

    private PropertyMap<Activity, ActivityDTO> activitytoDTO = new PropertyMap<Activity, ActivityDTO>() {
        protected void configure() {
            map().setActivityId(source.getId());
        }
    };

    private PropertyMap<ActivityDTO, Activity> activityDTOtoEntity = new PropertyMap<ActivityDTO, Activity>() {
        protected void configure() {
            map().setId(source.getActivityId());
        }
    };

    private PropertyMap<PersonalizedPackage, PersonalizedPackageDTO> personalizedPackagetoDTO = new PropertyMap<PersonalizedPackage, PersonalizedPackageDTO>() {
        protected void configure() {
            map().setPersonalizedPackageId(source.getId());
        }
    };

    private PropertyMap<PersonalizedPackageDTO, PersonalizedPackage> personalizedPackageDTOtoEntity = new PropertyMap<PersonalizedPackageDTO, PersonalizedPackage>() {
        protected void configure() {
            map().setId(source.getPersonalizedPackageId());
        }
    };

    private PropertyMap<Booking, PersonalizedPackageBookingDTO> personalizedBookingtoDTO = new PropertyMap<Booking, PersonalizedPackageBookingDTO>() {
        protected void configure() {
            map().setPersonalizedPackageId(source.getPersonalizedPackageId());

        }
    };

    private PropertyMap<PersonalizedPackageBookingDTO, Booking> personalizedPackageBookingDTOtoEntity = new PropertyMap<PersonalizedPackageBookingDTO, Booking>() {
        protected void configure() {
            map().setId(source.getPersonalizedPackageId());
        }
    };

    private PropertyMap<Booking, BookingDTO> bookingToBookingDTO = new PropertyMap<Booking, BookingDTO>() {
        protected void configure() {
            map().setId(source.getId());
        }
    };

    private PropertyMap<PersonalizedPackageUpdateDTO, PersonalizedPackage> personalizedPackageUpdateDTOtoEntity = new PropertyMap<PersonalizedPackageUpdateDTO, PersonalizedPackage>() {
        protected void configure() {
            map().setId(source.getPersonalizedPackageId());
        }
    };

    private PropertyMap<PackageBusiness, PackageBusinessDTO> packageBusinesstoDTO = new PropertyMap<PackageBusiness, PackageBusinessDTO>() {
        protected void configure() {
            map().setPackageBusinessId(source.getId());
        }
    };

    private PropertyMap<PackageBusinessDTO, PackageBusiness> packageBusinessDTOtoEntity = new PropertyMap<PackageBusinessDTO, PackageBusiness>() {
        protected void configure() {
            map().setId(source.getPackageBusinessId());
        }
    };

    private PropertyMap<PaymentIntentResponse, PaymentInfo> paymentIntentResponseToPaymentInfo = new PropertyMap<PaymentIntentResponse, PaymentInfo>() {
        protected void configure() {
            map().setId(0);
        }
    };

    // Define a custom PropertyMap to ignore specific fields
    PropertyMap<PackageBusinessDTO, PackageBusiness> packageBusinessSkipingFields = new PropertyMap<>() {
        protected void configure() {
            skip().setActivities(null);
            skip().setActivityPackages(null);
        }
    };

    @Bean
    public ModelMapper model() {
        ModelMapper modelMapper = new ModelMapper();
        // Activity package :- add custom map: because DTO deferent attribute to keep ID
        // of entity
        modelMapper.addMappings(activityPackageToDTO);
        modelMapper.addMappings(activityPackageDTOtoEntity);

        // add custom map: because DTO deferent attribute to keep ID of entity
        modelMapper.addMappings(activitytoDTO);
        modelMapper.addMappings(activityDTOtoEntity);

        // add custom map: because DTO deferent attribute to keep ID of entity
        modelMapper.addMappings(personalizedPackageDTOtoEntity);
        modelMapper.addMappings(personalizedPackagetoDTO);

        // add custom map: bookings require custom attributes to convert DTO
        modelMapper.addMappings(personalizedPackageBookingDTOtoEntity);
        modelMapper.addMappings(personalizedBookingtoDTO);

        // add custom map: for generalized booking DTO
        modelMapper.addMappings(bookingToBookingDTO);

        // add custom map: for convert personalizedPackageUpdateDTO to Entity
        modelMapper.addMappings(personalizedPackageUpdateDTOtoEntity);

        // add custom map: for convert paymentIntentResponse to PaymentInfo
        modelMapper.addMappings(paymentIntentResponseToPaymentInfo);

        // add custom map: because DTO deferent attribute to keep ID of entity
        modelMapper.addMappings(packageBusinesstoDTO);
        modelMapper.addMappings(packageBusinessDTOtoEntity);
        modelMapper.addMappings(packageBusinessSkipingFields);

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }

}
