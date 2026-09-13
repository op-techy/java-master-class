package com.ope.booking;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {
    private CarBooking[] bookings = new CarBooking[0];

    @Override
    public CarBooking[] getBookings(){
        return Arrays.copyOf(bookings, bookings.length);
    }

    @Override
    public Optional<CarBooking> findBookingById(UUID bookingId){
        for (CarBooking booking : bookings){
            if (booking.getId().equals(bookingId)) return Optional.of(booking);
        }

        return Optional.empty();
    }

    @Override
    public void saveBooking(CarBooking booking){
        CarBooking[] newBookings = new CarBooking[bookings.length + 1];
        System.arraycopy(bookings,0,newBookings,0,bookings.length);
        newBookings[bookings.length] = booking;
        bookings = newBookings;
    }

    @Override
    public boolean deleteBookingById(UUID bookingId){
        if (bookingId == null || !bookingExists(bookingId)) return false;

        for (CarBooking booking : bookings){
            if (booking.getId().equals(bookingId)) {
                booking.setStatus(BookingStatus.CANCELLED);
                break;
            }
        }

        return true;
    }

    public boolean bookingExists(UUID bookingId){
        Optional<CarBooking> carBooking = findBookingById(bookingId);
        return carBooking.isPresent();
    }
}
