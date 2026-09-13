package com.ope.booking;

import java.util.Optional;
import java.util.UUID;

public interface CarBookingDao {
    CarBooking[] getBookings();

    Optional<CarBooking> findBookingById(UUID bookingId);

    void saveBooking(CarBooking booking);

    boolean deleteBookingById(UUID bookingId);
}
