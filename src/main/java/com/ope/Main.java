package com.ope;
// TODO 1. create a new branch called initial-implementation
// TODO 2. create a package with your name. i.e com.franco and move this file inside the new package
// TODO 3. implement https://amigoscode.com/learn/java-cli-build/lectures/3a83ecf3-e837-4ae5-85a8-f8ae3f60f7f5

import com.ope.booking.CarBooking;
import com.ope.booking.CarBookingService;
import com.ope.car.Car;
import com.ope.user.User;
import com.ope.user.UserService;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService();
        CarBookingService carBookingService = new CarBookingService();

        Scanner sc = new Scanner(System.in);

        boolean running = true;

        while (running){
            displayMenu();
            String menuOption = sc.nextLine();

            try {
                switch (Integer.parseInt(menuOption)){
                    case 1 -> bookCar(sc, carBookingService);
                    case 2 -> deleteBooking(sc, carBookingService);
                    case 3 -> userBookedCars(sc,carBookingService);
                    case 4 -> allBookings(carBookingService);
                    case 5 -> displayAvailableCars(carBookingService);
                    case 6 -> displayAvailableElectricCars(carBookingService);
                    case 7 -> allUsers(userService);
                    case 8 -> {
                        System.out.println("Exiting...");
                        running = false;
                    }
                    default -> {
                        System.out.println("Invalid Option! Try again.");
                        System.out.println();
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 1 and 8.");
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());;
            }

        }

        sc.close();
    }

    /**
     * FR-01: System prompts for user ID, car selection,
     * start date and end date. Price is calculated
     * from the car’s rental price per day. A car that
     * is already booked cannot be booked again
     * @param sc
     * @param carBookingService
     */
    private static void bookCar(Scanner sc,CarBookingService carBookingService) {
        System.out.print("User ID: ");
        UUID userId = UUID.fromString(sc.nextLine().trim());

        System.out.print("Car ID: ");
        UUID carId = UUID.fromString(sc.nextLine().trim());

        System.out.print("Start date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(sc.nextLine().trim());

        System.out.print("End date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(sc.nextLine().trim());

        CarBooking booking = carBookingService.bookCar(userId, carId, startDate, endDate);
        System.out.println("Booking confirmed: " + booking);
    }

    /**
     * FR-02: Cancel an existing booking by booking ID,
     * making the car available again
     * @param sc
     * @param carBookingService
     */
    private static void deleteBooking(Scanner sc,CarBookingService carBookingService) {
        System.out.print("Booking ID: ");
        UUID bookingID = UUID.fromString(sc.nextLine().trim());

        carBookingService.deleteBooking(bookingID);
        System.out.println("Booking deleted.");
    }

    /**
     * FR-03: Display all cars booked by a specific user
     * @param sc
     * @param carBookingService
     */
    private static void userBookedCars(Scanner sc, CarBookingService carBookingService) {
        System.out.print("User ID: ");
        UUID userId = UUID.fromString(sc.nextLine().trim());

        Car[] cars = carBookingService.getCarBookingsForUser(userId);
        printCars(cars);
    }

    /**
     * FR-04: Display every booking in the system
     * @param carBookingService
     */
    private static void allBookings(CarBookingService carBookingService) {
        CarBooking[] bookings = carBookingService.getAllBookings();
        if (bookings.length == 0){
            System.out.println("No bookings found.");
            return;
        }
        for (CarBooking booking : bookings){
            System.out.println(booking);
        }

    }

    /**
     * FR-05: List all cars currently not booked
     * @param carBookingService
     */
    private static void displayAvailableCars(CarBookingService carBookingService) {
        printCars(carBookingService.getAvailableCars());
    }

    /**
     * FR-06: Filter and display only available electric cars
     * @param carBookingService
     */
    private static void displayAvailableElectricCars(CarBookingService carBookingService) {
        printCars(carBookingService.getAvailableElectricCars());
    }

    /**
     * FR-07: List all registered users
     * @param userService
     */
    private static void allUsers(UserService userService) {
        User[] users = userService.findAllUsers();
        if (users.length == 0){
            System.out.println("No users found.");
            return;
        }
        for (User user : users){
            System.out.println(user);
        }
    }

    /**
     * Helper method to print cars
     * @param cars
     */
    private static void printCars(Car[] cars){
        if (cars.length == 0){
            System.out.println("No cars to display");
        }

        for (Car car : cars){
            System.out.println(car);
        }
    }

    public static void displayMenu(){
        System.out.println("""
                1 - Book Car
                2 - Delete Booking
                3 - View All User Booked Cars
                4 - View All Bookings
                5 - View Available Cars
                6 - View Available Electric Cars
                7 - View All Users
                8 - Exit
                """);
        System.out.print("Select an option from the menu above: ");
    }
}
