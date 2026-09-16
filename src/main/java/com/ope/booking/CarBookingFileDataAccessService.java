package com.ope.booking;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao{
    private final String filePath;

    public CarBookingFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    // --- helper: read the full list from disk ---
    public CarBooking[] readBookingsFromFile(){
        File file = new File(filePath);
        if (!file.exists()) return new CarBooking[0];

        try(ObjectInputStream ois = new ObjectInputStream( new FileInputStream(filePath))){
            return (CarBooking[]) ois.readObject();
        }  catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // --- helper: write the full list to disk ---
    private void writeBookingsToFile(CarBooking[] bookings){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))){
            oos.writeObject(bookings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void saveBooking(CarBooking booking) {
        CarBooking[] bookings = readBookingsFromFile();

        CarBooking[] newBookings = new CarBooking[bookings.length + 1];
        System.arraycopy(bookings,0,newBookings,0,bookings.length);
        newBookings[bookings.length] = booking;

        writeBookingsToFile(newBookings);
    }

    @Override
    public boolean deleteBookingById(UUID bookingId) {
        CarBooking[] bookings = readBookingsFromFile();
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
    public CarBooking[] getBookings() {
        return readBookingsFromFile();
    }

    @Override
    public Optional<CarBooking> findBookingById(UUID bookingId) {
        CarBooking[] bookings = readBookingsFromFile();

        for (CarBooking booking : bookings){
            if (bookingId.equals(booking.getId())) return Optional.of(booking);
        }

        return Optional.empty();
    }


}
