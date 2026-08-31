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

    public void deleteBookingById(UUID bookingId){
        CarBooking[] newBookings = new CarBooking[bookings.length - 1];
        int index = 0;

        for (CarBooking booking : bookings){
            if (!booking.getId().equals(bookingId)) {
                newBookings[index++] = booking;
            }
        }

        bookings = newBookings;
    }
}
