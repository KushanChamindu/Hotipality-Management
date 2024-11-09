package com.megabliss.mbpackageservice.controller;

import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.booking.ActivityBookingDTO;
import com.megabliss.mbpackageservice.dto.booking.PackageBookingDTO;
import com.megabliss.mbpackageservice.dto.booking.PersonalizedPackageBookingDTO;
import com.megabliss.mbpackageservice.model.Booking;
import com.megabliss.mbpackageservice.model.enums.BookingStatus;
import com.megabliss.mbpackageservice.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/package-service/booking")
@Validated
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * This method adds an activity booking based on information provided via the
     * ActivityBookingDTO
     *
     * @param bookingDTO ActivityBookingDTO
     * @return ResponseEntity<ActivityBookingDTO> savedBooking
     */
    @PostMapping("/activity")
    public ResponseEntity<ActivityBookingDTO> addActivityBooking(@Valid @RequestBody ActivityBookingDTO bookingDTO) {
        if (bookingDTO.getSpecialActivityTimeId() != null && bookingDTO.getActivityTimeId() != null) {
            throw new IllegalArgumentException(
                    "Both special activity and normal activity time id values cannot be present");
        } else if (bookingDTO.getActivityTimeId() != null && bookingDTO.getIsOneTime()) {
            throw new IllegalArgumentException("A one time activity cannot have activity times (recurring)");
        } else if (bookingDTO.getSpecialActivityTimeId() != null && !bookingDTO.getIsOneTime()) {
            throw new IllegalArgumentException("A normal activity (recurring) cannot have special activity times" +
                    "(one time)");
        }
        return ResponseEntity.ok().body(bookingService.bookActivity(bookingDTO));
    }

    /**
     * This method adds a personalized activity booking via a
     * PersonalizedBookingDTO.
     *
     * @param personalizedBookingDTO PersonalizedBookingDTO
     * @return ResponseEntity<PersonalizedPackageBookingDTO> savedBooking
     */
    @PostMapping("/personalized-package")
    public ResponseEntity<PersonalizedPackageBookingDTO> addPersonalizedActivityBooking(
            @Valid @RequestBody PersonalizedPackageBookingDTO personalizedBookingDTO) {
        return ResponseEntity.ok().body(bookingService.bookPersonalizedActivity(personalizedBookingDTO));
    }

    @PostMapping("/package")
    public ResponseEntity<PackageBookingDTO> addActivityPackageBooking(
            @Valid @RequestBody PackageBookingDTO packageBookingDTO) {
        return ResponseEntity.ok().body(bookingService.bookActivityPackage(packageBookingDTO));
    }

    /**
     * Retrieves a list of booked activities based on customer id. Optionally
     * pagination parameters may be set.
     *
     * @param customerId
     * @param pageNo
     * @param size
     * @param sortBy
     * @param direction
     * @return
     */
    @GetMapping("/activity")
    public ResponseEntity<ResponseModel<Booking>> getBookedActivity(
            @RequestParam(name = "customerId", required = true) @NotBlank(message = "Customer Id must not be blank " +
                    "or null") String customerId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number " +
                    "must be a positive integer") int pageNo,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must " +
                    "be a positive integer") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "bookingDate") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);
        return ResponseEntity.ok().body(bookingService.getBookedActivities(customerId, pageRequestData));
    }

    /**
     * This endpoint retrieves booked personalized packages by customer ID.
     *
     * @param customerId
     * @return
     */
    @GetMapping("/personalized-package-bookings")
    public ResponseEntity<List<PersonalizedPackageBookingDTO>> getBookedPersonalizedPackage(
            @RequestParam(name = "customerId", required = true) @NotBlank(message = "Customer id must not be blank or null") String customerId) {

        return ResponseEntity.ok().body(bookingService.getPersonalizedPackageBooking(customerId));
    }

    /**
     * This endpoint updates a bookings status by taking in the booking ID and
     * status to update it too.
     *
     * @param bookingId
     * @param bookingStatus
     * @return
     */
    @PatchMapping("/booking-status/{bookingId}")
    public ResponseEntity<Booking> updateBookingStatus(
            @PathVariable(name = "bookingId") @PositiveOrZero(message = "Booking id must be a positive or zero value Long") Long bookingId,
            @RequestParam(name = "bookingStatus", required = true) @NotNull(message = "Booking status must not be null") BookingStatus bookingStatus) {
        if (bookingId != null) {
            return ResponseEntity.ok().body(bookingService.updateBookingStatus(bookingId, bookingStatus));
        } else {
            throw new NullArgumentException("Booking Id cannot be null");
        }
    }

    /**
     * This endpoint retrieves bookings by date for providers.
     * Enter from date and optionally a "to date"(default value is the "from date").
     * Uses pagination.
     *
     * @param startDate
     * @param pageNo
     * @param size
     * @param sortBy
     * @param direction
     * @return
     */
    @GetMapping("/provider/getBookingByDate")
    public ResponseEntity<ResponseModel<Booking>> getBookingByDate(
            @RequestParam(name = "startDate", required = true) @NotBlank(message = "Date must not be blank or null") String startDate,
            @RequestParam(name = "endDate", required = false) @NotBlank(message = "Date must not be blank or null") String endDate,
            @RequestParam(name = "serviceProviderId", required = true, defaultValue = "false") @NotBlank(message = "Service provider id must not be blank or null") String serviceProviderId,
            @RequestParam(name = "isPay", required = true) @NotNull(message = "isPay must not be blank or null") Boolean isPay,
            @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number " +
                    "must be a positive integer") int pageNo,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must " +
                    "be a positive integer") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "fromDate") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "DESC") String direction) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);
        return ResponseEntity.ok().body(bookingService.getBookingByDate(startDate, endDate, serviceProviderId, isPay,
                pageRequestData));

    }

    /**
     * This endpoint provides serviceProviders a means to retrieve all thier
     * historical bookings (bookings that have
     * already finished). This grabs the current local date of the provider and
     * finds any bookings. If none are found
     * or if the service provider does not exist, it will return all the historical
     * bookings.
     *
     * @param serviceProviderId
     * @param pageNo
     * @param size
     * @param sortBy
     * @param direction
     * @return
     */
    @GetMapping("/provider/historical")
    public ResponseEntity<ResponseModel<Booking>> getHistoricalBookingsByProviderId(
            @RequestParam(name = "serviceProviderId", required = true) @NotBlank(message = "Service provider id must " +
                    "not be blank or null") String serviceProviderId,
            @RequestParam(name = "isPay", required = true) @NotNull(message = "isPay must not be blank or null") Boolean isPay,
            @RequestParam(name = "pageNo", required = false, defaultValue = "1") @Positive(message = "Page number " +
                    "must be a positive integer") int pageNo,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive(message = "Page size must " +
                    "be a positive integer") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "fromDate") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "DESC") String direction) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size, sort);

        return ResponseEntity.ok().body(bookingService.getHistoricalBookingByProviderId(serviceProviderId, isPay,
                pageRequestData));
    }

}
