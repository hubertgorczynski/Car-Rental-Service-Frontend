package com.carRental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long id;
    private String vin;
    private String brand;
    private String model;
    private int productionYear;
    private String fuelType;
    private double engineCapacity;
    private String bodyClass;
    private int mileage;
    private BigDecimal costPerDay;
    private Status status;
}
