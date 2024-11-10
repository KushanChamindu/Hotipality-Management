package com.mbpackageservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.mbpackageservice.model.activity.ActivityBooking;
import com.mbpackageservice.model.enums.BookingStatus;
import com.mbpackageservice.model.payment.PaymentInfo;

@Entity
@Table(name = "booking")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String customerId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentInfo paymentInfo;

    private String serviceProviderId;
    @Column(name = "user_name")
    private String userName;

    @Column(name = "phone_Number")
    private String phoneNumber;

    @Column(name = "booking_no")
    private String bookingNo;

    @Column(name = "booking_note")
    private String notes;

    // this will be repeat for packages and personalized packages
    private Float totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDateTime bookingDate;

    private Long personalizedPackageId;
    private Long activityPackageId;
    private Boolean isPersonalizedPackage;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private List<ActivityBooking> activityBookings;
}
