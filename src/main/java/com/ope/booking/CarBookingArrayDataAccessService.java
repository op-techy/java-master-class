package com.ope.booking;

import java.util.*;

public class CarBookingArrayDataAccessService implements CarBookingDao {
    private final List<CarBooking> bookings = new ArrayList<>();

    @Override
    public List<CarBooking> getBookings(){
        return List.copyOf(bookings);
    }

    @Override
    public Optional<CarBooking> findBookingById(UUID bookingId){
        return getBookings().stream()
                .filter(booking -> bookingId.equals(booking.getId()))
                .findFirst();
    }

    @Override
    public void saveBooking(CarBooking booking){
        bookings.add(booking);
    }

    @Override
    public boolean deleteBookingById(UUID bookingId){
        if (bookingId == null) return false;

        Optional<CarBooking> booking = bookings.stream()
                .filter(b -> bookingId.equals(b.getId()))
                .findFirst();

        if (booking.isEmpty()){
            return false;
        }

        booking.get().setStatus(BookingStatus.CANCELLED);
        return true;
    }

}
