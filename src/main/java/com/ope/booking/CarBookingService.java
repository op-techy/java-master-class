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

        List<CarBooking> bookings = getAllBookings();
        for (CarBooking existing : bookings){
            if (existing != null && existing.getCar().equals(car) && existing.getStatus() == BookingStatus.ACTIVE){
                throw new IllegalStateException("Car %s is already booked!".formatted(car.getRegNumber()));
            }
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

        List<Car> userCars = new ArrayList<>();
        for (CarBooking b : carBookings){
            if (b != null && b.getUser().getId().equals(userId)) {
                userCars.add(b.getCar());
            }
        }

        return userCars;
    }

    public List<Car> getAvailableCars(){
        List<Car> allCars = carService.findAllCars();
        List<CarBooking> bookings = carBookingDao.getBookings();

        List<Car> availableCars = new ArrayList<>();
        for (Car car : allCars){
            if (!isBooked(car, bookings)) availableCars.add(car);
        }

        return availableCars;
    }

    public List<Car> getAvailableElectricCars(){
        List<Car> allCars = getAvailableCars();

        List<Car> availableElectricCars = new ArrayList<>();
        for (Car car : allCars){
            if (car.isElectric()) availableElectricCars.add(car);
        }

        return availableElectricCars;
    }

    public void deleteBooking (UUID bookingId){
        boolean isDeleted = carBookingDao.deleteBookingById(bookingId);
        if(!isDeleted){
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }
    }

    private boolean isBooked(Car car, List<CarBooking> bookings) {
        for(CarBooking booking : bookings){
            if (booking.getCar().equals(car) && booking.getStatus() == BookingStatus.ACTIVE){
                return true;
            }
        }

        return false;
    }
}
