package com.carRental.views.rental;

import com.carRental.client.RentalClient;
import com.carRental.domain.RentalComplexDto;
import com.carRental.domain.UserDto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class RentalsView extends VerticalLayout {

    private final Grid<RentalComplexDto> rentalGrid = new Grid<>(RentalComplexDto.class);
    private final RentalClient rentalClient;
    private UserDto loggedUserDto;

    @Autowired
    public RentalsView(RentalClient rentalClient) {
        this.rentalClient = rentalClient;

        rentalGrid.setColumns(
                "id",
                "rentedFrom",
                "rentedTo",
                "rentalCost",
                "carBrand",
                "carModel",
                "userName",
                "userLastName",
                "userEmail",
                "userPhoneNumber");

        add(rentalGrid);
    }

    public void refreshRentalsForAdmin() {
        loggedUserDto = null;
        List<RentalComplexDto> rentals = rentalClient.getRentals();
        rentalGrid.setItems(rentals);
    }

    public void refreshRentalsForUser(UserDto userDto) {
        loggedUserDto = userDto;
        List<RentalComplexDto> rentals = rentalClient.getRentalsByUserId(userDto.getId());
        rentalGrid.setItems(rentals);
    }
}
