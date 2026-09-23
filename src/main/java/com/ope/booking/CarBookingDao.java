package com.ope.booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarBookingDao {
    List<CarBooking> getBookings();

    Optional<CarBooking> findBookingById(UUID bookingId);

    void saveBooking(CarBooking booking);

    boolean deleteBookingById(UUID bookingId);
}
