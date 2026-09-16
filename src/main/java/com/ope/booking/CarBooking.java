package com.ope.booking;

import com.ope.car.Car;
import com.ope.user.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class CarBooking implements Serializable {

    private static final long serialVersionUID = 3L;

    private final UUID id;
    private User user;
    private Car car;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private BookingStatus status;
    private LocalDateTime bookedAt;

    public CarBooking(UUID id, User user, Car car, LocalDate startDate, LocalDate endDate,
                      BigDecimal price, BookingStatus status, LocalDateTime bookedAt) {
        this.id = id;
        this.user = user;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status;
        this.bookedAt = bookedAt;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CarBooking that = (CarBooking) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CarBooking{" +
                "id=" + id +
                ", user=" + user +
                ", car=" + car +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", status=" + status +
                ", bookedAt=" + bookedAt +
                '}';
    }
}
