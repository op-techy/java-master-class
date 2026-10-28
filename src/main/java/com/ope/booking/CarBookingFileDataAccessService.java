package com.ope.booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

            return  rawBookings.stream()
                    .map(object -> {
                        if (!(object instanceof CarBooking))
                            throw new IllegalStateException("Illegal booking file: expected a CarBooking object");

                        return (CarBooking) object;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));

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
        if (bookingId == null) return false;

        List<CarBooking> bookings = readBookingsFromFile();

        Optional<CarBooking> bookingToDelete = bookings.stream()
                .filter(b -> bookingId.equals(b.getId()))
                .findFirst();

        if (bookingToDelete.isEmpty()){
            return false;
        }

        bookingToDelete.get().setStatus(BookingStatus.CANCELLED);
        writeBookingsToFile(bookings);
        return true;
    }

    @Override
    public List<CarBooking> getBookings() {
        return readBookingsFromFile();
    }

    @Override
    public Optional<CarBooking> findBookingById(UUID bookingId) {
        if (bookingId == null) return Optional.empty();

        List<CarBooking> bookings = readBookingsFromFile();

        return bookings.stream()
                .filter(b -> bookingId.equals(b.getId()))
                .findFirst();
    }


}
