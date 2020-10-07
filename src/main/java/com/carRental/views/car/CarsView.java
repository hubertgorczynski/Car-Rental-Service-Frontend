package com.carRental.views.car;

import com.carRental.client.CarClient;
import com.carRental.domain.CarDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarsView extends VerticalLayout {

    private final Grid<CarDto> carGrid = new Grid<>(CarDto.class);
    private final CarClient carClient;

    @Autowired
    public CarsView(CarClient carClient) {
        this.carClient = carClient;

        Button addCarButton = new Button("Add new car");

        carGrid.setColumns(
                "id",
                "vin",
                "brand",
                "model",
                "productionYear",
                "fuelType",
                "engineCapacity",
                "bodyClass",
                "mileage",
                "costPerDay",
                "status");

        add(addCarButton, carGrid);
    }

    public void refreshCars() {
        List<CarDto> cars = carClient.getCars();
        carGrid.setItems(cars);
    }
}
