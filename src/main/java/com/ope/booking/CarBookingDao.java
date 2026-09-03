package com.ope.booking;

import java.util.Optional;
import java.util.UUID;

public class CarBookingDao {
    private CarBooking[] bookings = new CarBooking[0];

    public CarBooking[] getBookings(){
        return bookings;
    }

    public Optional<CarBooking> findBookingById(UUID bookingId){
        for (CarBooking booking : bookings){
            if (booking.getId().equals(bookingId)) return Optional.of(booking);
        }

        return Optional.empty();
    }

    public void saveBooking(CarBooking booking){
        CarBooking[] newBookings = new CarBooking[bookings.length + 1];
        System.arraycopy(bookings,0,newBookings,0,bookings.length);
        newBookings[bookings.length] = booking;
        bookings = newBookings;
    }

    public boolean deleteBookingById(UUID bookingId){
        if (!bookingExists(bookingId) || bookingId == null) return false;

        for (CarBooking booking : bookings){
            if (booking.getId().equals(bookingId)) {
                booking.setStatus(BookingStatus.CANCELLED);
            }
        }

        return true;
    }

    public boolean bookingExists(UUID bookingId){
        Optional<CarBooking> carBooking = findBookingById(bookingId);
        return carBooking.isPresent();
    }
}
