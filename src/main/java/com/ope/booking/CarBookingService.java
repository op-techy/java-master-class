package com.ope.booking;

import com.ope.car.Car;
import com.ope.car.CarService;
import com.ope.user.User;
import com.ope.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class CarBookingService {

    private final UserService userService;
    private final CarService carService;
    private final CarBookingDao carBookingDao;

    public CarBookingService() {
        this.userService = new UserService();
        this.carService = new CarService();
        this.carBookingDao = new CarBookingDao();
    }

    public CarBooking bookCar(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate){
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Car car = carService.findCarById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

        if (startDate.isBefore(LocalDate.now()) || !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("Invalid start or end date");
        }

        CarBooking[] bookings = getAllBookings();
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

    public CarBooking[] getAllBookings(){
        return carBookingDao.getBookings();
    }


    public Car[] getCarBookingsForUser(UUID userId){
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("No user with the id %s".formatted(userId)));
        CarBooking[] carBookings = carBookingDao.getBookings();
        int count = 0;
        for (CarBooking booking : carBookings){
            if (booking!= null && booking.getUser().getId().equals(userId)) count++;
        }

        Car[] userCars = new Car[count];
        int index = 0;
        for (CarBooking b : carBookings){
            if (b != null && b.getUser().getId().equals(userId)) {
                userCars[index++] = b.getCar();
            }
        }

        return userCars;
    }

    public Car[] getAvailableCars(){
        Car[] allCars = carService.findAllCars();
        CarBooking[] bookings = carBookingDao.getBookings();

        int count = 0;
        for (Car car : allCars){
            if (!isBooked(car, bookings)) count++;
        }

        Car[] availableCars = new Car[count];
        int index = 0;
        for (Car car : allCars){
            if (!isBooked(car, bookings)){
                availableCars[index++] = car;
            }
        }

        return availableCars;
    }

    public Car[] getAvailableElectricCars(){
        Car[] allCars = getAvailableCars();

        int count = 0;
        for (Car car : allCars){
            if (car.isElectric()) count++;
        }

        Car[] availableElectricCars = new Car[count];
        int index = 0;
        for (Car car : allCars){
            if (car.isElectric()) {
                availableElectricCars[index++] = car;
            }
        }
        return availableElectricCars;
    }

    public void deleteBooking (UUID bookingId){
        boolean isDeleted = carBookingDao.deleteBookingById(bookingId);
        if(!isDeleted){
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }
    }

    private boolean isBooked(Car car, CarBooking[] bookings) {
        for(CarBooking booking : bookings){
            if (booking.getCar().equals(car) && booking.getStatus() == BookingStatus.ACTIVE){
                return true;
            }
        }

        return false;
    }
}
