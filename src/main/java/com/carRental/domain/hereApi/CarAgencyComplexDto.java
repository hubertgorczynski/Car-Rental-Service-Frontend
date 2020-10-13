package com.carRental.domain.hereApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarAgencyComplexDto {
    private String name;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String state;
    private String countryName;
}
