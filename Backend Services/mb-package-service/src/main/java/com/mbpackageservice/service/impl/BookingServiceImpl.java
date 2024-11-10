package com.mbpackageservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbpackageservice.dao.mapper.PaymentDataMapper;
import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.booking.ActivityBookingDTO;
import com.mbpackageservice.dto.booking.PackageBookingDTO;
import com.mbpackageservice.dto.booking.PersonalizedPackageBookingDTO;
import com.mbpackageservice.dto.payment.PaymentIntentResponse;
import com.mbpackageservice.exception.BookingException;
import com.mbpackageservice.exception.ResourceNotFoundException;
import com.mbpackageservice.model.Booking;
import com.mbpackageservice.model.PersonalizedPackageActivityBind;
import com.mbpackageservice.model.activity.Activity;
import com.mbpackageservice.model.activity.ActivityBooking;
import com.mbpackageservice.model.activity.ActivitySpecialTime;
import com.mbpackageservice.model.activity.ActivityTime;
import com.mbpackageservice.model.activitypackage.ActivityPackage;
import com.mbpackageservice.model.enums.BookingStatus;
import com.mbpackageservice.model.payment.PaymentInfo;
import com.mbpackageservice.model.personalizedpackage.PersonalizedPackage;
import com.mbpackageservice.repository.BookingRepository;
import com.mbpackageservice.repository.activity.ActivityRepository;
import com.mbpackageservice.repository.activitypackage.ActivityPackageRepository;
import com.mbpackageservice.repository.personalizepackage.PersonalizedPackageRepository;
import com.mbpackageservice.service.BookingService;
import com.mbpackageservice.service.PaymentService;
import com.mbpackageservice.utils.BookingUtils;
import com.mbpackageservice.utils.CommonPageUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

        @Autowired
        BookingUtils bookingUtils;
        @Autowired
        CommonPageUtils commonPageUtils;
        @Autowired
        PaymentService paymentService;
        @Autowired
        PaymentDataMapper paymentDataMapper;
        @Autowired
        private BookingRepository bookingRepository;
        @Autowired
        private ActivityRepository activityRepository;
        @Autowired
        private ActivityPackageRepository activityPackageRepository;
        @Autowired
        private PersonalizedPackageRepository personalizedPackageRepository;

        @Override
        public ActivityBookingDTO bookActivity(ActivityBookingDTO activityBookingDTO) {
                Activity bookingActivity = activityRepository.findById(activityBookingDTO.getActivityId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Couldn't find activity related to activityId = "
                                                                + activityBookingDTO.getActivityId()));
                if (bookingActivity.getIsOneTimeEvent()) { // if is one time event - different handling
                        return bookOneTimeActivity(bookingActivity, activityBookingDTO);
                }
                // Get all confirmed booking of current service provider
                List<Booking> relatedBookings = bookingRepository.findAllByServiceProviderIdAndBookingStatusAndFromDate(
                                bookingActivity.getServiceProviderId(), BookingStatus.CONFIRMED,
                                activityBookingDTO.getFromDate());

                // Get realated activityBooking for considered activity
                List<ActivityBooking> activityBookings = bookingUtils.getRelatedActivityBooking(
                                activityBookingDTO.getActivityTimeId(),
                                bookingActivity, relatedBookings);

                // Get all activity time of cuurent activity
                List<ActivityTime> activityTimes = bookingActivity.getActivityTimes().stream()
                                .filter(item -> item.getId() == activityBookingDTO.getActivityTimeId())
                                .collect(Collectors.toList());

                // check for the availability of current activity (If this activity is all day
                // activity, don't check for the availability)
                if (Boolean.TRUE.equals(bookingActivity.getIsAlldayActivity())
                                || !activityTimes.isEmpty() && bookingUtils
                                                .isOutOfStock(activityBookings, activityBookingDTO.getQuantity(),
                                                                activityTimes.get(0).getQuantity())) {

                        // Calculate the price
                        Float totalPrice = bookingActivity.getPrice() * activityBookingDTO.getQuantity();

                        // Creat activity booking and booking object and save.
                        List<ActivityBooking> activityBookingObject = Arrays
                                        .asList(ActivityBooking.builder().quantity(activityBookingDTO.getQuantity())
                                                        .activityTimeId(activityBookingDTO.getActivityTimeId())
                                                        .activity(bookingActivity)
                                                        .startTime(LocalDateTime.of(activityBookingDTO.getFromDate(),
                                                                        activityTimes.get(0).getStartTime()))
                                                        .finishTime(LocalDateTime.of(activityBookingDTO.getFromDate(),
                                                                        activityTimes.get(0).getEndTime()))
                                                        .build());

                        Booking bookingObject = Booking.builder().customerId(activityBookingDTO.getCustomerId())
                                        .totalPrice(totalPrice)
                                        .bookingStatus(BookingStatus.REQUEST_PAYMENT)
                                        .fromDate(activityBookingDTO.getFromDate())
                                        .toDate(activityBookingDTO.getToDate()).activityBookings(activityBookingObject)
                                        .bookingDate(LocalDateTime.now())
                                        .isPersonalizedPackage(Boolean.FALSE)
                                        .bookingNo(bookingUtils.generateRandomString())
                                        .userName(activityBookingDTO.getUserName())
                                        .notes(activityBookingDTO.getNotes())
                                        .phoneNumber(activityBookingDTO.getPhoneNumber())
                                        .serviceProviderId(bookingActivity.getServiceProviderId()).build();
                        PaymentIntentResponse paymentIntentResponse = paymentService.createPayementIntent(
                                        bookingObject.getCustomerId(),
                                        bookingObject.getServiceProviderId(),
                                        bookingObject.getTotalPrice(), null, "nzd"); // here hard coded the currency for
                                                                                     // now
                        PaymentInfo paymentInfo = paymentDataMapper.mapToPaymentInfo(paymentIntentResponse);
                        bookingObject.setPaymentInfo(paymentInfo);
                        bookingRepository.save(bookingObject);
                        activityBookingDTO.setPaymentInfo(paymentInfo);
                        return activityBookingDTO;
                } else {
                        // return null for now, have to handle exception
                        throw new BookingException("Out of stock or acivity time id is wrong");
                }
        }

        public ActivityBookingDTO bookOneTimeActivity(Activity activity, ActivityBookingDTO activityBookingDTO) {
                // Get all confirmed booking of current service provider
                List<Booking> relatedBookings = bookingRepository.findAllByServiceProviderIdAndBookingStatusAndFromDate(
                                activity.getServiceProviderId(), BookingStatus.CONFIRMED,
                                activityBookingDTO.getFromDate());

                // Get realated activityBooking for considered activity
                List<ActivityBooking> activityBookings = bookingUtils.getRelatedSpecialActivityBooking(
                                activityBookingDTO.getSpecialActivityTimeId(),
                                activity, relatedBookings);

                // Get all activity time of cuurent activity
                List<ActivitySpecialTime> specialActivityTimes = activity.getSpecialActivityTimes().stream()
                                .filter(item -> item.getId() == activityBookingDTO.getSpecialActivityTimeId())
                                .collect(Collectors.toList());

                if (Boolean.TRUE.equals(activity.getIsAlldayActivity())
                                || !specialActivityTimes.isEmpty() && bookingUtils
                                                .isOutOfStock(activityBookings, activityBookingDTO.getQuantity(),
                                                                specialActivityTimes.get(0).getQuantity())) {
                        // Calculate the price
                        Float totalPrice = activity.getPrice() * activityBookingDTO.getQuantity();

                        // Creat activity booking and booking object and save.
                        List<ActivityBooking> activityBookingObject = Arrays
                                        .asList(ActivityBooking.builder().quantity(activityBookingDTO.getQuantity())
                                                        .specialActivityTimeId(
                                                                        activityBookingDTO.getSpecialActivityTimeId())
                                                        .startTime(specialActivityTimes.get(0).getStartTime())
                                                        .finishTime(specialActivityTimes.get(0).getEndTime())
                                                        .activity(activity).build());

                        Booking bookingObject = Booking.builder().customerId(activityBookingDTO.getCustomerId())
                                        .totalPrice(totalPrice)
                                        .bookingStatus(BookingStatus.REQUEST_PAYMENT)
                                        .fromDate(activityBookingDTO.getFromDate())
                                        .toDate(activityBookingDTO.getToDate()).activityBookings(activityBookingObject)
                                        .bookingDate(LocalDateTime.now())
                                        .isPersonalizedPackage(Boolean.FALSE)
                                        .bookingNo(bookingUtils.generateRandomString())
                                        .userName(activityBookingDTO.getUserName())
                                        .notes(activityBookingDTO.getNotes())
                                        .phoneNumber(activityBookingDTO.getPhoneNumber())
                                        .serviceProviderId(activity.getServiceProviderId()).build();
                        bookingRepository.save(bookingObject);
                        return activityBookingDTO;
                } else {
                        throw new BookingException("Out of stock or acivity time id is wrong");
                }

        }

        @Override
        public ResponseModel<Booking> getBookedActivities(String customerId, Pageable pageable) {
                Page<Booking> bookActivityPage = bookingRepository.findByCustomerId(customerId, pageable);
                List<Booking> updatedBookings = bookingUtils.getLatestUserPaymentUpdate(bookActivityPage.getContent());
                Page<Booking> updatedPage = new PageImpl<>(updatedBookings, bookActivityPage.getPageable(),
                                bookActivityPage.getTotalElements());
                return commonPageUtils.buildPagedResponseModel(updatedPage);
        }

        @Override
        public List<PersonalizedPackageBookingDTO> getPersonalizedPackageBooking(String customerId) {

                List<Booking> personalizedPackageBookings = bookingRepository
                                .findAllByCustomerIdAndPersonalizedPackageIdNotNull(customerId);

                List<Booking> personalizedPackageBookingsUpdated = bookingUtils
                                .getLatestUserPaymentUpdate(personalizedPackageBookings);

                List<PersonalizedPackageBookingDTO> personalizedPackageBookingDTOS = new ArrayList<>();
                // create a for each loop to map and add quantity then add to list of DTOS
                for (Booking booking : personalizedPackageBookingsUpdated) {
                        PersonalizedPackageBookingDTO personalizedPackageBookingDTO = bookingUtils.getBookingMapper()
                                        .mapToPersonalizedPackageBookingDTO(booking);
                        personalizedPackageBookingDTO.setQuantity(
                                        personalizedPackageBookingDTO.getActivityBookings().get(0).getQuantity());
                        // add to list of DTOS
                        personalizedPackageBookingDTOS.add(personalizedPackageBookingDTO);
                }

                return personalizedPackageBookingDTOS;
        }

        @Override
        public PersonalizedPackageBookingDTO bookPersonalizedActivity(
                        PersonalizedPackageBookingDTO personalizedBookingDTO) {
                // Customer only can book personalized package one for a particular day
                if (!bookingRepository
                                .findAllByCustomerIdAndPersonalizedPackageIdAndFromDate(
                                                personalizedBookingDTO.getCustomerId(),
                                                personalizedBookingDTO.getPersonalizedPackageId(),
                                                personalizedBookingDTO.getFromDate())
                                .isEmpty()) {
                        throw new BookingException(
                                        "Already booked this package activities for date: "
                                                        + personalizedBookingDTO.getFromDate());
                }

                PersonalizedPackage personalizedPackage = personalizedPackageRepository
                                .findById(personalizedBookingDTO.getPersonalizedPackageId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Couldn't find personalized package related to id = "
                                                                + personalizedBookingDTO.getPersonalizedPackageId()));

                if (personalizedPackage.getPersonalizedPackageActivityBinds().isEmpty()) {
                        throw new BookingException("Couldn't find personalized package activity binds");
                }

                for (PersonalizedPackageActivityBind personalizedPackageActivityBind : personalizedPackage
                                .getPersonalizedPackageActivityBinds()) {
                        // Get from date
                        LocalDate fromDate = personalizedBookingDTO.getFromDate()
                                        .plusDays(personalizedPackageActivityBind.getDayNumber() - 1L);
                        Activity bookingActivity = personalizedPackageActivityBind.getActivity();

                        // Get all confirmed booking of current service provider
                        List<Booking> relatedBookings = bookingRepository
                                        .findAllByServiceProviderIdAndBookingStatusAndFromDate(
                                                        bookingActivity.getServiceProviderId(), BookingStatus.CONFIRMED,
                                                        fromDate);

                        // Get related activityBooking for considered activity
                        List<ActivityBooking> activityBookings = bookingUtils.getRelatedActivityBooking(
                                        personalizedPackageActivityBind.getActivityTimeId(), bookingActivity,
                                        relatedBookings);

                        // Get activity times for considered activity
                        List<ActivityTime> activityTimes = bookingActivity.getActivityTimes().stream()
                                        .filter(item -> item.getId() == personalizedPackageActivityBind
                                                        .getActivityTimeId())
                                        .collect(Collectors.toList());

                        // check for the availability of current activity (If this activity is all day
                        // activity, don't check for the availability)
                        if (Boolean.TRUE.equals(bookingActivity.getIsAlldayActivity())
                                        || !activityTimes.isEmpty() && bookingUtils.isOutOfStock(activityBookings,
                                                        personalizedBookingDTO.getQuantity(),
                                                        activityTimes.get(0).getQuantity())) {
                                Float totalPrice = bookingActivity.getPrice() * personalizedBookingDTO.getQuantity();
                                List<ActivityBooking> activityBookingObject = Arrays
                                                .asList(ActivityBooking.builder()
                                                                .quantity(personalizedBookingDTO.getQuantity())
                                                                .activityTimeId(personalizedPackageActivityBind
                                                                                .getActivityTimeId())
                                                                .startTime(LocalDateTime.of(fromDate,
                                                                                activityTimes.get(0).getStartTime()))
                                                                .finishTime(LocalDateTime.of(fromDate,
                                                                                activityTimes.get(0).getEndTime()))
                                                                .activity(bookingActivity).build());

                                Booking bookingObject = Booking.builder()
                                                .customerId(personalizedBookingDTO.getCustomerId())
                                                .totalPrice(totalPrice).bookingStatus(BookingStatus.REQUEST_PAYMENT)
                                                .fromDate(fromDate)
                                                .toDate(personalizedBookingDTO.getToDate())
                                                .activityBookings(activityBookingObject)
                                                .bookingDate(LocalDateTime.now())
                                                .serviceProviderId(bookingActivity.getServiceProviderId())
                                                .personalizedPackageId(
                                                                personalizedBookingDTO.getPersonalizedPackageId())
                                                .phoneNumber(personalizedBookingDTO.getPhoneNumber())
                                                .userName(personalizedBookingDTO.getUserName())
                                                .notes(personalizedBookingDTO.getNotes())
                                                .bookingNo(bookingUtils.generateRandomString())
                                                .build();
                                bookingRepository.save(bookingObject);
                        } else {
                                // return null for now, have to handle exception
                                throw new BookingException("Out of stock or acivity time id is wrong");
                        }
                }
                return personalizedBookingDTO;
        }

        @Override
        public PackageBookingDTO bookActivityPackage(PackageBookingDTO packageBookingDTO) {
                // get the activity package from db
                final ActivityPackage activityPackage = activityPackageRepository
                                .findById(packageBookingDTO.getActivityPackageId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Record not found activity package  with id : "
                                                                + packageBookingDTO.getActivityPackageId()));
                final List<ActivityBooking> bookingsForActivities = new ArrayList<>();
                final AtomicReference<Float> totalAmount = new AtomicReference<>(0.0F);
                activityPackage.getPackageActivityBinds().forEach(packageActivityBind -> {
                        // Get from date
                        final LocalDate fromDate = packageBookingDTO.getFromDate()
                                        .plusDays(packageActivityBind.getDayNumber() - 1L);

                        // Get all confirmed booking of current service provider
                        final List<Booking> relatedBookings = bookingRepository
                                        .findAllByActivityPackageIdAndBookingStatusAndFromDate(
                                                        packageBookingDTO.getActivityPackageId(),
                                                        BookingStatus.CONFIRMED, fromDate);
                        // get already booked activity quantity.
                        final AtomicInteger alreadyBookedQuantity = new AtomicInteger(0);
                        relatedBookings.forEach(item -> alreadyBookedQuantity.addAndGet(item.getActivityBookings()
                                        .stream()
                                        .filter(activityBooking -> activityBooking.getActivity()
                                                        .getId() == packageActivityBind.getId())
                                        .mapToInt(ActivityBooking::getQuantity)
                                        .sum()));

                        // find the activity time corresponding to activity package.
                        final ActivityTime activityTime = packageActivityBind.getActivity().getActivityTimes().stream()
                                        .filter(item -> item.getId() == packageActivityBind.getActivityTimeId())
                                        .findFirst().orElseThrow();

                        if (activityTime.getQuantity() >= (alreadyBookedQuantity.get()
                                        + packageBookingDTO.getQuantity())) {

                                totalAmount.set(totalAmount.get() + (packageActivityBind.getActivity().getPrice()
                                                * packageBookingDTO.getQuantity()));

                                bookingsForActivities.add(ActivityBooking.builder()
                                                .quantity(packageBookingDTO.getQuantity())
                                                .activityTimeId(packageActivityBind.getActivityTimeId())
                                                .startTime(LocalDateTime.of(fromDate, activityTime.getStartTime()))
                                                .finishTime(LocalDateTime.of(fromDate, activityTime.getEndTime()))
                                                .activity(packageActivityBind.getActivity())
                                                .build());

                        } else {
                                throw new BookingException(String.format(
                                                "Activity %s if Out of stock or activity time id is wrong",
                                                packageActivityBind.getActivity().getTitle()));
                        }

                });
                bookingRepository.save(Booking
                                .builder()
                                .customerId(packageBookingDTO.getCustomerId())
                                .totalPrice(totalAmount.get())
                                .bookingStatus(BookingStatus.REQUEST_PAYMENT)
                                .fromDate(packageBookingDTO.getFromDate())
                                .toDate(packageBookingDTO.getToDate())
                                .activityBookings(bookingsForActivities)
                                .bookingDate(LocalDateTime.now())
                                .serviceProviderId(activityPackage.getServiceProviderId())
                                .activityPackageId(packageBookingDTO.getActivityPackageId())
                                .isPersonalizedPackage(Boolean.FALSE)
                                .notes(packageBookingDTO.getNotes())
                                .userName(packageBookingDTO.getUserName())
                                .phoneNumber(packageBookingDTO.getPhoneNumber())
                                .bookingNo(bookingUtils.generateRandomString())
                                .build());
                return packageBookingDTO;
        }

        @Override
        public Booking updateBookingStatus(Long bookingId, BookingStatus bookingStatus) {
                Optional<Booking> retrieveBookingById = bookingRepository.findById(bookingId);
                if (retrieveBookingById.isPresent()) {
                        Booking retrievedBooking = retrieveBookingById.get();

                        // set booking status here
                        retrievedBooking.setBookingStatus(bookingStatus);

                        Booking updatedAndSavedBooking = bookingRepository.save(retrievedBooking);

                        return updatedAndSavedBooking;
                } else {
                        throw new ResourceNotFoundException("Record not found booking with id : " + bookingId);
                }
        }

        @Override
        public ResponseModel<Booking> getBookingByDate(String startDate, String endDate, String serviceProviderId,
                        Boolean isPay,
                        Pageable pageable) {
                // if the end date is not specified then make it the start date.
                if (endDate == null) {
                        endDate = startDate;
                }

                // If the start date is after the end date, return an exception
                if (LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {
                        throw new IllegalArgumentException(
                                        "The end date : " + endDate + " comes before the start date : " + startDate);
                }

                Page<Booking> getBookingByDate;
                if (Boolean.TRUE.equals(isPay)) {
                        getBookingByDate = bookingRepository
                                        .findBookingsByFromDateBetweenAndServiceProviderIdAndPaymentInfo_Status(
                                                        LocalDate.parse(startDate),
                                                        LocalDate.parse(endDate), serviceProviderId, "succeeded",
                                                        pageable);
                } else {
                        getBookingByDate = bookingRepository.findBookingsByFromDateBetweenAndServiceProviderId(
                                        LocalDate.parse(startDate),
                                        LocalDate.parse(endDate), serviceProviderId, pageable);
                }

                return commonPageUtils.buildPagedResponseModel(getBookingByDate);
        }

        @Override
        public ResponseModel<Booking> getHistoricalBookingByProviderId(String providerId, Boolean isPay,
                        Pageable pageable) {

                // get current date
                LocalDate currentDate = java.time.LocalDate.now();
                Page<Booking> retrieveHistoricalBooking;
                if (Boolean.TRUE.equals(isPay)) {
                        retrieveHistoricalBooking = bookingRepository
                                        .findBookingsByToDateBeforeAndServiceProviderIdAndPaymentInfo_Status(
                                                        currentDate, providerId,
                                                        "succeeded", pageable);
                } else {
                        retrieveHistoricalBooking = bookingRepository
                                        .findBookingsByToDateBeforeAndServiceProviderId(currentDate, providerId,
                                                        pageable);
                }

                return commonPageUtils.buildPagedResponseModel(retrieveHistoricalBooking);

        }
}
