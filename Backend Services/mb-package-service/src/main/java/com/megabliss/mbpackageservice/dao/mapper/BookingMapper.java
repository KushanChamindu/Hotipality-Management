package com.megabliss.mbpackageservice.dao.mapper;

import com.megabliss.mbpackageservice.dto.booking.BookingDTO;
import com.megabliss.mbpackageservice.dto.booking.PersonalizedPackageBookingDTO;
import com.megabliss.mbpackageservice.model.Booking;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BookingMapper {

    @Autowired
    private ModelMapper modelMapper;

    public PersonalizedPackageBookingDTO mapToPersonalizedPackageBookingDTO(Booking booking) {
        return modelMapper.map(booking, PersonalizedPackageBookingDTO.class);
    }


    public Booking mapToBooking(PersonalizedPackageBookingDTO personalizedPackageBookingDTO) {
        return modelMapper.map(personalizedPackageBookingDTO, Booking.class);
    }

    public BookingDTO mapToBookingDTO(Booking booking) {
        return modelMapper.map(booking, BookingDTO.class);
    }
}
