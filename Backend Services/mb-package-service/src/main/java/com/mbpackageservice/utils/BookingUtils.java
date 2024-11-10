package com.mbpackageservice.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dao.mapper.BookingMapper;
import com.mbpackageservice.dao.mapper.PaymentDataMapper;
import com.mbpackageservice.dto.payment.PaymentIntentResponse;
import com.mbpackageservice.exception.BookingException;
import com.mbpackageservice.model.Booking;
import com.mbpackageservice.model.activity.Activity;
import com.mbpackageservice.model.activity.ActivityBooking;
import com.mbpackageservice.model.payment.PaymentInfo;
import com.mbpackageservice.repository.BookingRepository;
import com.mbpackageservice.service.PaymentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Random;

@Component
@Getter
public class BookingUtils {

    @Autowired
    BookingMapper bookingMapper;
    private Random random = new Random();

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentDataMapper paymentDataMapper;

    @Autowired
    private BookingRepository bookingRepository;

    public List<ActivityBooking> getRelatedActivityBooking(Long activityTimeId, Activity bookingActivity,
            List<Booking> relatedBookings) {
        List<ActivityBooking> relatedActivityBookings = new ArrayList<>();
        for (Booking relatedBooking : relatedBookings) {
            List<ActivityBooking> activityBookings = relatedBooking.getActivityBookings();
            for (ActivityBooking activityBooking : activityBookings) {
                if (activityBooking.getActivity().getId() == bookingActivity.getId()
                        && Objects.equals(activityBooking.getActivityTimeId(), activityTimeId)) {
                    relatedActivityBookings.add(activityBooking);
                }
            }
        }
        return relatedActivityBookings;
    }

    public List<ActivityBooking> getRelatedSpecialActivityBooking(Long specialActivityTimeId, Activity bookingActivity,
            List<Booking> relatedBookings) {
        List<ActivityBooking> relatedActivityBookings = new ArrayList<>();
        for (Booking relatedBooking : relatedBookings) {
            List<ActivityBooking> activityBookings = relatedBooking.getActivityBookings();
            for (ActivityBooking activityBooking : activityBookings) {
                if (activityBooking.getActivity().getId() == bookingActivity.getId()
                        && Objects.equals(activityBooking.getSpecialActivityTimeId(), specialActivityTimeId)) {
                    relatedActivityBookings.add(activityBooking);
                }
            }
        }
        return relatedActivityBookings;
    }

    public boolean isOutOfStock(List<ActivityBooking> activityBookings, int bookingQuantity, int activityQuantity) {
        int totalQuantityBooked = 0;
        for (ActivityBooking activityBooking : activityBookings) {
            totalQuantityBooked += activityBooking.getQuantity();
        }
        return (activityQuantity - totalQuantityBooked) >= bookingQuantity;
    }

    public List<Booking> getLatestUserPaymentUpdate(List<Booking> bookings) {
        Set<String> userIds = bookings.stream().map(Booking::getCustomerId).collect(Collectors.toSet());
        if (userIds.size() != 1) {
            throw new BookingException("User id should be same for all bookings!", "BOOKING_ERROR",
                    HttpStatus.CONFLICT);
        }
        List<String> paymentIntents = new ArrayList<>();
        bookings.forEach(item -> {
            // remove dead end status :beacuse nothing to update on those
            if (!"succeeded".equals(item.getPaymentInfo().getStatus())
                    && !"canceled".equals(item.getPaymentInfo().getStatus())) { // maybe we have to check refund status
                                                                                // here
                paymentIntents.add(item.getPaymentInfo().getPaymentIntentId());
            }
        });
        List<PaymentIntentResponse> paymentIntentResponseList;
        if (!paymentIntents.isEmpty()) {
            paymentIntentResponseList = paymentService.getPaymentIntents(
                    bookings.get(0).getCustomerId(),
                    paymentIntents);
        } else {
            paymentIntentResponseList = new ArrayList<>();
        }
        List<Booking> updatedBokings = new ArrayList<>();
        bookings.forEach(item -> {
            Optional<PaymentIntentResponse> paymentIntentResponse = paymentIntentResponseList.stream().filter(
                    paymentItem -> Objects.equals(item.getPaymentInfo().getPaymentIntentId(),
                            paymentItem.getPaymentIntentId()))
                    .findFirst();
            if (paymentIntentResponse.isPresent()) {
                PaymentInfo updatedPaymentInfo = paymentDataMapper.mapToPaymentInfo(paymentIntentResponse.get());
                item.setPaymentInfo(updatedPaymentInfo);
                bookingRepository.save(item);
            }
            if (item.getPaymentInfo().getExpireDate().isBefore(LocalDateTime.now())
                    || (!"succeeded".equals(item.getPaymentInfo().getStatus())
                            && !"canceled".equals(item.getPaymentInfo().getStatus()))) {
                bookingRepository.deleteById(item.getId());
            } else {
                updatedBokings.add(item);
            }
        });
        return updatedBokings;
    }

    public String generateRandomString() {
        int min = 100000;
        int max = 999999;
        return "MB" + Integer.toString(this.random.nextInt(max - min + 1) + min);
    }

}
