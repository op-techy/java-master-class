package com.ope.booking;

import com.ope.car.Car;
import com.ope.car.CarService;
import com.ope.user.User;
import com.ope.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CarBookingService {

    private final UserService userService;
    private final CarService carService;
    private final CarBookingDao carBookingDao;

    public CarBookingService(CarBookingDao carBookingDao, UserService userService, CarService carService) {
        this.carBookingDao = carBookingDao;
        this.userService = userService;
        this.carService = carService;
    }

    public CarBooking bookCar(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate){
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Car car = carService.findCarById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

        if (startDate.isBefore(LocalDate.now()) || !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("Invalid start or end date");
        }

        boolean alreadyBooked = getAllBookings().stream()
                .anyMatch(existing -> existing != null
                        && existing.getCar().equals(car)
                        && existing.getStatus() == BookingStatus.ACTIVE);

        if (alreadyBooked){
            throw new IllegalStateException("Car %s is already booked!".formatted(car.getRegNumber()));
        }

        long numberOfDays = ChronoUnit.DAYS.between(startDate,endDate);
        BigDecimal price = car.getRentalPricePerDay().multiply(BigDecimal.valueOf(numberOfDays));

        CarBooking booking = new CarBooking(
                UUID.randomUUID(),
                user,
                car,
                startDate,
                endDate,
                price,
                BookingStatus.ACTIVE,
                LocalDateTime.now()
        );

        carBookingDao.saveBooking(booking);

        return booking;
    }

    public List<CarBooking> getAllBookings(){
        return carBookingDao.getBookings();
    }


    public List<Car> getCarBookingsForUser(UUID userId){
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("No user with the id %s".formatted(userId)));
        List<CarBooking> carBookings = getAllBookings();

        return carBookings.stream()
                .filter(b -> b != null
                        && b.getUser().getId().equals(userId)
                        && b.getStatus() == BookingStatus.ACTIVE)
                .map(CarBooking::getCar)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public List<Car> getAvailableCars(){
        List<Car> allCars = carService.findAllCars();
        List<CarBooking> bookings = carBookingDao.getBookings();

        return allCars.stream()
                .filter(c -> !isBooked(c, bookings))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public List<Car> getAvailableElectricCars(){
        List<Car> allCars = getAvailableCars();

        return allCars.stream()
                .filter(Car::isElectric)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public void deleteBooking (UUID bookingId){
        boolean isDeleted = carBookingDao.deleteBookingById(bookingId);
        if(!isDeleted){
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }
    }

    private boolean isBooked(Car car, List<CarBooking> bookings) {
        return bookings.stream()
                .anyMatch(b -> b.getCar().equals(car) && b.getStatus() == BookingStatus.ACTIVE);

    }
}
