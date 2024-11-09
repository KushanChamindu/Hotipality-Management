package com.megabliss.mbpackageservice.service;

import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.booking.ActivityBookingDTO;
import com.megabliss.mbpackageservice.dto.booking.PackageBookingDTO;
import com.megabliss.mbpackageservice.dto.booking.PersonalizedPackageBookingDTO;
import com.megabliss.mbpackageservice.model.Booking;
import com.megabliss.mbpackageservice.model.enums.BookingStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {
    public ActivityBookingDTO bookActivity(ActivityBookingDTO bookingDTO);

    public ResponseModel<Booking> getBookedActivities(String customerId, Pageable pageable);

    /**
     * Retrieves a personazlied package booking
     *
     * @param customerId
     * @return
     */
    public List<PersonalizedPackageBookingDTO> getPersonalizedPackageBooking(String customerId);

    public PersonalizedPackageBookingDTO bookPersonalizedActivity(PersonalizedPackageBookingDTO personalizedBookingDTO);

    public PackageBookingDTO bookActivityPackage(PackageBookingDTO packageBookingDTO);

    /**
     * Updates the booking status
     *
     * @param bookingId
     * @param bookingStatus
     * @return
     */
    public Booking updateBookingStatus(Long bookingId, BookingStatus bookingStatus);

    /**
     * Gets a range of bookings from a serviceProvider between two specified dates
     *
     * @param fromDate
     * @param toDate
     * @param serviceProviderId
     * @param pageable
     * @return
     */
    public ResponseModel<Booking> getBookingByDate(String fromDate, String toDate, String serviceProviderId,
            Boolean isPay,
            Pageable pageable);

    public ResponseModel<Booking> getHistoricalBookingByProviderId(String providerId, Boolean isPay, Pageable pageable);

}
