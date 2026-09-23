package com.ope.booking;

import java.util.*;

public class CarBookingArrayDataAccessService implements CarBookingDao {
    private List<CarBooking> bookings = new ArrayList<>();

    @Override
    public List<CarBooking> getBookings(){
        return List.copyOf(bookings);
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
        bookings.add(booking);
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
