package com.ope.car;

import java.util.Optional;
import java.util.UUID;

public class CarService {

    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public Optional<Car> findCarById(UUID id){
        return carDao.findCarById(id);
    }

    public Car[] findAllCars(){
        return carDao.getCars();
    }

}
