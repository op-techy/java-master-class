package com.ope.car;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarArrayDataAccessService implements CarDao {

    private static final List<Car> CARS;

    static {
        CARS = List.of(
                new Car(UUID.fromString("5e9b9ea6-79a3-4079-893b-928c5a7342dd"),"8XKP421",new BigDecimal("100.00"),Brand.AUDI,false),
                new Car(UUID.fromString("8fecf186-eb5b-47e9-894a-ac00e4314bf9"),"3TVN087",new BigDecimal("150.00"),Brand.TESLA,true),
                new Car(UUID.fromString("ae8996a5-0e12-45e0-b73c-01a836b1cf9a"),"6BQR552",new BigDecimal("250.00"),Brand.MERCEDES,false),
                new Car(UUID.fromString("6ca0a25d-3be2-4a62-8bfb-e898e08b41a0"),"1LWH936",new BigDecimal("120.00"),Brand.TOYOTA,false)
        );

    }

    @Override
    public List<Car> getCars() {
        return CARS;
    }

    @Override
    public Optional<Car> findCarById(UUID id){
        List<Car> cars = getCars();

        for (Car car : cars) {
            if (id.equals(car.getId())) return Optional.of(car);
        }

        return Optional.empty();
    }

}
