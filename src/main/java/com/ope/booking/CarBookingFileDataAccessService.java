package com.ope.booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao{
    private final String filePath;

    public CarBookingFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    // --- helper: read the full list from disk ---
    public List<CarBooking> readBookingsFromFile(){
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();

        try(ObjectInputStream ois = new ObjectInputStream( new FileInputStream(filePath))){
            Object data = ois.readObject();

            if (!(data instanceof List<?>)) throw new IllegalStateException("Illegal booking file: expected List");

            List<?> rawBookings = (List<?>) data;

            List<CarBooking> bookings = new ArrayList<>();

            for (Object o : rawBookings){
                if (!(o instanceof CarBooking)) throw new IllegalStateException("Illegal booking file: expected a CarBooking object");

                bookings.add((CarBooking) o);
            }

            return bookings;

        }  catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // --- helper: write the full list to disk ---
    private void writeBookingsToFile(List<CarBooking> bookings){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))){
            oos.writeObject(bookings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void saveBooking(CarBooking booking) {
        List<CarBooking> bookings = readBookingsFromFile();

        bookings.add(booking);

        writeBookingsToFile(bookings);
    }

    @Override
    public boolean deleteBookingById(UUID bookingId) {
        List<CarBooking> bookings = readBookingsFromFile();
        boolean found = false;

        for (CarBooking booking : bookings){
            if (bookingId.equals(booking.getId())){
                booking.setStatus(BookingStatus.CANCELLED);
                found = true;
                break;
            }
        }

        if (found){
            writeBookingsToFile(bookings);
        }

        return found;
    }

    @Override
    public List<CarBooking> getBookings() {
        return readBookingsFromFile();
    }

    @Override
    public Optional<CarBooking> findBookingById(UUID bookingId) {
        List<CarBooking> bookings = readBookingsFromFile();

        for (CarBooking booking : bookings){
            if (bookingId.equals(booking.getId())) return Optional.of(booking);
        }

        return Optional.empty();
    }


}
