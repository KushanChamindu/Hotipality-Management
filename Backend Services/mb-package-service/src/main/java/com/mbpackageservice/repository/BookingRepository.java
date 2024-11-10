package com.mbpackageservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.Booking;
import com.mbpackageservice.model.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
        Page<Booking> findByCustomerId(String customerId, Pageable pageable);

        List<Booking> findAllByServiceProviderIdAndBookingStatusAndFromDate(String serviceProviderId,
                        BookingStatus bookingStatus, LocalDate fromDate);

        List<Booking> findAllByActivityPackageIdAndBookingStatusAndFromDate(Long activityPackageId,
                        BookingStatus bookingStatus, LocalDate fromDate);

        List<Booking> findAllByCustomerIdAndPersonalizedPackageIdAndFromDate(String customerId,
                        Long personalizedPackageId,
                        LocalDate fromDate);

        List<Booking> findAllByCustomerIdAndPersonalizedPackageIdNotNull(String customerId);

        Page<Booking> findBookingsByFromDateBetweenAndServiceProviderId(LocalDate startDate,
                        LocalDate endDate,
                        String serviceProviderId,
                        Pageable pageable);

        Page<Booking> findBookingsByFromDateBetweenAndServiceProviderIdAndPaymentInfo_Status(LocalDate startDate,
                        LocalDate endDate, String serviceProviderId, String status, Pageable pageable);

        Page<Booking> findBookingsByToDateBeforeAndServiceProviderId(LocalDate toDate, String serviceProviderId,
                        Pageable pageable);

        Page<Booking> findBookingsByToDateBeforeAndServiceProviderIdAndPaymentInfo_Status(LocalDate toDate,
                        String serviceProviderId, String status,
                        Pageable pageable);

        Page<Booking> findBookingsByToDateBefore(LocalDate toDate, Pageable pageable);

}