package com.carRental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalDto {
    private Long id;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;
    private Long carId;
    private Long userId;
}
